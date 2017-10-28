package pokemoninfodisplayer.lowlevel.emulator;

import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IEmulatorExtractor {

	public void open() throws ProcessNotFoundException;
	public boolean close();
	public boolean readWRAM(byte[] buffer) throws ProcessNotOpenedException;
	
}
