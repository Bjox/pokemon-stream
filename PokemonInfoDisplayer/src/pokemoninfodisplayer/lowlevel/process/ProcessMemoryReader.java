package pokemoninfodisplayer.lowlevel.process;

import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class ProcessMemoryReader implements IProcessMemoryReader {
	
	public final int NATIVE_ACCESS_PROCESS_READ;
	public final int NATIVE_ACCESS_PROCESS_WRITE;
	public final int pid;
	public final int nativeAccess;
	public final Access access;
	private boolean open;

	public ProcessMemoryReader(int nativeAccessRead, int NativeAccessWrite, int pid, Access access) {
		this.open = false;
		this.NATIVE_ACCESS_PROCESS_READ = nativeAccessRead;
		this.NATIVE_ACCESS_PROCESS_WRITE = NativeAccessWrite;
		this.pid = pid;
		this.access = access;
		this.nativeAccess = getNativeAccess(access);
	}

	protected abstract void _openProcess() throws ProcessNotFoundException;
	protected abstract boolean _closeProcess();
	protected abstract boolean _readBytes(long address, byte[] buffer, int length);
	protected abstract int _readInt(long address);
	
	@Override
	public final boolean isOpen() {
		return open;
	}
	
	@Override
	public final void openProcess() throws ProcessNotFoundException {
		if (isOpen()) {
			return;
		}
		_openProcess();
		open = true;
	}
	
	@Override
	public final boolean closeProcess() {
		if (!isOpen()) {
			return true;
		}
		return _closeProcess();
	}
	
	private void throwExIfNotOpened() throws ProcessNotOpenedException {
		if (!isOpen()) {
			throw new ProcessNotOpenedException();
		}
	}
	
	@Override
	public boolean readBytes(long address, byte[] buffer, int length) throws ProcessNotOpenedException {
		throwExIfNotOpened();
		return _readBytes(address, buffer, length);
	}
	
	@Override
	public int readInt(long address) throws ProcessNotOpenedException {
		throwExIfNotOpened();
		return _readInt(address);
	}
	
	protected final int getNativeAccess(Access access) {
		switch (access) {
			case READ:
				return NATIVE_ACCESS_PROCESS_READ;
			case WRITE:
				return NATIVE_ACCESS_PROCESS_WRITE;
			default:
				throw new AssertionError(access.name());
		}
	}
}
