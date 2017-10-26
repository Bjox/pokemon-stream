package pokemoninfodisplayer.jna;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ProcessMemoryReader {
	
	public static final int ACCESS_PROCESS_VM_READ = 0x10;
	public static final int ACCESS_PROCESS_VM_WRITE = 0x20;
	public static final int ACCESS_PROCESS_VM_OPERATION = 0x08;
	
	private static final User32 USER32 = User32.INSTANCE;
	private static final Kernel32 KERNEL32 = Kernel32.INSTANCE;
	
	
	private final Pointer processHandle;
	public final int pid;
	private boolean closed;
	
	public ProcessMemoryReader(int pid, int access) throws ProcessNotFoundException {
		this.pid = pid;
		this.processHandle = KERNEL32.OpenProcess(access, false, pid);
		
		if (this.processHandle == null) {
			throw new ProcessNotFoundException(pid);
		}
	}
	
	public ProcessMemoryReader(String windowTitle, int access) throws ProcessNotFoundException {
		Pointer windowHandle = USER32.FindWindowA(null, windowTitle);
		
		IntByReference pidPtr = new IntByReference();
		USER32.GetWindowThreadProcessId(windowHandle, pidPtr);
		this.pid = pidPtr.getValue();
		
		this.processHandle = KERNEL32.OpenProcess(access, false, pid);
		
		if (windowHandle == null || this.pid == 0 || this.processHandle == null) {
			throw new ProcessNotFoundException(windowTitle);
		}
	}
	
	/**
	 * Close this process memory reader.
	 * @return A nonzero value if the function succeeds.
	 */
	public int close() {
		closed = true;
		return KERNEL32.CloseHandle(processHandle);
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	/**
	 * Read bytes from the memory area of this process.
	 * @param address The address where this read operation should start.
	 * @param buffer The buffer in which to place the read bytes.
	 * @param length The number of bytes to read.
	 * @return 
	 */
	public int readBytes(long address, byte[] buffer, int length) {
		return KERNEL32.ReadProcessMemory(processHandle, address, buffer, length, null);
	}
	
	public int readInt(long address) {
		byte[] buff = new byte[Integer.BYTES];
		KERNEL32.ReadProcessMemory(processHandle, address, buff, buff.length, null);
		return ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
}
