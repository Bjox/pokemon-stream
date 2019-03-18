package pokemoninfodisplayer.models.event;

import pokemoninfodisplayer.models.BattleFlag;
import pokemoninfodisplayer.models.PokemonModel;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class PokemonHitPointChangeEvent {
	
	public final PokemonModel pokemon;
	public final BattleFlag battleFlag;
	public final int newHp;
	public final int oldHp;

	public PokemonHitPointChangeEvent(PokemonModel pokemon, BattleFlag battleFlag, int newHp, int oldHp) {
		this.pokemon = pokemon;
		this.battleFlag = battleFlag;
		this.newHp = newHp;
		this.oldHp = oldHp;
	}
	
}
