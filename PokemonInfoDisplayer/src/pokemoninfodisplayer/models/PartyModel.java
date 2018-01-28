package pokemoninfodisplayer.models;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class PartyModel {
	
	private final PokemonModel[] partySlot;

	public PartyModel() {
		this.partySlot = new PokemonModel[6];
	}
	
	public void setPartySlot(int index, PokemonModel model) {
		partySlot[index] = model;
	}
	
	public PokemonModel getPartySlot(int index) {
		return partySlot[index];
	}
}
