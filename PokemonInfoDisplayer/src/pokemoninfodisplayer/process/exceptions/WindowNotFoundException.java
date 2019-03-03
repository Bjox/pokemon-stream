package pokemoninfodisplayer.process.exceptions;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class WindowNotFoundException extends ProcessNotFoundException {
	
	public WindowNotFoundException(String windowTitle) {
		super(String.format("Window \"%s\" not found.", windowTitle));
	}
	
}
