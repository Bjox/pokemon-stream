package pokemoninfodisplayer.models;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum StatusCondition {
	SLEEP(0, 3, "SLP"),
	POISON(3, 1, "PSN"),
	BURN(4, 1, "BRN"),
	FREEZE(5, 1, "FRZ"),
	PARALYSIS(6, 1, "PAR"),
	BAD_POISON(7, 1, "PSN!");

	public final int bits;
	public final int offset;
	public final String shortName;
	public final int mask;

	private StatusCondition(int offset, int bits, String shortName) {
		this.offset = offset;
		this.bits = bits;
		this.shortName = shortName;
		this.mask = (1 << bits) - 1 << offset;
	}
	
	public static StatusCondition parse(byte statusConditionByte) {
		int statCondByte = Byte.toUnsignedInt(statusConditionByte);
		StatusCondition[] conditions = StatusCondition.values();

		for (int i = conditions.length - 1; i >= 0; i--) {
			if ((conditions[i].mask & statCondByte) != 0) {
				return conditions[i];
			}
		}

		return null;
	}
}
