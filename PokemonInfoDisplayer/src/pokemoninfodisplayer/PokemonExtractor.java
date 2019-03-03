package pokemoninfodisplayer;

import java.io.IOException;
import pokemoninfodisplayer.data.MemoryDataSource;
import pokemoninfodisplayer.data.gba.VBAReader;
import pokemoninfodisplayer.data.memory.MemoryMap;
import pokemoninfodisplayer.data.nds.DesmumeReader;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonGame;
import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.models.TrainerModel;
import pokemoninfodisplayer.models.gen3.Gen3Extractor;
import pokemoninfodisplayer.models.gen4.Gen4Extractor;
import pokemoninfodisplayer.models.gen5.Gen5Extractor;
import pokemoninfodisplayer.models.memory.PokemonMemoryModel;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.UnsupportedPlatformException;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public abstract class PokemonExtractor<T extends MemoryMap> implements PokemonInterface {
	
	private final MemoryDataSource<T> dataSource;
	protected final PokemonGame game;
	private final PokemonMemoryModel[] pokMemoryModelBuffer = new PokemonMemoryModel[6];
	private final PokemonMemoryModel[] pokMemoryModelCheckBuffer = new PokemonMemoryModel[6];
	private boolean autoUpdate = true;

	public PokemonExtractor(PokemonGame game, MemoryDataSource<T> dataSource) {
		this.dataSource = dataSource;
		this.game = game;
	}

	/**
	 * Setting auto update will call the update() method automatically whenever getParty(), getTrainer() etc. is called.
	 * Defaults to true.
	 * @param autoUpdate 
	 */
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}
	
	private void checkAndPerformAutoUpdate() {
		if (autoUpdate) {
			try {
				update();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	protected abstract void updatePokemonMemoryModels(PokemonMemoryModel[] party, T memoryMap);
	
	@Override
	public void update() throws Exception {
		dataSource.update();
	}

	@Override
	public void updateParty(PartyModel party) {
		checkAndPerformAutoUpdate();
		updatePokemonMemoryModels(pokMemoryModelBuffer, dataSource.getMemoryMap());
		
		for (int i = 0; i < pokMemoryModelBuffer.length; i++) {
			
			PokemonMemoryModel memModel = pokMemoryModelBuffer[i];
			PokemonModel pok = memModel.toPokemonModel();
			
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
			}
			else if (this.pokMemoryModelCheckBuffer[i].equals(memModel)){
				this.pokMemoryModelCheckBuffer[i] = null;
				party.setPartySlot(i, pok);
			}
			else {
				this.pokMemoryModelCheckBuffer[i] = null;
			}
		}
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
	public void close() throws IOException {
		dataSource.close();
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
