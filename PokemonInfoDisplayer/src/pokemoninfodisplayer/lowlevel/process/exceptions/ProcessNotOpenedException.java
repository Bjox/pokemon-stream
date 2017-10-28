package pokemoninfodisplayer.lowlevel.process.exceptions;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ProcessNotOpenedException extends Exception {

	public ProcessNotOpenedException() {
		super("The process has not been opened.");
	}
	
}
