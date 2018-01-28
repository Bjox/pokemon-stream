package pokemoninfodisplayer.models.gen3;

import pokemoninfodisplayer.models.memory.PokemonMemoryModel;
import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.models.memory.Byt;
import pokemoninfodisplayer.models.memory.Bytes;
import pokemoninfodisplayer.models.memory.Dword;
import pokemoninfodisplayer.models.memory.MField;
import pokemoninfodisplayer.models.memory.Word;

/**
 *
 * @author Endre
 */
public class Gen3MemoryModel extends PokemonMemoryModel {
	
	@MField(offset = 0) public Dword personalityValue;
	@MField(offset = 4) public Dword OTID;
	@MField(offset = 8, size = 10) public Bytes nickname;
	@MField(offset = 18) public Word language;
	@MField(offset = 20, size = 7) public Bytes OTName;
	@MField(offset = 27) public Byt markings;
	@MField(offset = 28) public Word checksum;
	@MField(offset = 32, size = 48) public Bytes data;
	@MField(offset = 80) public Dword statusCondition;
	@MField(offset = 84) public Byt level;
	@MField(offset = 85) public Byt pokerusRemaining;
	@MField(offset = 86) public Word currentHP;
	@MField(offset = 88) public Word maxHP;
	@MField(offset = 90) public Word attack;
	@MField(offset = 92) public Word defense;
	@MField(offset = 94) public Word speed;
	@MField(offset = 96) public Word specialAttack;
	@MField(offset = 98) public Word specialDefense;
	
	private final Data dataDecoded;
	
	
	public Gen3MemoryModel(byte[] plainPartyElementBytes) throws Exception {
		update(plainPartyElementBytes);
		dataDecoded = new Data(data.getBytes(), personalityValue.getBytes(), OTID.getBytes());
	}
	
	@Override
	public PokemonModel toPokemonModel() {
		PokemonModel model = new PokemonModel();
		
		model.nickname = Gen3Util.decodeGen3String(nickname.getBytes());
		model.current_hp = currentHP.getUInt();
		model.max_hp = maxHP.getUInt();
		model.level = level.getUInt();
		model.setStatusCondition(statusCondition.getBytes()[0]);
		model.setDexEntry(dataDecoded.getDexEntry());
		model.shiny = Gen3Util.isShiny(OTID.getUInt(), personalityValue.getUInt());
		
		boolean isEgg = (dataDecoded.miscellaneousBlock.IVEggAbility.getUInt() & 0x40000000) != 0;
		if (isEgg) {
			int eggSteps = 0; // TODO: extract egg steps for gen3
			model.setEgg(true, eggSteps);
		}
		
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
		return checksum.getUInt() == dataDecoded.sum;
	}

}
