package pokemoninfodisplayer.data.nds;

import java.nio.ByteOrder;
import pokemoninfodisplayer.data.memory.MemoryMap;
import pokemoninfodisplayer.data.memory.MemorySegment;
import static pokemoninfodisplayer.data.memory.SegmentType.*;

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
