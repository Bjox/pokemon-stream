package pokemoninfodisplayer.lowlevel.memory;

import java.nio.ByteOrder;
import static pokemoninfodisplayer.lowlevel.memory.SegmentType.*;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class NDSMemoryMap extends MemoryMap implements MemoryMap.Wram {

	public NDSMemoryMap() {
		super(ByteOrder.LITTLE_ENDIAN);
		addMemorySegment(MB(4), 0x2000000, WRAM);
	}

	@Override
	public MemorySegment getWram() {
		return getSegment(WRAM);
	}
	
}
