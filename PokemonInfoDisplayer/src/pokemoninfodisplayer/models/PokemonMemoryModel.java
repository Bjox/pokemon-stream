package pokemoninfodisplayer.models;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class PokemonMemoryModel {
	
	protected final byte[] rawBytes;
	
	public PokemonMemoryModel(byte[] rawBytes) {
		this.rawBytes = rawBytes;
	}
	
	public abstract PokemonModel toPokemonModel();
	public abstract boolean validateChecksum();
}
