package pokemoninfodisplayer.lowlevel.emulator.visualboyadvance;

import pokemoninfodisplayer.lowlevel.emulator.EmulatorExtractor;
import pokemoninfodisplayer.lowlevel.process.Access;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;

/**
 * An EmulatorExtractor implementation for the
 * Visual Boy Advance GB/GBC/GBA emulator.
 * 
 * Currently only supports GBA games.
 * 
 * @author Bjørnar W. Alvestad
 */
public class VBAExtractor extends EmulatorExtractor {
	
	public static final int WRAM_SIZE = 0x40000;
	public static final String WINDOW_TITLE = "VisualBoyAdvance";
	
	public VBAExtractor() throws ProcessNotFoundException, UnsupportedPlatformException {
		super(WINDOW_TITLE, Access.READ);
	}
	
	@Override
	public long getWRAMStartAddress() throws ProcessNotOpenedException {
		long wramPtrAdr = 0x6778E8L;
		return Integer.toUnsignedLong(processReader.readInt(wramPtrAdr));
	}

	@Override
	public int getWRAMSize() {
		return WRAM_SIZE;
	}

}
