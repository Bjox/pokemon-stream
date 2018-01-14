package pokemoninfodisplayer.models.gen3;

import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.models.GenExtractor;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonGame;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Gen3Extractor extends GenExtractor {

	public Gen3Extractor(PokemonGame game) throws ProcessNotFoundException, UnsupportedPlatformException {
		super(game);
	}
	
	@Override
	public void update(PartyModel party) throws ProcessNotOpenedException {
		final int partyElementSize = 100;
		byte[] wram = readWRAM();
		
		for (int partyIndex = 0; partyIndex < 6; partyIndex++) {
			byte[] partyElementBytes = new byte[partyElementSize];
			System.arraycopy(wram, 148100 + (partyIndex * partyElementSize), partyElementBytes, 0, partyElementSize);
			
			Gen3MemoryModel memModel = new Gen3MemoryModel(partyElementBytes);
			party.setPartySlot(partyIndex, memModel.toPokemonModel());
		}
		
	}
	
}
