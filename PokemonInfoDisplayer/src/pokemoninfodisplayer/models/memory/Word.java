package pokemoninfodisplayer.models.memory;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Word extends MemoryField {
	
	public Word() {
		super(2);
	}

	@Override
	public int getUInt() {
		return Short.toUnsignedInt(bytes.getShort(0));
	}

	@Override
	public String getStringValue() {
		return String.format("Word: 0x%04X, %d", getUInt(), getUInt());
	}
	
}
