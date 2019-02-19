package helper;

import java.util.HashMap;

public class MapUtils {

	/**
	 * Essentially applies the transitive mapping of one to two. The first map's values are keys to the second map.
	 * The returned value has the original keys to the key->value->value mapping.
	 * @param one The first map (key -> intermediate)
	 * @param two The second map of (intermediate -> value)
	 * @param <A> The key type of one.
	 * @param <B> The type of intermediate.
	 * @param <C> The value's type.
	 * @return The new map, of A -> C
	 */
	public static <A, B, C> HashMap<A, C> map(HashMap<A, B> one, HashMap<B, C> two) {
		HashMap<A, C> result = new HashMap<>(one.size());
		for (A a : one.keySet()) {
			B intermediate = one.get(a);
			if (two.containsKey(intermediate)) {
				result.put(a, two.get(intermediate));
			}
		}
		return result;
	}
}
