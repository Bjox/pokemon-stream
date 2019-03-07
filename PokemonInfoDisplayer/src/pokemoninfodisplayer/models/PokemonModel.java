package pokemoninfodisplayer.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import pokemoninfodisplayer.DisplayerOptions;

/**
 *
 * @author Endre
 */
public class PokemonModel {
	
	public static class Builder {
		
		private int personalityValue;
		private int dexEntry;
		private int maxHp;
		private int currentHp;
		private int level;
		private StatusCondition statusCondition = null;
		private String nickname;
		private boolean shiny = false;
		private boolean egg = false;
		private int eggSteps = 0;
		private Gender gender = Gender.GENDERLESS;

		public Builder() {
		}

		public Builder setPersonalityValue(int personalityValue) {
			this.personalityValue = personalityValue;
			return this;
		}
		
		public Builder setDexEntry(int dexEntry) {
			this.dexEntry = dexEntry;
			return this;
		}

		public Builder setMaxHp(int maxHp) {
			this.maxHp = maxHp;
			return this;
		}

		public Builder setCurrentHp(int currentHp) {
			this.currentHp = currentHp;
			return this;
		}

		public Builder setLevel(int level) {
			this.level = level;
			return this;
		}

		public Builder setStatusCondition(StatusCondition statusCondition) {
			this.statusCondition = statusCondition;
			return this;
		}

		public Builder setNickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public Builder setShiny(boolean shiny) {
			this.shiny = shiny;
			return this;
		}

		public Builder setEgg(boolean egg) {
			this.egg = egg;
			return this;
		}

		public Builder setEggSteps(int eggSteps) {
			this.eggSteps = eggSteps;
			return this;
		}
		
		public Builder setGender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public PokemonModel build() {
			return new PokemonModel(personalityValue, dexEntry, maxHp, currentHp, level, statusCondition, nickname, shiny, egg, eggSteps, gender);
		}
	}

	private final int personalityValue;
	private final int dexEntry;
	private final int maxHp;
	private final int currentHp;
	private final int level;
	private final StatusCondition statusCondition;
	private final String nickname;
	private final boolean shiny;
	private final boolean egg;
	private final int eggSteps;
	private final BufferedImage img;
	private final Gender gender;
	
	//private final BufferedImage imgGray;
	//private static final BufferedImage imgEgg; // This can be static because egg is egg no matter what pok it is.

	private PokemonModel(
			int personalityValue,
			int dexEntry,
			int maxHp,
			int currentHp,
			int level,
			StatusCondition statusCondition,
			String nickname,
			boolean shiny,
			boolean egg,
			int eggSteps,
			Gender gender
	) {
		this.personalityValue = personalityValue;
		this.dexEntry = dexEntry;
		this.maxHp = maxHp;
		this.currentHp = currentHp;
		this.level = level;
		this.statusCondition = statusCondition;
		this.nickname = nickname;
		this.shiny = shiny;
		this.egg = egg;
		this.eggSteps = eggSteps;
		this.gender = gender;
		
		BufferedImage imgBuff = null;
		try {
			imgBuff = createImage(dexEntry, egg, currentHp == 0, shiny);
		} catch (IOException e) {
			System.err.println("Error while reading image: " + e.toString());
			System.err.println("dex: " + dexEntry);
			System.err.println("totalhp: "+ maxHp);
			System.err.println("currenthp: " + currentHp);
			System.err.println("nickname: " + nickname);
		}
		this.img = imgBuff;
	}

	public boolean validate() {
		if (level < 0 || level > 100) {
			return false;
		}
		if (currentHp < 0 || currentHp > maxHp) {
			return false;
		}
		if (dexEntry < 1) {
			return false;
		}
		return true;
	}
	
	private static BufferedImage createImage(int dexEntry, boolean egg, boolean fainted, boolean shiny) throws IOException {
		File input;

		if (egg) {
			input = new File("./res/dex_collections/" + DisplayerOptions.PATH_PREFIX() + "/regular/egg.png");
		}
		else {
			input = new File("./res/dex_collections/" + DisplayerOptions.PATH_PREFIX() + (shiny ? "/shiny/" : "/regular/") + dexEntry + ".png");
		}

		BufferedImage img = ImageIO.read(input);
		
		if (egg || !fainted) {
			return img;
		}
		
		BufferedImage imgGray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int p = img.getRGB(x, y);
				int a = (p >>> 24) & 0xff;
				int r = (p >>> 16) & 0xff;
				int g = (p >>> 8) & 0xff;
				int b = p & 0xff;
				int avg = (r + g + b) / 3;

				p = (a << 24) | (avg << 16) | (avg << 8) | avg;
				imgGray.setRGB(x, y, p);
			}
		}
		
		return imgGray;
	}

	// Pokemon model getters
	
	public int getPersonalityValue() {
		return personalityValue;
	}
	
	public int getDexEntry() {
		return this.dexEntry;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public int getLevel() {
		return level;
	}

	public String getNickname() {
		return nickname;
	}

	public boolean isShiny() {
		return shiny;
	}

	public boolean isEgg() {
		return egg;
	}

	public BufferedImage getImg() {
		return img;//IsEgg() ? imgEgg : isFainted() ? imgGray : img;
	}

	public boolean isFainted() {
		return this.currentHp <= 0;
	}

	public int getEggSteps() {
		return this.eggSteps;
	}

	public StatusCondition getStatusCondition() {
		return this.statusCondition;
	}
	
	public Gender getGender() {
		return gender;
	}

}
