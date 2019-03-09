package pokemoninfodisplayer;

import java.io.Closeable;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonKillHandler;
import pokemoninfodisplayer.models.TrainerModel;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface PokemonInterface extends Closeable {

	void update() throws Exception;
	@Deprecated
	void updateParty(PartyModel party);
	PartyModel getParty();
	TrainerModel getTrainer();
	boolean isInBattle();
	void addPokemonKillHandler(PokemonKillHandler handler);
	
}
