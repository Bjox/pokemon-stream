package pokemoninfodisplayer.models;

import pokemoninfodisplayer.lowlevel.emulator.IEmulatorExtractor;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class PokemonMemoryModel {
	
	protected final IEmulatorExtractor emulatorExtractor;

	public PokemonMemoryModel(IEmulatorExtractor emulatorExtractor) {
		this.emulatorExtractor = emulatorExtractor;
	}

	public abstract void load() throws ProcessNotOpenedException;
	public abstract void open() throws ProcessNotFoundException;
	public abstract void close();
	
}
