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
	
	public VBAExtractor() throws ProcessNotFoundException, UnsupportedPlatformException {
		super("VisualBoyAdvance", Access.READ);
	}
	
	public long getWRAMStartAddress() throws ProcessNotOpenedException {
		long wramPtrAdr = 0x6778E8L;
		return Integer.toUnsignedLong(processReader.readInt(wramPtrAdr));
	}
	
	@Override
	public boolean readWRAM(byte[] buffer) throws ProcessNotOpenedException {
		if (buffer.length < 0x40000) {
			throw new RuntimeException("The supplied buffer is too small.");
		}
		return processReader.readBytes(getWRAMStartAddress(), buffer, 0x40000);
	}

	

	
}
