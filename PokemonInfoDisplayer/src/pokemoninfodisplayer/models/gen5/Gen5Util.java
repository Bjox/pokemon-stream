package pokemoninfodisplayer.models.gen5;

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
		int code = 43;
		for (int i = 0; i < upperCase.length(); i++) {
			CHARACTER_SET_GEN5[code++] = upperCase.substring(i, i+1);
		}
		
		String lowerCase = upperCase.toLowerCase();
		code = 69;
		for (int i = 0; i < lowerCase.length(); i++) {
			CHARACTER_SET_GEN5[code++] = lowerCase.substring(i, i+1);
		}
		
		String numbers = "0123456789";
		code = 33;
		for (int i = 0; i < numbers.length(); i++) {
			CHARACTER_SET_GEN5[code++] = numbers.substring(i, i+1);
		}
		
		CHARACTER_SET_GEN5[173] = ",";
		CHARACTER_SET_GEN5[174] = ".";
		CHARACTER_SET_GEN5[196] = ":";
		CHARACTER_SET_GEN5[197] = ";";
		CHARACTER_SET_GEN5[171] = "!";
		CHARACTER_SET_GEN5[172] = "?";
		CHARACTER_SET_GEN5[185] = "(";
		CHARACTER_SET_GEN5[186] = ")";
		CHARACTER_SET_GEN5[189] = "+";
		CHARACTER_SET_GEN5[190] = "-";
	}
	
	public static String decodeGen4String(byte[] bytes) {
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
	
}
