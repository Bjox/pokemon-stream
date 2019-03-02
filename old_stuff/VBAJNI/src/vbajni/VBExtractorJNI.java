package vbajni;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class VBExtractorJNI implements IVBExtractor {
	
	static {
		System.loadLibrary("VisualBoyExtractor");
	}

	@Override
	public native boolean readWRAM(byte[] buffer);

	@Override
	public native boolean openProcess();

	@Override
	public native boolean closeProcess();
	
}
