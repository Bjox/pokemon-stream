package pokemoninfodisplayer.data.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MemorySegment implements Comparable<MemorySegment> {
	
	private final ByteBuffer dataBuffer;
	private final byte[] data;
	private final long address;
	public final SegmentType type;

	public MemorySegment(byte[] data, long address, SegmentType type) {
		this(data.length, address, type);
		System.arraycopy(data, 0, this.data, 0, data.length);
	}
	
	public MemorySegment(int size, long address, SegmentType type) {
		this.data = new byte[size];
		this.dataBuffer = ByteBuffer.wrap(this.data);
		this.dataBuffer.rewind();
		this.address = address;
		this.type = type;
	}
	
	public void setByteOrder(ByteOrder byteOrder) {
		this.dataBuffer.order(byteOrder);
	}
	
	private int index(long addr) {
		return (int)(addr - this.address);
	}
	
	private long address(int index) {
		return this.address + index;
	}
	
	private void checkBounds(long addr) {
		int i = index(addr);
		if (i < 0 || i > size()) {
			throw new UnaddressableLocationException(this, addr);
		}
	}
	
	public byte getByte(long addr) {
		checkBounds(addr);
		return data[index(addr)];
	}
	
	public int getUByte(long addr) {
		return Byte.toUnsignedInt(getByte(addr));
	}
	
	public int getDword(long addr) {
		checkBounds(addr);
		return dataBuffer.getInt(index(addr));
	}
	
	public int getWord(long addr) {
		checkBounds(addr);
		return (int)dataBuffer.getShort(index(addr));
	}
	
	public int getUWord(long addr) {
		checkBounds(addr);
		return Short.toUnsignedInt(dataBuffer.getShort(index(addr)));
	}
	
	public void get(byte[] destinationBuffer, long addr, int length) {
		checkBounds(addr);
		dataBuffer.position(index(addr));
		dataBuffer.get(destinationBuffer, 0, length);
	}
	
	public void get(ByteBuffer destinationBuffer, long addr) {
		checkBounds(addr);
		destinationBuffer.rewind();
		destinationBuffer.put(this.data, index(addr), destinationBuffer.capacity());
	}
	
	/**
	 * Fill this memory region with n bytes provided by the
	 * specified byte array, where n is the size of this
	 * memory region. Start reading bytes from data at
	 * the specified offset. If the specified data array
	 * does not contain enough bytes to completely fill
	 * the memory region, an ArrayIndexOutOfBoundsException
	 * will be thrown.
	 * @param data
	 * @param offset 
	 */
	public void fill(byte[] data, int offset) {
		if (data.length + offset < size()) throw new ArrayIndexOutOfBoundsException(
				String.format("The provided data array (len=%d) does not contain enough bytes to fill %s.", data.length, this));
		
		System.arraycopy(data, offset, this.data, 0, this.data.length);
	}
	
	/**
	 * The size in bytes.
	 * @return 
	 */
	public int size() {
		return data.length;
	}
	
	/**
	 * Address of the first addressable byte.
	 * @return 
	 */
	public long startAddress() {
		return address;
	}
	
	/**
	 * Address of the last addressable byte.
	 * @return 
	 */
	public long endAddress() {
		return address + size() - 1;
	}
	
	public boolean isAddressable(long address) {
		return startAddress() <= address && address <= endAddress();
	}
	
	public boolean intersect(MemorySegment other) {
		return 
				isAddressable(other.startAddress()) ||
				isAddressable(other.endAddress()) ||
				other.isAddressable(startAddress()) ||
				other.isAddressable(endAddress());
	}
	
	/**
	 * Returns a reference to the backing byte array.
	 * Use only for writing/filling the memory segment.
	 * @return 
	 */
	public byte[] getBackingByteArray() {
		return data;
	}
	
	public String formattedSizeStr() {
		int size = size();
		final String[] units = { "B", "KiB", "MiB", "GiB" };
		int unitIndex = 0;
		while (size > 1023 && unitIndex < units.length) {
			size /= 1024;
			unitIndex++;
		}
		return size + units[unitIndex];
	}

	@Override
	public String toString() {
		if (type == null)
			return String.format("MemorySegment start=0x%X size=%s", address, formattedSizeStr());
		else
			return String.format("MemorySegment %s start=0x%X size=%s", type, address, formattedSizeStr());
	}

	@Override
	public int compareTo(MemorySegment o) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;
		
		if (this == o) return EQUAL;
		
		if (this.address < o.address) return BEFORE;
		if (this.address > o.address) return AFTER;
		
		return EQUAL;
	}
	
}
