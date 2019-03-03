package pokemoninfodisplayer.data.gba;

import javax.naming.OperationNotSupportedException;
import pokemoninfodisplayer.data.memory.SegmentType;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.process.exceptions.UnsupportedPlatformException;

/**
 * Visual Boy Advance emulator reader.
 * 
 * @author Bjørnar W. Alvestad
 */
public class VBAReader extends GBAEmulatorReader {

	public static final String WINDOW_TITLE = "VisualBoyAdvance";
	
	public VBAReader() throws ProcessNotFoundException, UnsupportedPlatformException {
		super(WINDOW_TITLE);
		open();
	}

	@Override
	protected long getProcessAddressForSegment(SegmentType type) throws ProcessNotOpenedException, OperationNotSupportedException {
		switch (type) {
			case WRAM:
				final long wramPtrAdr = 0x2778E8L + 0x400000L;
				return Integer.toUnsignedLong(readInt(wramPtrAdr));
				
			case IRAM:
				final long iramPtrAdr = 0x2778F0L + 0x400000L;
				return Integer.toUnsignedLong(readInt(iramPtrAdr));
				
			default:
				throw new OperationNotSupportedException();
		}
	}
	
}
