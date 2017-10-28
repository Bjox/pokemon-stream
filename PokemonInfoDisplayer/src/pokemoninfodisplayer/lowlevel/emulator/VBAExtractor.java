package pokemoninfodisplayer.lowlevel.emulator;

import pokemoninfodisplayer.lowlevel.process.Access;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;

/**
 * An EmulatorExtractor implementation for the
 * Visual Boy Advance GB/GBC/GBA emulator.
 * 
 * @author Bjørnar W. Alvestad
 */
public class VBAExtractor extends EmulatorExtractor {
	
	public VBAExtractor() throws ProcessNotFoundException, UnsupportedPlatformException {
		super("VisualBoyAdvance", Access.READ);
	}
	
	@Override
	public void open() throws ProcessNotFoundException {
		processReader.openProcess();
	}
	
	@Override
	public boolean close() {
		return processReader.closeProcess();
	}
	
	public long getWRAMStartAddress() {
		long wramPtrAdr = 0x6778E8L;
		return Integer.toUnsignedLong(processReader.readInt(wramPtrAdr));
	}
	
	@Override
	public boolean readWRAM(byte[] buffer) {
		if (buffer.length < 0x40000) {
			throw new RuntimeException("The supplied buffer is too small.");
		}
		return processReader.readBytes(getWRAMStartAddress(), buffer, 0x40000);
	}

	

	
}
