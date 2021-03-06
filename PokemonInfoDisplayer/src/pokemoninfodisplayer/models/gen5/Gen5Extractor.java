package pokemoninfodisplayer.models.gen5;

import pokemoninfodisplayer.PokemonExtractor;
import pokemoninfodisplayer.data.MemoryDataSource;
import pokemoninfodisplayer.data.memory.MemorySegment;
import pokemoninfodisplayer.data.nds.NDSMemoryMap;
import pokemoninfodisplayer.graphics.InfoFrame;
import pokemoninfodisplayer.models.BattleFlag;
import pokemoninfodisplayer.models.PokemonGame;
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
	
	private boolean first = true;
	
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
		
		if (getBattleFlag().isInBattle() && !first) {
			inBattlePartyUpdate(wram, party);
		}
		else {
			first = false;
			outOfBattlePartyUpdate(wram, party);
		}
	}
	
	private void inBattlePartyUpdate(MemorySegment wram, Gen5PokemonMemoryModel[] party) {
		for (int i = 0; i < 6; i++) {
			int offset = i * 0x224;
			
			long inBattleMaxHpAddr = 0x225B1F2 + offset;
			long inBattleCurrentHpAddr = 0x225B1F4 + offset;
			long inBattleLevelAddr = 0x225B1FC + offset;
			long inBattleStatusCondAddr = 0x225B204 + offset;
			long inBattleXpPointsAddr = 0x225B1EC + offset;

			party[i].maxHP.set(wram, inBattleMaxHpAddr);
			party[i].currentHP.set(wram, inBattleCurrentHpAddr);
			party[i].level.set(wram, inBattleLevelAddr);
			party[i].statusCond.set(wram, inBattleStatusCondAddr);
			party[i].experiencePoints.set(wram, inBattleXpPointsAddr);
		}
	}
	
	private void outOfBattlePartyUpdate(MemorySegment wram, Gen5PokemonMemoryModel[] party) {
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
	protected BattleFlag getInBattleFlag(NDSMemoryMap memoryMap) {
		MemorySegment wram = memoryMap.getWram();
		boolean isInBattle = wram.getUWord(0x2143A5E) == 0xFFFF;
		
		if (!isInBattle) {
			return BattleFlag.OUT_OF_BATTLE;
		}
		
		int rewardMoney = wram.getDword(0x22576D0);
		
		// The value will sometimes "flash" 0x10101010 which we will interpret as a wild battle because it's unlikely
		// that a trainer battle will give you $269,488,144 in reward...
		return rewardMoney == 0 || rewardMoney == 0x10101010 ? BattleFlag.WILD_BATTLE : BattleFlag.TRAINER_BATTLE;
	}

	@Deprecated
	@Override
	protected int extractActivePid(NDSMemoryMap memoryMap) {
		final MemorySegment wram = memoryMap.getWram();
		long inBattlePidAddr = 0x22968F0;
		int inBattlePid = wram.getDword(inBattlePidAddr);
		if (inBattlePid == 0x0) {
			long inBattlePidAddrBackup = 0x2257D74;
			inBattlePid = wram.getDword(inBattlePidAddrBackup); 
		}
		return inBattlePid;
	}

	@Override
	protected int getActiveInBattleIndex(NDSMemoryMap memoryMap) {
		return memoryMap.getWram().getUByte(0x21DB98E);
	}

	@Override
	protected boolean isDualBattle(NDSMemoryMap memoryMap) {
		return memoryMap.getWram().getUWord(0x225C534) != 0; // enemy pok slot 2 dex number
	}

	@Override
	protected int getSecondActiveInBattleIndex(NDSMemoryMap memoryMap) {
		return -1;//return memoryMap.getWram().getUByte(0x225F450); // is wronk
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
