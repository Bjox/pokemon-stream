package pokemoninfodisplayer.models.gen5;

import java.util.Arrays;
import pokemoninfodisplayer.models.Gender;
import pokemoninfodisplayer.models.gen3.Gen3Util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen5Util {
	
	public static final String[] CHARACTER_SET_GEN5 = new String[256];
	static {
		for (int i = 0; i < CHARACTER_SET_GEN5.length; i++) {
			CHARACTER_SET_GEN5[i] = " ";
		}
		
		String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int code = 65;
		for (int i = 0; i < upperCase.length(); i++) {
			CHARACTER_SET_GEN5[code++] = upperCase.substring(i, i+1);
		}
		
		String lowerCase = upperCase.toLowerCase();
		code = 97;
		for (int i = 0; i < lowerCase.length(); i++) {
			CHARACTER_SET_GEN5[code++] = lowerCase.substring(i, i+1);
		}
		
		String numbers = "0123456789";
		code = 48;
		for (int i = 0; i < numbers.length(); i++) {
			CHARACTER_SET_GEN5[code++] = numbers.substring(i, i+1);
		}
		
		CHARACTER_SET_GEN5[44] = ",";
		CHARACTER_SET_GEN5[46] = ".";
		CHARACTER_SET_GEN5[58] = ":";
		CHARACTER_SET_GEN5[59] = ";";
		CHARACTER_SET_GEN5[33] = "!";
		CHARACTER_SET_GEN5[63] = "?";
		CHARACTER_SET_GEN5[40] = "(";
		CHARACTER_SET_GEN5[41] = ")";
		CHARACTER_SET_GEN5[43] = "+";
		CHARACTER_SET_GEN5[45] = "-";
	}
	
	public static String decodeGen5String(byte[] bytes) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < bytes.length; i += 2) {
			if (bytes[i] == (byte)0xFF && bytes[i+1] == (byte)0xFF) {
				break;
			}
			String chr = CHARACTER_SET_GEN5[Byte.toUnsignedInt(bytes[i])];
			if (!chr.equals("")) {
				str.append(chr);
			}
		}
		return str.toString();
	}
	
	public static boolean isShiny(int ot_id, int pid) {
		return Gen3Util.isShiny(ot_id, pid);
	}
	
	public static Gender getGender(byte genderByte) {
		if ((genderByte & 4) != 0) {
			return Gender.GENDERLESS;
		}
		return (genderByte & 2) == 0 ? Gender.MALE : Gender.FEMALE;
	}
	
}
