package pokemoninfodisplayer.lowlevel.process;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class ProcessMemoryReader implements IProcessMemoryReader {
	
	protected boolean open;

	public ProcessMemoryReader() {
		this.open = false;
	}

	@Override
	public boolean isOpen() {
		return open;
	}
	
}
