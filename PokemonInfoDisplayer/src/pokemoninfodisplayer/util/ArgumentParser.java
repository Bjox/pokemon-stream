package pokemoninfodisplayer.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Very simple argument parser. Options must start with dash: "-".
 * @author Bjørnar W. Alvestad
 */
public class ArgumentParser {
	
	private final Map<String, String> map;
	
	/**
	 * 
	 * @param args The String[] arguement passed to the main method.
	 */
	public ArgumentParser(String[] args) {
		this.map = new HashMap<>();
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("-")) {
				String value = null;
				if (i+1 < args.length && !args[i+1].startsWith("-")) value = args[i+1];
				map.put(arg, value);
			}
		}
	}
	
	
	public boolean isPresent(String option) {
		return map.containsKey(option);
	}
	
	
	public String getString(String option) {
		return map.get(option);
	}
	
	
	public String getString(String option, String def) {
		return isPresent(option) ? map.get(option) : def;
	}
	
	
	public int getInt(String option) {
		return Integer.parseInt(map.get(option));
	}
	
	
	public int getInt(String option, int def) {
		return isPresent(option) ? Integer.parseInt(map.get(option)) : def;
	}
	
	
	public double getDouble(String option) {
		return Double.parseDouble(map.get(option));
	}
	
	
	public double getDouble(String option, double def) {
		return isPresent(option) ? Double.parseDouble(map.get(option)) : def;
	}
	
	
	public void printAllOptions() {
		for (String key : map.keySet()) {
			String val = map.get(key);
			if (val == null) System.out.println(key);
			else System.out.println(key + "=" + val);
		}
	}
	
	
	public int numArguments() {
		return map.size();
	}
	
}
