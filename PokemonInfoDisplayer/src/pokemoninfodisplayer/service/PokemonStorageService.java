package pokemoninfodisplayer.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import pokemoninfodisplayer.PokemonInfoDisplayer;
import pokemoninfodisplayer.models.PokemonKillHandler;
import pokemoninfodisplayer.models.PokemonModel;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class PokemonStorageService extends Service implements PokemonKillHandler {
	
	private static final boolean ENCRYPT_STORAGE = false;
	private static final boolean USE_HMAC_VERIFICATION = true;
	private static final String STORAGE_FILE = "./pokemon_storage.txt";
	private static final String HMAC_DIGEST_FILE = "./pokemon_storage_hmac";
	private static final String ENCRYPTION_KEY = "fyfaenendruscode"; // This is secure
	
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
	private boolean persistOnClose;
	
	private PokemonStorageService(File storageFile) {
		this.storageFile = storageFile;
		this.properties = new CleanProperties();
		this.persistOnClose = true;
		
		try {
			loadStorage();
		}
		catch (Exception e) {
			System.err.println("Error when loading pokemon storage: " + e.toString());
			
			this.persistOnClose = false;
			
			if (PokemonInfoDisplayer.DEBUG) {
				throw new RuntimeException(e);
			} else {
				JOptionPane.showMessageDialog(null, "Error when loading pokemon storage: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}

	private void loadStorage() throws Exception {
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
		
		if (USE_HMAC_VERIFICATION) {
			validateHmac();
		}
	}
	
	/**
	 * Writes the current storage to disk.
	 * @throws java.io.IOException
	 */
	public void persistStorage() throws Exception {
		OutputStream out = new FileOutputStream(storageFile);
		
		if (ENCRYPT_STORAGE) {
			var cipher = createCipher(Cipher.ENCRYPT_MODE);
			out = new CipherOutputStream(out, cipher);
		}
		
		out = new BufferedOutputStream(out);
		properties.store(out, null);
		
		out.flush();
		out.close();
		
		if (USE_HMAC_VERIFICATION) {
			var hmacBytes = computeHmac();
			try (var hmacOut = new BufferedOutputStream(new FileOutputStream(HMAC_DIGEST_FILE))) {
				hmacOut.write(hmacBytes);
				hmacOut.flush();
			}
		}
	}
	
	private Cipher createCipher(int mode) throws Exception {
		var secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
		var cipher = Cipher.getInstance("AES");
		cipher.init(mode, secretKey);
		return cipher;
	}
	
	private byte[] computeHmac() throws Exception {
		var propertiesByteStream = new ByteArrayOutputStream(4096);
		properties.store(propertiesByteStream, null);

		var key = ENCRYPTION_KEY.getBytes();
		var sha512Hmac = Mac.getInstance("HmacSHA512");
		var secretKey = new SecretKeySpec(key, "HmacSHA512");

		sha512Hmac.init(secretKey);
		return sha512Hmac.doFinal(propertiesByteStream.toByteArray());
	}
	
	private boolean validateHmac() throws Exception {
		File hmacFile = new File(HMAC_DIGEST_FILE);
		
		try (var in = new BufferedInputStream(new FileInputStream(hmacFile))) {
			var hmacBytesRead = in.readAllBytes();
			var computedHmac = computeHmac();
			
			if (Arrays.equals(hmacBytesRead, computedHmac)) {
				return true;
			}
		}
		
		throw new Exception("The HMAC digest validation failed");
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
		if (persistOnClose) {
			try {
				persistStorage();
			}
			catch (IOException e) {
				throw e;
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
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
