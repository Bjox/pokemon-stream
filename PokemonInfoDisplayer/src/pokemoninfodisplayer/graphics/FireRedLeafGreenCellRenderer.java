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
public class FireRedLeafGreenCellRenderer extends PokemonCellRenderer{
		
		
	public static final FireRedLeafGreenCellRenderer Instance = new FireRedLeafGreenCellRenderer();

	private FireRedLeafGreenCellRenderer(){
		super(DisplayerOptions.Skin.FIRERED_LEAFGREEN);
		
		this.COLOR_HP_GREEN_NORMAL = new Color(112, 248, 168);
		this.COLOR_HP_GREEN_SHADOW = new Color(88, 208, 128);
		this.COLOR_HP_YELLOW_NORMAL = new Color(248, 224, 56);
		this.COLOR_HP_YELLOW_SHADOW = new Color(200, 168, 8);
		this.COLOR_HP_RED_NORMAL = new Color(248, 88, 56);
		this.COLOR_HP_RED_SHADOW = new Color(168, 64, 72);
		this.COLOR_TEXT_NORMAL = new Color(0x404040);
		this.COLOR_TEXT_SHADOW = new Color(0xC5C5C5);
		
		this.POS_TEXT_NAME = new Point(4, 12);
		this.POS_POKEMON_IMG = new Point(13, 13);
		this.POS_TEXT_LVL = new Point(69, 12);
		this.POS_OVERLAY_BAR = new Point(4, 79);
		this.POS_HP_BAR_START = new Point(20, 81);
		this.POS_HP_BAR_END = new Point(83, 83);
	}
}
