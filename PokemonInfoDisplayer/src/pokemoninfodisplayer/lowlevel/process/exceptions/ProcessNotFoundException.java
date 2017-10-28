package pokemoninfodisplayer.lowlevel.process.exceptions;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ProcessNotFoundException extends Exception {
	
	public ProcessNotFoundException(String message) {
		super(message);
	}
	
	public ProcessNotFoundException(int pid) {
		this(String.format("Process with pid %d was not found.", pid));
	}
	
}
