package pokemoninfodisplayer.models.gen4;

import pokemoninfodisplayer.models.PokemonMemoryModel;
import pokemoninfodisplayer.lowlevel.emulator.desmume.DeSmuMeExtractor;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.util.Util;

/**
 * Pokemon generation 4 (Platinum)
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen4MemoryModel extends PokemonMemoryModel {

	private static final int A = 0;
	private static final int B = 1;
	private static final int C = 2;
	private static final int D = 3;
	private static final int[][] SHUFFLE_ORDER
			= {
				{A, B, C, D},
				{A, B, D, C},
				{A, C, B, D},
				{A, C, D, B},
				{A, D, B, C},
				{A, D, C, B},
				{B, A, C, D},
				{B, A, D, C},
				{B, C, A, D},
				{B, C, D, A},
				{B, D, A, C},
				{B, D, C, A},
				{C, A, B, D},
				{C, A, D, B},
				{C, B, A, D},
				{C, B, D, A},
				{C, D, A, B},
				{C, D, B, A},
				{D, A, B, C},
				{D, A, C, B},
				{D, B, A, C},
				{D, B, C, A},
				{D, C, A, B},
				{D, C, B, A}
			};

	private final byte[] wrambuffer;

	public Gen4MemoryModel() throws ProcessNotFoundException, UnsupportedPlatformException {
		super(new DeSmuMeExtractor());
		this.wrambuffer = new byte[0x400000];
	}

	@Override
	public void open() throws ProcessNotFoundException {
		emulatorExtractor.open();
	}

	@Override
	public void close() {
		emulatorExtractor.close();
	}

	@Override
	public void load() throws ProcessNotOpenedException {
		boolean res = emulatorExtractor.readWRAM(this.wrambuffer);
		decryptAndUnshuffle();
	}

	private void decryptAndUnshuffle() {
		final int pokemonBlockSize = 236;
		for (int partyIndex = 0; partyIndex < 3; partyIndex++) {

			byte[] encPartyElement = new byte[pokemonBlockSize];
			System.arraycopy(wrambuffer, 0x27E204 + partyIndex * pokemonBlockSize, encPartyElement, 0, encPartyElement.length);

			int personalityValue = Util.readDword(encPartyElement, 0);
			int checksum = Util.readWord(encPartyElement, 0x6);

			// Decrypt
			{
				PRNG prng = new PRNG(checksum);
				int pos = 0x8;
				while (pos < 0x87) {
					int unencryptedWord = Util.readWord(encPartyElement, pos) ^ prng.rand();
					encPartyElement[pos++] = (byte) (unencryptedWord & 0xFF);
					encPartyElement[pos++] = (byte) ((unencryptedWord >>> 8) & 0xFF);
				}
			}

			// Unshuffle
			int shiftValue = ((personalityValue & 0x3E000) >>> 0xD) % 24;
			System.out.println("shift value: " + shiftValue);

			int[] blockOrder = SHUFFLE_ORDER[shiftValue];
			byte[] decPartyElement = new byte[pokemonBlockSize];

			System.arraycopy(encPartyElement, 0, decPartyElement, 0, 8);
			System.arraycopy(encPartyElement, 8, decPartyElement, 32 * blockOrder[A] + 8, 32);
			System.arraycopy(encPartyElement, 8 + 32, decPartyElement, 32 * blockOrder[B] + 8, 32);
			System.arraycopy(encPartyElement, 8 + 32 * 2, decPartyElement, 32 * blockOrder[C] + 8, 32);
			System.arraycopy(encPartyElement, 8 + 32 * 3, decPartyElement, 32 * blockOrder[D] + 8, 32);

			// Validate checksum
			int sum = 0;
			int pos = 0x8;
			while (pos < 0x87) {
				sum += Util.readWord(decPartyElement, pos);
				pos += 2;
			}
			sum &= 0xFFFF;
			if (checksum != sum) {
				System.err.printf("Invalid checksum: stored=0x%04X, calculated=0x%04X\n", checksum, sum);
			}

			// Decrypt battle stats
			{
				PRNG prng = new PRNG(personalityValue);
				pos = 0x88;
				while (pos < 0xEB) {
					int decWord = Util.readWord(encPartyElement, pos) ^ prng.rand();
					decPartyElement[pos++] = (byte) (decWord & 0xFF);
					decPartyElement[pos++] = (byte) ((decWord >>> 8) & 0xFF);
				}
			}

			int species_id = Util.readWord(decPartyElement, 0x8);
			System.out.println("species: " + species_id);
			int ot_id = Util.readWord(decPartyElement, 0xC);
			System.out.println("ot: " + ot_id);
			int level = Byte.toUnsignedInt(decPartyElement[0x8C]);
			System.out.println("level: " + level);
			int status_cond = Byte.toUnsignedInt(decPartyElement[0x88]);
			System.out.println("status_cond: " + status_cond);
			int current_hp = Util.readWord(decPartyElement, 0x8E);
			System.out.println("current_hp: " + current_hp);
			
			System.out.println();
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

	public static void main(String[] args) throws Exception {
		PokemonMemoryModel gen4 = new Gen4MemoryModel();
		gen4.open();
		gen4.load();
	}
}
