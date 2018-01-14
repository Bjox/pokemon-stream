package pokemoninfodisplayer.models.gen4;

import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.models.GenExtractor;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonGame;
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
	public void update(PartyModel party) throws ProcessNotOpenedException {
		final byte[] wram = readWRAM();
		
		final int pokemonBlockSize = 236;
		for (int partyIndex = 0; partyIndex < 6; partyIndex++) {

			byte[] encPartyElement = new byte[pokemonBlockSize];
			System.arraycopy(wram, 0x27E204 + (partyIndex * pokemonBlockSize), encPartyElement, 0, encPartyElement.length);

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
			
			Gen4MemoryModel memModel = new Gen4MemoryModel(decPartyElement);
			party.setPartySlot(partyIndex, memModel.toPokemonModel());

//			int species_id = Util.readWord(decPartyElement, 0x8);
//			int ot_id = Util.readWord(decPartyElement, 0xC);
//			int level = Byte.toUnsignedInt(decPartyElement[0x8C]);
//			int status_cond = Byte.toUnsignedInt(decPartyElement[0x88]);
//			int current_hp = Util.readWord(decPartyElement, 0x8E);
//			
//			System.out.println("species: " + species_id);
//			System.out.println("ot: " + ot_id);
//			System.out.println("level: " + level);
//			System.out.println("status_cond: " + status_cond);
//			System.out.println("current_hp: " + current_hp);
//			System.out.printf("personality value: 0x%08X\n", personalityValue);
//
//			System.out.println();
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
