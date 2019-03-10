package pokemoninfodisplayer.graphics;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import pokemoninfodisplayer.PokemonInfoDisplayer;
import pokemoninfodisplayer.models.PokemonKillHandler;
import pokemoninfodisplayer.models.PokemonModel;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class PokemonStorageService implements Closeable, PokemonKillHandler {
	
	private static final String STORAGE_FILE = "./pokemon_storage";
	private static final boolean ENCRYPT_STORAGE = true;
	private static final String ENCRYPTION_KEY = "fyfaenendruscode"; // This is secure
	private static final String STORAGE_COMMENTS = "PokemonInfoDisplayer storage file. DO NOT MODIFY";
	
	static {
		var storageFile = new File(STORAGE_FILE);
		instance = new PokemonStorageService(storageFile);
	}
	
	private static final PokemonStorageService instance;
	
	public static PokemonStorageService getInstance() {
		return instance;
	}
	
	
	private final File storageFile;
	private final Properties properties;
	
	private PokemonStorageService(File storageFile) {
		this.storageFile = storageFile;
		this.properties = new Properties();
		
		try {
			loadStorage();
		}
		catch (IOException e) {
			System.err.println("Error when loading pokemon storage: " + e.toString());
			if (PokemonInfoDisplayer.DEBUG) {
				throw new RuntimeException(e);
			} else {
				CompletableFuture.runAsync(() -> JOptionPane.showMessageDialog(null, "Error when loading pokemon storage: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
			}
		}
	}

	private void loadStorage() throws IOException {
		if (!storageFile.exists()) {
			return;
		}
		
		InputStream in = new FileInputStream(storageFile);
		
		if (ENCRYPT_STORAGE) {
			var cipher = createCipher(Cipher.DECRYPT_MODE);
			in = new CipherInputStream(in, cipher);
		}
		
		in = new BufferedInputStream(in);
		properties.load(in);
		
		in.close();
	}
	
	private Cipher createCipher(int mode) {
		try {
			var secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
			var cipher = Cipher.getInstance("AES");
			cipher.init(mode, secretKey);
			return cipher;
		}
		catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Writes the current storage to disk.
	 * @throws java.io.IOException
	 */
	public void persistStorage() throws IOException {
		OutputStream out = new FileOutputStream(storageFile);
		
		if (ENCRYPT_STORAGE) {
			var cipher = createCipher(Cipher.ENCRYPT_MODE);
			out = new CipherOutputStream(out, cipher);
		}
		
		out = new BufferedOutputStream(out);
		properties.store(out, STORAGE_COMMENTS);
	}
	
	/**
	 * Returns a value from the storage, or null if the key is not found.
	 * @param key
	 * @return 
	 */
	public String get(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * Returns a value from the storage, or the provided default value if the key is not found.
	 * @param key
	 * @param defaultValue
	 * @return 
	 */
	public String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	
	public void set(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public int getInt(String key, int defaultValue) {
		if (!properties.containsKey(key)) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(properties.getProperty(key));
		}
		catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public void setInt(String key, int value) {
		set(key, String.valueOf(value));
	}
	
	@Override
	public void close() throws IOException {
		persistStorage();
	}
	
	@Override
	public void handleKill(PokemonModel pokemon) {
		String propKey = getKillCountKey(pokemon);
		int killCount = getInt(propKey, 0) + 1;
		setInt(propKey, killCount);
		System.out.println(pokemon.getNickname() + " kill count=" + killCount);
	}

	private String getKillCountKey(PokemonModel pokemon) {
		return String.format("kc_%X", pokemon.getPersonalityValue());
	}

	
	
}
