package pokemoninfodisplayer.models;

import static pokemoninfodisplayer.models.Emulator.*;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum GameConsole {
	
	GAME_BOY_ADVANCE(VISUAL_BOY_ADVANCE),
	NINTENDO_DS(DESMUME)
	;
	
	public final Emulator[] emulators;

	private GameConsole(Emulator... emulators) {
		this.emulators = emulators;
	}
	
}
