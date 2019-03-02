package pokemoninfodisplayer.models;

import pokemoninfodisplayer.models.memory.PokemonMemoryModel;
import pokemoninfodisplayer.PokemonInfoDisplayer;
import pokemoninfodisplayer.lowlevel.emulator.DeSmuMeExtractor;
import pokemoninfodisplayer.lowlevel.emulator.EmulatorExtractor;
import pokemoninfodisplayer.lowlevel.emulator.VBAExtractor;
import pokemoninfodisplayer.lowlevel.memory.MemoryMap;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public abstract class GenExtractor<T extends MemoryMap> {
	
	private final EmulatorExtractor<T> emuExtractor;
	private final PokemonMemoryModel[] memModelBuffer;
	private final PokemonMemoryModel[] memModelCheckBuffer;
	public final PokemonGame game;

	public GenExtractor(PokemonGame game) throws ProcessNotFoundException, UnsupportedPlatformException {
		this.emuExtractor = createEmulatorExtractor(game);
		this.game = game;
		this.emuExtractor.open();
		this.memModelBuffer = new PokemonMemoryModel[6];
		this.memModelCheckBuffer = new PokemonMemoryModel[6];
	}
	
	protected T memoryMap() {
		return emuExtractor.memoryMap;
	}
	
	public void close() {
		this.emuExtractor.close();
	}
	
	public final void update(PartyModel party) throws Exception {
		emuExtractor.update();
		readMemoryModels(memModelBuffer);
		
		for (int i = 0; i < memModelBuffer.length; i++) {
			
			PokemonMemoryModel memModel = memModelBuffer[i];
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
			
			if (this.memModelCheckBuffer[i] == null) {
				this.memModelCheckBuffer[i] = memModel;
			}
			else if (this.memModelCheckBuffer[i].equals(memModel)){
				this.memModelCheckBuffer[i] = null;
				party.setPartySlot(i, pok);
			}
			else {
				this.memModelCheckBuffer[i] = null;
			}
		}
	}
	
	protected abstract void readMemoryModels(PokemonMemoryModel[] party) throws Exception;
	
	private static EmulatorExtractor createEmulatorExtractor(PokemonGame game)
			throws ProcessNotFoundException, UnsupportedPlatformException {
		switch (game.generation) {
			case 3:
				return new VBAExtractor();
			case 4:
				return new DeSmuMeExtractor();
			case 5:
				return new DeSmuMeExtractor();
			default:
				throw new UnsupportedOperationException("No emulator extractor is implemented for generation " + game.generation);
		}
	}
	
	
}
