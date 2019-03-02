package vbajni;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IVBExtractor {
	
	public boolean openProcess();
	
	public boolean closeProcess();
	
	public boolean readWRAM(byte[] buffer);
	
}
