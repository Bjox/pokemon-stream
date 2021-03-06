package pokemoninfodisplayer.models.gen3;

import pokemoninfodisplayer.PokemonExtractor;
import pokemoninfodisplayer.data.MemoryDataSource;
import pokemoninfodisplayer.data.gba.GBAMemoryMap;
import pokemoninfodisplayer.models.BattleFlag;
import pokemoninfodisplayer.models.PokemonGame;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen3Extractor extends PokemonExtractor<GBAMemoryMap, Gen3PokemonMemoryModel> {

	public Gen3Extractor(PokemonGame game, MemoryDataSource<GBAMemoryMap> dataSource) {
		super(game, dataSource, Gen3PokemonMemoryModel.class);
		if (game.generation != 3) {
			throw new IllegalArgumentException("Invalid game. " + game.toString() + " is not generation 3");
		}
	}

	@Override
	protected void updatePokemonMemoryModels(Gen3PokemonMemoryModel[] party, GBAMemoryMap memoryMap) {
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
			memoryMap.readBytes(partyElementBytes, partyStart + (partyIndex * partyElementSize), partyElementSize);
			party[partyIndex] = new Gen3PokemonMemoryModel(partyElementBytes);
		}
	}

	// TODO: implement methods in Gen3Extractor
	@Override
	protected BattleFlag getInBattleFlag(GBAMemoryMap memoryMap) {
		return BattleFlag.OUT_OF_BATTLE;
	}

	@Override
	protected int extractActivePid(GBAMemoryMap memoryMap) {
		return 0;
	}

	@Override
	protected int getActiveInBattleIndex(GBAMemoryMap memoryMap) {
		return 0;
	}

	@Override
	protected boolean isDualBattle(GBAMemoryMap memoryMap) {
		return false;
	}

	@Override
	protected int getSecondActiveInBattleIndex(GBAMemoryMap memoryMap) {
		return 0;
	}

	
}
