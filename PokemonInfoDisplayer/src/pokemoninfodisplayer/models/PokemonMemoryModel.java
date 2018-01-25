package pokemoninfodisplayer.models;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class PokemonMemoryModel {
	
	public static final int BYTE = 1;
	public static final int WORD = 2;
	public static final int DWORD = 4;
			
	protected final byte[] rawBytes;
	
	public PokemonMemoryModel(byte[] rawBytes) {
		this.rawBytes = rawBytes;
	}
	
	public boolean isPresent() {
		for (int i = 0; i < rawBytes.length; i++) {
			if (rawBytes[i] != 0) return true;
		}
		return false;
	}
	
	public abstract PokemonModel toPokemonModel();
	public abstract boolean validateChecksum();
}
