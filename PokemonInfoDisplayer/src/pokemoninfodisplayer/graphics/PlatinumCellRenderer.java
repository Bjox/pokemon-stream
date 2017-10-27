/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemoninfodisplayer.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import pokemoninfodisplayer.DisplayerOptions;
import pokemoninfodisplayer.models.PokemonModel;

/**
 *
 * @author Endre
 */
public class PlatinumCellRenderer extends PokemonCellRenderer{
	
	public static final PlatinumCellRenderer Instance = new PlatinumCellRenderer();

	private PlatinumCellRenderer(){
		super(DisplayerOptions.Skin.PLATINUM);
		
		this.COLOR_HP_GREEN_NORMAL = new Color(68, 162, 73);
		this.COLOR_HP_GREEN_SHADOW = new Color(68, 162, 73);
		this.COLOR_HP_YELLOW_NORMAL = new Color(222, 186, 100);
		this.COLOR_HP_YELLOW_SHADOW = new Color(222, 186, 100);
		this.COLOR_HP_RED_NORMAL = new Color(215, 70, 25);
		this.COLOR_HP_RED_SHADOW = new Color(215, 70, 25);
		this.COLOR_TEXT_NORMAL = new Color(0xF6F5EE);
		this.COLOR_TEXT_SHADOW = new Color(0x292E21);
		
		this.POS_TEXT_NAME = new Point(7, 15);
		this.POS_POKEMON_IMG = new Point(6, 5);
		this.POS_TEXT_LVL = new Point(59, 15);
		this.POS_OVERLAY_STATUS = new Point(1, 75);
		this.POS_HP_BAR_START = new Point(23, 79);
		this.POS_HP_BAR_END = new Point(77, 80);
	}
	
	@Override
	protected void renderLevelText(PokemonModel pokemon, Graphics2D g2){
		this.renderTextWithShadow("Lv"+String.valueOf(pokemon.level), POS_TEXT_LVL.x, POS_TEXT_LVL.y, g2);
	}
}
