package pokemoninfodisplayer.process.windows;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import pokemoninfodisplayer.process.Access;
import pokemoninfodisplayer.process.ProcessReader;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.WindowNotFoundException;

/**
 * Class for reading process memory on Windows operating systems.
 *
 * @author Bjørnar W. Alvestad
 */
public class Win32ProcessReader extends ProcessReader {

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
	public Win32ProcessReader(int pid, Access access) {
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
	public Win32ProcessReader(String windowTitle, Access access) throws WindowNotFoundException {
		this(getPidFromWindowTitle(windowTitle), access);
	}

	@Override
	protected void _open() throws ProcessNotFoundException {
		this.processHandle = KERNEL32.OpenProcess(nativeAccess, false, pid);
		if (this.processHandle == null) {
			throw new ProcessNotFoundException(pid);
		}
	}

	@Override
	public void close() {
		KERNEL32.CloseHandle(processHandle);
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
