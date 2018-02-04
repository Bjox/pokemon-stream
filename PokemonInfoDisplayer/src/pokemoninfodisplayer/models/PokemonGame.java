package pokemoninfodisplayer.models;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum PokemonGame {
	RUBY_SAPPHIRE(3),
	FIRERED_LEAFGREEN(3),
	EMERALD(3),
	PLATINUM(4),
	HEARTGOLD_SOULSILVER(4)
	;
	
	public final int generation;

	private PokemonGame(int generation) {
		this.generation = generation;
	}
	
}
