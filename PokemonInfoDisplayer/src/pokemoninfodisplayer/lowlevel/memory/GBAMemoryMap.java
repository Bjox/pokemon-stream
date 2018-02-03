package pokemoninfodisplayer.lowlevel.memory;

import java.nio.ByteOrder;
import static pokemoninfodisplayer.lowlevel.memory.SegmentType.*;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class GBAMemoryMap extends MemoryMap implements MemoryMap.Wram, MemoryMap.Iram {

	public GBAMemoryMap() {
		super(ByteOrder.LITTLE_ENDIAN);
		addMemorySegment(KB(256), 0x2000000, WRAM);
		addMemorySegment(KB(32), 0x3000000, IRAM);
	}
	
	@Override
	public MemorySegment getWram() {
		return getSegment(WRAM);
	}
	
	@Override
	public MemorySegment getIram() {
		return getSegment(IRAM);
	}
	
}
