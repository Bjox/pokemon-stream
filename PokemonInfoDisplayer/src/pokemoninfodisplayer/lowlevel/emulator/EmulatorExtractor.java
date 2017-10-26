package pokemoninfodisplayer.lowlevel.emulator;

import pokemoninfodisplayer.lowlevel.process.IProcessMemoryReader;
import pokemoninfodisplayer.lowlevel.process.ProcessReaderFactory;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class EmulatorExtractor implements IEmulatorExtractor {
	public static final int ACCESS_READ = ProcessReaderFactory.ACCESS_READ;
	public static final int ACCESS_WRITE = ProcessReaderFactory.ACCESS_WRITE;
	
	protected final IProcessMemoryReader processReader;

	public EmulatorExtractor(String windowTitle, int access) throws ProcessNotFoundException, UnsupportedPlatformException {
		this.processReader = ProcessReaderFactory.create(windowTitle, access);
	}
	
	public EmulatorExtractor(int pid, int access) throws ProcessNotFoundException, UnsupportedPlatformException {
		this.processReader = ProcessReaderFactory.create(pid, access);
	}
}
