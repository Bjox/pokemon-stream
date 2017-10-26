package pokemoninfodisplayer.jna;

import vbajni.IVBExtractor;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class VBExtractorJNA extends ProcessMemoryReader implements IVBExtractor {

	public VBExtractorJNA() throws ProcessNotFoundException {
		super("VisualBoyAdvance", ProcessMemoryReader.ACCESS_PROCESS_VM_READ);
	}
	
	@Override
	public boolean openProcess() {
		return !super.isClosed();
	}

	@Override
	public boolean closeProcess() {
		if (super.isClosed()) {
			return false;
		}
		return super.close() != 0;
	}
	
	public long getWRAMStartAddress() {
		long wramPtrAdr = 0x6778E8; // The magic number
		return Integer.toUnsignedLong(super.readInt(wramPtrAdr));
	}

	@Override
	public boolean readWRAM(byte[] bytes) {
		if (bytes.length < 0x40000) {
			throw new RuntimeException("The supplied buffer is too small.");
		}
		return super.readBytes(getWRAMStartAddress(), bytes, 0x40000) != 0;
	}
	
}
