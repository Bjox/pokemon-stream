package pokemoninfodisplayer.models.gen5;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.stream.Stream;
import pokemoninfodisplayer.PokemonExtractor;
import pokemoninfodisplayer.data.MemoryDataSource;
import pokemoninfodisplayer.data.memory.MemorySegment;
import pokemoninfodisplayer.data.nds.NDSMemoryMap;
import pokemoninfodisplayer.models.PokemonGame;
import pokemoninfodisplayer.models.memory.Dword;
import pokemoninfodisplayer.models.memory.PokemonMemoryModel;
import pokemoninfodisplayer.models.memory.Word;
import pokemoninfodisplayer.util.Util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen5Extractor extends PokemonExtractor<NDSMemoryMap, Gen5PokemonMemoryModel> {

	private static final int A = 0;
	private static final int B = 1;
	private static final int C = 2;
	private static final int D = 3;
	
	private final int CHANGE_UPDATE_BUFFER = 17; // Tested to match timings of entering battle and switching pokemon
	
	private int activePokemonChangedCounter = 0;
	private int activePokemonPid = 0;
	
	private static final int[][] SHUFFLE_ORDER = {
		{A, B, C, D}, {A, B, D, C}, {A, C, B, D}, {A, C, D, B},
		{A, D, B, C}, {A, D, C, B}, {B, A, C, D}, {B, A, D, C},
		{B, C, A, D}, {B, C, D, A}, {B, D, A, C}, {B, D, C, A},
		{C, A, B, D}, {C, A, D, B}, {C, B, A, D}, {C, B, D, A},
		{C, D, A, B}, {C, D, B, A}, {D, A, B, C}, {D, A, C, B},
		{D, B, A, C}, {D, B, C, A}, {D, C, A, B}, {D, C, B, A}
	};

	public Gen5Extractor(PokemonGame game, MemoryDataSource<NDSMemoryMap> dataSource) {
		super(game, dataSource, Gen5PokemonMemoryModel.class);
	}

	@Override
	protected void updatePokemonMemoryModels(Gen5PokemonMemoryModel[] party, NDSMemoryMap memoryMap) {
		final MemorySegment wram = memoryMap.getWram();
		boolean inBattleFlag = getInBattleFlag(memoryMap);
		
		if (inBattleFlag) {
			inBattlePartyUpdate(wram, party);
		}
		else {
			outOfBattlePartyUpdate(wram, party);
		}
	}
	
	private void inBattlePartyUpdate(MemorySegment wram, Gen5PokemonMemoryModel[] party) {
		int prepInBattlePid = wram.getDword(0x22968F0); // This value is 0x0 until you enter party menu in battle, then it becomes PID of pokemon in battle
		
		if (prepInBattlePid == 0x0) {
			prepInBattlePid = wram.getDword(0x2257D74); 
			// This value is the PID of the pokemon in spot 1 in the party in battle, needs to be used until you've entered the party menu once. 
			// Only updates when ENTERING the menu, and doesn't on switch, therefore cannot be used at all times
		}

		int inBattlePid = prepInBattlePid;

		if (inBattlePid != activePokemonPid) {
			if (activePokemonChangedCounter++ < CHANGE_UPDATE_BUFFER) {
				return;
			}
			activePokemonPid = inBattlePid;
			activePokemonChangedCounter = 0;
		}
		
		// Get the pokemon in-battle
		Gen5PokemonMemoryModel battlePokemon = Stream.of(party)
				.filter(pokemon -> pokemon.personalityValue.getUInt() == inBattlePid)
				.findFirst()
				.get();

		long inBattleMaxHpAddr = 0x22A84A0;
		long inBattleCurrentHpAddr = 0x22A849C;
		long inBattleLevelAddr = 0x22A84C0;

		battlePokemon.maxHP.set(wram, inBattleMaxHpAddr);
		battlePokemon.currentHP.set(wram, inBattleCurrentHpAddr);
		battlePokemon.level.set(wram, inBattleLevelAddr);
	}
	
	private void outOfBattlePartyUpdate(MemorySegment wram, Gen5PokemonMemoryModel[] party) {
		//Reset active pokemon
		activePokemonPid = 0;

		final long startAddr = 0x0221E3EC;
		long partyStart = startAddr;
		final int pokemonBlockSize = 220;

		for (int partyIndex = 0; partyIndex < 6; partyIndex++) {
			byte[] encPartyElement = new byte[pokemonBlockSize];
			wram.get(encPartyElement, partyStart + (partyIndex * pokemonBlockSize), pokemonBlockSize);

			int personalityValue = Util.readDword(encPartyElement, 0);
			int checksum = Util.readWord(encPartyElement, 0x6);

			// Decrypt
			{
				PRNG prng = new PRNG(checksum);
				int pos = 0x8;
				while (pos < 0x87) {
					int decryptedWord = Util.readWord(encPartyElement, pos) ^ prng.rand();
					encPartyElement[pos++] = (byte) (decryptedWord & 0xFF);
					encPartyElement[pos++] = (byte) ((decryptedWord >>> 8) & 0xFF);
				}
			}

			// Unshuffle
			int shiftValue = ((personalityValue & 0x3E000) >>> 0xD) % 24;

			int[] blockOrder = SHUFFLE_ORDER[shiftValue];
			byte[] decPartyElement = new byte[pokemonBlockSize];

			System.arraycopy(encPartyElement, 0, decPartyElement, 0, 8);
			System.arraycopy(encPartyElement, 8, decPartyElement, 32 * blockOrder[A] + 8, 32);
			System.arraycopy(encPartyElement, 8 + 32, decPartyElement, 32 * blockOrder[B] + 8, 32);
			System.arraycopy(encPartyElement, 8 + 32 * 2, decPartyElement, 32 * blockOrder[C] + 8, 32);
			System.arraycopy(encPartyElement, 8 + 32 * 3, decPartyElement, 32 * blockOrder[D] + 8, 32);

			// Decrypt battle stats
			{
				PRNG prng = new PRNG(personalityValue);
				int pos = 0x88;
				while (pos < pokemonBlockSize) {
					int decWord = Util.readWord(encPartyElement, pos) ^ prng.rand();
					decPartyElement[pos++] = (byte) (decWord & 0xFF);
					decPartyElement[pos++] = (byte) ((decWord >>> 8) & 0xFF);
				}
			}

			Gen5PokemonMemoryModel pkmnMemModel = new Gen5PokemonMemoryModel(decPartyElement);
			party[partyIndex] = pkmnMemModel;
		}
	}

	@Override
	protected boolean getInBattleFlag(NDSMemoryMap memoryMap) {
		return memoryMap.getWram().getUWord(0x2143A5E) == 0xFFFF;
	}

	private static class PRNG {
		private int last;

		public PRNG(int seed) {
			this.last = seed;
		}

		public int rand() {
			last = 0x41C64E6D * last + 0x6073;
			return (last >>> 16) & 0xFFFF;
		}
	}

}
