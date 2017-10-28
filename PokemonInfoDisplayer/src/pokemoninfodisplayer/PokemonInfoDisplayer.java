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
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import pokemoninfodisplayer.DisplayerOptions.Skin;
import pokemoninfodisplayer.graphics.PokemonCellRenderer;
import pokemoninfodisplayer.lowlevel.emulator.IEmulatorExtractor;
import pokemoninfodisplayer.lowlevel.emulator.visualboyadvance.VBAExtractor;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.models.gen3.Gen3MemoryModel;
import pokemoninfodisplayer.models.PokemonModel;
import pokemoninfodisplayer.util.ArgumentParser;

/**
 *
 * @author Endre
 */
public class PokemonInfoDisplayer {

	public static boolean DEBUG = false;
	
	public static PokemonCellRenderer renderer = DisplayerOptions.RENDERER();

	public static final int CELL_WIDTH = renderer.SIZE_OVERLAY_TILE.x;
	public static final int CELL_HEIGHT = renderer.SIZE_OVERLAY_TILE.y;
	public static final double SCALE_FACTOR = PokemonCellRenderer.SCALE_FACTOR;

	public static void main(String[] args) throws Exception {
		ArgumentParser argp = new ArgumentParser(args);
		DEBUG = argp.isPresent("-debug");
		
		String skin = argp.getString("-skin", "platinum");
		switch (skin) {
			case "firered":
			case "leafgreen":
			case "gen3":
				DisplayerOptions.setSkin(Skin.FIRERED_LEAFGREEN);
				break;
			case "platinum":
			case "gen4":
				DisplayerOptions.setSkin(Skin.PLATINUM);
				break;
		}
		
		try {
			run();
		} catch (ProcessNotFoundException e) {
			if (DEBUG) throw e;
			JOptionPane.showMessageDialog(null, "Please start VisualBoyAdvance first!", "Info", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			if (DEBUG) throw e;
			JOptionPane.showMessageDialog(null, e.toString(), "An exception occurred", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void run() throws Exception {
		IEmulatorExtractor memoryExtractor = new VBAExtractor();
		memoryExtractor.open();
		
		byte[] bytes = new byte[0x40000];
		memoryExtractor.readWRAM(bytes);

		Gen3MemoryModel[] partyMemory = new Gen3MemoryModel[]{
			new Gen3MemoryModel(Utils.getPartyPokemon(0, bytes)),
			new Gen3MemoryModel(Utils.getPartyPokemon(1, bytes)),
			new Gen3MemoryModel(Utils.getPartyPokemon(2, bytes)),
			new Gen3MemoryModel(Utils.getPartyPokemon(3, bytes)),
			new Gen3MemoryModel(Utils.getPartyPokemon(4, bytes)),
			new Gen3MemoryModel(Utils.getPartyPokemon(5, bytes))
		};

		PokemonModel[] party = new PokemonModel[]{
			partyMemory[0].toPokemonModel(),
			partyMemory[1].toPokemonModel(),
			partyMemory[2].toPokemonModel(),
			partyMemory[3].toPokemonModel(),
			partyMemory[4].toPokemonModel(),
			partyMemory[5].toPokemonModel()
		};

		Point[] structure = new Point[]{
			new Point(0, 0),
			new Point(1, 0),
			new Point(0, 1),
			new Point(1, 1),
			new Point(0, 2),
			new Point(1, 2)
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

				for (int i = 0; i < party.length; i++) {
					if (party[i].getDexEntry() != 0) {
						Point cellPos = structure[i];
						AffineTransform trans = g2.getTransform();
						g2.scale(renderer.SCALE_FACTOR, renderer.SCALE_FACTOR);
						g2.translate(cellPos.x * renderer.SIZE_OVERLAY_TILE.x, cellPos.y * renderer.SIZE_OVERLAY_TILE.y);
						renderer.renderPokemonCell(party[i], g2);
						g2.setTransform(trans);
					}
				}
			}
		};

		panel.setPreferredSize(new Dimension((int) (CELL_WIDTH * SCALE_FACTOR * 2), (int) (CELL_HEIGHT * SCALE_FACTOR * 3)));
		frame.add(panel);
		frame.pack();

		while (true) {
			memoryExtractor.readWRAM(bytes);

			for (int i = 0; i < party.length; i++) {
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
