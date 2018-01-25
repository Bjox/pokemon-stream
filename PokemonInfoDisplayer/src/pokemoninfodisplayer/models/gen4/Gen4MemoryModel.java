package pokemoninfodisplayer.models.gen4;

import pokemoninfodisplayer.models.PokemonMemoryModel;
import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.util.Bytes;
import pokemoninfodisplayer.util.Util;

/**
 * Pokemon generation 4 (Platinum)
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen4MemoryModel extends PokemonMemoryModel {
	
	public final Bytes personality_value = new Bytes(DWORD);
	public final Bytes ot_id = new Bytes(DWORD);
	public final Bytes checksum = new Bytes(WORD);
	public final Bytes current_hp = new Bytes(WORD);
	public final Bytes total_hp = new Bytes(WORD);
	public final Bytes level = new Bytes(BYTE);
	public final Bytes nickname = new Bytes(22);
	public final Bytes species_id = new Bytes(WORD);
	public final Bytes status_cond = new Bytes(BYTE);
	public final Bytes IV_stat = new Bytes(DWORD);
	public final Bytes egg_steps = new Bytes(BYTE);

	/**
	 *
	 * @param pokmem Decrypted and decoded party element.
	 */
	public Gen4MemoryModel(byte[] pokmem) {
		super(pokmem);
		checksum.set(pokmem, 0x6);
		current_hp.set(pokmem, 0x8E);
		level.set(pokmem, 0x8C);
		nickname.set(pokmem, 0x48);
		personality_value.set(pokmem, 0x0);
		species_id.set(pokmem, 0x8);
		status_cond.set(pokmem, 0x88);
		total_hp.set(pokmem, 0x90);
		ot_id.set(pokmem, 0x0C);
		IV_stat.set(pokmem, 0x38);
		egg_steps.set(pokmem, 0x14);
	}

	@Override
	public PokemonModel toPokemonModel() {
		PokemonModel model = new PokemonModel();

		model.current_hp = current_hp.getWord();
		model.level = level.getByte();
		model.max_hp = total_hp.getWord();
		model.nickname = Gen4Util.decodeGen4String(nickname.getBytes());
		model.setDexEntry(species_id.getWord());
		model.setStatusCondition(status_cond.getBytes());
		model.shiny = Gen4Util.isShiny(ot_id.getDword(), personality_value.getDword());
		
		boolean isEgg = (IV_stat.getDword() & 0x40000000) != 0;
		if (isEgg) {
			int eggSteps = Byte.toUnsignedInt(egg_steps.getByte());
			model.setEgg(true, eggSteps);
		}
		
		return model;
	}

	@Override
	public boolean validateChecksum() {
		int sum = 0;
		int pos = 0x8;
		while (pos < 0x87) {
			sum += Util.readWord(rawBytes, pos);
			pos += 2;
		}
		sum &= 0xFFFF;
		return checksum.getWord() == sum;
	}

}
