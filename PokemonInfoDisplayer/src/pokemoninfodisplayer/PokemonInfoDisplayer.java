/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemoninfodisplayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
	
	public static final int CELL_WIDTH = 200;
	public static final int CELL_HEIGHT = 200;
	
	public static void main(String[] args) {
		// TODO code application logic here
		
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
			partyMemory[0].toPokemonModel(),
			partyMemory[0].toPokemonModel(),
			partyMemory[0].toPokemonModel(),
			partyMemory[0].toPokemonModel(),
			partyMemory[0].toPokemonModel()
		};
		
		Point[] structure = new Point[] {
			new Point(0,0),
			new Point(1,0),
			new Point(0,1),
			new Point(1,1),
			new Point(0,2),
			new Point(1,2)
		};
		
		memoryExtractor.closeProcess();
		
		
		
		JFrame frame = new JFrame("Party Info");
		frame.setSize((int)(CELL_WIDTH*2.5), (int)(CELL_HEIGHT*3.5));
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel(){
			
			@Override
			public void paint(Graphics g){
				super.paint(g);
				
				Graphics2D g2 = (Graphics2D) g;

				//Set  anti-alias!
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON); 

				// Set anti-alias for text
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 
				
				g2.setColor(Color.MAGENTA);
				g2.fillRect(0, 0, CELL_WIDTH*3, CELL_HEIGHT*4);
				for (int i = 0; i < party.length; i++){
					if (party[i].getDexEntry() != 0)
						PokemonCellRenderer.renderPokemonCell(party[i], structure[i], g2);
				}
			}
		};
		panel.setSize(frame.getSize());
		panel.setPreferredSize(frame.getSize());
		
		frame.add(panel);
		long startTime = System.currentTimeMillis();
		long deltaTime = 0;
		while(true) {
			deltaTime += System.currentTimeMillis()-startTime;
			if (deltaTime > 1000) {
				memoryExtractor.openProcess();
				memoryExtractor.readWRAM(bytes);
				memoryExtractor.closeProcess();
				
				for(int i = 0; i < party.length; i++){
					partyMemory[i].update(Utils.getPartyPokemon(i, bytes));
					party[i] = partyMemory[i].toPokemonModel();
				}
				panel.repaint();
				deltaTime = 0;
				startTime = System.currentTimeMillis();
			}
		}
		
	}
	
}
