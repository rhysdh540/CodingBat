import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * I'm not making one-liners for these, if you want to see an example see {@link #mapAB}
 */
@SuppressWarnings("unused")
public class Map1 {
	/**
	 * Modify and return the given map as follows: if the key "a" has a value, set the key "b" to have that value,
	 * and set the key "a" to have the value "". Basically "b" is a bully,
	 * taking the value and replacing it with the empty string.
	 */
	public Map<String, String> mapBully(Map<String, String> map) {
		if(map.containsKey("a"))
			map.put("b", map.put("a", ""));
		return map;
	}

	/**
	 * Modify and return the given map as follows: if the key "a" has a value,
	 * set the key "b" to have that same value. In all cases remove the key "c",
	 * leaving the rest of the map unchanged.
	 */
	public Map<String, String> mapShare(Map<String, String> map) {
		if(map.containsKey("a"))
			map.put("b", map.get("a"));
		map.remove("c");
		return map;
	}

	/**
	 * Modify and return the given map as follows: for this problem the map may or may not contain the "a" and "b" keys.
	 * If both keys are present, append their 2 string values together and store the result under the key "ab".
	 */
	public Map<String, String> mapAB(Map<String, String> map) {
		// monstrous one-liner: (only for this one)
		return !map.containsKey("a") || !map.containsKey("b") ? map :
				java.util.stream.Stream
						.concat(map.entrySet()
										.stream()
										.filter(e -> !"ab".equals(e.getKey())),
								Collections.singletonMap("ab", map.get("a") + map.get("b"))
										.entrySet()
										.stream())
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		/* or just:
		if(map.containsKey("a") && map.containsKey("b"))
			map.put("ab", map.get("a") + map.get("b"));
		return map;
		 */
	}

	/**
	 * Given a map of food keys and topping values, modify and return the map as follows:
	 * if the key "ice cream" is present, set its value to "cherry".
	 * In all cases, set the key "bread" to have the value "butter".
	 */
	public Map<String, String> topping1(Map<String, String> map) {
		if(map.containsKey("ice cream"))
			map.put("ice cream", "cherry");
		map.put("bread", "butter");
		return map;
	}

	/**
	 * Given a map of food keys and their topping values, modify and return the map as follows:
	 * if the key "ice cream" has a value, set that as the value for the key "yogurt" also.
	 * If the key "spinach" has a value, change that value to "nuts".
	 */
	public Map<String, String> topping2(Map<String, String> map) {
		if(map.containsKey("ice cream"))
			map.put("yogurt", map.get("ice cream"));
		if(map.containsKey("spinach"))
			map.put("spinach", "nuts");
		return map;
	}

	/**
	 * Given a map of food keys and topping values, modify and return the map as follows:
	 * if the key "potato" has a value, set that as the value for the key "fries".
	 * If the key "salad" has a value, set that as the value for the key "spinach".
	 */
	public Map<String, String> topping3(Map<String, String> map) {
		if(map.containsKey("potato"))
			map.put("fries", map.get("potato"));
		if(map.containsKey("salad"))
			map.put("spinach", map.get("salad"));
		return map;
	}

	/**
	 * Modify and return the given map as follows: if the keys "a" and "b" are both in the map and have equal values,
	 * remove them both.
	 */
	public Map<String, String> mapAB2(Map<String, String> map) {
		if(map.get("a") != null && map.get("a").equals(map.get("b"))) {
			map.remove("a");
			map.remove("b");
		}
		return map;
	}

	/**
	 * Modify and return the given map as follows: if exactly one of the keys "a" or "b" has a value in the map
	 * (but not both), set the other to have that same value in the map.
	 */
	public Map<String, String> mapAB3(Map<String, String> map) {
		if(map.containsKey("a") ^ map.containsKey("b")) {
			map.put(map.containsKey("a") ? "b" : "a", map.get(map.containsKey("a") ? "a" : "b"));
		}
		return map;
	}

	/**
	 * Modify and return the given map as follows: if the keys "a" and "b" have values that have different lengths,
	 * then set "c" to have the longer value. If the values exist and have the same length,
	 * change them both to the empty string in the map.
	 */
	public Map<String, String> mapAB4(Map<String, String> map) {
		if(map.containsKey("a") && map.containsKey("b")) {
			String a = map.get("a"), b = map.get("b");
			if(a.length() != b.length())
				map.put("c", a.length() > b.length() ? a : b);
			else {
				map.put("a", "");
				map.put("b", "");
			}
		}
		return map;
	}
}

