package pokemoninfodisplayer.process;

import pokemoninfodisplayer.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.windows.Win32ProcessReader;
import com.sun.jna.Platform;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class ProcessReaderFactory {
	
	public static IProcessReader create(String windowTitle, Access access) throws ProcessNotFoundException, UnsupportedPlatformException {
		int platform = getPlatform();
		
		switch (platform) {
			case Platform.WINDOWS:
				return new Win32ProcessReader(windowTitle, access);
			default:
				throw new UnsupportedPlatformException();
		}
	}
	
	public static IProcessReader create(int pid, Access access) throws ProcessNotFoundException, UnsupportedPlatformException {
		int platform = getPlatform();
		
		switch (platform) {
			case Platform.WINDOWS:
				return new Win32ProcessReader(pid, access);
			default:
				throw new UnsupportedPlatformException();
		}
	}
	
	private static int getPlatform() {
		return Platform.getOSType();
	}
	
}
