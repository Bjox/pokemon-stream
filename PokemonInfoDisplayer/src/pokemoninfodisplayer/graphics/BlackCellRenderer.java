package pokemoninfodisplayer.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import pokemoninfodisplayer.DisplayerOptions;
import pokemoninfodisplayer.models.Gender;
import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.util.Pair;

/**
 *
 * @author Endre
 */
public class BlackCellRenderer extends PokemonCellRenderer {
	
	public static final BlackCellRenderer Instance = new BlackCellRenderer();
	
	protected final String PATH_OVERLAY_FEMALE;
	protected final String PATH_OVERLAY_MALE;
	
	protected BufferedImage IMG_OVERLAY_FEMALE;
	protected BufferedImage IMG_OVERLAY_MALE;
	
	protected Point POS_TEXT_HP;
	protected Point POS_OVERLAY_GENDER;
	
	protected BitmapFont hpFont;

	private BlackCellRenderer(){
		super(DisplayerOptions.Skin.BLACK);
		
		this.COLOR_HP_GREEN_SHADOW = new Color(0x63FE63);
		this.COLOR_HP_GREEN_NORMAL = new Color(0x18C521);
		this.COLOR_HP_YELLOW_SHADOW = new Color(0xFEDD00);
		this.COLOR_HP_YELLOW_NORMAL = new Color(0xEEAC00);
		this.COLOR_HP_RED_SHADOW = new Color(0xFF3142);
		this.COLOR_HP_RED_NORMAL = new Color(0x942131);
		this.COLOR_TEXT_NORMAL = new Color(0xFEFEFE);
		this.COLOR_TEXT_SHADOW = new Color(0x838383);
		
		this.POS_TEXT_NAME = new Point(11, 13); // Done
		this.POS_POKEMON_IMG = new Point(-3, -3); // Done 
		this.POS_TEXT_LVL = new Point(5, 78); // Done
		this.POS_OVERLAY_BAR = new Point(4, 70); // Done
		this.POS_HP_BAR_START = new Point(27, 73); // Done
		this.POS_HP_BAR_END = new Point(81, 75); // Done
		
		this.PATH_OVERLAY_FEMALE = PATH_OVERLAY + "female.png";
		this.PATH_OVERLAY_MALE = PATH_OVERLAY + "male.png";
		
		this.IMG_OVERLAY_FEMALE = readImgFromFile(PATH_OVERLAY_FEMALE);
		this.IMG_OVERLAY_MALE = readImgFromFile(PATH_OVERLAY_MALE);
		
		this.POS_TEXT_HP = new Point(33, 78);
		this.POS_OVERLAY_GENDER = new Point(76, POS_TEXT_NAME.y - IMG_OVERLAY_FEMALE.getHeight() + 1);
		
		this.hpFont = new BitmapFont(new File(PATH_OVERLAY + "font"),
				new Pair<>("slash", '/'),
				new Pair<>("lv", 'L')
		);
	}
	
	@Override
	public void renderPokemonCell(PokemonModel pokemon, Graphics2D g2) {
		super.renderPokemonCell(pokemon, g2);
		this.renderHPAsText(pokemon, g2);
	}

	@Override
	protected void renderName(PokemonModel pokekmon, Graphics2D g2) {
		super.renderName(pokekmon, g2);
		
		Gender gender = pokekmon.getGender();
		
		if (gender == Gender.GENDERLESS) {
			return;
		}
		
		BufferedImage img = pokekmon.getGender() == Gender.FEMALE ? IMG_OVERLAY_FEMALE : IMG_OVERLAY_MALE;
		g2.drawImage(img, POS_OVERLAY_GENDER.x, POS_OVERLAY_GENDER.y, null);
	}

	@Override
	protected void renderLevelText(PokemonModel pokemon, Graphics2D g2) {
		var transform = g2.getTransform();
		g2.translate(POS_TEXT_LVL.x, POS_TEXT_LVL.y);
		g2.scale(0.8, 1);
		hpFont.draw(g2, "L" + pokemon.getLevel(), 0, 0);
		g2.setTransform(transform);
	}
	
	protected void renderHPAsText(PokemonModel pokemon, Graphics2D g2) {
		int currentHp = pokemon.getCurrentHp();
		if (InfoFrame.CURRENT_HP_GUI_MAP.containsKey(pokemon.getPersonalityValue())) {
			currentHp = (int) Math.round(InfoFrame.CURRENT_HP_GUI_MAP.get(pokemon.getPersonalityValue()));
		}
		
		String maxHpStr = String.valueOf(pokemon.getMaxHp());
		String currHpStr = String.valueOf(currentHp);
		currHpStr = " ".repeat(Math.max(0, maxHpStr.length() - currHpStr.length())) + currHpStr;
		
		String hpText = currHpStr + "/" + maxHpStr;
		
		var scale = pokemon.getMaxHp() > 99 ? 0.8 : 1.0;
		var transform = g2.getTransform();
		
		g2.translate(POS_TEXT_HP.x, POS_TEXT_HP.y);
		g2.scale(scale, 1);
		hpFont.draw(g2, hpText, 0, 0);
		g2.setTransform(transform);
	}
	
	
}
