package pokemoninfodisplayer;

import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.models.GenExtractor;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonGame;
import pokemoninfodisplayer.models.gen3.Gen3Extractor;
import pokemoninfodisplayer.models.gen4.Gen4Extractor;
import pokemoninfodisplayer.models.gen5.Gen5Extractor;
import pokemoninfodisplayer.util.Util;

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
			case 5: 
				return new Gen5Extractor(game);
			default:
				throw new UnsupportedOperationException("No memory extractor is currently implemented for generation " + game.generation);
		}
	}
	
	/*
	public static void main(String[] args) throws InterruptedException {
		int numOfRuns = 10;
		int sumCounter = 0;
		int sum = 0;
		while(sumCounter++ < numOfRuns) {
			int counter = 1;
			while (Math.ceil(Math.random()*37000000) != 1){
				counter++;
			}
			sum += counter;
			
			if (counter == 3201){
				System.out.println(sumCounter + ": OBB");
			}
			else if (counter == 1) {
				System.out.println(sumCounter + ": WTF KA I HELVETE");
			}
				
		}
		
		System.out.println("AVG: " + sum/numOfRuns);
	}
	*/
}
