package pokemoninfodisplayer.models.gen3;

import pokemoninfodisplayer.models.memory.Byt;
import pokemoninfodisplayer.models.memory.Dword;
import pokemoninfodisplayer.models.memory.MField;
import pokemoninfodisplayer.models.memory.MemoryModel;
import pokemoninfodisplayer.models.memory.Word;
import pokemoninfodisplayer.util.Util;

/**
 *
 * @author Endre
 */
class Data {
	
	public final static int G = 0;
	public final static int A = 1;
	public final static int E= 2;
	public final static int M = 3;
	
	public static class Growth extends MemoryModel {
		@MField(offset = 0) public Word species;
		@MField(offset = 2) public Word itemHeld;
		@MField(offset = 4) public Dword experience;
		@MField(offset = 8) public Byt PPBonus;
		@MField(offset = 9) public Byt friendship;
	}
	
	public static class Attacks extends MemoryModel {
		@MField(offset = 0) public Word move1;
		@MField(offset = 2) public Word move2;
		@MField(offset = 4) public Word move3;
		@MField(offset = 6) public Word move4;
		@MField(offset = 8) public Byt pp1;
		@MField(offset = 9) public Byt pp2;
		@MField(offset = 10) public Byt pp3;
		@MField(offset = 11) public Byt pp4;
	}
	
	public static class EVCondition extends MemoryModel {
		@MField(offset = 0) public Byt HPEV;
		@MField(offset = 1) public Byt attackEV;
		@MField(offset = 2) public Byt defenseEV;
		@MField(offset = 3) public Byt speedEV;
		@MField(offset = 4) public Byt specialAttackEV;
		@MField(offset = 5) public Byt specialDefenseEV;
		@MField(offset = 6) public Byt coolness;
		@MField(offset = 7) public Byt beauty;
		@MField(offset = 8) public Byt cuteness;
		@MField(offset = 9) public Byt smartness;
		@MField(offset = 10) public Byt toughness;
		@MField(offset = 11) public Byt feel;
	}
	
	public static class Miscellaneous extends MemoryModel {
		@MField(offset = 0) public Byt pokerusStatus;
		@MField(offset = 1) public Byt metLocation;
		@MField(offset = 2) public Word originsInfo;
		@MField(offset = 4) public Dword IVEggAbility;
		@MField(offset = 8) public Dword ribbonsObedience;
	}
	
	public final Growth growthBlock;
	public final Attacks attacksBlock;
	public final EVCondition evConditionBlock;
	public final Miscellaneous miscellaneousBlock;
	
	public final int sum;
	
	public Data(byte[] enc_data, byte[] personality_value, byte[] ot_id) throws IllegalAccessException, InstantiationException {
		
		byte[] dec_key = Util.XOR(personality_value, ot_id);
		byte[] dec_data = new byte[enc_data.length];
		
		for (int i = 0; i < enc_data.length; i+=4) {
			byte[] enc_byte = new byte[4];
			for (int j = 0; j < 4; j++) {
				enc_byte[j] = enc_data[i+j];
			}
			byte[] dec_byte = Util.XOR(dec_key, enc_byte);
			System.arraycopy(dec_byte, 0, dec_data, i, 4);
		}
		int personality_value_calc = 0;
		
		for (int i = 0; i < personality_value.length; i++) {
			personality_value_calc |= Byte.toUnsignedInt(personality_value[i]) << (i*8);
		}
		
		int look_up = (int) (Integer.toUnsignedLong(personality_value_calc)%24L);
		
		int[] order = Gen3Util.DataOrderLookUp[look_up];
		
		//Growth
		byte[] growthBlockBytes = new byte[12];
		System.arraycopy(dec_data, 12*order[G], growthBlockBytes, 0, growthBlockBytes.length);
		growthBlock = new Growth();
		growthBlock.update(growthBlockBytes);
		
		//Attacks
		byte[] attacksBlockBytes = new byte[12];
		System.arraycopy(dec_data, 12*order[A], attacksBlockBytes, 0, attacksBlockBytes.length);
		attacksBlock = new Attacks();
		attacksBlock.update(attacksBlockBytes);
		
		//EV
		byte[] evCondBlockBytes = new byte[12];
		System.arraycopy(dec_data, 12*order[E], evCondBlockBytes, 0, evCondBlockBytes.length);
		evConditionBlock = new EVCondition();
		evConditionBlock.update(evCondBlockBytes);
		
		//Misc
		byte[] miscBlockBytes = new byte[12];
		System.arraycopy(dec_data, 12*order[M], miscBlockBytes, 0, miscBlockBytes.length);
		miscellaneousBlock = new Miscellaneous();
		miscellaneousBlock.update(miscBlockBytes);
		
		int sum = 0;
		for (int i = 0; i < dec_data.length; i += 2) {
			sum += Util.readWord(dec_data, i);
		}
		sum &= 0xFFFF;
		this.sum = sum;
	}
	
	public int getDexEntry() {
		int index = growthBlock.species.getUInt();
		if (index < 0 || index >= Gen3Util.SPECIES_TO_DEX_LOOKUP.length) {
			index = 0;
		}
		return Gen3Util.SPECIES_TO_DEX_LOOKUP[index];
	}
	
}
