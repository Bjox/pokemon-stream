package pokemoninfodisplayer.process;

import java.io.Closeable;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.ProcessNotOpenedException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IProcessReader extends Closeable {
	
	void open() throws ProcessNotFoundException;
	boolean isOpen();
	boolean readBytes(long address, byte[] buffer, int length) throws ProcessNotOpenedException;
	
}
