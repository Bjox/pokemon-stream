package pokemoninfodisplayer.lowlevel.emulator;

import pokemoninfodisplayer.lowlevel.process.Access;
import pokemoninfodisplayer.lowlevel.process.IProcessMemoryReader;
import pokemoninfodisplayer.lowlevel.process.ProcessReaderFactory;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class EmulatorExtractor implements IEmulatorExtractor {
	
	protected final IProcessMemoryReader processReader;

	public EmulatorExtractor(String windowTitle, Access access)
			throws ProcessNotFoundException, UnsupportedPlatformException {
		this.processReader = ProcessReaderFactory.create(windowTitle, access);
	}
	
	public EmulatorExtractor(int pid, Access access)
			throws ProcessNotFoundException, UnsupportedPlatformException {
		this.processReader = ProcessReaderFactory.create(pid, access);
	}
	
	@Override
	public void open() throws ProcessNotFoundException {
		processReader.openProcess();
	}
	
	@Override
	public boolean close() {
		return processReader.closeProcess();
	}
}
