package pokemoninfodisplayer.models;

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

	public GenExtractor(PokemonGame game) throws ProcessNotFoundException, UnsupportedPlatformException {
		this.emuExtractor = createEmulatorExtractor(game);
		this.game = game;
		this.wramBuffer = emuExtractor.createWRAMBuffer();
		this.emuExtractor.open();
	}
	
	protected byte[] readWRAM() throws ProcessNotOpenedException {
		emuExtractor.readWRAM(wramBuffer);
		return wramBuffer;
	}
	
	public void close() {
		this.emuExtractor.close();
	}
	
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
	
	public abstract void update(PartyModel party) throws ProcessNotOpenedException;
	
}
