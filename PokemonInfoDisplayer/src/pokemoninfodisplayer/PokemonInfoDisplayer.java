package pokemoninfodisplayer;

import java.util.Arrays;
import javax.swing.JOptionPane;
import pokemoninfodisplayer.DisplayerOptions.Skin;
import pokemoninfodisplayer.graphics.InfoFrame;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonGame;
import pokemoninfodisplayer.util.ArgumentParser;

/**
 *
 * @author Endre
 */
public class PokemonInfoDisplayer {

	public static boolean DEBUG = false;

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

		InfoFrame frame = new InfoFrame();

		try {
			while (true) {
				PokemonExtractor extractor = null;
				try {
					extractor = new PokemonExtractor(game);
					PartyModel party = new PartyModel();

					while (true) {
						extractor.updateParty(party);
						frame.updateParty(party);
						Thread.sleep(1000);
					}
				} catch (ProcessNotFoundException e) {
					if (DEBUG) {
						throw e;
					}
					JOptionPane.showMessageDialog(null, "Please start the emulator first!", "Info", JOptionPane.INFORMATION_MESSAGE);
					break;
				} catch (UnsupportedPlatformException e) {
					if (DEBUG) {
						throw e;
					}
					JOptionPane.showMessageDialog(null, "This platform is not supported.", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				} catch (IndexOutOfBoundsException e) {
					if (DEBUG) {
						throw e;
					}
				} catch (Exception e) {
					if (DEBUG) {
						throw e;
					}
					int option = JOptionPane.showOptionDialog(
							null, e.toString(), "An exception occurred", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"Continue", "Exit"}, null);
					if (option != 0) {
						break;
					}
				}
				finally {
					if (extractor != null) extractor.close();
				}

				Thread.sleep(1000);
			}
		} finally {
			frame.dispose();
		}

	}
}
