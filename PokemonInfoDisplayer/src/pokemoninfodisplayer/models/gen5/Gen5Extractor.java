package pokemoninfodisplayer.models.gen5;

import pokemoninfodisplayer.PokemonInfoDisplayer;
import pokemoninfodisplayer.lowlevel.memory.MemorySegment;
import pokemoninfodisplayer.lowlevel.memory.NDSMemoryMap;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.models.GenExtractor;
import pokemoninfodisplayer.models.PokemonGame;
import pokemoninfodisplayer.models.memory.PokemonMemoryModel;
import pokemoninfodisplayer.util.Util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen5Extractor extends GenExtractor<NDSMemoryMap> {

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

	public Gen5Extractor(PokemonGame game) throws ProcessNotFoundException, UnsupportedPlatformException {
		super(game);
	}

	@Override
	protected void readMemoryModels(PokemonMemoryModel[] party) throws Exception {
		final MemorySegment wram = memoryMap().getWram();
		long ptrAddr;
		
		// Set correct start pointer address
		switch (game) {
			//case PLATINUM:
			//	ptrAddr = 0x2101D2CL; break;
			//case BLACK2_WHITE2:
			//	ptrAddr = 0x211186CL; break;
			case BLACK2_WHITE2:
				ptrAddr = 0x02259DD8; break;
			default:
				throw new AssertionError(game);
		}
		
		final long startAddr = 0x0221E3EC;
		
		// In-battle stats
		/*for (PokemonMemoryModel m : party) {
			if (m != null) {
				Gen5PokemonMemoryModel battleMon = (Gen5PokemonMemoryModel) m;
				
				long inbattle_pid_adr = startAddr;
				
				switch (game) {
					//case PLATINUM:
					//	inbattle_pid_adr += 0x54600L; break;
					case BLACK2_WHITE2:
						inbattle_pid_adr += 0x56E5CL; break;
					default:
						throw new AssertionError(game);
				}
				
				int inbattle_pid = wram.getDword(inbattle_pid_adr);

				if (battleMon.personalityValue.getUInt() == inbattle_pid) {
					battleFlagCounter++;
					if (!PokemonInfoDisplayer.DEBUG && battleFlagCounter < 20) {
						return;
					}
					
					long current_hp_adr = startAddr;
					long max_hp_adr = startAddr;
					long lvl_adr = startAddr;
					long stat_cond_adr = startAddr;
					
					switch (game) {
						//case PLATINUM:
						//	current_hp_adr += 0x59F94L;
						//	max_hp_adr += 0x59F98L;
						//	lvl_adr += 0x59FB4L;
						//	stat_cond_adr += 0x54604L;
						//	break;
							
						case BLACK2_WHITE2:
							current_hp_adr += 0x5D268L;
							max_hp_adr += 0x5D26AL;
							lvl_adr += 0x5D220L;
							stat_cond_adr += 0x56E60L;
							break;
							
						default:
							throw new AssertionError(game);
					}
					
					battleMon.currentHP.set(wram, current_hp_adr);
					battleMon.maxHP.set(wram, max_hp_adr);
					battleMon.level.set(wram, lvl_adr);
					battleMon.statusCond.set(wram, stat_cond_adr);

					return;
				}
			}
		}*/
		battleFlagCounter = 0;
		
		// Party
		long partyStart = startAddr;
		//switch (game) {
			//case PLATINUM:
			//	partyStart += 0xD094L; break;
		//	case BLACK2_WHITE2:
		//		partyStart += 0xD088L; break;
		//	default:
		//		throw new AssertionError(game);
		//}
		
		final int pokemonBlockSize = 236;
		
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
				while (pos < 0xEB) {
					int decWord = Util.readWord(encPartyElement, pos) ^ prng.rand();
					decPartyElement[pos++] = (byte) (decWord & 0xFF);
					decPartyElement[pos++] = (byte) ((decWord >>> 8) & 0xFF);
				}
			}

			PokemonMemoryModel pkmnMemModel = new Gen5PokemonMemoryModel();
			pkmnMemModel.update(decPartyElement);
			party[partyIndex] = pkmnMemModel; //new Gen4MemoryModel(decPartyElement);
			
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
