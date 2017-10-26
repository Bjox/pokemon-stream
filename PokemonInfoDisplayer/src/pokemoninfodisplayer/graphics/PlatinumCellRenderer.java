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
import static pokemoninfodisplayer.graphics.PokemonCellRenderer.FONT;
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
		this.POS_TEXT_LVL = new Point(66, 14);
		this.POS_OVERLAY_STATUS = new Point(1, 75);
		this.POS_HP_BAR_START = new Point(23, 79);
		this.POS_HP_BAR_END = new Point(77, 80);
	}

	@Override
	public void renderPokemonCell(PokemonModel pokemon, Graphics2D g2) {
		// Draw tile overlay
		g2.drawImage(IMG_OVERLAY_TILE, 0, 0, SIZE_OVERLAY_TILE.x, SIZE_OVERLAY_TILE.y, null);

		// Draw pokemon image
		BufferedImage pokemonImg = pokemon.getImage();
		g2.drawImage(pokemonImg, POS_POKEMON_IMG.x, POS_POKEMON_IMG.y, null);

		// Draw pokemon name
		g2.setFont(FONT);
		PokemonCellRenderer.renderTextWithShadow(pokemon.nickname, POS_TEXT_NAME.x, POS_TEXT_NAME.y, COLOR_TEXT_NORMAL, COLOR_TEXT_SHADOW, g2);

		// Draw lvl text
		PokemonCellRenderer.renderTextWithShadow(String.valueOf(pokemon.level), POS_TEXT_LVL.x, POS_TEXT_LVL.y, COLOR_TEXT_NORMAL, COLOR_TEXT_SHADOW, g2);

		// Draw status overlay
		BufferedImage statusCondImg = getStatusConditionImg(pokemon.getStatusCondition());
		if (statusCondImg != null) {
			g2.drawImage(statusCondImg, POS_OVERLAY_STATUS.x, POS_OVERLAY_STATUS.y, null);
		}

		// If fainted, draw fainted overlay
		if (pokemon.isFainted()) {
			g2.drawImage(IMG_OVERLAY_FAINTED, POS_OVERLAY_STATUS.x, POS_OVERLAY_STATUS.y, null);
		}
		// else draw HP bar
		else {
			this.renderHPBar(pokemon.current_hp, pokemon.max_hp, g2);
		}
	}
}
