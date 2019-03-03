package pokemoninfodisplayer.process;

import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.ProcessNotOpenedException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class ProcessReader implements IProcessReader {
	
	public final int NATIVE_ACCESS_PROCESS_READ;
	public final int NATIVE_ACCESS_PROCESS_WRITE;
	public final int pid;
	public final int nativeAccess;
	public final Access access;
	private boolean opened;

	public ProcessReader(int nativeAccessRead, int NativeAccessWrite, int pid, Access access) {
		this.opened = false;
		this.NATIVE_ACCESS_PROCESS_READ = nativeAccessRead;
		this.NATIVE_ACCESS_PROCESS_WRITE = NativeAccessWrite;
		this.pid = pid;
		this.access = access;
		this.nativeAccess = getNativeAccess(access);
	}

	protected abstract void _open() throws ProcessNotFoundException;
	protected abstract boolean _readBytes(long address, byte[] buffer, int length);
	
	@Override
	public final boolean isOpen() {
		return opened;
	}
	
	@Override
	public final void open() throws ProcessNotFoundException {
		if (isOpen()) {
			return;
		}
		_open();
		opened = true;
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
