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
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import pokemoninfodisplayer.DisplayerOptions.Skin;
import pokemoninfodisplayer.graphics.PokemonCellRenderer;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonGame;
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

	public static final Point[] LAYOUT = new Point[]{
		new Point(0, 0),
		new Point(1, 0),
		new Point(0, 1),
		new Point(1, 1),
		new Point(0, 2),
		new Point(1, 2)
	};

	public static void main(String[] args) throws Exception {
		ArgumentParser argp = new ArgumentParser(args);
		DEBUG = argp.isPresent("-debug");
		
		// Parse skin
		String skinArg = argp.getString("-skin", "platinum");
		switch (skinArg) {
			case "firered":
			case "leafgreen":
			case "gen3":
				DisplayerOptions.setSkin(Skin.FIRERED_LEAFGREEN);
				break;
			case "platinum":
			case "gen4":
				DisplayerOptions.setSkin(Skin.PLATINUM);
				break;
			default:
				System.out.println("Invalid skin.");
				return;
		}

		// Parse game
		String gameArg = argp.getString("-game");
		PokemonGame game = null;
		if (gameArg == null) {
			Object obj = JOptionPane.showInputDialog(
					null, "Select a game:", "Select game", JOptionPane.INFORMATION_MESSAGE, null, PokemonGame.values(), null);
			if (obj == null) {
				System.out.println("Please specify a game using -game <name>");
				return;
			}
			game = (PokemonGame) obj;
		}
		for (PokemonGame pg : PokemonGame.values()) {
			if (pg.toString().equalsIgnoreCase(gameArg)) {
				game = pg;
				break;
			}
		}
		if (game == null) {
			System.out.println("Invalid game. Legal values are: " + Arrays.toString(PokemonGame.values()));
			return;
		}

		System.out.println("Game: " + game.toString());
		System.out.println("Skin: " + skinArg);

		try {
			run(game);
		} catch (ProcessNotFoundException e) {
			if (DEBUG) {
				throw e;
			}
			JOptionPane.showMessageDialog(null, "Please start the emulator first!", "Info", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			if (DEBUG) {
				throw e;
			}
			JOptionPane.showMessageDialog(null, e.toString(), "An exception occurred", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void run(PokemonGame game) throws Exception {
		PokemonExtractor extractor = new PokemonExtractor(game);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			extractor.close();
		}));
		
		PartyModel party = new PartyModel();
		extractor.updateParty(party);

		JFrame frame = new JFrame("Party Info");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);

				Graphics2D g2 = (Graphics2D) g;

//				// Set anti-alias
//				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON); 
//				// Set anti-alias for text
//				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//				RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 
				
				this.setBackground(Color.MAGENTA);
				g2.setColor(Color.WHITE);
				
				for (int i = 0; i < 6; i++) {
					PokemonModel pok = party.getPartySlot(i);
					
					if (pok.getDexEntry() != 0) {
						Point cellPos = LAYOUT[i];
						AffineTransform trans = g2.getTransform();
						
						g2.scale(SCALE_FACTOR, SCALE_FACTOR);
						g2.translate(cellPos.x * renderer.SIZE_OVERLAY_TILE.x, cellPos.y * renderer.SIZE_OVERLAY_TILE.y);
						
						renderer.renderPokemonCell(pok, g2);
						g2.setTransform(trans);
					}
				}
			}
		};

		panel.setPreferredSize(new Dimension((int) (CELL_WIDTH * SCALE_FACTOR * 2), (int) (CELL_HEIGHT * SCALE_FACTOR * 3)));
		frame.add(panel);
		frame.pack();

		while (true) {
			Thread.sleep(1000);
			extractor.updateParty(party);
			panel.repaint();
		}
	}

}
