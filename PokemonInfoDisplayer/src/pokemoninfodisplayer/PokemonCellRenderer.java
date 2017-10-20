/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemoninfodisplayer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import static pokemoninfodisplayer.PokemonInfoDisplayer.CELL_HEIGHT;
import static pokemoninfodisplayer.PokemonInfoDisplayer.CELL_WIDTH;
import pokemoninfodisplayer.models.PokemonModel;

/**
 *
 * @author Endre
 */
public abstract class PokemonCellRenderer {
	
	public static Color greenHPColor = new Color(60, 180, 100);
	public static Color yellowHPColor = new Color(240, 210, 80);
	public static Color redHPColor = new Color(180, 50, 40);
	public static Color outline = new Color(0, 0, 0, 100);
	
	public static int HP_BAR_X = CELL_WIDTH / 10;
	public static int HP_BAR_Y = (CELL_HEIGHT / 10) * 8;
	public static int HP_BAR_WIDTH = (CELL_WIDTH / 10) * 8-2;
	public static int HP_BAR_HEIGHT = (CELL_HEIGHT / 10);
	
	public static int NICKNAME_X = CELL_WIDTH / 10;
	public static int NICKNAME_Y = (int) ((CELL_HEIGHT / 10) + 15);
	
	
	public static int LVL_TXT_X = CELL_WIDTH / 2 + 10;
	public static int LVL_TXT_Y = (CELL_HEIGHT / 2) - 20;
	
	public static int LVL_X = CELL_WIDTH / 2 + 25;
	public static int LVL_Y = (CELL_HEIGHT / 2) + 20;
	
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
		
		g.setFont(new Font("Power Red and Green", Font.BOLD, 30));
		g.drawChars(model.nickname.toCharArray(), 0, model.nickname.length(), NICKNAME_X+p.x*CELL_WIDTH, NICKNAME_Y+p.y*CELL_HEIGHT);
		
		
		g.setFont(new Font("Power Red and Green", Font.BOLD, 40));
		g.drawChars("Lvl".toCharArray(), 0, "Lvl".length(), LVL_TXT_X+p.x*CELL_WIDTH, LVL_TXT_Y+p.y*CELL_HEIGHT);
		g.drawChars(String.valueOf(model.level).toCharArray(), 0, String.valueOf(model.level).length(), LVL_X+p.x*CELL_WIDTH, LVL_Y+p.y*CELL_HEIGHT);
		
		g.setStroke(new BasicStroke(10));
		g.drawRoundRect(p.x*CELL_WIDTH+5, p.y*CELL_HEIGHT+5, CELL_WIDTH-10, CELL_HEIGHT-5, 30, 30);
	}
}
