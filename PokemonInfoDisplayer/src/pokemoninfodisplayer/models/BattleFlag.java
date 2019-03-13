package pokemoninfodisplayer.models;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum BattleFlag {
	OUT_OF_BATTLE, WILD_BATTLE, TRAINER_BATTLE;
	
	/**
	 * Return true for any kind of battle.
	 * @return 
	 */
	public boolean isInBattle() {
		return this != OUT_OF_BATTLE;
	}
	
}
