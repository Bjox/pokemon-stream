package pokemoninfodisplayer.data.memory;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class UnaddressableLocationException extends RuntimeException {
	
	public UnaddressableLocationException(MemorySegment segment, long addr) {
			super(String.format("Unaddressable location 0x%X in %s", addr, segment));
		}
		
		public UnaddressableLocationException(long addr) {
			super(String.format("Unaddressable location 0x%X", addr));
		}
		
		public UnaddressableLocationException(long addr, MemoryMap map) {
			super(String.format("Unaddressable location 0x%X in %s", addr, map));
		}
	
}
