package pokemoninfodisplayer.data.nds;

import javax.naming.OperationNotSupportedException;
import pokemoninfodisplayer.data.memory.SegmentType;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.process.exceptions.UnsupportedPlatformException;

/**
 * DeSmuME emulator reader.
 * 
 * @author Bjørnar W. Alvestad
 */
public class DesmumeReader extends NDSEmulatorReader {

	public static final String WINDOW_TITLE = "DeSmuME 0.9.11 x64";
	
	/** The offset in bytes from process start to the start of NDS WRAM. */
	private static final long PROCESS_WRAM_OFFSET = 0x145411250L;
	
	public DesmumeReader() throws ProcessNotFoundException, UnsupportedPlatformException {
		super(WINDOW_TITLE);
		open();
	}

	@Override
	protected long getProcessAddressForSegment(SegmentType type) throws ProcessNotOpenedException, OperationNotSupportedException {
		switch (type) {
			case WRAM:
				return PROCESS_WRAM_OFFSET;
				
			default:
				throw new OperationNotSupportedException();
		}
	}
	
}
