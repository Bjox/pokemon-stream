package pokemoninfodisplayer.models.medals;

import pokemoninfodisplayer.models.medals.Medals.MedalType;
import pokemoninfodisplayer.service.PokemonStorageEntry;
import pokemoninfodisplayer.service.PokemonStorageService;

/**
 *
 * @author Endre
 */
public class Medal {
	
	public final int MIN_VALUE;
	public final boolean UNIQUE;
	
	public MedalType type;
	
	public Medal(MedalType type, int minValue, boolean unique) {
		this.MIN_VALUE = minValue;
		this.UNIQUE = unique;
		
		this.type = type;
	}
	
	public PokemonStorageEntry[] getStoredData() {
		if (PokemonStorageService.getInstance() == null){
			return null;
		}
		
		switch (this.type) {
			case MVP: return PokemonStorageService.getInstance().getKillCounts();
			case TANK: return PokemonStorageService.getInstance().getDamageTaken();
			case SURVIVOR: return PokemonStorageService.getInstance().getSurvivorCounts();
			default: return null;
		}
	}
	
}
