package pokemoninfodisplayer.models.gen3;

import pokemoninfodisplayer.lowlevel.memory.GBAMemoryMap;
import pokemoninfodisplayer.lowlevel.memory.MemoryMap;
import pokemoninfodisplayer.lowlevel.memory.MemorySegment;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.models.GenExtractor;
import pokemoninfodisplayer.models.PokemonGame;
import pokemoninfodisplayer.models.memory.PokemonMemoryModel;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen3Extractor extends GenExtractor<GBAMemoryMap> {

	public Gen3Extractor(PokemonGame game) throws ProcessNotFoundException, UnsupportedPlatformException {
		super(game);
	}

	@Override
	protected void readMemoryModels(PokemonMemoryModel[] party) throws Exception {
		final int partyElementSize = 100;
		final long partyStart;
		
		switch (game) {
			case FIRERED_LEAFGREEN:
				partyStart = 0x2024284L; break;
			case RUBY_SAPPHIRE:
				partyStart = 0x3004360L; break;
			case EMERALD:
				partyStart = 0x20244ECL; break;
			default:
				throw new AssertionError(game);
		}
		
		for (int partyIndex = 0; partyIndex < 6; partyIndex++) {
			byte[] partyElementBytes = new byte[partyElementSize];
			memoryMap().readBytes(partyElementBytes, partyStart + (partyIndex * partyElementSize), partyElementSize);
			party[partyIndex] = new Gen3MemoryModel(partyElementBytes);
		}
	}
	
}
