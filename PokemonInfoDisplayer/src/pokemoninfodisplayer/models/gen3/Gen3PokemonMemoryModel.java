package pokemoninfodisplayer.models.gen3;

import pokemoninfodisplayer.models.memory.PokemonMemoryModel;
import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.models.StatusCondition;
import pokemoninfodisplayer.models.memory.Byt;
import pokemoninfodisplayer.models.memory.Bytes;
import pokemoninfodisplayer.models.memory.Dword;
import pokemoninfodisplayer.models.memory.MField;
import pokemoninfodisplayer.models.memory.Word;

/**
 *
 * @author Endre
 */
public class Gen3PokemonMemoryModel extends PokemonMemoryModel {
	
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
	
	
	public Gen3PokemonMemoryModel(byte[] plainPartyElementBytes) {
		try {
			update(plainPartyElementBytes);
			dataDecoded = new Data(data.getBytes(), personalityValue.getBytes(), OTID.getBytes());
		}
		catch (IllegalAccessException | InstantiationException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public PokemonModel toPokemonModel() {
		return new PokemonModel.Builder()
				.setNickname(Gen3Util.decodeGen3String(nickname.getBytes()))
				.setCurrentHp(currentHP.getUInt())
				.setMaxHp(maxHP.getUInt())
				.setLevel(level.getUInt())
				.setStatusCondition(StatusCondition.parse(statusCondition.getBytes()[0]))
				.setDexEntry(dataDecoded.getDexEntry())
				.setShiny(Gen3Util.isShiny(OTID.getUInt(), personalityValue.getUInt()))
				.setEgg((dataDecoded.miscellaneousBlock.IVEggAbility.getUInt() & 0x40000000) != 0)
				.build();
	}

	@Override
	public boolean validateChecksum() {
		return checksum.getUInt() == dataDecoded.sum;
	}

}
