/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemoninfodisplayer;

import pokemoninfodisplayer.graphics.FireRedLeafGreenCellRenderer;
import pokemoninfodisplayer.graphics.PlatinumCellRenderer;
import pokemoninfodisplayer.graphics.PokemonCellRenderer;

/**
 *
 * @author Endre
 */
public class DisplayerOptions {
	
	public enum Skin {
		FIRERED_LEAFGREEN("FireRedLeafGreen"),
		PLATINUM("Platinum");
		
		public final String path_prefix;
 
		Skin(String envUrl) {
			this.path_prefix = envUrl;
		}
	}
	
	private static Skin SKIN = Skin.PLATINUM;
	
	public static PokemonCellRenderer RENDERER() {
		switch(SKIN){
			case FIRERED_LEAFGREEN: return FireRedLeafGreenCellRenderer.Instance;
			case PLATINUM: return PlatinumCellRenderer.Instance;
		}
		return null;
	}
	
	public static String PATH_PREFIX(){
		switch(SKIN){
			case FIRERED_LEAFGREEN: return Skin.FIRERED_LEAFGREEN.path_prefix;
			case PLATINUM: return Skin.PLATINUM.path_prefix;
		}
		return null;
	}
	
	public static void setSkin(Skin skin){
		SKIN = skin;
	}
}
