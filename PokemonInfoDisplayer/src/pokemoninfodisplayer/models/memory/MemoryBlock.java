package pokemoninfodisplayer.models.memory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MemoryBlock {
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface BlockField {
		int offset();
		int length() default 1;
	}
	
}
