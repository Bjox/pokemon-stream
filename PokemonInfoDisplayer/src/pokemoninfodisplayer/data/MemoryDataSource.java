package pokemoninfodisplayer.data;

import java.io.Closeable;
import pokemoninfodisplayer.data.memory.MemoryMap;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public abstract class MemoryDataSource<T extends MemoryMap> implements Closeable {

	private final T memoryMap;
	
	public MemoryDataSource(T memoryMap) {
		this.memoryMap = memoryMap;
	}
	
	protected abstract void _update(T memoryMap) throws Exception;
	
	public void update() throws Exception {
		_update(memoryMap);
	}
	
	public T getMemoryMap() {
		return memoryMap;
	}
	
}
