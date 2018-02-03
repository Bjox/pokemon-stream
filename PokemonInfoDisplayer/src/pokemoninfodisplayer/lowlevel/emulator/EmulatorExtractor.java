package pokemoninfodisplayer.lowlevel.emulator;

import javax.naming.OperationNotSupportedException;
import pokemoninfodisplayer.lowlevel.memory.MemoryMap;
import pokemoninfodisplayer.lowlevel.memory.MemorySegment;
import pokemoninfodisplayer.lowlevel.memory.SegmentType;
import pokemoninfodisplayer.lowlevel.process.Access;
import pokemoninfodisplayer.lowlevel.process.IProcessMemoryReader;
import pokemoninfodisplayer.lowlevel.process.ProcessReaderFactory;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public abstract class EmulatorExtractor<T extends MemoryMap> {
	
	public final T memoryMap;
	protected final IProcessMemoryReader processReader;
	
	public EmulatorExtractor(String windowTitle, T memoryMap)
			throws ProcessNotFoundException, UnsupportedPlatformException {
		this.memoryMap = memoryMap;
		this.processReader = ProcessReaderFactory.create(windowTitle, Access.READ);
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
	
	public void update() throws ProcessNotOpenedException {
		for (MemorySegment segment : memoryMap) {
			try {
				long processAddr = getProcessAddressForSegment(segment.type);
				processReader.readBytes(processAddr, segment.getBackingByteArray(), segment.size());
			} catch (OperationNotSupportedException ex) {
				throw new RuntimeException(String.format(
						"No process address resolver is implemented for segment type %s in %s.", segment.type, getClass().getSimpleName()), ex);
			}
		}
	}
	
	protected abstract long getProcessAddressForSegment(SegmentType type) throws ProcessNotOpenedException, OperationNotSupportedException;
}
