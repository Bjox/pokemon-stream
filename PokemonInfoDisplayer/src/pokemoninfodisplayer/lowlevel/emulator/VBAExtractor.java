package pokemoninfodisplayer.lowlevel.emulator;

import javax.naming.OperationNotSupportedException;
import pokemoninfodisplayer.lowlevel.emulator.GBAExtractor;
import pokemoninfodisplayer.lowlevel.memory.SegmentType;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;

/**
 * A Visual Boy Advance GBA extractor.
 * 
 * Currently only supports GBA games.
 * 
 * @author Bjørnar W. Alvestad
 */
public class VBAExtractor extends GBAExtractor {

	private static final String WINDOW_TITLE = "VisualBoyAdvance";
	
	public VBAExtractor() 
			throws ProcessNotFoundException, UnsupportedPlatformException {
		super(WINDOW_TITLE);
	}

	@Override
	protected long getProcessAddressForSegment(SegmentType type)
			throws ProcessNotOpenedException, OperationNotSupportedException {
		switch (type) {
			case WRAM:
				final long wramPtrAdr = 0x2778E8L + 0x400000L;
				return Integer.toUnsignedLong(processReader.readInt(wramPtrAdr));
				
			case IRAM:
				final long iramPtrAdr = 0x2778F0L + 0x400000L;
				return Integer.toUnsignedLong(processReader.readInt(iramPtrAdr));
				
			default:
				throw new OperationNotSupportedException();
		}
	}
	
	
	
}
