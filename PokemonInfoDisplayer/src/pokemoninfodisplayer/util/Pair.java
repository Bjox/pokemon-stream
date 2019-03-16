package pokemoninfodisplayer.util;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> {
	public final T1 value1;
	public final T2 value2;

	public Pair(T1 value1, T2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

	@Override
	public String toString() {
		return String.format("Pair {%s, %s}", value1, value2);
	}
}
