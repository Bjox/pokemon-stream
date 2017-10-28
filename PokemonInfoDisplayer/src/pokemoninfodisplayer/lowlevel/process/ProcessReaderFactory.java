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
	
	public static IProcessMemoryReader create(String windowTitle, Access access)
			throws ProcessNotFoundException, UnsupportedPlatformException {
		int platform = getPlatform();
		switch (platform) {
			case Platform.WINDOWS:
				return new Win32ProcessMemoryReader(windowTitle, access);
			default:
				throw new UnsupportedPlatformException();
		}
	}
	
	public static IProcessMemoryReader create(int pid, Access access)
			throws ProcessNotFoundException, UnsupportedPlatformException {
		int platform = getPlatform();
		switch (platform) {
			case Platform.WINDOWS:
				return new Win32ProcessMemoryReader(pid, access);
			default:
				throw new UnsupportedPlatformException();
		}
	}
	
	private static int getPlatform() {
		return Platform.getOSType();
	}
	
}
