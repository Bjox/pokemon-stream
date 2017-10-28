package pokemoninfodisplayer.lowlevel.emulator;

import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IEmulatorExtractor {

	public void open() throws ProcessNotFoundException;
	public boolean close();
	public boolean readWRAM(byte[] buffer);
	
}
