package pokemoninfodisplayer.lowlevel.process.windows;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import pokemoninfodisplayer.lowlevel.process.Access;
import pokemoninfodisplayer.lowlevel.process.ProcessMemoryReader;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.WindowNotFoundException;

/**
 * Class for reading process memory on Windows operating systems.
 *
 * @author Bjørnar W. Alvestad
 */
public class Win32ProcessMemoryReader extends ProcessMemoryReader {

	private static final int WIN32_ACCESS_PROCESS_READ = 0x10;
	private static final int WIN32_ACCESS_PROCESS_WRITE = 0x20;
	private static final int WIN32_ACCESS_PROCESS_OPERATION = 0x08;

	private static final User32 USER32 = User32.INSTANCE;
	private static final Kernel32 KERNEL32 = Kernel32.INSTANCE;

	private Pointer processHandle;

	/**
	 * Create a new ProcessMemoryReader using process id (pid).
	 *
	 * @param pid
	 * @param access
	 */
	public Win32ProcessMemoryReader(int pid, Access access) {
		super(
				WIN32_ACCESS_PROCESS_READ,
				WIN32_ACCESS_PROCESS_WRITE | WIN32_ACCESS_PROCESS_OPERATION,
				pid,
				access
		);
	}

	/**
	 * Create a new ProcessMemoryReader by finding the process with a given window title.
	 *
	 * @param windowTitle
	 * @param access
	 * @throws WindowNotFoundException
	 */
	public Win32ProcessMemoryReader(String windowTitle, Access access) throws WindowNotFoundException {
		this(getPidFromWindowTitle(windowTitle), access);
	}

	@Override
	protected void _openProcess() throws ProcessNotFoundException {
		this.processHandle = KERNEL32.OpenProcess(nativeAccess, false, pid);
		if (this.processHandle == null) {
			throw new ProcessNotFoundException(pid);
		}
	}

	@Override
	protected boolean _closeProcess() {
		return KERNEL32.CloseHandle(processHandle) != 0;
	}

	/**
	 * Read bytes from the memory area of this process.
	 *
	 * @param address The address where this read operation should start.
	 * @param buffer The buffer in which to place the read bytes.
	 * @param length The number of bytes to read.
	 * @return
	 */
	@Override
	protected boolean _readBytes(long address, byte[] buffer, int length) {
		return KERNEL32.ReadProcessMemory(processHandle, address, buffer, length, null) != 0;
	}

	/**
	 * Reads a single integer from the process memory.
	 *
	 * @param address
	 * @return
	 */
	@Override
	protected int _readInt(long address) {
		byte[] buff = new byte[Integer.BYTES];
		KERNEL32.ReadProcessMemory(processHandle, address, buff, buff.length, null);
		return ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	private static int getPidFromWindowTitle(String windowTitle) throws WindowNotFoundException {
		Pointer windowHandle = USER32.FindWindowA(null, windowTitle);
		IntByReference pidPtr = new IntByReference();
		USER32.GetWindowThreadProcessId(windowHandle, pidPtr);
		int pid = pidPtr.getValue();

		if (windowHandle == null || pid == 0) {
			throw new WindowNotFoundException(windowTitle);
		}

		return pid;
	}

}
