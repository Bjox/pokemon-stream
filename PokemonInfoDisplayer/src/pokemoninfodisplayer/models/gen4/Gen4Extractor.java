package pokemoninfodisplayer.models.gen4;

import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.models.GenExtractor;
import pokemoninfodisplayer.models.PokemonGame;
import pokemoninfodisplayer.models.PokemonMemoryModel;
import pokemoninfodisplayer.util.Util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen4Extractor extends GenExtractor {

	private static final int A = 0;
	private static final int B = 1;
	private static final int C = 2;
	private static final int D = 3;
	
	private int battleFlagCounter = 0;
	
	private static final int[][] SHUFFLE_ORDER = {
		{A, B, C, D}, {A, B, D, C}, {A, C, B, D}, {A, C, D, B},
		{A, D, B, C}, {A, D, C, B}, {B, A, C, D}, {B, A, D, C},
		{B, C, A, D}, {B, C, D, A}, {B, D, A, C}, {B, D, C, A},
		{C, A, B, D}, {C, A, D, B}, {C, B, A, D}, {C, B, D, A},
		{C, D, A, B}, {C, D, B, A}, {D, A, B, C}, {D, A, C, B},
		{D, B, A, C}, {D, B, C, A}, {D, C, A, B}, {D, C, B, A}
	};

	public Gen4Extractor(PokemonGame game) throws ProcessNotFoundException, UnsupportedPlatformException {
		super(game);
	}

	@Override
	protected void readMemoryModels(PokemonMemoryModel[] party) throws ProcessNotOpenedException {
		final byte[] wram = readWRAM();
		
		final int start = Util.readDword(wram, 0x101D2C) - 0x2000000;
		
		// In-battle stats
		for (PokemonMemoryModel m : party) {
			if (m != null) {
				Gen4MemoryModel battleMon = (Gen4MemoryModel) m;
				int inbattle_pid = Util.readDword(wram, start + 0x54600);

				if (battleMon.personality_value.getDword() == inbattle_pid) {
					battleFlagCounter++;
					if (battleFlagCounter < 10) {
						return;
					}
					
					int current_hp_adr = start + 0x59F94;
					int max_hp_adr = start + 0x59F98;
					int lvl_adr = start + 0x59FB4;
					int stat_cond_adr = start + 0x54604;
					
					battleMon.current_hp.set(wram, current_hp_adr);
					battleMon.total_hp.set(wram, max_hp_adr);
					battleMon.level.set(wram, lvl_adr);
					battleMon.status_cond.set(wram, stat_cond_adr);

					return;
				}
			}
		}
		battleFlagCounter = 0;
		
		final int partyStart = start + 0xD094;
		final int pokemonBlockSize = 236;
		for (int partyIndex = 0; partyIndex < 6; partyIndex++) {

			byte[] encPartyElement = new byte[pokemonBlockSize];
			System.arraycopy(wram, partyStart + (partyIndex * pokemonBlockSize), encPartyElement, 0, encPartyElement.length);

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
				while (pos < 0xEB) {
					int decWord = Util.readWord(encPartyElement, pos) ^ prng.rand();
					decPartyElement[pos++] = (byte) (decWord & 0xFF);
					decPartyElement[pos++] = (byte) ((decWord >>> 8) & 0xFF);
				}
			}

			party[partyIndex] = new Gen4MemoryModel(decPartyElement);
		}
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
