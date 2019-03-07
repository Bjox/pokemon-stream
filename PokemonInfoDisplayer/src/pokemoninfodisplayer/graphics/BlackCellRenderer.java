package pokemoninfodisplayer.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import pokemoninfodisplayer.DisplayerOptions;
import pokemoninfodisplayer.models.PokemonModel;

/**
 *
 * @author Endre
 */
public class BlackCellRenderer extends PokemonCellRenderer {
	
	public static final BlackCellRenderer Instance = new BlackCellRenderer();
	
	
	protected final Point POS_TEXT_HP;

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
		
		this.POS_TEXT_HP = new Point(39, 84); // Done
	}
	
	@Override
	public void renderPokemonCell(PokemonModel pokemon, Graphics2D g2) {
		super.renderPokemonCell(pokemon, g2);
		this.renderHPAsText(pokemon, g2);
	}

	@Override
	protected void renderName(PokemonModel pokemon, Graphics2D g2) {
		AffineTransform orig = g2.getTransform();
		int overflowChars = Math.max(pokemon.getNickname().length() - 8, 0);
		g2.scale(1.0 - overflowChars * 0.1, 1.0); // Not 100% accurate scaling, but works for nick length up to 10 chars.
		super.renderName(pokemon, g2);
		g2.setTransform(orig);
	}
	
	protected void renderHPAsText(PokemonModel pokemon, Graphics2D g2) {
		
		String hpText = pokemon.getCurrentHp() + "/" + pokemon.getMaxHp();
		
		this.renderTextWithShadow(hpText, this.POS_TEXT_HP.x, this.POS_TEXT_HP.y, g2);
	}
	
	
}
