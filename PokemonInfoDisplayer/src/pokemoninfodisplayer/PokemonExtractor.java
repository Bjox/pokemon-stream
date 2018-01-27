package pokemoninfodisplayer;

import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.models.GenExtractor;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonGame;
import pokemoninfodisplayer.models.gen3.Gen3Extractor;
import pokemoninfodisplayer.models.gen4.Gen4Extractor;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class PokemonExtractor {
	
	private final PokemonGame game;
	private final GenExtractor extractor;
	
	public PokemonExtractor(PokemonGame game) throws ProcessNotFoundException, UnsupportedPlatformException {
		this.game = game;
		extractor = createGenExtractor(game);
	}
	
	public void updateParty(PartyModel party) throws Exception {
		extractor.update(party);
	}
	
	public void close() {
		extractor.close();
	}
	
	private static GenExtractor createGenExtractor(PokemonGame game) throws ProcessNotFoundException, UnsupportedPlatformException {
		switch (game.generation) {
			case 3:
				return new Gen3Extractor(game);
			case 4:
				return new Gen4Extractor(game);
			default:
				throw new UnsupportedOperationException("No memory extractor is currently implemented for generation " + game.generation);
		}
	}
	
}
