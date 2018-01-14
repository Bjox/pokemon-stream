package pokemoninfodisplayer.lowlevel.emulator.desmume;

import pokemoninfodisplayer.lowlevel.emulator.EmulatorExtractor;
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
	
	public static final String WINDOW_TITLE = "DeSmuME 0.9.11 x64";
	
	/** The offset in bytes from process start to the start of NDS WRAM. */
	public static final long PROCESS_WRAM_OFFSET = 0x145411250L;
	/** NDS WRAM size in bytes. */
	public static final int WRAM_SIZE = 0x400000;
	
	public DeSmuMeExtractor() throws ProcessNotFoundException, UnsupportedPlatformException {
		super(WINDOW_TITLE, Access.READ);
	}

	@Override
	public int getWRAMSize() {
		return WRAM_SIZE;
	}

	@Override
	protected long getWRAMStartAddress() throws ProcessNotOpenedException {
		return PROCESS_WRAM_OFFSET;
	}
	
}
