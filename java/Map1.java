import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unused",
		"ConditionalExpressionWithIdenticalBranches", // using for the side effect
		"StringEquality" // shorter than `== null`
})
public class Map1 {
	/**
	 * Modify and return the given map as follows: if the key "a" has a value, set the key "b" to have that value,
	 * and set the key "a" to have the value "". Basically "b" is a bully,
	 * taking the value and replacing it with the empty string.
	 */
	public Map<String, String> mapBully(Map<String, String> map) {
		map.computeIfPresent("a", (k, v) -> map.put("b", v) == v ? "" : "");
		return map;
	}

	/**
	 * Modify and return the given map as follows: if the key "a" has a value,
	 * set the key "b" to have that same value. In all cases remove the key "c",
	 * leaving the rest of the map unchanged.
	 */
	public Map<String, String> mapShare(Map<String, String> map) {
		map.computeIfPresent("a", (k, v) -> map.put("b", v) == v ? v : v);
		map.remove("c");
		return map;
	}

	/**
	 * Modify and return the given map as follows: for this problem the map may or may not contain the "a" and "b" keys.
	 * If both keys are present, append their 2 string values together and store the result under the key "ab".
	 */
	public Map<String, String> mapAB(Map<String, String> map) {
		// or just:
		// if(map.containsKey("a") && map.containsKey("b"))
		// 	map.put("ab", map.get("a") + map.get("b"));
		map.computeIfPresent("a", (k, v) ->
				map.computeIfPresent("b", (k2, v2) ->
						map.put("ab", v + v2) == null ? v2 : v2) == v ? v : v);
		return map;
	}

	/**
	 * Given a map of food keys and topping values, modify and return the map as follows:
	 * if the key "ice cream" is present, set its value to "cherry".
	 * In all cases, set the key "bread" to have the value "butter".
	 */
	public Map<String, String> topping1(Map<String, String> map) {
		map.computeIfPresent("ice cream", (k, v) -> "cherry");
		map.put("bread", "butter");
		return map;
	}

	/**
	 * Given a map of food keys and their topping values, modify and return the map as follows:
	 * if the key "ice cream" has a value, set that as the value for the key "yogurt" also.
	 * If the key "spinach" has a value, change that value to "nuts".
	 */
	public Map<String, String> topping2(Map<String, String> map) {
		map.computeIfPresent("ice cream", (k, v) -> map.put("yogurt", v) == v ? v : v);
		map.computeIfPresent("spinach", (k, v) -> "nuts");
		return map;
	}

	/**
	 * Given a map of food keys and topping values, modify and return the map as follows:
	 * if the key "potato" has a value, set that as the value for the key "fries".
	 * If the key "salad" has a value, set that as the value for the key "spinach".
	 */
	public Map<String, String> topping3(Map<String, String> map) {
		map.computeIfPresent("potato", (k, v) -> map.put("fries", v) == v ? v : v);
		map.computeIfPresent("salad", (k, v) -> map.put("spinach", v) == v ? v : v);
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
			if(a.length() != b.length()) {
				map.put("c", a.length() > b.length() ? a : b);
			} else {
				map.put("a", "");
				map.put("b", "");
			}
		}
		return map;
	}
}

