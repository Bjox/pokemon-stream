package pokemoninfodisplayer.models.gen4;

import pokemoninfodisplayer.models.PokemonMemoryModel;
import pokemoninfodisplayer.lowlevel.emulator.IEmulatorExtractor;
import pokemoninfodisplayer.lowlevel.emulator.desmume.DeSmuMeExtractor;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotFoundException;
import pokemoninfodisplayer.lowlevel.process.exceptions.ProcessNotOpenedException;
import pokemoninfodisplayer.lowlevel.process.exceptions.UnsupportedPlatformException;

/**
 * Pokemon generation 4 (Platinum)
 * 
 * @author Bjørnar W. Alvestad
 */
public class Gen4MemoryModel extends PokemonMemoryModel {
	
	
	private final byte[] buffer;

	public Gen4MemoryModel() throws ProcessNotFoundException, UnsupportedPlatformException {
		super(new DeSmuMeExtractor());
		this.buffer = new byte[0x400000];
	}
	
	@Override
	public void open() throws ProcessNotFoundException {
		emulatorExtractor.open();
	}

	@Override
	public void close() {
		emulatorExtractor.close();
	}
	
	@Override
	public void load() throws ProcessNotOpenedException {
		boolean res = emulatorExtractor.readWRAM(this.buffer);
		
		for (int i = 0; i < 100; i++) {
			System.out.printf("%02X ", this.buffer[i]);
		}
		System.out.println();
		
		decrypt();
	}
	
	private void decrypt() {
		
	}
	
	
	public static void main(String[] args) throws Exception {
		PokemonMemoryModel gen4 = new Gen4MemoryModel();
		gen4.open();
		gen4.load();
	}

}
