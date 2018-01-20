package pokemoninfodisplayer.models.gen4;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen4Util {
	
	public static final String[] CHARACTER_SET_GEN4 = new String[256];
	static {
		for (int i = 0; i < CHARACTER_SET_GEN4.length; i++) {
			CHARACTER_SET_GEN4[i] = " ";
		}
		
		String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int code = 43;
		for (int i = 0; i < upperCase.length(); i++) {
			CHARACTER_SET_GEN4[code++] = upperCase.substring(i, i+1);
		}
		
		String lowerCase = upperCase.toLowerCase();
		code = 69;
		for (int i = 0; i < lowerCase.length(); i++) {
			CHARACTER_SET_GEN4[code++] = lowerCase.substring(i, i+1);
		}
		
		String numbers = "0123456789";
		code = 33;
		for (int i = 0; i < numbers.length(); i++) {
			CHARACTER_SET_GEN4[code++] = numbers.substring(i, i+1);
		}
		
		CHARACTER_SET_GEN4[173] = ",";
		CHARACTER_SET_GEN4[174] = ".";
		CHARACTER_SET_GEN4[196] = ":";
		CHARACTER_SET_GEN4[197] = ";";
		CHARACTER_SET_GEN4[171] = "!";
		CHARACTER_SET_GEN4[172] = "?";
		CHARACTER_SET_GEN4[185] = "(";
		CHARACTER_SET_GEN4[186] = ")";
		CHARACTER_SET_GEN4[189] = "+";
		CHARACTER_SET_GEN4[190] = "-";
	}
	
	public static String decodeGen4String(byte[] bytes) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < bytes.length; i += 2) {
			if (bytes[i] == (byte)0xFF && bytes[i+1] == (byte)0xFF) {
				break;
			}
			String chr = CHARACTER_SET_GEN4[Byte.toUnsignedInt(bytes[i])];
			if (!chr.equals("")) {
				str.append(chr);
			}
		}
		return str.toString();
	}
	
}
