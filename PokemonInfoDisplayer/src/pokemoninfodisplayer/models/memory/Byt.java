package pokemoninfodisplayer.models.memory;

/**
 * Byte. Not to be confused with java.lang.Byte
 * 
 * @author Bjørnar W. Alvestad
 */
public class Byt extends MemoryField {

	public Byt() {
		super(1);
	}

	@Override
	public int getUInt() {
		return Byte.toUnsignedInt(bytes.get(0));
	}

	@Override
	public String getStringValue() {
		return String.format("Byte: 0x%02X, %d", bytes.get(0), bytes.get(0));
	}
	
	public byte getByte() {
		return bytes.get(0);
	}
	
}
