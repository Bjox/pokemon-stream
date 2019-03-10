package pokemoninfodisplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import pokemoninfodisplayer.DisplayerOptions.Skin;
import pokemoninfodisplayer.graphics.InfoFrame;
import pokemoninfodisplayer.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.process.exceptions.UnsupportedPlatformException;
import pokemoninfodisplayer.models.PartyModel;
import pokemoninfodisplayer.models.PokemonGame;
import pokemoninfodisplayer.util.ArgumentParser;

/**
 *
 * @author Endre
 */
public class PokemonInfoDisplayer {

	public static boolean DEBUG = false;
	private static final String KILL_COUNT_STORAGE_KEY = "super secret key";
	
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
			try (PokemonInterface pokemonInterface = PokemonExtractor.createPokemonExtractor(game)) {
				
				pokemonInterface.addPokemonKillHandler(pokemon -> {
					var kcPropFile = new File("kc");
					var prop = new Properties();
					var key = String.valueOf(pokemon.getPersonalityValue());
					
					try {
						int newKillCount = 1;
						if (kcPropFile.exists()) {
							try (var in = new CipherInputStream(new FileInputStream(kcPropFile), createCipher(Cipher.DECRYPT_MODE, KILL_COUNT_STORAGE_KEY))) {	
								prop.load(in);
								var existingValue = prop.getProperty(key);
								if (existingValue != null) {
									newKillCount = Integer.parseInt(existingValue) + 1;
								}
							}
						}
						
						prop.setProperty(key, String.valueOf(newKillCount));
						try (var out = new CipherOutputStream(new FileOutputStream(kcPropFile), createCipher(Cipher.ENCRYPT_MODE, KILL_COUNT_STORAGE_KEY))) {
							prop.store(out, "Pokemon kill count storage. Do not modify");
						}
						
						System.out.println("!!! Detected kill for pokemon " + pokemon.toShortString() + ", killCount=" + newKillCount);
					}
					catch (IOException e) {
						System.err.println("Error updating kill count storage for pokemon " + pokemon.toShortString());
						if (DEBUG) {
							throw new RuntimeException(e);
						} else {
							JOptionPane.showMessageDialog(null, "Error updating kill count storage: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				
				PartyModel party = new PartyModel();
				
				while (true) {
					pokemonInterface.update();
					pokemonInterface.updateParty(party);
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

			Thread.sleep(1000);
		}

		frame.dispose();
	}
	
	private static Skin parseSkinArgument(ArgumentParser argp, Skin defaultSkin) {
		String skinArg = argp.getString("-skin");
		
		if (!argp.isPresent("-skin")) {
			Object obj = JOptionPane.showInputDialog(
					null, "Select a skin:", "Select skin", JOptionPane.INFORMATION_MESSAGE, null, Skin.values(), null);
			
			if (obj == null) {
				System.out.println("Please specify a skin using -skin <name>");
				return null;
			}
			
			return (Skin) obj;
		}
		
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
				System.out.println("Invalid skin. Legal values are: " + Arrays.toString(Skin.values()));
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
	
	private static Skin parseSkinArgument(ArgumentParser argp) {
		String skinArg = argp.getString("-skin");
		
		if (skinArg == null) {
			Object obj = JOptionPane.showInputDialog(
					null, "Select a game:", "Select game", JOptionPane.INFORMATION_MESSAGE, null, Skin.values(), null);
			
			if (obj == null) {
				System.out.println("Please specify a skin using -skin <name>");
				return null;
			}
			
			return (Skin) obj;
		}
		
		try {
			return Stream.of(Skin.values())
					.filter(skin -> skin.toString().toLowerCase().equals(skinArg.toLowerCase()))
					.findFirst()
					.get();
		}
		catch (NoSuchElementException e) {
			System.out.println("Invalid skin. Legal values are: " + Arrays.toString(Skin.values()));
			return null;
		}
	}
	
	private static Cipher createCipher(int mode, String key) {
		try {
			var secretKey = new SecretKeySpec(key.getBytes(), "AES");
			var cipher = Cipher.getInstance("AES");
			cipher.init(mode, secretKey);
			return cipher;
		}
		catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}
}
