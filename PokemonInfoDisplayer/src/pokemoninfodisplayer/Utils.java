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
	
	public static final String[] CHARACTER_SET_GEN3 = {
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
	
	public static String decodeGen3String(byte[] chars) {
		StringBuilder str = new StringBuilder();
		for (byte charCode : chars) {
			str.append(CHARACTER_SET_GEN3[Byte.toUnsignedInt(charCode)]);
		}
		return str.toString();
	}
	
	public static final String[] CHARACTER_SET_GEN4 = new String[256];
	static {
		for (int i = 0; i < CHARACTER_SET_GEN4.length; i++) {
			CHARACTER_SET_GEN4[i] = "";
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
			String chr = CHARACTER_SET_GEN4[Byte.toUnsignedInt(bytes[i])];
			if (!chr.equals("")) {
				str.append(chr);
			}
		}
		return str.toString();
	}
	
	public static byte[] getPartyPokemon(int x, byte[] wram){
		byte[] pokemonInfo = new byte[100];
		System.arraycopy(wram, 148100 + x*100, pokemonInfo, 0, pokemonInfo.length);
		return pokemonInfo;
	}
	
	public int speciesToDexEntry(int species){
		if (species < 252) return species;
		if (species < 301) return species - 25;
		if (species > 305) return species - 25 + 14;
		return 0;
	}
	
	public static int[] DEX_TO_SPECIES_LOOKUP = new int[]{
		0, //0
		1, //1
		2, //2
		3, //3
		4, //4
		5, //5
		6, //6
		7, //7
		8, //8
		9, //9
		10, //10
		11, //11
		12, //12
		13, //13
		14, //14
		15, //15
		16, //16
		17, //17
		18, //18
		19, //19
		20, //20
		21, //21
		22, //22
		23, //23
		24, //24
		25, //25
		26, //26
		27, //27
		28, //28
		29, //29
		30, //30
		31, //31
		32, //32
		33, //33
		34, //34
		35, //35
		36, //36
		37, //37
		38, //38
		39, //39
		40, //40
		41, //41
		42, //42
		43, //43
		44, //44
		45, //45
		46, //46
		47, //47
		48, //48
		49, //49
		50, //50
		51, //51
		52, //52
		53, //53
		54, //54
		55, //55
		56, //56
		57, //57
		58, //58
		59, //59
		60, //60
		61, //61
		62, //62
		63, //63
		64, //64
		65, //65
		66, //66
		67, //67
		68, //68
		69, //69
		70, //70
		71, //71
		72, //72
		73, //73
		74, //74
		75, //75
		76, //76
		77, //77
		78, //78
		79, //79
		80, //80
		81, //81
		82, //82
		83, //83
		84, //84
		85, //85
		86, //86
		87, //87
		88, //88
		89, //89
		90, //90
		91, //91
		92, //92
		93, //93
		94, //94
		95, //95
		96, //96
		97, //97
		98, //98
		99, //99
		100, //100
		101, //101
		102, //102
		103, //103
		104, //104
		105, //105
		106, //106
		107, //107
		108, //108
		109, //109
		110, //110
		111, //111
		112, //112
		113, //113
		114, //114
		115, //115
		116, //116
		117, //117
		118, //118
		119, //119
		120, //120
		121, //121
		122, //122
		123, //123
		124, //124
		125, //125
		126, //126
		127, //127
		128, //128
		129, //129
		130, //130
		131, //131
		132, //132
		133, //133
		134, //134
		135, //135
		136, //136
		137, //137
		138, //138
		139, //139
		140, //140
		141, //141
		142, //142
		143, //143
		144, //144
		145, //145
		146, //146
		147, //147
		148, //148
		149, //149
		150, //150
		151, //151
		152, //152
		153, //153
		154, //154
		155, //155
		156, //156
		157, //157
		158, //158
		159, //159
		160, //160
		161, //161
		162, //162
		163, //163
		164, //164
		165, //165
		166, //166
		167, //167
		168, //168
		169, //169
		170, //170
		171, //171
		172, //172
		173, //173
		174, //174
		175, //175
		176, //176
		177, //177
		178, //178
		179, //179
		180, //180
		181, //181
		182, //182
		183, //183
		184, //184
		185, //185
		186, //186
		187, //187
		188, //188
		189, //189
		190, //190
		191, //191
		192, //192
		193, //193
		194, //194
		195, //195
		196, //196
		197, //197
		198, //198
		199, //199
		200, //200
		201, //201
		202, //202
		203, //203
		204, //204
		205, //205
		206, //206
		207, //207
		208, //208
		209, //209
		210, //210
		211, //211
		212, //212
		213, //213
		214, //214
		215, //215
		216, //216
		217, //217
		218, //218
		219, //219
		220, //220
		221, //221
		222, //222
		223, //223
		224, //224
		225, //225
		226, //226
		227, //227
		228, //228
		229, //229
		230, //230
		231, //231
		232, //232
		233, //233
		234, //234
		235, //235
		236, //236
		237, //237
		238, //238
		239, //239
		240, //240
		241, //241
		242, //242
		243, //243
		244, //244
		245, //245
		246, //246
		247, //247
		248, //248
		249, //249
		250, //250
		251, //251
		277, //252
		278, //253
		279, //254
		280, //255
		281, //256
		282, //257
		283, //258
		284, //259
		285, //260
		286, //261
		287, //262
		288, //263
		289, //264
		290, //265
		291, //266
		292, //267
		293, //268
		294, //269
		295, //270
		296, //271
		297, //272
		298, //273
		299, //274
		300, //275
		304, //276
		305, //277
		309, //278
		310, //279
		392, //280
		393, //281
		394, //282
		311, //283
		312, //284
		306, //285
		307, //286
		364, //287
		365, //288
		366, //289
		301, //290
		302, //291
		303, //292
		370, //293
		371, //294
		372, //295
		335, //296
		336, //297
		350, //298
		320, //299
		315, //300
		316, //301
		322, //302
		355, //303
		382, //304
		383, //305
		384, //306
		356, //307
		357, //308
		337, //309
		338, //310
		353, //311
		354, //312
		386, //313
		387, //314
		363, //315
		367, //316
		368, //317
		330, //318
		331, //319
		313, //320
		314, //321
		339, //322
		340, //323
		321, //324
		351, //325
		352, //326
		308, //327
		332, //328
		333, //329
		334, //330
		344, //331
		345, //332
		358, //333
		359, //334
		380, //335
		379, //336
		348, //337
		349, //338
		323, //339
		324, //340
		326, //341
		327, //342
		318, //343
		319, //344
		388, //345
		389, //346
		390, //347
		391, //348
		328, //349
		329, //350
		385, //351
		317, //352
		377, //353
		378, //354
		361, //355
		362, //356
		369, //357
		411, //358
		376, //359
		360, //360
		346, //361
		347, //362
		341, //363
		342, //364
		343, //365
		373, //366
		374, //367
		375, //368
		381, //369
		325, //370
		395, //371
		396, //372
		397, //373
		398, //374
		399, //375
		400, //376
		401, //377
		402, //378
		403, //379
		407, //380
		408, //381
		404, //382
		405, //383
		406, //384
		409, //385
		410  //386
	};
	
	public static int[] SPECIES_TO_DEX_LOOKUP = new int[]{
		0, //0
		1, //1
		2, //2
		3, //3
		4, //4
		5, //5
		6, //6
		7, //7
		8, //8
		9, //9
		10, //10
		11, //11
		12, //12
		13, //13
		14, //14
		15, //15
		16, //16
		17, //17
		18, //18
		19, //19
		20, //20
		21, //21
		22, //22
		23, //23
		24, //24
		25, //25
		26, //26
		27, //27
		28, //28
		29, //29
		30, //30
		31, //31
		32, //32
		33, //33
		34, //34
		35, //35
		36, //36
		37, //37
		38, //38
		39, //39
		40, //40
		41, //41
		42, //42
		43, //43
		44, //44
		45, //45
		46, //46
		47, //47
		48, //48
		49, //49
		50, //50
		51, //51
		52, //52
		53, //53
		54, //54
		55, //55
		56, //56
		57, //57
		58, //58
		59, //59
		60, //60
		61, //61
		62, //62
		63, //63
		64, //64
		65, //65
		66, //66
		67, //67
		68, //68
		69, //69
		70, //70
		71, //71
		72, //72
		73, //73
		74, //74
		75, //75
		76, //76
		77, //77
		78, //78
		79, //79
		80, //80
		81, //81
		82, //82
		83, //83
		84, //84
		85, //85
		86, //86
		87, //87
		88, //88
		89, //89
		90, //90
		91, //91
		92, //92
		93, //93
		94, //94
		95, //95
		96, //96
		97, //97
		98, //98
		99, //99
		100, //100
		101, //101
		102, //102
		103, //103
		104, //104
		105, //105
		106, //106
		107, //107
		108, //108
		109, //109
		110, //110
		111, //111
		112, //112
		113, //113
		114, //114
		115, //115
		116, //116
		117, //117
		118, //118
		119, //119
		120, //120
		121, //121
		122, //122
		123, //123
		124, //124
		125, //125
		126, //126
		127, //127
		128, //128
		129, //129
		130, //130
		131, //131
		132, //132
		133, //133
		134, //134
		135, //135
		136, //136
		137, //137
		138, //138
		139, //139
		140, //140
		141, //141
		142, //142
		143, //143
		144, //144
		145, //145
		146, //146
		147, //147
		148, //148
		149, //149
		150, //150
		151, //151
		152, //152
		153, //153
		154, //154
		155, //155
		156, //156
		157, //157
		158, //158
		159, //159
		160, //160
		161, //161
		162, //162
		163, //163
		164, //164
		165, //165
		166, //166
		167, //167
		168, //168
		169, //169
		170, //170
		171, //171
		172, //172
		173, //173
		174, //174
		175, //175
		176, //176
		177, //177
		178, //178
		179, //179
		180, //180
		181, //181
		182, //182
		183, //183
		184, //184
		185, //185
		186, //186
		187, //187
		188, //188
		189, //189
		190, //190
		191, //191
		192, //192
		193, //193
		194, //194
		195, //195
		196, //196
		197, //197
		198, //198
		199, //199
		200, //200
		201, //201
		202, //202
		203, //203
		204, //204
		205, //205
		206, //206
		207, //207
		208, //208
		209, //209
		210, //210
		211, //211
		212, //212
		213, //213
		214, //214
		215, //215
		216, //216
		217, //217
		218, //218
		219, //219
		220, //220
		221, //221
		222, //222
		223, //223
		224, //224
		225, //225
		226, //226
		227, //227
		228, //228
		229, //229
		230, //230
		231, //231
		232, //232
		233, //233
		234, //234
		235, //235
		236, //236
		237, //237
		238, //238
		239, //239
		240, //240
		241, //241
		242, //242
		243, //243
		244, //244
		245, //245
		246, //246
		247, //247
		248, //248
		249, //249
		250, //250
		251, //251
		0, //252
		0, //253
		0, //254
		0, //255
		0, //256
		0, //257
		0, //258
		0, //259
		0, //260
		0, //261
		0, //262
		0, //263
		0, //264
		0, //265
		0, //266
		0, //267
		0, //268
		0, //269
		0, //270
		0, //271
		0, //272
		0, //273
		0, //274
		0, //275
		0, //276
		252, //277
		253, //278
		254, //279
		255, //280
		256, //281
		257, //282
		258, //283
		259, //284
		260, //285
		261, //286
		262, //287
		263, //288
		264, //289
		265, //290
		266, //291
		267, //292
		268, //293
		269, //294
		270, //295
		271, //296
		272, //297
		273, //298
		274, //299
		275, //300
		290, //301
		291, //302
		292, //303
		276, //304
		277, //305
		285, //306
		286, //307
		327, //308
		278, //309
		279, //310
		283, //311
		284, //312
		320, //313
		321, //314
		300, //315
		301, //316
		352, //317
		343, //318
		344, //319
		299, //320
		324, //321
		302, //322
		339, //323
		340, //324
		370, //325
		341, //326
		342, //327
		349, //328
		350, //329
		318, //330
		319, //331
		328, //332
		329, //333
		330, //334
		296, //335
		297, //336
		309, //337
		310, //338
		322, //339
		323, //340
		363, //341
		364, //342
		365, //343
		331, //344
		332, //345
		361, //346
		362, //347
		337, //348
		338, //349
		298, //350
		325, //351
		326, //352
		311, //353
		312, //354
		303, //355
		307, //356
		308, //357
		333, //358
		334, //359
		360, //360
		355, //361
		356, //362
		315, //363
		287, //364
		288, //365
		289, //366
		316, //367
		317, //368
		357, //369
		293, //370
		294, //371
		295, //372
		366, //373
		367, //374
		368, //375
		359, //376
		353, //377
		354, //378
		336, //379
		335, //380
		369, //381
		304, //382
		305, //383
		306, //384
		351, //385
		313, //386
		314, //387
		345, //388
		346, //389
		347, //390
		348, //391
		280, //392
		281, //393
		282, //394
		371, //395
		372, //396
		373, //397
		374, //398
		375, //399
		376, //400
		377, //401
		378, //402
		379, //403
		382, //404
		383, //405
		384, //406
		380, //407
		381, //408
		385, //409
		386, //410
		358, //411
		999, //412
	};

}
