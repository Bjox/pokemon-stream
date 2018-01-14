package pokemoninfodisplayer.lowlevel.emulator;

import pokemoninfodisplayer.lowlevel.process.Access;
import pokemoninfodisplayer.lowlevel.process.IProcessMemoryReader;
import pokemoninfodisplayer.lowlevel.process.ProcessReaderFactory;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class EmulatorExtractor {
	
	protected final IProcessMemoryReader processReader;
	
	public EmulatorExtractor(String windowTitle, Access access)
			throws ProcessNotFoundException, UnsupportedPlatformException {
		this.processReader = ProcessReaderFactory.create(windowTitle, access);
	}
	
	public EmulatorExtractor(int pid, Access access)
			throws ProcessNotFoundException, UnsupportedPlatformException {
		this.processReader = ProcessReaderFactory.create(pid, access);
	}
	
	public void open() throws ProcessNotFoundException {
		processReader.openProcess();
	}
	
	public boolean close() {
		return processReader.closeProcess();
	}
	
	public boolean isOpen() {
		return processReader.isOpen();
	}
	
	public byte[] createWRAMBuffer() {
		return new byte[getWRAMSize()];
	}
	
	public boolean readWRAM(byte[] buffer) throws ProcessNotOpenedException {
		int wramSize = getWRAMSize();
		if (buffer.length < wramSize) {
			throw new RuntimeException("The supplied buffer is too small.");
		}
		return processReader.readBytes(getWRAMStartAddress(), buffer, wramSize);
	}
	
	public abstract int getWRAMSize();
	protected abstract long getWRAMStartAddress() throws ProcessNotOpenedException;
}
