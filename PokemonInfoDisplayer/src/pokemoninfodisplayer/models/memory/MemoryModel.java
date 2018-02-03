package pokemoninfodisplayer.models.memory;

import java.lang.reflect.Field;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class MemoryModel {
	
	public static final int BYTE = 1;
	public static final int WORD = 2;
	public static final int DWORD = 4;
	
	private static final Class<MField> MFIELD_ANNOTATION = MField.class;
	
	protected byte[] memoryBytes;

	public MemoryModel() {
		this(new byte[0]);
	}
	
	public MemoryModel(byte[] data) {
		this.memoryBytes = data;
	}
	
	public final void update(byte[] memoryBytes) throws IllegalAccessException, InstantiationException {
		if (memoryBytes.length != this.memoryBytes.length) {
			this.memoryBytes = new byte[memoryBytes.length];
			System.arraycopy(memoryBytes, 0, this.memoryBytes, 0, memoryBytes.length);
		}
		
		Field[] fields = getClass().getDeclaredFields();
		
		for (Field field : fields) {
			if (!field.isAnnotationPresent(MFIELD_ANNOTATION)) {
				continue;
			}
			
			MField ann = field.getAnnotation(MFIELD_ANNOTATION);
			Class datatype = field.getType();
			
			int offset = ann.offset();
			int size = ann.size();
			
			if (field.get(this) == null) {
				if (datatype == Bytes.class) {
					if (size == 0) {
						throw new RuntimeException(String.format(
								"Field \"%s\" of class \"%s\" has no specified size.\n", field.getName(), field.getDeclaringClass().getSimpleName()));
					}
					field.set(this, new Bytes(size));
				} else {
					field.set(this, datatype.newInstance());
				}
			}

			MemoryField fieldObj = (MemoryField) field.get(this);
			fieldObj.set(memoryBytes, offset);
		}
	}
	
	
}
