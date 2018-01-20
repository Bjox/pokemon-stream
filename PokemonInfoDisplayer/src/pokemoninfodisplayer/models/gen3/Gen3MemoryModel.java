package pokemoninfodisplayer.models.gen3;

import pokemoninfodisplayer.models.PokemonMemoryModel;
import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.util.Util;

/**
 *
 * @author Endre
 */
public class Gen3MemoryModel extends PokemonMemoryModel {
	
	private final byte[] personality_value = new byte[4];	
	private final byte[] OT_ID = new byte[4];
	private final byte[] nickname = new byte[10];
	private final byte[] language = new byte[2];
	private final byte[] OT_name = new byte[7];
	private final byte[] markings = new byte[1];
	private final byte[] checksum = new byte[2];
	private final byte[] unknown = new byte[2];
	private final byte[] data = new byte[48];
	private final byte[] status_condition = new byte[4];
	private final byte[] level = new byte[1];
	private final byte[] pokerus = new byte[1];
	private final byte[] current_hp = new byte[2];
	private final byte[] total_hp = new byte[2];
	private final byte[] attack = new byte[2];
	private final byte[] defense = new byte[2];
	private final byte[] speed = new byte[2];
	private final byte[] sp_attack = new byte[2];
	private final byte[] sp_defense = new byte[2];
	private final Data dataDecoded;
	
	public Gen3MemoryModel(byte[] plainPartyElementBytes) {
		super(plainPartyElementBytes);
		update(plainPartyElementBytes);
		dataDecoded = new Data(data, personality_value, OT_ID);
	}
	
	private void update(byte[] memory) {
		System.arraycopy(memory, 0, personality_value, 0, personality_value.length);
		System.arraycopy(memory, 4, OT_ID, 0, OT_ID.length);
		System.arraycopy(memory, 8, nickname, 0, nickname.length);
		System.arraycopy(memory, 18, language, 0, language.length);
		System.arraycopy(memory, 20, OT_name, 0, OT_name.length);
		System.arraycopy(memory, 27, markings, 0, markings.length);
		System.arraycopy(memory, 28, checksum, 0, checksum.length);
		System.arraycopy(memory, 30, unknown, 0, unknown.length);
		System.arraycopy(memory, 32, data, 0, data.length);
		System.arraycopy(memory, 80, status_condition, 0, status_condition.length);
		System.arraycopy(memory, 84, level, 0, level.length);
		System.arraycopy(memory, 85, pokerus, 0, pokerus.length);
		System.arraycopy(memory, 86, current_hp, 0, current_hp.length);
		System.arraycopy(memory, 88, total_hp, 0, total_hp.length);
		System.arraycopy(memory, 90, attack, 0, attack.length);
		System.arraycopy(memory, 92, defense, 0, defense.length);
		System.arraycopy(memory, 94, speed, 0, speed.length);
		System.arraycopy(memory, 96, sp_attack, 0, sp_attack.length);
		System.arraycopy(memory, 98, sp_defense, 0, sp_defense.length);
	}
	
	@Override
	public PokemonModel toPokemonModel() {
		PokemonModel model = new PokemonModel();
		
		model.nickname = Gen3Util.decodeGen3String(nickname);
		model.current_hp = Util.byteArrayToUint(current_hp);
		model.max_hp = Util.byteArrayToUint(total_hp);
		model.level = Util.byteArrayToUint(level);
		model.setStatusCondition(status_condition);
		model.setDexEntry(dataDecoded.getDexEntry());
		model.shiny = Gen3Util.isShiny(Util.readDword(OT_ID, 0), Util.readDword(personality_value, 0));
		
		return model;
	}
	
//	public boolean isShiny() {
//		byte[] trainer_id = new byte[2];
//		System.arraycopy(OT_ID, 0, trainer_id, 0, trainer_id.length);
//		byte[] secret_id = new byte[2];
//		System.arraycopy(OT_ID, 2, secret_id, 0, secret_id.length);
//		
//		byte[] ids_xor = Util.XOR(trainer_id, secret_id);
//		
//		byte[] pv_first = new byte[2];
//		System.arraycopy(personality_value, 0, pv_first, 0, pv_first.length);
//		byte[] pv_last = new byte[2];
//		System.arraycopy(personality_value, 2, pv_last, 0, pv_last.length);
//		
//		byte[] pvs_xor = Util.XOR(pv_first, pv_last);
//		
//		byte[] result = Util.XOR(ids_xor, pvs_xor);
//		
//		int value = 0;
//		
//		for (int i = 0; i < result.length; i++) {
//			value |= Byte.toUnsignedInt(result[i]) << (i*8);
//		}
//		
//		return value < 8;
//	}

	@Override
	public boolean validateChecksum() {
		return Util.readWord(checksum, 0) == dataDecoded.getSum();
	}

}
