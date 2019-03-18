package pokemoninfodisplayer.models.memory;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Dword extends MemoryField {
	
	public Dword() {
		super(4);
	}

	@Override
	public int getUInt() {
		return bytes.getInt(0);
	}

	@Override
	public String getStringValue() {
		return String.format("Dword: 0x%08X, %d", getUInt(), getUInt());
	}
	
}
