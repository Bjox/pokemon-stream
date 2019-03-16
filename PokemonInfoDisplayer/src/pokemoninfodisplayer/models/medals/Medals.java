/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokemoninfodisplayer.models.medals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pokemoninfodisplayer.models.event.StorageUpdatedEvent;
import pokemoninfodisplayer.models.event.StorageUpdatedHandler;
import pokemoninfodisplayer.service.PokemonStorageEntry;
import pokemoninfodisplayer.service.PokemonStorageService;

/**
 *
 * @author Endre
 */
public class Medals implements StorageUpdatedHandler {
	
	private static final Medals instance;
	
	static {
		instance = new Medals();
		PokemonStorageService.getInstance().addListener(instance);
	}
	
	public static Medals getInstance() {
		return instance;
	}
	
	private Medals() {
		
	}
	
	private Medal[] medals = new Medal[] { new Medal(MedalType.MVP, 10, true),
										   new Medal(MedalType.TANK, 10, true),
										   new Medal(MedalType.SURVIVOR, 1, false)};

	@Override
	public void handle(StorageUpdatedEvent event) {
		var newList = new ArrayList<PokemonMedal>();
		
		for (Medal medal : this.medals) {
			PokemonStorageEntry[] relevantStoredData = medal.getStoredData();
			if (relevantStoredData == null) {
				continue;
			}
			if (medal.UNIQUE){
				var oldMedalWinner = this.PokemonMedals.stream().filter(pm -> pm.medal == medal.type).findFirst().orElse(null);
				var medalWinner = Stream.of(relevantStoredData)
										.filter(pse -> pse.getQuantity() >= medal.MIN_VALUE)
										.max(Comparator.comparing(PokemonStorageEntry::getQuantity))
										.orElse(null);
				if (oldMedalWinner != null && (medalWinner == null || oldMedalWinner.value == medalWinner.getQuantity())) {
					newList.add(oldMedalWinner);
				}
				else if (medalWinner != null) {
					newList.add(new PokemonMedal(medalWinner.getPid(), medal.type, medalWinner.getQuantity()));
				}
			}
			else {
				var medalWinners = Stream.of(relevantStoredData)
										 .filter(pse -> pse.getQuantity() >= medal.MIN_VALUE)
										 .map(pse -> new PokemonMedal(pse.getPid(), medal.type, pse.getQuantity()))
										 .collect(Collectors.toList());
				
				newList.addAll(medalWinners);
			}
		}
		this.PokemonMedals = newList;
		
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
		public int value;
		
		public PokemonMedal(String pid, MedalType medal, int value) {
			this.pid = pid;
			this.medal = medal;
			this.value = value;
		}
	}
	
}


