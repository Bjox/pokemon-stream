package pokemoninfodisplayer.models.gen5;

import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.models.StatusCondition;
import pokemoninfodisplayer.models.memory.Byt;
import pokemoninfodisplayer.models.memory.Bytes;
import pokemoninfodisplayer.models.memory.Dword;
import pokemoninfodisplayer.models.memory.Word;
import pokemoninfodisplayer.models.memory.MField;
import pokemoninfodisplayer.models.memory.PokemonMemoryModel;
import pokemoninfodisplayer.util.Util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen5PokemonMemoryModel extends PokemonMemoryModel {
	
	// Unencrypted block
	@MField(offset = 0x00) public Dword personalityValue;
	@MField(offset = 0x06) public Word checksum;
	
	// Block A
	@MField(offset = 0x08) public Word speciesID;
	@MField(offset = 0x0A) public Word heldItem;
	@MField(offset = 0x0C) public Word OTID;
	@MField(offset = 0x0E) public Word OTSecretID;
	@MField(offset = 0x10) public Dword experiencePoints;
	@MField(offset = 0x14) public Byt friendship_eggSteps;
	@MField(offset = 0x15) public Byt ability;
	@MField(offset = 0x16) public Byt markings;
	@MField(offset = 0x17) public Byt languageOfOrigin;
	@MField(offset = 0x18) public Byt HPEV;
	@MField(offset = 0x19) public Byt attackEV;
	@MField(offset = 0x1A) public Byt defenseEV;
	@MField(offset = 0x1B) public Byt speedEV;
	@MField(offset = 0x1C) public Byt specialAttackEV;
	@MField(offset = 0x1D) public Byt specialDefenseEV;
	@MField(offset = 0x1E) public Byt coolContestStat;
	@MField(offset = 0x1F) public Byt beautyContestStat;
	@MField(offset = 0x20) public Byt cuteContestStat;
	@MField(offset = 0x21) public Byt smartContestStat;
	@MField(offset = 0x22) public Byt toughContestStat;
	@MField(offset = 0x23) public Byt sheen;
	@MField(offset = 0x24) public Word sinnohRibbonsSet1;
	@MField(offset = 0x26) public Word unovaRibbonsSet1;
	
	// Block B
	@MField(offset = 0x28) public Word move1;
	@MField(offset = 0x2A) public Word move2;
	@MField(offset = 0x2C) public Word move3;
	@MField(offset = 0x2E) public Word move4;
	@MField(offset = 0x30) public Byt move1PP;
	@MField(offset = 0x31) public Byt move2PP;
	@MField(offset = 0x32) public Byt move3PP;
	@MField(offset = 0x33) public Byt move4PP;
	@MField(offset = 0x34) public Dword movePPUps;
	@MField(offset = 0x38) public Dword individualValues;
	@MField(offset = 0x3C) public Word hoennRibbonSet1;
	@MField(offset = 0x3E) public Word hoennRibbonSet2;
	@MField(offset = 0x40) public Byt fatefulFlag_female_genderless_altForms;
	@MField(offset = 0x41) public Byt nature;
	@MField(offset = 0x42) public Byt dreamWorldAbility_NsPokemon; 
	
	// Block C
	@MField(offset = 0x48, size = 22) public Bytes nickname;
	@MField(offset = 0x5F) public Byt gameOfOrigin;
	@MField(offset = 0x60) public Word sinnohRibbonSet3;
	@MField(offset = 0x62) public Word sinnohRibbonSet4;
	
	// Block D
	@MField(offset = 0x68, size = 16) public Bytes OTName;
	@MField(offset = 0x78, size = 3) public Bytes dateEggReceived;
	@MField(offset = 0x7B, size = 3) public Bytes dateMet;
	@MField(offset = 0x7E) public Word diamonPearlEggLocation;
	@MField(offset = 0x80) public Word diamondPearlMetAtLocation;
	@MField(offset = 0x82) public Byt pokerus;
	@MField(offset = 0x83) public Byt pokeBall;
	@MField(offset = 0x84) public Byt metAtLvl_OTGender;
	@MField(offset = 0x85) public Byt encounterType;
	
	// Battle stats
	@MField(offset = 0x88) public Byt statusCond;
	@MField(offset = 0x8C) public Byt level;
	@MField(offset = 0x8D) public Byt capsuleIndex;
	@MField(offset = 0x8E) public Word currentHP;
	@MField(offset = 0x90) public Word maxHP;
	@MField(offset = 0x92) public Word attack;
	@MField(offset = 0x94) public Word defense;
	@MField(offset = 0x96) public Word speed;
	@MField(offset = 0x98) public Word specialAttack;
	@MField(offset = 0x9A) public Word specialDefense;
	@MField(offset = 0x9C, size = 38) public Bytes mailMessage;

	public Gen5PokemonMemoryModel(byte[] decryptedData) {
		try {
			update(decryptedData);
		}
		catch (IllegalAccessException | InstantiationException e) {
		}
	}
	
	@Override
	public PokemonModel toPokemonModel() {
		return new PokemonModel.Builder()
				.setPersonalityValue(personalityValue.getUInt())
				.setCurrentHp(currentHP.getUInt())
				.setLevel(level.getUInt())
				.setMaxHp(maxHP.getUInt())
				.setNickname(Gen5Util.decodeGen5String(nickname.getBytes()))
				.setShiny(Gen5Util.isShiny(OTID.getUInt(), personalityValue.getUInt()))
				.setDexEntry(speciesID.getUInt())
				.setStatusCondition(StatusCondition.parse(statusCond.getByte()))
				.setEgg((individualValues.getUInt() & 0x40000000) != 0)
				.setGender(Gen5Util.getGender(fatefulFlag_female_genderless_altForms.getByte()))
				.setExperiencePoints(experiencePoints.getUInt())
				.build();
	}

	@Override
	public boolean validateChecksum() {
		int sum = 0;
		int pos = 0x8;
		while (pos < 0x87) {
			sum += Util.readWord(memoryBytes, pos);
			pos += 2;
		}
		sum &= 0xFFFF;
		return checksum.getUInt() == sum;
	}

}
