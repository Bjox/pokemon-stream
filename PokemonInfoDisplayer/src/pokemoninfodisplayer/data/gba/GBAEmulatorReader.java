package pokemoninfodisplayer.data.gba;

import pokemoninfodisplayer.data.EmulatorReader;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.UnsupportedPlatformException;

/**
 * Game Boy Advance emulator reader.
 * 
 * @author Bjørnar W. Alvestad
 */
public abstract class GBAEmulatorReader extends EmulatorReader<GBAMemoryMap> {

	public GBAEmulatorReader(String windowTitle) throws ProcessNotFoundException, UnsupportedPlatformException {
		super(windowTitle, new GBAMemoryMap());
	}
	
}
