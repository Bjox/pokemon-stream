package pokemoninfodisplayer.process.windows;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface User32 extends W32APIOptions {
	
	public User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

	/**
	 * Get a window handle.
	 * @param winClass Unknown
	 * @param title Window title
	 * @return Window handle
	 */
	public Pointer FindWindowA(String winClass, String title);
	
	/**
	 * Get the pid of a window.
	 * @param hWnd Window handle
	 * @param lpdwProcessId Buffer to put the pid
	 * @return Unknown
	 */
	public int GetWindowThreadProcessId(Pointer hWnd, IntByReference lpdwProcessId);
}
