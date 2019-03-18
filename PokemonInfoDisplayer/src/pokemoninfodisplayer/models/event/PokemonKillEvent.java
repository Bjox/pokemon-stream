package pokemoninfodisplayer.models.event;

import pokemoninfodisplayer.models.BattleFlag;
import pokemoninfodisplayer.models.PokemonModel;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class PokemonKillEvent {
	
	public final PokemonModel pokemon;
	public final BattleFlag battleType;

	public PokemonKillEvent(PokemonModel pokemon, BattleFlag battleType) {
		this.pokemon = pokemon;
		this.battleType = battleType;
	}
	
}
