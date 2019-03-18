package pokemoninfodisplayer.service;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class Service implements Closeable {
	
	private static final Collection<Service> services = new HashSet<>();
	
	protected Service() {
		register();
	}
	
	private void register() {
		registerService(this);
	}
	
	private static void registerService(Service service) {
		if (services.contains(service)) {
			throw new RuntimeException("Cannot register service " + service.getClass().getSimpleName() + ". The service is already registered.");
		}
		services.add(service);
	}
	
	public static void closeAllServices() {
		services.forEach(s -> {
			var serviceClass = s.getClass();
			System.out.println("Closing service " + serviceClass.getSimpleName());
			try {
				s.close();
			}
			catch (IOException e) {
				System.err.println("An error occurred while closing service " + serviceClass.getSimpleName() + ": " + e.toString());
				e.printStackTrace();
			}
		});
	}
	
}
