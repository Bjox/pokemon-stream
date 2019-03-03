package pokemoninfodisplayer.process.exceptions;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class UnsupportedPlatformException extends Exception {

	public UnsupportedPlatformException(String platformName) {
		super(String.format("Unsupported platform \"%s\"", platformName));
	}
	
	public UnsupportedPlatformException() {
		super("Unsupported platform");
	}
	
}
