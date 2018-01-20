package pokemoninfodisplayer.models.gen4;

import java.util.Arrays;
import pokemoninfodisplayer.models.PokemonMemoryModel;
import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.models.gen3.Gen3Util;
import pokemoninfodisplayer.util.Bytes;
import pokemoninfodisplayer.util.Util;

/**
 * Pokemon generation 4 (Platinum)
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen4MemoryModel extends PokemonMemoryModel {
	
	public final Bytes personality_value = new Bytes(4);
	public final Bytes ot_id = new Bytes(4);
	public final Bytes checksum = new Bytes(2);
	public final Bytes current_hp = new Bytes(2);
	public final Bytes total_hp = new Bytes(2);
	public final Bytes level = new Bytes(1);
	public final Bytes nickname = new Bytes(22);
	public final Bytes species_id = new Bytes(2);
	public final Bytes status_cond = new Bytes(1);

	/**
	 *
	 * @param plainPartyElementBytes Decrypted and decoded party element.
	 */
	public Gen4MemoryModel(byte[] plainPartyElementBytes) {
		super(plainPartyElementBytes);
		checksum.set(plainPartyElementBytes, 0x6);
		current_hp.set(plainPartyElementBytes, 0x8E);
		level.set(plainPartyElementBytes, 0x8C);
		nickname.set(plainPartyElementBytes, 0x48);
		personality_value.set(plainPartyElementBytes, 0x0);
		species_id.set(plainPartyElementBytes, 0x8);
		status_cond.set(plainPartyElementBytes, 0x88);
		total_hp.set(plainPartyElementBytes, 0x90);
		ot_id.set(plainPartyElementBytes, 0x0C);
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
		model.shiny = Gen3Util.isShiny(ot_id.getDword(), personality_value.getDword());
		
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
