package pokemoninfodisplayer.lowlevel.emulator;

import pokemoninfodisplayer.lowlevel.memory.NDSMemoryMap;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class NDSExtractor extends EmulatorExtractor<NDSMemoryMap> {
	
	public NDSExtractor(String windowTitle)
			throws ProcessNotFoundException, UnsupportedPlatformException {
		super(windowTitle, new NDSMemoryMap());
	}
	
}
