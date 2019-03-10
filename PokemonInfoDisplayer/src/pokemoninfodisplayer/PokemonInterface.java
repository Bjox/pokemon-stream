package pokemoninfodisplayer;

import java.io.Closeable;
import pokemoninfodisplayer.data.memory.MemoryMap;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonKillHandler;
import pokemoninfodisplayer.models.TrainerModel;
import pokemoninfodisplayer.models.memory.Dword;

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
	int getActivePid();
	boolean isInBattle();
	void addPokemonKillHandler(PokemonKillHandler handler);
	
}
