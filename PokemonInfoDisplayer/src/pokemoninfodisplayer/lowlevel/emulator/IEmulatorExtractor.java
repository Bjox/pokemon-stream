package pokemoninfodisplayer.lowlevel.emulator;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IEmulatorExtractor {

	public boolean close();
	public boolean readWRAM(byte[] buffer);
	
}
