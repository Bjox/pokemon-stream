/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemoninfodisplayer.models.event;

/**
 *
 * @author Endre
 */
@FunctionalInterface
public interface StorageUpdatedHandler {
	
	void handle(StorageUpdatedEvent event);
	
}
