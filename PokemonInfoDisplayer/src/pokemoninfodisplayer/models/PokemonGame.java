package pokemoninfodisplayer.models;

import static pokemoninfodisplayer.models.GameConsole.*;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum PokemonGame {
	RUBY_SAPPHIRE(       3, GAME_BOY_ADVANCE),
	FIRERED_LEAFGREEN(   3, GAME_BOY_ADVANCE),
	EMERALD(             3, GAME_BOY_ADVANCE),
	PLATINUM(            4, NINTENDO_DS),
	HEARTGOLD_SOULSILVER(4, NINTENDO_DS),
	BLACK2_WHITE2(       5, NINTENDO_DS)
	;
	
	public final int generation;
	public final GameConsole gameConsole;
	
	private PokemonGame(int generation, GameConsole gameConsole) {
		this.generation = generation;
		this.gameConsole = gameConsole;
	}
	
}
