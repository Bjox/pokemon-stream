/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemoninfodisplayer;

/**
 *
 * @author Endre
 */
public abstract class Utils {
	
	public static final int[][] DataOrderLookUp = new int[][] {
		new int[] {0, 1, 2, 3}, //GAEM
		new int[] {0, 1, 3, 2}, //GAME
		new int[] {0, 2, 1, 3}, //GEAM
		new int[] {0, 3, 1, 2}, //GEMA
		new int[] {0, 2, 3, 1}, //GMAE
		new int[] {0, 3, 2, 1}, //GMEA
		new int[] {1, 0, 2, 3}, //AGEM
		new int[] {1, 0, 3, 2}, //AGME
		new int[] {2, 0, 1, 3}, //AEGM
		new int[] {3, 0, 1, 2}, //AEMG
		new int[] {2, 0, 3, 1}, //AMGE
		new int[] {3, 0, 2, 1}, //AMEG
		new int[] {1, 2, 0, 3}, //EGAM
		new int[] {1, 3, 0, 2}, //EGMA
		new int[] {2, 1, 0, 3}, //EAGM
		new int[] {3, 1, 0, 2}, //EAMG
		new int[] {2, 3, 0, 1}, //EMGA
		new int[] {3, 2, 0, 1}, //EMAG
		new int[] {1, 2, 3, 0}, //MGAE
		new int[] {1, 3, 2, 0}, //MGEA
		new int[] {2, 1, 3, 0}, //MAGE
		new int[] {3, 1, 2, 0}, //MAEG
		new int[] {2, 3, 1, 0}, //MEGA
		new int[] {3, 2, 1, 0}, //MEAG
	};
	
	
	public static byte[] XOR(byte[] a, byte[] b) {
		byte[] bytes = new byte[a.length];
		
		for (int i = 0; i < a.length; i++){
			bytes[i] = (byte) (0xFF & (Byte.toUnsignedInt(a[i]) ^ Byte.toUnsignedInt(b[i])));
		}
		
		return bytes;
	}
	
	public static int byteArrayToUint(byte[] bytes) {
		int val = 0;
		
		for (int i = 0; i < bytes.length; i++)  {
			val |= Byte.toUnsignedInt(bytes[i]) << i;
		}
		
		return val;
	}
	
	public static final String[] CHARACTER_SET = {
		"","À","Á","Â","Ç","È","É","Ê","Ë","Ì","こ","Î","Ï","Ò","Ó","Ô",
		"Œ","Ù","Ú","Û","Ñ","ß","à","á","ね","ç","è","é","ê","ë","ì","ま",
		"î","ï","ò","ó","ô","œ","ù","ú","û","ñ","º","ª","","&","+","あ",
		"ぃ","ぅ","ぇ","ぉ","Lv","=","ょ","が","ぎ","ぐ","げ","ご","ざ","じ","ず","ぜ",
		"ぞ","だ","ぢ","づ","で","ど","ば","び","ぶ","べ","ぼ","ぱ","ぴ","ぷ","ぺ","ぽ",
		"っ","¿","¡","PK","MN","PO","Ké","","","","Í","%","(",")","セ","ソ",
		"タ","チ","ツ","テ","ト","ナ","ニ","ヌ","â","ノ","ハ","ヒ","フ","ヘ","ホ","í",
		"ミ","ム","メ","モ","ヤ","ユ","ヨ","ラ","リ","⬆","⬇","⬅","➡","ヲ","ン","ァ",
		"ィ","ゥ","ェ","ォ","ャ","ュ","ョ","ガ","ギ","グ","ゲ","ゴ","ザ","ジ","ズ","ゼ",
		"ゾ","ダ","ヂ","ヅ","デ","ド","バ","ビ","ブ","ベ","ボ","パ","ピ","プ","ペ","ポ",
		"ッ","0","1","2","3","4","5","6","7","8","9","!","?",".","-","・",
		"…","“","”","‘","’","♂","♀","",",","×","/","A","B","C","D","E",
		"F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U",
		"V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k",
		"l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","▶",
		":","Ä","Ö","Ü","ä","ö","ü","⬆","⬇","⬅","","","","","",""
	};
	
	public static String decodeString(byte[] chars) {
		StringBuilder str = new StringBuilder();
		for (byte charCode : chars) {
			str.append(CHARACTER_SET[Byte.toUnsignedInt(charCode)]);
		}
		return str.toString();
	}
	
	public static byte[] getPartyPokemon(int x, byte[] wram){
		byte[] pokemonInfo = new byte[100];
		System.arraycopy(wram, 148100 + x*100, pokemonInfo, 0, pokemonInfo.length);
		return pokemonInfo;
	}
}
