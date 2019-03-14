package pokemoninfodisplayer;

import java.io.Closeable;
import pokemoninfodisplayer.models.BattleFlag;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.event.PokemonKillHandler;
import pokemoninfodisplayer.models.TrainerModel;
import pokemoninfodisplayer.models.event.PokemonHitPointChangeHandler;

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
	
	@Deprecated
	int getActivePid();
	
	BattleFlag getBattleFlag();
	
	void addPokemonKillHandler(PokemonKillHandler handler);
	
	void addPokemonHPChangeHandler(PokemonHitPointChangeHandler handler);
	
}
