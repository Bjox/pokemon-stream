package pokemoninfodisplayer.lowlevel.emulator;

import pokemoninfodisplayer.lowlevel.memory.GBAMemoryMap;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class GBAExtractor extends EmulatorExtractor<GBAMemoryMap> {
	
	public GBAExtractor(String windowTitle)
			throws ProcessNotFoundException, UnsupportedPlatformException {
		super(windowTitle, new GBAMemoryMap());
	}
	
	
}
