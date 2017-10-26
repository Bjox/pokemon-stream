package pokemoninfodisplayer.jna;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ProcessNotFoundException extends Exception {
	public ProcessNotFoundException(int pid) {
		super(String.format("Process with pid %d was not found.", pid));
	}
	
	public ProcessNotFoundException(String windowTitle) {
		super(String.format("Window \"%s\" not found.", windowTitle));
	}
}
