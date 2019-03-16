/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemoninfodisplayer.service;

/**
 *
 * @author Endre
 */
public class PokemonStorageEntry {

	private String pid;
	private int quantity;

	public PokemonStorageEntry(String pid, int quantity) {
		this.pid = pid;
		this.quantity = quantity;
	}
	
	public String getPid() {
		return this.pid;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
}
