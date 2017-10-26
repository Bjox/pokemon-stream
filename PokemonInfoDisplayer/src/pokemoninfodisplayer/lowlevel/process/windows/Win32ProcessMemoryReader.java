package pokemoninfodisplayer.lowlevel.process.windows;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import pokemoninfodisplayer.lowlevel.process.ProcessMemoryReader;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;

/**
 * Class for reading process memory on Windows operating systems.
 * 
 * @author Bjørnar W. Alvestad
 */
public class Win32ProcessMemoryReader extends ProcessMemoryReader {
	
	public static final int ACCESS_PROCESS_VM_READ = 0x10;
	public static final int ACCESS_PROCESS_VM_WRITE = 0x20;
	public static final int ACCESS_PROCESS_VM_OPERATION = 0x08;
	
	private static final User32 USER32 = User32.INSTANCE;
	private static final Kernel32 KERNEL32 = Kernel32.INSTANCE;
	
	private Pointer processHandle;
	public final int pid;
	public final int access;
	
	/**
	 * Create a new ProcessMemoryReader using process id (pid).
	 * @param pid
	 * @param access
	 */
	public Win32ProcessMemoryReader(int pid, int access) {
		this.pid = pid;
		this.access = access;
	}
	
	/**
	 * Create a new ProcessMemoryReader by finding the process
	 * with a given window title.
	 * @param windowTitle
	 * @param access
	 * @throws ProcessNotFoundException 
	 */
	public Win32ProcessMemoryReader(String windowTitle, int access) throws ProcessNotFoundException {
		this.access = access;
		Pointer windowHandle = USER32.FindWindowA(null, windowTitle);
		
		IntByReference pidPtr = new IntByReference();
		USER32.GetWindowThreadProcessId(windowHandle, pidPtr);
		this.pid = pidPtr.getValue();
		
		if (windowHandle == null || this.pid == 0) {
			throw new ProcessNotFoundException(windowTitle);
		}
	}
	
	@Override
	public boolean openProcess() throws ProcessNotFoundException {
		this.processHandle = KERNEL32.OpenProcess(access, false, pid);
		
		if (this.processHandle == null) {
			throw new ProcessNotFoundException(pid);
		}
		
		open = true;
		return true;
	}

	@Override
	public boolean closeProcess() {
		open = false;
		return KERNEL32.CloseHandle(processHandle) != 0;
	}
	
	/**
	 * Read bytes from the memory area of this process.
	 * @param address The address where this read operation should start.
	 * @param buffer The buffer in which to place the read bytes.
	 * @param length The number of bytes to read.
	 * @return 
	 */
	@Override
	public boolean readBytes(long address, byte[] buffer, int length) {
		return KERNEL32.ReadProcessMemory(processHandle, address, buffer, length, null) != 0;
	}
	
	/**
	 * Reads a single integer from the process memory.
	 * @param address
	 * @return 
	 */
	@Override
	public int readInt(long address) {
		byte[] buff = new byte[Integer.BYTES];
		KERNEL32.ReadProcessMemory(processHandle, address, buff, buff.length, null);
		return ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
}
