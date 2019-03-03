package pokemoninfodisplayer.data.nds;

import pokemoninfodisplayer.data.EmulatorReader;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.UnsupportedPlatformException;

/**
 * Nintendo DS emulator reader.
 * 
 * @author Bjørnar W. Alvestad
 */
public abstract class NDSEmulatorReader extends EmulatorReader<NDSMemoryMap> {

	public NDSEmulatorReader(String windowTitle) throws ProcessNotFoundException, UnsupportedPlatformException {
		super(windowTitle, new NDSMemoryMap());
	}
	
}
