/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pokemoninfodisplayer.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

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
	
	private BufferedImage img;
	
	
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
	}
	
	public BufferedImage getImage() {
		if (img == null) {
			try {
				File input = new File("./res/dex_imgs/" + dex_entry + ".png");
				img = ImageIO.read(input);
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
		return img;
	}
	
	public void setDexEntry(int dex_entry) {
		if (dex_entry == -1) {
			return;
		}
		this.dex_entry = dex_entry;
		this.img = null;
	}
	
	public void setStatusCondition(byte[] dataBytes) {
		if (dataBytes.length != 4) {
			System.err.printf("Wrong number of bytes passed to setStatusCondition. Expected 4, got %d\n", dataBytes.length);
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
