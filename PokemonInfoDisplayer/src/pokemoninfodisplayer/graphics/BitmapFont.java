package pokemoninfodisplayer.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import pokemoninfodisplayer.util.Pair;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class BitmapFont {
	
	private final Map<Character, BufferedImage> bitmaps;
	private final Map<String, Character> filenameExceptions;
	private final double averageBitmapWidth;

	public BitmapFont(File bitmapFontFolder, Pair<String, Character>... exceptions) {
		
		this.bitmaps = new HashMap<>();
		this.filenameExceptions = new HashMap<>();
		
		if (!bitmapFontFolder.isDirectory()) {
			throw new IllegalArgumentException("Cannot create new BitmapFont. Must provide a directory in constructor");
		}
		
		Stream.of(exceptions).forEach(e -> {
			var exceptionFilename = e.value1;
			var exceptionCharKey = e.value2;
			if (this.filenameExceptions.containsKey(exceptionFilename)) {
				throw new IllegalArgumentException("Duplicate bitmap exception entry: " + e.toString());
			}
			this.filenameExceptions.put(exceptionFilename, exceptionCharKey);
		});
		
		Stream.of(bitmapFontFolder.listFiles())
				.filter(File::isFile)
				.filter(file -> getFileType(file).equals("png"))
				.forEach(file -> {
					var filename = getSimpleFilename(file);
					char bitmapKey;
					
					if (filename.length() == 1) {
						bitmapKey = filename.charAt(0);
					} else if (filenameExceptions.containsKey(filename)) {
						bitmapKey = filenameExceptions.get(filename);
					} else {
						throw new RuntimeException("No font filename exception specified for bitmap font file \"" + file.getName() + "\"");
					}
					
					if (bitmaps.containsKey(bitmapKey)) {
						throw new RuntimeException("Duplicate bitmap font \"" + file.getName() + "\"");
					}
					
					try {
						BufferedImage img = ImageIO.read(file);
						bitmaps.put(bitmapKey, img);
						
					}
					catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
		
		this.averageBitmapWidth = bitmaps.values()
				.stream()
				.mapToDouble(BufferedImage::getWidth)
				.average()
				.orElse(0);
	}
	
	private static String getFileType(File file) {
		if (file.isDirectory()) {
			return "";
		}
		var parts = file.getName().split("\\.");
		if (parts.length < 2) {
			return "";
		}
		return parts[1];
	}
	
	private static String getSimpleFilename(File file) {
		var parts = file.getName().split("\\.");
		return parts[0];
	}
	
	public boolean canDraw(char ch) {
		return bitmaps.containsKey(ch);
	}
	
	public boolean canDraw(int ch) {
		return canDraw((char)ch);
	}
	
	public boolean canDraw(String str) {
		return str.chars().allMatch(this::canDraw);
	}
	
	public void draw(Graphics2D g2, String text, int x, int y) {
		if (text == null || "".equals(text)) {
			return;
		}
			
		for (int i = 0; i < text.length(); i++) {
			char charToRender = text.charAt(i);
			if (!canDraw(charToRender)) {
				x += averageBitmapWidth;
				continue;
			}
			var img = bitmaps.get(charToRender);
			g2.drawImage(img, x, y, null);
			x += img.getWidth();
		}
	}
	
}
