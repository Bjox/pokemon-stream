package pokemoninfodisplayer.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import pokemoninfodisplayer.DisplayerOptions.Skin;
import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.models.StatusCondition;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class PokemonCellRenderer {
	
	protected PokemonCellRenderer(Skin skin) {
		this.PATH_SPRITE				= "./res/skins/" + skin.path_prefix + "/dex_imgs/";
		this.PATH_OVERLAY				= "./res/skins/" + skin.path_prefix + "/overlay/";
		this.PATH_OVERLAY_STATUS		= "./res/skins/" + skin.path_prefix + "/overlay/status/";
		this.PATH_OVERLAY_TILE			= PATH_OVERLAY + "pokemontile.png";
		this.PATH_OVERLAY_TILE_ACTIVE   = PATH_OVERLAY + "pokemontileactive.png";
		this.PATH_OVERLAY_HPBAR			= PATH_OVERLAY + "hpbar.png";
		this.PATH_OVERLAY_BADPOISON		= PATH_OVERLAY_STATUS + "badpoison.png";
		this.PATH_OVERLAY_BURN			= PATH_OVERLAY_STATUS + "burn.png";
		this.PATH_OVERLAY_FAINTED		= PATH_OVERLAY_STATUS + "fainted.png";
		this.PATH_OVERLAY_FREEZE		= PATH_OVERLAY_STATUS + "freeze.png";
		this.PATH_OVERLAY_PARALYZE		= PATH_OVERLAY_STATUS + "paralyze.png";
		this.PATH_OVERLAY_POISON		= PATH_OVERLAY_STATUS + "poison.png";
		this.PATH_OVERLAY_SLEEP			= PATH_OVERLAY_STATUS + "sleep.png";
		
		this.IMG_OVERLAY_TILE			= readImgFromFile(PATH_OVERLAY_TILE);
		this.IMG_OVERLAY_TILE_ACTIVE	= readImgFromFile(PATH_OVERLAY_TILE_ACTIVE);
		this.IMG_OVERLAY_HPBAR			= readImgFromFile(PATH_OVERLAY_HPBAR);
		this.IMG_OVERLAY_BADPOISON		= readImgFromFile(PATH_OVERLAY_BADPOISON);
		this.IMG_OVERLAY_BURN			= readImgFromFile(PATH_OVERLAY_BURN);
		this.IMG_OVERLAY_FAINTED		= readImgFromFile(PATH_OVERLAY_FAINTED);
		this.IMG_OVERLAY_FREEZE			= readImgFromFile(PATH_OVERLAY_FREEZE);
		this.IMG_OVERLAY_PARALYZE		= readImgFromFile(PATH_OVERLAY_PARALYZE);
		this.IMG_OVERLAY_POISON			= readImgFromFile(PATH_OVERLAY_POISON);
		this.IMG_OVERLAY_SLEEP			= readImgFromFile(PATH_OVERLAY_SLEEP);
		
		this.SIZE_OVERLAY_TILE = new Point(IMG_OVERLAY_TILE.getWidth(), IMG_OVERLAY_TILE.getHeight());
		this.FONT_NAME = "./res/skins/" + skin.path_prefix + "/font.ttf";
		this.FONT = createFont(FONT_NAME, Font.PLAIN, FONT_SIZE_DEFAULT);
	}
	
	protected final String PATH_SPRITE;
	protected final String PATH_OVERLAY;
	protected final String PATH_OVERLAY_STATUS;
	protected final String PATH_OVERLAY_TILE;
	protected final String PATH_OVERLAY_TILE_ACTIVE;
	protected final String PATH_OVERLAY_HPBAR;
	protected final String PATH_OVERLAY_BADPOISON;
	protected final String PATH_OVERLAY_BURN;
	protected final String PATH_OVERLAY_FAINTED;
	protected final String PATH_OVERLAY_FREEZE;
	protected final String PATH_OVERLAY_PARALYZE;
	protected final String PATH_OVERLAY_POISON;
	protected final String PATH_OVERLAY_SLEEP;
	
	protected BufferedImage IMG_OVERLAY_TILE;
	protected BufferedImage IMG_OVERLAY_TILE_ACTIVE;
	protected BufferedImage IMG_OVERLAY_HPBAR;
	protected BufferedImage IMG_OVERLAY_BADPOISON;
	protected BufferedImage IMG_OVERLAY_BURN;
	protected BufferedImage IMG_OVERLAY_FAINTED;
	protected BufferedImage IMG_OVERLAY_FREEZE;
	protected BufferedImage IMG_OVERLAY_PARALYZE;
	protected BufferedImage IMG_OVERLAY_POISON;
	protected BufferedImage IMG_OVERLAY_SLEEP;
	
	protected Color COLOR_HP_GREEN_NORMAL;
	protected Color COLOR_HP_GREEN_SHADOW;
	protected Color COLOR_HP_YELLOW_NORMAL;
	protected Color COLOR_HP_YELLOW_SHADOW;
	protected Color COLOR_HP_RED_NORMAL;
	protected Color COLOR_HP_RED_SHADOW;
	protected Color COLOR_TEXT_NORMAL;
	protected Color COLOR_TEXT_SHADOW;
	
	protected Point POS_TEXT_NAME;
	protected Point POS_POKEMON_IMG;
	protected Point POS_TEXT_LVL;
	protected Point POS_OVERLAY_BAR;
	protected Point POS_HP_BAR_START;
	protected Point POS_HP_BAR_END;
	
	protected final String FONT_NAME;
	protected final Font FONT;
	protected static final int FONT_SIZE_DEFAULT = 11;
	
	public static final double SCALE_FACTOR = 2;
	public final Point SIZE_OVERLAY_TILE;
	
	private static boolean SUPPRESS_ERROR_MSG = false;
	
	
	protected BufferedImage getStatusConditionImg(StatusCondition status) {
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
	
	
	protected void renderTextWithShadow(String str, int x, int y, Graphics2D g2) {
		g2.setColor(this.COLOR_TEXT_SHADOW);
		g2.drawString(str, x+1, y);
		g2.drawString(str, x, y+1);
		g2.drawString(str, x+1, y+1);
		
		g2.setColor(this.COLOR_TEXT_NORMAL);
		g2.drawString(str, x, y);
	}
	
	
	protected void renderHPBar(double currenthp, int maxhp, Graphics2D g2) {
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
	
	public void renderPokemonCell(PokemonModel pokemon, Graphics2D g2) {
		// Draw tile overlay
		g2.drawImage(pokemon.isActive() ? IMG_OVERLAY_TILE_ACTIVE : IMG_OVERLAY_TILE, 0, 0, SIZE_OVERLAY_TILE.x, SIZE_OVERLAY_TILE.y, null);

		// Draw pokemon image
		BufferedImage pokemonImg = pokemon.getImg();
		g2.drawImage(pokemonImg, POS_POKEMON_IMG.x, POS_POKEMON_IMG.y, null);

		g2.setFont(FONT);
		
		if (pokemon.isEgg()) {
			// If egg, write Egg as pokemon name
			renderTextWithShadow("Egg", POS_TEXT_NAME.x, POS_TEXT_NAME.y, g2);
			return;
		}
		
		// Draw pokemon name
		renderName(pokemon, g2);

		// Draw lvl text
		this.renderLevelText(pokemon, g2);

		// If fainted, draw fainted overlay
		if (pokemon.isFainted()) {
			g2.drawImage(IMG_OVERLAY_FAINTED, POS_OVERLAY_BAR.x, POS_OVERLAY_BAR.y, null);
		}
		// else draw HP bar
		else {
			// Draw status overlay
			BufferedImage statusCondImg = getStatusConditionImg(pokemon.getStatusCondition());
			if (statusCondImg == null) {
				// Draw regular hp bar overlay
				g2.drawImage(IMG_OVERLAY_HPBAR, POS_OVERLAY_BAR.x, POS_OVERLAY_BAR.y, null);
			} else {
				// Draw status condition hp bar
				g2.drawImage(statusCondImg, POS_OVERLAY_BAR.x, POS_OVERLAY_BAR.y, null);
			}
			double currentHp = pokemon.getCurrentHp();
			if (InfoFrame.CURRENT_HP_GUI_MAP.containsKey(pokemon.getPersonalityValue())) {
				currentHp = InfoFrame.CURRENT_HP_GUI_MAP.get(pokemon.getPersonalityValue());
			}
			this.renderHPBar(currentHp, pokemon.getMaxHp(), g2);
		}
		
	}
	
	protected void renderName(PokemonModel pokekmon, Graphics2D g2) {
		this.renderTextWithShadow(pokekmon.getNickname(), POS_TEXT_NAME.x, POS_TEXT_NAME.y, g2);
	}
	
	protected void renderLevelText(PokemonModel pokemon, Graphics2D g2){
		this.renderTextWithShadow("Lv" + String.valueOf(pokemon.getLevel()), POS_TEXT_LVL.x, POS_TEXT_LVL.y, g2);
	}
	
	protected final Font createFont(int style, int size) {
		return createFont(FONT_NAME, style, size);
	}
	
	private static Font createFont(String filename, int style, int size) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(filename));
			font = font.deriveFont(style, size);
			return font;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, String.format("An error occurred when reading font, %s.", e.getMessage()), "Error", JOptionPane.ERROR_MESSAGE);
			return new Font("Consolas", style, size);
		}
	}
	
	
	protected static BufferedImage readImgFromFile(String filename) {
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
}

