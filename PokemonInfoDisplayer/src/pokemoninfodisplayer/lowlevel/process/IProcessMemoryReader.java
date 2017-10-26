package pokemoninfodisplayer.lowlevel.process;

import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IProcessMemoryReader {
	
	public boolean openProcess() throws ProcessNotFoundException;
	public boolean closeProcess();
	public boolean isOpen();
	public boolean readBytes(long address, byte[] buffer, int length);
	public int readInt(long address);
	
}
