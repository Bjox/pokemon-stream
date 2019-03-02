package pokemoninfodisplayer;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
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
		DisplayerOptions.setSkin(parseSkinArgument(argp, Skin.PLATINUM));

		// Parse game
		PokemonGame game = parseGameArgument(argp);
		if (game == null) {
			return;
		}

		System.out.println("Game: " + game);
		System.out.println("Skin: " + DisplayerOptions.getCurrentSkin());

		InfoFrame frame = new InfoFrame();
		
		while (true) {
			PokemonExtractor extractor = null;

			try {
				extractor = new PokemonExtractor(game);
				PartyModel party = new PartyModel();

				while (true) {
					extractor.updateParty(party);
					frame.updateParty(party);
					Thread.sleep(500);
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
				if (extractor != null) {
					extractor.close();
				}
			}

			Thread.sleep(1000);
		}

		frame.dispose();
	}
	
	private static Skin parseSkinArgument(ArgumentParser argp, Skin defaultSkin) {
		if (!argp.isPresent("-skin")) {
			return defaultSkin;
		}
		
		String skinArg = argp.getString("-skin");
		
		switch (skinArg) {
			case "firered":
			case "leafgreen":
			case "gen3":
				return Skin.FIRERED_LEAFGREEN;
			case "platinum":
			case "gen4":
				return Skin.PLATINUM;
			case "black2_white2":
			case "black2":
			case "white2":
			case "gen5":
				return Skin.BLACK;
			default:
				return defaultSkin;
		}
	}
	
	private static PokemonGame parseGameArgument(ArgumentParser argp) {
		String gameArg = argp.getString("-game");
		
		if (gameArg == null) {
			Object obj = JOptionPane.showInputDialog(
					null, "Select a game:", "Select game", JOptionPane.INFORMATION_MESSAGE, null, PokemonGame.values(), null);
			
			if (obj == null) {
				System.out.println("Please specify a game using -game <name>");
				return null;
			}
			
			return (PokemonGame) obj;
		}
		
		try {
			return Stream.of(PokemonGame.values())
					.filter(game -> game.toString().toLowerCase().equals(gameArg.toLowerCase()))
					.findFirst()
					.get();
		}
		catch (NoSuchElementException e) {
			System.out.println("Invalid game. Legal values are: " + Arrays.toString(PokemonGame.values()));
			return null;
		}
	}
}
