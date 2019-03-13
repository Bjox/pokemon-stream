package pokemoninfodisplayer;

import java.io.Closeable;
import pokemoninfodisplayer.models.BattleFlag;
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
	
	@Deprecated
	int getActivePid();
	
	BattleFlag getBattleFlag();
	
	void addPokemonKillHandler(PokemonKillHandler handler);
	
}
