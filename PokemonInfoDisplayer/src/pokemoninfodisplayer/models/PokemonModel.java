package pokemoninfodisplayer.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import pokemoninfodisplayer.DisplayerOptions;

/**
 *
 * @author Endre
 */
public class PokemonModel {
	
	private int dex_entry;
	public int max_hp;
	public int current_hp;
	public int level;
	private StatusCondition statusCondition;
	public String nickname;
	public boolean shiny;
	
	private BufferedImage img;
	private BufferedImage gray_img;
	
	
	public enum StatusCondition {
		SLEEP     (0, 3, "SLP"),
		POISON    (3, 1, "PSN"),
		BURN      (4, 1, "BRN"),
		FREEZE    (5, 1, "FRZ"),
		PARALYSIS (6, 1, "PAR"),
		BAD_POISON(7, 1, "PSN!");
		
		public final int bits;
		public final int offset;
		public final String shortName;
		public final int mask;
		
		private StatusCondition(int offset, int bits, String shortName) {
			this.offset = offset;
			this.bits = bits;
			this.shortName = shortName;
			this.mask = (1 << bits) - 1 << offset;
		}
	}
	
	
	public PokemonModel() {
		nickname = "";
	}
	
	public boolean validate() {
		if (level < 0 || level > 100) return false;
		if (current_hp < 0 || current_hp > max_hp) return false;
		if (dex_entry < 1) return false;
		return true;
	}
	
	public BufferedImage getImage() {
		if (img == null) {
			try {
				File input = new File("./res/dex_collections/" + DisplayerOptions.PATH_PREFIX() + (shiny ? "/shiny/" : "/regular/") + dex_entry + ".png");
				img = ImageIO.read(input);
				gray_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
				for (int x = 0; x < img.getWidth(); x++){
					for (int y = 0; y < img.getHeight(); y++){
						int p = img.getRGB(x,y);
						int a = (p>>>24)&0xff;
						int r = (p>>>16)&0xff;
						int g = (p>>>8)&0xff;
						int b = p&0xff;
						int avg = (r+g+b)/3;
						
						p = (a<<24) | (avg<<16) | (avg<<8) | avg;
						gray_img.setRGB(x, y, p);
					}
				}
			} catch (FileNotFoundException e){
				System.out.println("File not found");
			} catch (IOException e){
				System.out.println("Wronk");
				System.out.println("Dex: "+dex_entry);
				System.out.println("totalhp: "+max_hp);
				System.out.println("currenthp: "+current_hp);
				System.out.println("nick: "+nickname);
			}
		}
		return isFainted() ? gray_img : img;
	}
	
	public void setDexEntry(int dex_entry) {
		if (dex_entry == -1) {
			return;
		}
		this.dex_entry = dex_entry;
		this.img = null;
	}
	
	public void setStatusCondition(byte[] dataBytes) {
		if (dataBytes.length < 1) {
			System.err.printf("Wrong number of bytes passed to setStatusCondition. Got %d\n", dataBytes.length);
			this.statusCondition = null;
			return;
		}
		
		int statCondByte = Byte.toUnsignedInt(dataBytes[0]);
		StatusCondition[] conditions = StatusCondition.values();
		
		for (int i = conditions.length-1; i >= 0; i--) {
			if ((conditions[i].mask & statCondByte) != 0) {
				this.statusCondition = conditions[i];
				return;
			}
		}
		
		this.statusCondition = null;
	}
	
	// Pokemon model getters
	
	public int getDexEntry(){
		return this.dex_entry;
	}
	
	public boolean isFainted() {
		return this.current_hp <= 0;
	}
	
	/**
	 * Get the status condition. Returns null for no status condition.
	 * @return 
	 */
	public StatusCondition getStatusCondition() {
		return this.statusCondition;
	}
}
