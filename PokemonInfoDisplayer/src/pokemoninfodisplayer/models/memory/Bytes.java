package pokemoninfodisplayer.models.memory;

import java.util.Arrays;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Bytes extends MemoryField {
	
	public Bytes(int size) {
		super(size);
	}

	@Override
	public int getUInt() {
		int value = 0;
		for (int i = 0; i < Math.min(Integer.BYTES, bytes.capacity()); i++) {
			value |= Byte.toUnsignedInt(bytes.get(i)) << (i*8);
		}
		return value;
	}

	@Override
	public String getStringValue() {
		String str = "";
		for (int i = 0; i < bytes.capacity(); i++) {
			str += String.format(", 0x%02X", bytes.get(i));
		}
		return "Bytes: [" + str.substring(2) + "]";
	}
	
	public byte[] getBytes() {
		return Arrays.copyOf(bytes.array(), bytes.capacity());
	}
	
}
