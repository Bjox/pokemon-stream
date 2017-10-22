/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemoninfodisplayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import pokemoninfodisplayer.graphics.PokemonCellRenderer;
import pokemoninfodisplayer.models.PokemonMemoryModel;
import pokemoninfodisplayer.models.PokemonModel;
import vbajni.*;

/**
 *
 * @author Endre
 */
public class PokemonInfoDisplayer {

	/**
	 * @param args the command line arguments
	 */
	
	public static final int CELL_WIDTH = PokemonCellRenderer.SIZE_OVERLAY_TILE.x;
	public static final int CELL_HEIGHT = PokemonCellRenderer.SIZE_OVERLAY_TILE.y;
	public static final double SCALE_FACTOR = PokemonCellRenderer.SCALE_FACTOR;
	
	public static void main(String[] args) {
		try {
			run();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "An exception occurred", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void run() throws Exception {
		IVBExtractor memoryExtractor = new VBExtractorJNI();
		
		byte[] bytes = new byte[0x40000];
		memoryExtractor.openProcess();
		memoryExtractor.readWRAM(bytes);
		
		PokemonMemoryModel[] partyMemory = new PokemonMemoryModel[] {
			new PokemonMemoryModel(Utils.getPartyPokemon(0, bytes)),
			new PokemonMemoryModel(Utils.getPartyPokemon(1, bytes)),
			new PokemonMemoryModel(Utils.getPartyPokemon(2, bytes)),
			new PokemonMemoryModel(Utils.getPartyPokemon(3, bytes)),
			new PokemonMemoryModel(Utils.getPartyPokemon(4, bytes)),
			new PokemonMemoryModel(Utils.getPartyPokemon(5, bytes))
		};
		
		PokemonModel[] party = new PokemonModel[] {
			partyMemory[0].toPokemonModel(),
			partyMemory[1].toPokemonModel(),
			partyMemory[2].toPokemonModel(),
			partyMemory[3].toPokemonModel(),
			partyMemory[4].toPokemonModel(),
			partyMemory[5].toPokemonModel()
		};
		
		Point[] structure = new Point[] {
			new Point(0,0),
			new Point(1,0),
			new Point(0,1),
			new Point(1,1),
			new Point(0,2),
			new Point(1,2)
		};
		
		
		JFrame frame = new JFrame("Party Info");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel panel = new JPanel() {
			
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				
				Graphics2D g2 = (Graphics2D) g;

				//Set  anti-alias!
//				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON); 

				// Set anti-alias for text
//				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//				RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 
				
				this.setBackground(Color.MAGENTA);
				g2.setColor(Color.WHITE);
				
				for (int i = 0; i < party.length; i++){
					if (party[i].getDexEntry() != 0) {
						Point cellPos = structure[i];
						AffineTransform trans = g2.getTransform();
						g2.scale(PokemonCellRenderer.SCALE_FACTOR, PokemonCellRenderer.SCALE_FACTOR);
						g2.translate(cellPos.x * PokemonCellRenderer.SIZE_OVERLAY_TILE.x, cellPos.y * PokemonCellRenderer.SIZE_OVERLAY_TILE.y);
						PokemonCellRenderer.renderPokemonCell(party[i], g2);
						g2.setTransform(trans);
					}
				}
			}
		};
		
		panel.setPreferredSize(new Dimension((int)(CELL_WIDTH*SCALE_FACTOR*2), (int)(CELL_HEIGHT*SCALE_FACTOR*3)));
		frame.add(panel);
		frame.pack();
		
		while(true) {
				memoryExtractor.readWRAM(bytes);
				
			for(int i = 0; i < party.length; i++) {
				try {
					partyMemory[i].update(Utils.getPartyPokemon(i, bytes));
					party[i] = partyMemory[i].toPokemonModel();
				} catch (SkipRenderTileException e) {
				}
			}
			panel.repaint();
			Thread.sleep(1000);
		}
	}
	
}
