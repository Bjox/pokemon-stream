package pokemoninfodisplayer.data.gba;

import java.nio.ByteOrder;
import pokemoninfodisplayer.data.memory.MemoryMap;
import pokemoninfodisplayer.data.memory.MemorySegment;
import static pokemoninfodisplayer.data.memory.SegmentType.*;

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
