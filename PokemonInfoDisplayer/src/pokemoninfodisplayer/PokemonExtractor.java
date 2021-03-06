package pokemoninfodisplayer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import pokemoninfodisplayer.data.MemoryDataSource;
import pokemoninfodisplayer.data.gba.VBAReader;
import pokemoninfodisplayer.data.memory.MemoryMap;
import pokemoninfodisplayer.data.nds.DesmumeReader;
import pokemoninfodisplayer.graphics.InfoFrame;
import pokemoninfodisplayer.models.BattleFlag;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonGame;
import pokemoninfodisplayer.models.event.PokemonKillEvent;
import pokemoninfodisplayer.models.event.PokemonKillHandler;
import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.models.TrainerModel;
import pokemoninfodisplayer.models.event.PokemonHitPointChangeEvent;
import pokemoninfodisplayer.models.event.PokemonHitPointChangeHandler;
import pokemoninfodisplayer.models.gen3.Gen3Extractor;
import pokemoninfodisplayer.models.gen4.Gen4Extractor;
import pokemoninfodisplayer.models.gen5.Gen5Extractor;
import pokemoninfodisplayer.models.memory.PokemonMemoryModel;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.UnsupportedPlatformException;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <TmemMap> The memory map type
 * @param <TpokMemModel> The pokemon memory model type
 */
public abstract class PokemonExtractor<TmemMap extends MemoryMap, TpokMemModel extends PokemonMemoryModel> implements PokemonInterface {
	
	private final MemoryDataSource<TmemMap> dataSource;
	protected final PokemonGame game;
	private final TpokMemModel[] pokMemoryModelBuffer;
	private final TpokMemModel[] pokMemoryModelCheckBuffer;
	private final List<PokemonKillHandler> killHandlers;
	private final List<PokemonHitPointChangeHandler> hpChangeHandlers;
	
	@SuppressWarnings("unchecked")
	public PokemonExtractor(PokemonGame game, MemoryDataSource<TmemMap> dataSource, Class<TpokMemModel> memoryModelType) {
		this.dataSource = dataSource;
		this.game = game;
		this.killHandlers = new ArrayList<>();
		this.hpChangeHandlers = new ArrayList<>();
		
		this.pokMemoryModelBuffer = (TpokMemModel[]) Array.newInstance(memoryModelType, 6);
		this.pokMemoryModelCheckBuffer = (TpokMemModel[]) Array.newInstance(memoryModelType, 6);
	}
	
	protected abstract void updatePokemonMemoryModels(TpokMemModel[] party, TmemMap memoryMap);
	protected abstract BattleFlag getInBattleFlag(TmemMap memoryMap);
	@Deprecated
	protected abstract int extractActivePid(TmemMap memoryMap);
	protected abstract int getActiveInBattleIndex(TmemMap memoryMap);
	protected abstract int getSecondActiveInBattleIndex(TmemMap memoryMap);
	protected abstract boolean isDualBattle(TmemMap memoryMap);

	@Override
	public boolean isDualBattle() {
		if (!getBattleFlag().isInBattle()) {
			return false;
		}
		return isDualBattle(dataSource.getMemoryMap());
	}
	
	@Override
	public void update() throws Exception {
		dataSource.update();
	}

	@Override
	public void updateParty(PartyModel party) {
		updatePokemonMemoryModels(pokMemoryModelBuffer, dataSource.getMemoryMap());
		
		for (int i = 0; i < 6; i++) {
			if (party.getPartySlot(i) == null) {
				continue;
			}
			int key = party.getPartySlot(i).getPersonalityValue();
			if (!InfoFrame.CURRENT_HP_GUI_MAP.containsKey(key)) {
				InfoFrame.CURRENT_HP_GUI_MAP.put(key, (double) party.getPartySlot(i).getCurrentHp());
			}
		}
		
		int activeIndex = getActiveInBattleIndex(dataSource.getMemoryMap());
		int secondActiveIndex = isDualBattle() ? getSecondActiveInBattleIndex(dataSource.getMemoryMap()) : activeIndex;
		
		for (int i = 0; i < pokMemoryModelBuffer.length; i++) {
			
			TpokMemModel memModel = pokMemoryModelBuffer[i];
			PokemonModel pok = memModel.toPokemonModel();
			
			if (getBattleFlag().isInBattle()) {
				pok.setActive(i == activeIndex || i == secondActiveIndex);
			}
			
			if (!memModel.isPresent() || pok.getDexEntry() < 1) {
				if (i == 0 && PokemonInfoDisplayer.DEBUG) {
					System.err.println("Party element " + i + " not present");
				}
				party.setPartySlot(i, null);
				break;
			}
			
			if (!memModel.validateChecksum()) {
				if (PokemonInfoDisplayer.DEBUG) {
					System.err.println("Invalid checksum for index " + i);
				}
				continue;
			}

			if (!pok.validate()) {
				if (PokemonInfoDisplayer.DEBUG) {
					System.err.println("Validation failed for index " + i);
				}
				continue;
			}
			
			if (this.pokMemoryModelCheckBuffer[i] == null) {
				this.pokMemoryModelCheckBuffer[i] = memModel;
				continue;
			}
			if (this.pokMemoryModelCheckBuffer[i].equals(memModel)) {
				var previousPok = party.getPartySlot(i);
				
				doKillDetection(pok, previousPok);
				doHPChangeDetection(pok, previousPok);
				
				party.setPartySlot(i, pok);
				//System.out.println(pok.toShortString());
			}
			this.pokMemoryModelCheckBuffer[i] = null;
			
		}
	}
	
	private void doKillDetection(PokemonModel pok, PokemonModel previousPok) {
		if (pok == null || previousPok == null || !pok.equals(previousPok)) {
			return;
		}
		
		boolean xpIncrease = pok.getExperiencePoints() > previousPok.getExperiencePoints();
		boolean equalLvl = pok.getLevel() == previousPok.getLevel();
		boolean isActive = pok.isActive();
		var battleFlag = getBattleFlag();
		
		if (battleFlag.isInBattle() && xpIncrease && equalLvl && isActive) {
			var killEvent = new PokemonKillEvent(pok, battleFlag);
			this.killHandlers.forEach(h -> h.handle(killEvent));
		}
	}
	
	private void doHPChangeDetection(PokemonModel pok, PokemonModel previousPok) {
		if (pok == null || previousPok == null || !pok.equals(previousPok) || pok.getCurrentHp() == previousPok.getCurrentHp()) {
			return;
		}
		
		var hpChangeEvent = new PokemonHitPointChangeEvent(pok, getBattleFlag(), pok.getCurrentHp(), previousPok.getCurrentHp());
		this.hpChangeHandlers.forEach(h -> h.handle(hpChangeEvent));
	}
	
	@Override
	public PartyModel getParty() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public TrainerModel getTrainer() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public BattleFlag getBattleFlag() {
		return getInBattleFlag(dataSource.getMemoryMap());
	}
	
	@Deprecated
	@Override
	public int getActivePid() {
		return extractActivePid(dataSource.getMemoryMap());
	}

	@Override
	public void close() throws IOException {
		dataSource.close();
	}

	@Override
	public void addPokemonKillHandler(PokemonKillHandler handler) {
		if (this.killHandlers.contains(handler)) {
			return;
		}
		this.killHandlers.add(handler);
	}

	@Override
	public void addPokemonHPChangeHandler(PokemonHitPointChangeHandler handler) {
		if (this.hpChangeHandlers.contains(handler)) {
			return;
		}
		this.hpChangeHandlers.add(handler);
	}
	
	
	public static PokemonExtractor createPokemonExtractor(PokemonGame game) throws ProcessNotFoundException, UnsupportedPlatformException {
		switch (game.generation) {
			case 3:
				return new Gen3Extractor(game, new VBAReader());
			case 4:
				return new Gen4Extractor(game, new DesmumeReader());
			case 5: 
				return new Gen5Extractor(game, new DesmumeReader());
			default:
				throw new UnsupportedOperationException("No memory extractor is currently implemented for generation " + game.generation);
		}
	}
	
}
