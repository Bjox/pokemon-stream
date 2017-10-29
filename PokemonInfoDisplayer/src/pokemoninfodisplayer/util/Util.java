package pokemoninfodisplayer.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Util {
	
	public static void printByteArray(byte[] array, int length) {
		for (int i = 0; i < Math.min(array.length, length); i++) {
			System.out.printf("%02X ", array[i]);
			if (i % 16 == 15) {
				System.out.println();
			}
		}
		System.out.println();
	}
	
	public static void printByteArray(byte[] array) {
		printByteArray(array, array.length);
	}
	
	/**
	 * Reads 4 bytes from a byte array at the specified offset.
	 * @param bytes
	 * @param offset
	 * @return 
	 */
	public static int readDword(byte[] bytes, int offset) {
		return ByteBuffer.wrap(bytes, offset, Integer.BYTES).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	/**
	 * Reads 2 bytes from a byte array at the specified offset.
	 * @param bytes
	 * @param offset
	 * @return 
	 */
	public static int readWord(byte[] bytes, int offset) {
		return Short.toUnsignedInt(ByteBuffer.wrap(bytes, offset, Short.BYTES).order(ByteOrder.LITTLE_ENDIAN).getShort());
	}
	
}
