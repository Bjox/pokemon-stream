package pokemoninfodisplayer.models.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import pokemoninfodisplayer.lowlevel.memory.MemorySegment;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class MemoryField {
	
	protected final ByteBuffer bytes;
	protected final int size;
	
	public MemoryField(int size) {
		this.size = size;
		bytes = ByteBuffer.allocate(size);
		bytes.order(ByteOrder.LITTLE_ENDIAN);
	}
	
	public void set(byte[] data, int offset) {
		bytes.rewind();
		bytes.put(data, offset, bytes.capacity());
	}
	
	public void set(MemorySegment segment, long addr) {
		bytes.rewind();
		segment.get(bytes, addr);
	}
	
	public byte[] getBytes() {
		return Arrays.copyOf(bytes.array(), bytes.capacity());
	}

	@Override
	public String toString() {
		return getStringValue();
	}
	
	public abstract String getStringValue();
	public abstract int getUInt();
}
