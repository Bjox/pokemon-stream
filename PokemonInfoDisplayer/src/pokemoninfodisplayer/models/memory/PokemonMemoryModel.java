package pokemoninfodisplayer.models.memory;

import pokemoninfodisplayer.models.PokemonModel;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class PokemonMemoryModel extends MemoryModel {
	
	public boolean isPresent() {
		for (int i = 0; i < memoryBytes.length; i++) {
			if (memoryBytes[i] != 0) return true;
		}
		return false;
	}
	
	public abstract PokemonModel toPokemonModel();
	public abstract boolean validateChecksum();
}
