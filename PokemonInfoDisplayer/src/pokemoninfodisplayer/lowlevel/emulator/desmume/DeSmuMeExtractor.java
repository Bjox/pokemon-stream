package pokemoninfodisplayer.lowlevel.emulator.desmume;

import pokemoninfodisplayer.lowlevel.emulator.EmulatorExtractor;
import pokemoninfodisplayer.lowlevel.emulator.IEmulatorExtractor;
import pokemoninfodisplayer.lowlevel.process.Access;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;

/**
 * An IEmulatorExtractor implementation for the
 * DeSmuME NDS emulator.
 * 
 * Currently only supports NDS games.
 * 
 * @author Bjørnar W. Alvestad
 */
public class DeSmuMeExtractor extends EmulatorExtractor {
	
	public static final String WINDOW_TITLE = "Paused"; //"DeSmuME 0.9.11 x64";
	
	public DeSmuMeExtractor() throws ProcessNotFoundException, UnsupportedPlatformException {
		super(WINDOW_TITLE, Access.READ);
	}

	@Override
	public boolean readWRAM(byte[] buffer) throws ProcessNotOpenedException {
		if (buffer.length < 0x400000) {
			throw new RuntimeException("The supplied buffer is too small.");
		}
		return processReader.readBytes(0x145411250L, buffer, 0x400000);
	}
	
}
