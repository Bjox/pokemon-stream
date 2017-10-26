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
		this.POS_TEXT_LVL = new Point(77, 12);
		this.POS_OVERLAY_STATUS = new Point(4, 79);
		this.POS_HP_BAR_START = new Point(20, 81);
		this.POS_HP_BAR_END = new Point(83, 83);
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
