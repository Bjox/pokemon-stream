package pokemoninfodisplayer.lowlevel.emulator;

import javax.naming.OperationNotSupportedException;
import pokemoninfodisplayer.lowlevel.emulator.NDSExtractor;
import pokemoninfodisplayer.lowlevel.memory.SegmentType;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;

/**
 * A DeSmuME NDS extractor.
 * 
 * Currently only supports NDS games.
 * 
 * @author Bjørnar W. Alvestad
 */
public class DeSmuMeExtractor extends NDSExtractor {
	
	public static final String WINDOW_TITLE = "DeSmuME 0.9.11 x64";
	
	/** The offset in bytes from process start to the start of NDS WRAM. */
	public static final long PROCESS_WRAM_OFFSET = 0x145411250L;
	
	public DeSmuMeExtractor() throws ProcessNotFoundException, UnsupportedPlatformException {
		super(WINDOW_TITLE);
	}

	@Override
	protected long getProcessAddressForSegment(SegmentType type)
			throws ProcessNotOpenedException, OperationNotSupportedException {
		switch (type) {
			case WRAM:
				return PROCESS_WRAM_OFFSET;
				
			default:
				throw new OperationNotSupportedException();
		}
	}
	
}
