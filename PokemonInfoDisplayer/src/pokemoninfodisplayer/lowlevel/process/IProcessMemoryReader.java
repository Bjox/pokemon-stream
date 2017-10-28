package pokemoninfodisplayer.lowlevel.process;

import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IProcessMemoryReader {
	
	public void openProcess() throws ProcessNotFoundException;
	public boolean closeProcess();
	public boolean isOpen();
	public boolean readBytes(long address, byte[] buffer, int length) throws ProcessNotOpenedException;
	public int readInt(long address) throws ProcessNotOpenedException;
	
}
