package pokemoninfodisplayer.models;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum PokemonGame {
	FIRERED_LEAFGREEN(3),
	PLATINUM(4),
	;
	
	public final int generation;

	private PokemonGame(int generation) {
		this.generation = generation;
	}
	
}
