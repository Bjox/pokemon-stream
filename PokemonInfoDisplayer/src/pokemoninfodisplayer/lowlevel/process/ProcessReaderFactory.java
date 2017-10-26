package pokemoninfodisplayer.lowlevel.process;

import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.windows.Win32ProcessMemoryReader;
import com.sun.jna.Platform;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class ProcessReaderFactory {
	
	public static final int ACCESS_READ = 0;
	public static final int ACCESS_WRITE = 1;
	
	public static IProcessMemoryReader create(String windowTitle, int access) throws ProcessNotFoundException, UnsupportedPlatformException {
		int platform = getPlatform();
		access = getPlatformSpecificAccess(access);
		switch (platform) {
			case Platform.WINDOWS:
				return new Win32ProcessMemoryReader(windowTitle, access);
			default:
				throw new UnsupportedPlatformException();
		}
	}
	
	public static IProcessMemoryReader create(int pid, int access) throws ProcessNotFoundException, UnsupportedPlatformException {
		int platform = getPlatform();
		access = getPlatformSpecificAccess(access);
		switch (platform) {
			case Platform.WINDOWS:
				return new Win32ProcessMemoryReader(pid, access);
			default:
				throw new UnsupportedPlatformException();
		}
	}
	
	private static int getPlatformSpecificAccess(int access) throws UnsupportedPlatformException {
		int platform = getPlatform();
		switch (platform) {
			case Platform.WINDOWS:
				if (access == ACCESS_READ) return Win32ProcessMemoryReader.ACCESS_PROCESS_VM_READ;
				if (access == ACCESS_WRITE) return Win32ProcessMemoryReader.ACCESS_PROCESS_VM_WRITE | Win32ProcessMemoryReader.ACCESS_PROCESS_VM_OPERATION;
			default:
				throw new UnsupportedPlatformException();
		}
	}
	
	private static int getPlatform() {
		return Platform.getOSType();
	}
	
}
