package pokemoninfodisplayer.util;

import java.util.Arrays;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Bytes {
	
	public final int length;
	private final byte[] bytes;

	public Bytes(int length) {
		this.length = length;
		this.bytes = new byte[length];
	}

	public Bytes(byte... bytes) {
		this.length = bytes.length;
		this.bytes = Arrays.copyOf(bytes, length);
	}
	
	public void set(byte[] source, int offset) {
		System.arraycopy(source, offset, bytes, 0, length);
	}
	
	public byte getByte() {
		return bytes[0];
	}
	
	public int getWord() {
		return Util.readWord(bytes, 0);
	}
	
	public int getDword() {
		return Util.readDword(bytes, 0);
	}
	
	public byte[] getBytes() {
		return Arrays.copyOf(bytes, length);
	}
}
