package pokemoninfodisplayer.jna;

import com.sun.jna.Memory;
import com.sun.jna.Platform;
import java.util.Arrays;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Test {
	
	public static void main(String[] args) throws Exception {
		if (!Platform.isWindows()) {
			System.out.println("Application not supported on this platform.");
			return;
		}
		
		VBExtractorJNA vbap = new VBExtractorJNA();
		
		System.out.printf("%X\n", vbap.getWRAMStartAddress());
		
		byte[] bytes = new byte[0x40000];
		vbap.readWRAM(bytes);
		
		for (int i = 0; i < bytes.length; i++) {
			System.out.printf("%02X ", bytes[i]);
		}
		System.out.println();
	}
	
}
