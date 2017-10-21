/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemoninfodisplayer.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import static pokemoninfodisplayer.PokemonInfoDisplayer.CELL_HEIGHT;
import static pokemoninfodisplayer.PokemonInfoDisplayer.CELL_WIDTH;
import pokemoninfodisplayer.models.PokemonModel;
import static pokemoninfodisplayer.models.PokemonModel.StatusCondition.*;

/**
 *
 * @author Endre
 */
public abstract class PokemonCellRenderer_old {
	
	
	public static Color greenHPColor = new Color(60, 180, 100);
	public static Color yellowHPColor = new Color(240, 210, 80);
	public static Color redHPColor = new Color(180, 50, 40);
	public static Color outline = new Color(0, 0, 0, 100);
	
	public static int HP_BAR_X = CELL_WIDTH / 10;
	public static int HP_BAR_Y = (CELL_HEIGHT / 10) * 8;
	public static int HP_BAR_WIDTH = (CELL_WIDTH / 10) * 8-2;
	public static int HP_BAR_HEIGHT = (CELL_HEIGHT / 10);
	
	public static int NICKNAME_X = CELL_WIDTH / 10 - 5;
	public static int NICKNAME_Y = (int) ((CELL_HEIGHT / 10) + 15);
	
	
	public static int LVL_TXT_X = CELL_WIDTH / 2 + 10;
	public static int LVL_TXT_Y = (CELL_HEIGHT / 2) - 20;
	
	public static int LVL_X = CELL_WIDTH / 2 + 25;
	public static int LVL_Y = (CELL_HEIGHT / 2) + 20;
	
	public static Color[] STATUS_COND_COLORS;
	static {
		STATUS_COND_COLORS = new Color[PokemonModel.StatusCondition.values().length];
		STATUS_COND_COLORS[BAD_POISON.ordinal()] = new Color(110, 0, 214);
		STATUS_COND_COLORS[POISON.ordinal()] = new Color(110, 0, 214);
		STATUS_COND_COLORS[SLEEP.ordinal()] = Color.GRAY;
		STATUS_COND_COLORS[PARALYSIS.ordinal()] = new Color(214, 210, 0);
		STATUS_COND_COLORS[BURN.ordinal()] = Color.RED;
		STATUS_COND_COLORS[FREEZE.ordinal()] = Color.BLUE;
	}
	
	public static void renderPokemonCell(PokemonModel model, Point p, Graphics2D g){
		
		g.drawImage(model.getImage(), p.x*CELL_WIDTH+15, p.y*CELL_HEIGHT+45, CELL_WIDTH/2, CELL_HEIGHT/2, null);
		
		g.setColor(Color.BLACK);
		g.fillRect(HP_BAR_X+p.x*CELL_WIDTH, HP_BAR_Y+p.y*CELL_HEIGHT, HP_BAR_WIDTH, HP_BAR_HEIGHT);
		float hp_ratio = model.current_hp/(float)model.max_hp;
		if (hp_ratio > 0.5) g.setColor(greenHPColor);
		else if (hp_ratio > 0.15) g.setColor(yellowHPColor);
		else g.setColor(redHPColor);
		g.fillRect(HP_BAR_X+p.x*CELL_WIDTH, HP_BAR_Y+p.y*CELL_HEIGHT, (int) (HP_BAR_WIDTH*hp_ratio), HP_BAR_HEIGHT);
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(3));
		g.drawRect(HP_BAR_X+p.x*CELL_WIDTH, HP_BAR_Y+p.y*CELL_HEIGHT, HP_BAR_WIDTH, HP_BAR_HEIGHT);
		
		g.setFont(new Font("Power Red and Green", Font.BOLD, 25));
		g.drawChars(model.nickname.toCharArray(), 0, model.nickname.length(), NICKNAME_X+p.x*CELL_WIDTH, NICKNAME_Y+p.y*CELL_HEIGHT);
		
		
		g.setFont(new Font("Power Red and Green", Font.BOLD, 40));
		g.drawChars("Lvl".toCharArray(), 0, "Lvl".length(), LVL_TXT_X+p.x*CELL_WIDTH, LVL_TXT_Y+p.y*CELL_HEIGHT);
		g.drawChars(String.valueOf(model.level).toCharArray(), 0, String.valueOf(model.level).length(), LVL_X+p.x*CELL_WIDTH, LVL_Y+p.y*CELL_HEIGHT);
		
		PokemonModel.StatusCondition statCond = model.getStatusCondition();
		if (statCond != null) {
			int xpos = LVL_X+p.x*CELL_WIDTH-5;
			int ypos = LVL_Y+p.y*CELL_HEIGHT+25;
			int fontsize = 18;
			g.setColor(STATUS_COND_COLORS[statCond.ordinal()]);
			g.fillRoundRect(xpos-8, ypos-fontsize+2, 60, 20, 5, 5);
			g.setFont(new Font("Power Red and Green", Font.ITALIC | Font.BOLD, fontsize));
			g.setColor(Color.WHITE);
			g.drawChars(statCond.shortName.toCharArray(), 0, statCond.shortName.length(), xpos, ypos);
		}
		
		g.setStroke(new BasicStroke(10));
		g.drawRoundRect(p.x*CELL_WIDTH+5, p.y*CELL_HEIGHT+5, CELL_WIDTH-10, CELL_HEIGHT-5, 30, 30);
	}
}
