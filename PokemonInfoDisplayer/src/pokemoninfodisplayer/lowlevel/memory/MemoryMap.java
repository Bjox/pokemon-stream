package pokemoninfodisplayer.lowlevel.memory;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class MemoryMap implements Iterable<MemorySegment> {
	
	private final List<MemorySegment> segments;
	private final Map<SegmentType, MemorySegment> segmentTypeMap;
	private final ByteOrder byteOrder;

	public MemoryMap(ByteOrder byteOrder) {
		this.segments = new ArrayList<>();
		this.segmentTypeMap = new EnumMap<>(SegmentType.class);
		this.byteOrder = byteOrder;
	}
	
	protected final void addMemorySegment(int size, long address, SegmentType type) {
		addMemorySegment(new MemorySegment(size, address, type));
	}
	
	protected final void addMemorySegment(byte[] data, long address, SegmentType type) {
		addMemorySegment(new MemorySegment(data, address, type));
	}
	
	protected final void addMemorySegment(MemorySegment newSegment) {
		for (MemorySegment s : segments) {
			if (s.intersect(newSegment)) {
				throw new OverlappingMemorySegmentsException(newSegment, s);
			}
		}
		
		if (segmentTypeMap.containsKey(newSegment.type)) {
			throw new RuntimeException(String.format("Memory map already contains segment type %s", newSegment.type));
		}
		
		newSegment.setByteOrder(byteOrder);
		segments.add(newSegment);
		segmentTypeMap.put(newSegment.type, newSegment);
		
		Collections.sort(segments);
	}
	
	protected final int KB(int kb) {
		return kb * 1024;
	}
	
	protected final int MB(int mb) {
		return mb * 1024 * 1024;
	}
	
	public byte readByte(long addr) {
		return getSegment(addr).getByte(addr);
	}
	
	public int readUByte(long addr) {
		return getSegment(addr).getUByte(addr);
	}
	
	public int readInt(long addr) {
		return getSegment(addr).getDword(addr);
	}
	
	public final MemorySegment getSegment(long addr) {
		try {
			return segments.stream()
				.filter(ms -> ms.isAddressable(addr))
				.findFirst()
				.get();
		} catch (NoSuchElementException e) {
			throw new MemorySegment.UnaddressableLocationException(addr, this);
		}
	}
	
	public final MemorySegment getSegment(SegmentType type) {
		return segmentTypeMap.get(type);
	}

	@Override
	public Iterator<MemorySegment> iterator() {
		return segments.iterator();
	}
	
	
	public static class OverlappingMemorySegmentsException extends RuntimeException {
		public OverlappingMemorySegmentsException(MemorySegment s1, MemorySegment s2) {
			super(String.format("%s intersects with %s", s1, s2));
		}
	}
	
	public interface Wram { public MemorySegment getWram(); }
	public interface Iram { public MemorySegment getIram(); }
	
}
