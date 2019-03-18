package pokemoninfodisplayer.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Util {
	
	public static void printByteArray(byte[] array) {
		printByteArray(array, array.length);
	}
	
	public static void printByteArray(byte[] array, int length) {
		printByteArray(array, 0, length);
	}
	
	public static void printByteArray(byte[] array, int offset, int length) {
		for (int i = offset; i < Math.min(array.length, length); i++) {
			System.out.printf("%02X ", array[i]);
			if (i % 16 == 15) {
				System.out.println();
			}
		}
		System.out.println();
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
	
	public static byte[] XOR(byte[] a, byte[] b) {
		byte[] bytes = new byte[a.length];
		
		for (int i = 0; i < a.length; i++){
			bytes[i] = (byte) (0xFF & (Byte.toUnsignedInt(a[i]) ^ Byte.toUnsignedInt(b[i])));
		}
		
		return bytes;
	}
	
	public static int byteArrayToUint(byte[] bytes) {
		int val = 0;
		
		for (int i = 0; i < bytes.length; i++)  {
			val |= Byte.toUnsignedInt(bytes[i]) << i;
		}
		
		return val;
	}
	
}
