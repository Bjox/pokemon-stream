package pokemoninfodisplayer.models;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class PartyModel {
	
	private final PokemonModel[] partySlot;

	public PartyModel() {
		this.partySlot = new PokemonModel[6];
//		for (int i = 0; i < partySlot.length; i++) {
//			partySlot[i] = new PokemonModel();
//		}
	}
	
	public void setPartySlot(int index, PokemonModel model) {
		partySlot[index] = model;
	}
	
	public PokemonModel getPartySlot(int index) {
		return partySlot[index];
	}
}
