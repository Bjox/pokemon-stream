package pokemoninfodisplayer.models;

/**
 *
 * @author Bjørnar W. Alvestad
 */
@FunctionalInterface
public interface PokemonKillHandler {
	
	void handleKill(PokemonModel pokemon);
	
}