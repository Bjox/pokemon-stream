package pokemoninfodisplayer.data;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.naming.OperationNotSupportedException;
import pokemoninfodisplayer.data.gba.VBAReader;
import pokemoninfodisplayer.data.memory.MemoryMap;
import pokemoninfodisplayer.data.memory.MemorySegment;
import pokemoninfodisplayer.data.memory.SegmentType;
import pokemoninfodisplayer.data.nds.DesmumeReader;
import pokemoninfodisplayer.models.Emulator;
import pokemoninfodisplayer.process.Access;
import pokemoninfodisplayer.process.IProcessReader;
import pokemoninfodisplayer.process.ProcessReaderFactory;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.process.exceptions.UnsupportedPlatformException;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public abstract class EmulatorReader<T extends MemoryMap> extends MemoryDataSource<T> {
	
	protected final IProcessReader processReader;
	private final ByteBuffer intReadBuffer;

	public EmulatorReader(String windowTitle, T memoryMap) throws ProcessNotFoundException, UnsupportedPlatformException {
		super(memoryMap);
		this.processReader = ProcessReaderFactory.create(windowTitle, Access.READ);
		this.intReadBuffer = ByteBuffer.allocate(Integer.BYTES);
		this.intReadBuffer.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	protected abstract long getProcessAddressForSegment(SegmentType type) throws ProcessNotOpenedException, OperationNotSupportedException;
	
	public void open() throws ProcessNotFoundException {
		processReader.open();
	}
	
	@Override
	public void close() throws IOException {
		processReader.close();
	}
	
	public boolean isOpen() {
		return processReader.isOpen();
	}
	
	/**
	 * Reads 4 bytes from the process reader and assembles them into an int.
	 * 
	 * @param address
	 * @return
	 * @throws ProcessNotOpenedException 
	 */
	protected int readInt(long address) throws ProcessNotOpenedException {
		processReader.readBytes(address, intReadBuffer.array(), intReadBuffer.capacity());
		return intReadBuffer.getInt(0); // TODO: confirm that the byte ordering is correct. Edit: is probably correct because gen3 works
	}

	@Override
	protected void _update(T memoryMap) throws ProcessNotOpenedException {
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
	
	public static EmulatorReader createEmulatorReader(Emulator emulator) throws ProcessNotFoundException, UnsupportedPlatformException {
		switch (emulator) {
			case VISUAL_BOY_ADVANCE:
				return new VBAReader();
				
			case DESMUME:
				return new DesmumeReader();
				
			default:
				throw new UnsupportedOperationException(emulator.toString());
		}
	}
	
}
