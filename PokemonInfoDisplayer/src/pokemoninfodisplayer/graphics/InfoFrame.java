package pokemoninfodisplayer.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import pokemoninfodisplayer.DisplayerOptions;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonModel;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class InfoFrame extends JFrame {
	
	public static final PokemonCellRenderer renderer = DisplayerOptions.RENDERER();
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
	
	private PartyModel party;
	
	public final static HashMap<Integer, Double> CURRENT_HP_GUI_MAP = new HashMap<>();

	public InfoFrame() throws HeadlessException {
		setTitle("PartyInfo");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				this.setBackground(Color.MAGENTA);
				g2.setColor(Color.WHITE);
				
				if (party == null) return;
				
				for (int i = 0; i < 6; i++) {
					PokemonModel pok = party.getPartySlot(i);
					updateGUIValues(pok);
					if (pok != null) {
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
		
		Dimension panelSize = new Dimension((int) (CELL_WIDTH * SCALE_FACTOR * 2), (int) (CELL_HEIGHT * SCALE_FACTOR * 3));
		
		panel.setPreferredSize(panelSize);
		add(panel);
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void updateParty(PartyModel party) {
		this.party = party;
		repaint();
		pack();
	}
	
	private void updateGUIValues(PokemonModel pok) {
		if (pok == null) {
			return;
		}
		if (!CURRENT_HP_GUI_MAP.containsKey(pok.getPersonalityValue())) {
			return;
		}
		var currentHpGUI = CURRENT_HP_GUI_MAP.get(pok.getPersonalityValue());
		if (currentHpGUI == null || currentHpGUI == pok.getCurrentHp()) {
			return;
		}
		var speed = pok.getMaxHp() * 0.005;
		var diff = pok.getCurrentHp() - currentHpGUI; 
		if (Math.abs(diff) < speed) {
			CURRENT_HP_GUI_MAP.put(pok.getPersonalityValue(), (double) pok.getCurrentHp());
			return;
		}
		var delta = speed * diff/(double)Math.abs(diff); // speed * direction
		
		CURRENT_HP_GUI_MAP.put(pok.getPersonalityValue(), currentHpGUI + delta);
	}

}
