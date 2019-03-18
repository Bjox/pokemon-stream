package pokemoninfodisplayer.models.event;

/**
 *
 * @author Bjørnar W. Alvestad
 */
@FunctionalInterface
public interface PokemonHitPointChangeHandler {
	
	void handle(PokemonHitPointChangeEvent event);
	
}
