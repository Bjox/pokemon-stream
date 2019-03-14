package pokemoninfodisplayer.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import pokemoninfodisplayer.DisplayerOptions;
import pokemoninfodisplayer.models.Gender;
import pokemoninfodisplayer.models.PokemonModel;

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
		this.POS_TEXT_LVL = new Point(8, 84); // Done
		this.POS_OVERLAY_BAR = new Point(4, 70); // Done
		this.POS_HP_BAR_START = new Point(27, 73); // Done
		this.POS_HP_BAR_END = new Point(81, 75); // Done
		
		this.PATH_OVERLAY_FEMALE = PATH_OVERLAY + "female.png";
		this.PATH_OVERLAY_MALE = PATH_OVERLAY + "male.png";
		
		this.IMG_OVERLAY_FEMALE = readImgFromFile(PATH_OVERLAY_FEMALE);
		this.IMG_OVERLAY_MALE = readImgFromFile(PATH_OVERLAY_MALE);
		
		this.POS_TEXT_HP = new Point(39, 84);
		this.POS_OVERLAY_GENDER = new Point(76, POS_TEXT_NAME.y - IMG_OVERLAY_FEMALE.getHeight() + 1);
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
	
	protected void renderHPAsText(PokemonModel pokemon, Graphics2D g2) {
		int currentHp = pokemon.getCurrentHp();
		if (InfoFrame.CURRENT_HP_GUI_MAP.containsKey(pokemon.getPersonalityValue())) {
			currentHp = (int) Math.round(InfoFrame.CURRENT_HP_GUI_MAP.get(pokemon.getPersonalityValue()));
		}
		String hpText = currentHp + "/" + pokemon.getMaxHp();
		this.renderTextWithShadow(hpText, this.POS_TEXT_HP.x, this.POS_TEXT_HP.y, g2);
	}
	
	
}
