package pokemoninfodisplayer.models.event;

/**
 *
 * @author Bjørnar W. Alvestad
 */
@FunctionalInterface
public interface PokemonKillHandler {
	
	void handle(PokemonKillEvent event);
	
}
