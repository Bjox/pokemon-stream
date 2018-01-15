package pokemoninfodisplayer.models.gen3;

import pokemoninfodisplayer.Utils;
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
	
	//Growth
	private final byte[] species = new byte[2];
	private final byte[] held_item = new byte[2];
	private final byte[] experience = new byte[4];
	private final byte[] pp_bonuses = new byte[1];
	private final byte[] friendship = new byte[1];
	private final byte[] unknown = new byte[2];
	
	//Attacks
	private final byte[] move_1 = new byte[2];
	private final byte[] move_2 = new byte[2];
	private final byte[] move_3 = new byte[2];
	private final byte[] move_4 = new byte[2];
	private final byte[] pp_1 = new byte[1];
	private final byte[] pp_2 = new byte[1];
	private final byte[] pp_3 = new byte[1];
	private final byte[] pp_4 = new byte[1];
	
	//EVs & Condition
	private final byte[] hp_ev = new byte[1];
	private final byte[] atk_ev = new byte[1];
	private final byte[] def_ev = new byte[1];
	private final byte[] spd_ev = new byte[1];
	private final byte[] spatk_ev = new byte[1];
	private final byte[] spdef_ev = new byte[1];
	private final byte[] coolness = new byte[1];
	private final byte[] beauty = new byte[1];
	private final byte[] cuteness = new byte[1];
	private final byte[] smartness = new byte[1];
	private final byte[] toughness = new byte[1];
	private final byte[] feel = new byte[1];
	
	//Miscellaneous
	private final byte[] pokerus_status = new byte[1];
	private final byte[] met_location = new byte[1];
	private final byte[] origins_info = new byte[2];
	private final byte[] ivs_egg_ability = new byte[4];
	private final byte[] ribbons_obedience = new byte[4];
	
	private final int sum;
	
	public Data(byte[] enc_data, byte[] personality_value, byte[] ot_id){
		
		byte[] dec_key = Utils.XOR(personality_value, ot_id);
		byte[] dec_data = new byte[enc_data.length];
		
		for (int i = 0; i < enc_data.length; i+=4) {
			byte[] enc_byte = new byte[4];
			for (int j = 0; j < 4; j++) {
				enc_byte[j] = enc_data[i+j];
			}
			byte[] dec_byte = Utils.XOR(dec_key, enc_byte);
			System.arraycopy(dec_byte, 0, dec_data, i, 4);
		}
		int personality_value_calc = 0;
		
		for (int i = 0; i < personality_value.length; i++) {
			personality_value_calc |= Byte.toUnsignedInt(personality_value[i]) << (i*8);
		}
		
		int look_up = (int) (Integer.toUnsignedLong(personality_value_calc)%24L);
		
		int[] order = Utils.DataOrderLookUp[look_up];
		
		//Growth
		System.arraycopy(dec_data, 0+12*order[G], species, 0, species.length);
		System.arraycopy(dec_data, 2+12*order[G], held_item, 0, held_item.length);
		System.arraycopy(dec_data, 4+12*order[G], experience, 0, experience.length);
		System.arraycopy(dec_data, 8+12*order[G], pp_bonuses, 0, pp_bonuses.length);
		System.arraycopy(dec_data, 9+12*order[G], friendship, 0, friendship.length);
		System.arraycopy(dec_data, 10+12*order[G], unknown, 0, unknown.length);
		
		//Attacks
		System.arraycopy(dec_data, 0+12*order[A], move_1, 0, move_1.length);
		System.arraycopy(dec_data, 2+12*order[A], move_2, 0, move_2.length);
		System.arraycopy(dec_data, 4+12*order[A], move_3, 0, move_3.length);
		System.arraycopy(dec_data, 6+12*order[A], move_4, 0, move_4.length);
		System.arraycopy(dec_data, 8+12*order[A], pp_1, 0, pp_1.length);
		System.arraycopy(dec_data, 9+12*order[A], pp_2, 0, pp_2.length);
		System.arraycopy(dec_data, 10+12*order[A], pp_3, 0, pp_3.length);
		System.arraycopy(dec_data, 11+12*order[A], pp_4, 0, pp_4.length);
		
		//EV
		System.arraycopy(dec_data, 0+12*order[E], hp_ev, 0, hp_ev.length);
		System.arraycopy(dec_data, 1+12*order[E], atk_ev, 0, atk_ev.length);
		System.arraycopy(dec_data, 2+12*order[E], def_ev, 0, def_ev.length);
		System.arraycopy(dec_data, 3+12*order[E], spd_ev, 0, spd_ev.length);
		System.arraycopy(dec_data, 4+12*order[E], spatk_ev, 0, spatk_ev.length);
		System.arraycopy(dec_data, 5+12*order[E], spdef_ev, 0, spdef_ev.length);
		System.arraycopy(dec_data, 6+12*order[E], coolness, 0, coolness.length);
		System.arraycopy(dec_data, 7+12*order[E], beauty, 0, beauty.length);
		System.arraycopy(dec_data, 8+12*order[E], cuteness, 0, cuteness.length);
		System.arraycopy(dec_data, 9+12*order[E], smartness, 0, smartness.length);
		System.arraycopy(dec_data, 10+12*order[E], toughness, 0, toughness.length);
		System.arraycopy(dec_data, 11+12*order[E], feel, 0, feel.length);
		
		//Misc
		System.arraycopy(dec_data, 0+12*order[M], pokerus_status, 0, pokerus_status.length);
		System.arraycopy(dec_data, 1+12*order[M], met_location, 0, met_location.length);
		System.arraycopy(dec_data, 2+12*order[M], origins_info, 0, origins_info.length);
		System.arraycopy(dec_data, 4+12*order[M], ivs_egg_ability, 0, ivs_egg_ability.length);
		System.arraycopy(dec_data, 8+12*order[M], ribbons_obedience, 0, ribbons_obedience.length);
		
		int sum = 0;
		for (int i = 0; i < dec_data.length; i += 2) {
			sum += Util.readWord(dec_data, i);
		}
		sum &= 0xFFFF;
		this.sum = sum;
	}
	
	public int getDexEntry() {
		int index = Byte.toUnsignedInt(species[0]) | (Byte.toUnsignedInt(species[1]) << 8);
		if (index < 0 || index >= Utils.SPECIES_TO_DEX_LOOKUP.length) {
			//throw new SkipRenderTileException();
			index = 0;
			System.out.println("SKIP RENDER TILE");
		}
		return Utils.SPECIES_TO_DEX_LOOKUP[index];
	}
	
	public int getSum() {
		return sum;
	}
}
