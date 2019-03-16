/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemoninfodisplayer.models.medals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pokemoninfodisplayer.models.event.StorageUpdatedEvent;
import pokemoninfodisplayer.models.event.StorageUpdatedHandler;
import pokemoninfodisplayer.service.PokemonStorageEntry;
import pokemoninfodisplayer.util.Pair;

/**
 *
 * @author Endre
 */
public class Medals implements StorageUpdatedHandler {
	
	static {
		instance = new Medals();
	}
	
	private static final Medals instance;
	
	public static Medals getInstance() {
		return instance;
	}
	
	private Medals() {
		
	}
	
	private Medal[] medals = new Medal[] { new Medal(MedalType.MVP, 1, true),
										   new Medal(MedalType.TANK, 1, true),
										   new Medal(MedalType.SURVIVOR, 1, false)};

	@Override
	public void handle(StorageUpdatedEvent event) {
		this.PokemonMedals = new ArrayList<>();
		
		for (Medal medal : this.medals) {
			PokemonStorageEntry[] relevantStoredData = medal.getStoredData();
			if (relevantStoredData == null) {
				continue;
			}
			if (medal.UNIQUE){
				var medalWinner = Stream.of(relevantStoredData)
										.filter(pse -> pse.getQuantity() >= medal.MIN_VALUE)
										.max(Comparator.comparing(PokemonStorageEntry::getQuantity))
										.orElse(null);
				if (medalWinner != null) {
					this.PokemonMedals.add(new PokemonMedal(medalWinner.getPid(), medal.type));
				}
			}
			else {
				var medalWinners = Stream.of(relevantStoredData)
										 .filter(pse -> pse.getQuantity() >= medal.MIN_VALUE)
										 .map(pse -> new PokemonMedal(pse.getPid(), medal.type))
										 .collect(Collectors.toList());
				
				this.PokemonMedals.addAll(medalWinners);
			}
		}
		
	}
	
	public enum MedalType {
		MVP, SURVIVOR, TANK
	}
	
	public ArrayList<PokemonMedal> PokemonMedals = new ArrayList<>();
	
	public MedalType[] getPokemonMedals(int pid) {
		String hexPid = Integer.toHexString(pid);
		return PokemonMedals
				.stream()
				.filter(pm -> pm.pid.equalsIgnoreCase(hexPid))
				.map(pm -> pm.medal)
				.toArray(MedalType[]::new);
	}
	
	public class PokemonMedal {
		
		public String pid;
		public MedalType medal;
		
		public PokemonMedal(String pid, MedalType medal) {
			this.pid = pid;
			this.medal = medal;
		}
	}
	
}


