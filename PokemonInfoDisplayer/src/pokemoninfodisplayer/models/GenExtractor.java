package pokemoninfodisplayer.models;

import pokemoninfodisplayer.models.memory.PokemonMemoryModel;
import pokemoninfodisplayer.PokemonInfoDisplayer;
import pokemoninfodisplayer.lowlevel.emulator.EmulatorExtractor;
import pokemoninfodisplayer.lowlevel.emulator.desmume.DeSmuMeExtractor;
import pokemoninfodisplayer.lowlevel.emulator.visualboyadvance.VBAExtractor;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class GenExtractor {
	
	private final EmulatorExtractor emuExtractor;
	public final PokemonGame game;
	private final byte[] wramBuffer;
	private final PokemonMemoryModel[] memModelBuffer;

	public GenExtractor(PokemonGame game) throws ProcessNotFoundException, UnsupportedPlatformException {
		this.emuExtractor = createEmulatorExtractor(game);
		this.game = game;
		this.wramBuffer = emuExtractor.createWRAMBuffer();
		this.emuExtractor.open();
		this.memModelBuffer = new PokemonMemoryModel[6];
	}
	
	protected byte[] readWRAM() throws ProcessNotOpenedException {
		emuExtractor.readWRAM(wramBuffer);
		return wramBuffer;
	}
	
	public void close() {
		this.emuExtractor.close();
	}
	
	public final void update(PartyModel party) throws Exception {
		readMemoryModels(memModelBuffer);
		
		for (int i = 0; i < memModelBuffer.length; i++) {
			PokemonMemoryModel memModel = memModelBuffer[i];
			PokemonModel pok = memModel.toPokemonModel();
			
			if (!memModel.isPresent() || pok.getDexEntry() < 1) {
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
			
			party.setPartySlot(i, pok);
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
			default:
				throw new UnsupportedOperationException("No emulator extractor is implemented for generation " + game.generation);
		}
	}
	
	
}
