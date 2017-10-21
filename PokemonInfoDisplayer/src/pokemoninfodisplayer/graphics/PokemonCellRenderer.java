package pokemoninfodisplayer.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import pokemoninfodisplayer.models.PokemonModel;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class PokemonCellRenderer {
	
	public static final String PATH_SPRITE            = "./res/dex_imgs/";
	public static final String PATH_OVERLAY           = "./res/overlay/";
	public static final String PATH_OVERLAY_STATUS    = "./res/overlay/status/";
	public static final String PATH_OVERLAY_TILE      = PATH_OVERLAY + "pokemontile.png";
	public static final String PATH_OVERLAY_BADPOISON = PATH_OVERLAY_STATUS + "badpoison.png";
	public static final String PATH_OVERLAY_BURN      = PATH_OVERLAY_STATUS + "burn.png";
	public static final String PATH_OVERLAY_FAINTED   = PATH_OVERLAY_STATUS + "fainted.png";
	public static final String PATH_OVERLAY_FREEZE    = PATH_OVERLAY_STATUS + "freeze.png";
	public static final String PATH_OVERLAY_PARALYZE  = PATH_OVERLAY_STATUS + "paralyze.png";
	public static final String PATH_OVERLAY_POISON    = PATH_OVERLAY_STATUS + "poison.png";
	public static final String PATH_OVERLAY_SLEEP     = PATH_OVERLAY_STATUS + "sleep.png";
	
	public static final BufferedImage IMG_OVERLAY_TILE      = readImgFromFile(PATH_OVERLAY_TILE);
	public static final BufferedImage IMG_OVERLAY_BADPOISON = readImgFromFile(PATH_OVERLAY_BADPOISON);
	public static final BufferedImage IMG_OVERLAY_BURN      = readImgFromFile(PATH_OVERLAY_BURN);
	public static final BufferedImage IMG_OVERLAY_FAINTED   = readImgFromFile(PATH_OVERLAY_FAINTED);
	public static final BufferedImage IMG_OVERLAY_FREEZE    = readImgFromFile(PATH_OVERLAY_FREEZE);
	public static final BufferedImage IMG_OVERLAY_PARALYZE  = readImgFromFile(PATH_OVERLAY_PARALYZE);
	public static final BufferedImage IMG_OVERLAY_POISON    = readImgFromFile(PATH_OVERLAY_POISON);
	public static final BufferedImage IMG_OVERLAY_SLEEP     = readImgFromFile(PATH_OVERLAY_SLEEP);
	
	public static final Point SIZE_OVERLAY_TILE = new Point(IMG_OVERLAY_TILE.getWidth(), IMG_OVERLAY_TILE.getHeight());
	
	public static final Color COLOR_HP_GREEN_NORMAL = new Color(112, 248, 168);
	public static final Color COLOR_HP_GREEN_SHADOW = new Color(88, 208, 128);
	public static final Color COLOR_HP_YELLOW_NORMAL = new Color(248, 224, 56);
	public static final Color COLOR_HP_YELLOW_SHADOW = new Color(200, 168, 8);
	public static final Color COLOR_HP_RED_NORMAL = new Color(248, 88, 56);
	public static final Color COLOR_HP_RED_SHADOW = new Color(168, 64, 72);
	public static final Color COLOR_TEXT_NORMAL = new Color(0x404040);
	public static final Color COLOR_TEXT_SHADOW = new Color(0xC5C5C5);
	
	public static final Point POS_TEXT_NAME = new Point(4, 12);
	public static final Point POS_POKEMON_IMG = new Point(13, 13);
	public static final Point POS_TEXT_LVL = new Point(77, 12);
	public static final Point POS_OVERLAY_STATUS = new Point(4, 79);
	public static final Point POS_HP_BAR_START = new Point(20, 81);
	public static final Point POS_HP_BAR_END = new Point(83, 83);
	
	public static final String FONT_NAME = "./res/pkmnems.ttf";
	public static final int FONT_SIZE = 11;
	public static final Font FONT = getFont(FONT_NAME, Font.PLAIN, FONT_SIZE);
	
	public static final double SCALE_FACTOR = 2;
	
	private static boolean SUPPRESS_ERROR_MSG = false;
	
	
	private static Font getFont(String filename, int style, int size) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(filename));
			font = font.deriveFont(style, size);
			return font;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, String.format("An error occurred when reading font, %s.", e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
			return new Font("Consolas", style, size);
		}
	}
	
	
	private static BufferedImage readImgFromFile(String filename) {
		try {
			File file = new File(filename);
			if (!file.exists()) {
				throw new FileNotFoundException("FileNotFoundException");
			}
			return ImageIO.read(file);
		} catch (IOException e) {
			if (!SUPPRESS_ERROR_MSG) {
				SUPPRESS_ERROR_MSG = true;
				JOptionPane.showMessageDialog(
						null, String.format("An error occurred when reading image %s: %s.", filename, e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
			}
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		}
	}
	
	
	private static BufferedImage getStatusConditionImg(PokemonModel.StatusCondition status) {
		if (status == null) return null;
		
		switch (status) {
			case SLEEP:
				return IMG_OVERLAY_SLEEP;
			case POISON:
				return IMG_OVERLAY_POISON;
			case BURN:
				return IMG_OVERLAY_BURN;
			case FREEZE:
				return IMG_OVERLAY_FREEZE;
			case PARALYSIS:
				return IMG_OVERLAY_PARALYZE;
			case BAD_POISON:
				return IMG_OVERLAY_BADPOISON;
			default:
				return null;
		}
	}
	
	
	private enum HPBarColor {
		GREEN, YELLOW, RED;
	}
	
	
	private static HPBarColor getHPBarColor(double ratio) {
		if (ratio > 0.5) {
			return HPBarColor.GREEN;
		}
		if (ratio > 0.2) {
			return HPBarColor.YELLOW;
		}
		return HPBarColor.RED;
	}
	
	
	private static void renderTextWithShadow(String str, int x, int y, Color textColor, Color shadowColor, Graphics2D g2) {
		g2.setColor(shadowColor);
		g2.drawString(str, x+1, y);
		g2.drawString(str, x, y+1);
		g2.drawString(str, x+1, y+1);
		
		g2.setColor(textColor);
		g2.drawString(str, x, y);
	}
	
	
	private static void renderHPBar(int currenthp, int maxhp, Graphics2D g2) {
		double ratio = currenthp / (double) maxhp;
		HPBarColor hpbarcolor = getHPBarColor(ratio);
		
		Color color = null;
		Color shadowColor = null;
		
		switch (hpbarcolor) {
			case GREEN:
				color = COLOR_HP_GREEN_NORMAL;
				shadowColor = COLOR_HP_GREEN_SHADOW;
				break;
			case YELLOW:
				color = COLOR_HP_YELLOW_NORMAL;
				shadowColor = COLOR_HP_YELLOW_SHADOW;
				break;
			case RED:
				color = COLOR_HP_RED_NORMAL;
				shadowColor = COLOR_HP_RED_SHADOW;
				break;
		}
		
		int width = (int) Math.ceil((POS_HP_BAR_END.x - POS_HP_BAR_START.x + 1) * ratio);
		int height = POS_HP_BAR_END.y - POS_HP_BAR_START.y + 1;
		
		g2.setColor(color);
		g2.fillRect(POS_HP_BAR_START.x, POS_HP_BAR_START.y, width, height);
		
		g2.setColor(shadowColor);
		g2.fillRect(POS_HP_BAR_START.x, POS_HP_BAR_START.y, width, 1);
	}
	
	
	public static void renderPokemonCell(PokemonModel pokemon, Graphics2D g2) {
		// Draw tile overlay
		g2.drawImage(IMG_OVERLAY_TILE, 0, 0, SIZE_OVERLAY_TILE.x, SIZE_OVERLAY_TILE.y, null);
		
		// Draw pokemon image
		BufferedImage pokemonImg = pokemon.getImage();
		g2.drawImage(pokemonImg, POS_POKEMON_IMG.x, POS_POKEMON_IMG.y, null);
		
		// Draw pokemon name
		g2.setFont(FONT);
		renderTextWithShadow(pokemon.nickname, POS_TEXT_NAME.x, POS_TEXT_NAME.y, COLOR_TEXT_NORMAL, COLOR_TEXT_SHADOW, g2);
		
		// Draw lvl text
		renderTextWithShadow(String.valueOf(pokemon.level), POS_TEXT_LVL.x, POS_TEXT_LVL.y, COLOR_TEXT_NORMAL, COLOR_TEXT_SHADOW, g2);
		
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
			renderHPBar(pokemon.current_hp, pokemon.max_hp, g2);
		}
	}
	
}
