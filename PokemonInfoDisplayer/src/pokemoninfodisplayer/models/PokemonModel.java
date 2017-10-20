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
	
	public String nickname;
	
	private BufferedImage img;
	
	// TODO Fix ordering
	public enum StatusCondition {
		PAR, SLP, BRN, FRZ
	}
	
	public BufferedImage getImage(){
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
	
	public int getDexEntry(){
		return this.dex_entry;
	}
	
	public void setDexEntry(int dex_entry) {
		this.dex_entry = dex_entry;
		this.img = null;
	}
}
