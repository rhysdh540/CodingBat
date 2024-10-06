import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Map2 {
	/**
	 * Given an array of strings, return a Map<String, Integer> containing a key
	 * for every different string in the array, always with the value 0. For example the string "hello"
	 * makes the pair "hello":0. We'll do more complicated counting later, but for this problem the value is simply 0.
	 */
	public Map<String, Integer> word0(String[] strings) {
		return Arrays.stream(strings).distinct().collect(Collectors.toMap(s -> s, s -> 0));
	}

	/**
	 * Given an array of strings, return a Map<String, Integer> containing a key
	 * for every different string in the array, and the value is that string's length.
	 */
	public Map<String, Integer> wordLen(String[] strings) {
		return Arrays.stream(strings).distinct().collect(Collectors.toMap(s -> s, String::length));
	}

	/**
	 * Given an array of non-empty strings, create and return a Map<String, String> as follows:
	 * for each string add its first character as a key with its last character as the value.
	 */
	public Map<String, String> pairs(String[] strings) {
		return Arrays.stream(strings).collect(Collectors.toMap(
				s -> s.substring(0, 1),
				s -> s.substring(s.length() - 1),
				(a, b) -> b // make sure to use the latest string if there are duplicates
		));
	}

	/**
	 * The classic word-count algorithm: given an array of strings, return a Map<String, Integer>
	 * with a key for each different string, with the value the number of times that string appears in the array.
	 */
	public Map<String, Integer> wordCount(String[] strings) {
		return Arrays.stream(strings).collect(Collectors.toMap(s -> s, s -> 1, Integer::sum));
	}

	/**
	 * Given an array of non-empty strings, return a Map<String, String> with a key for every different first character seen,
	 * with the value of all the strings starting with that character appended together in the order they appear in the array.
	 */
	public Map<String, String> firstChar(String[] strings) {
		return Arrays.stream(strings).collect(Collectors.groupingBy(s -> s.substring(0, 1), Collectors.joining()));
	}

	/**
	 * Loop over the given array of strings to build a result string like this: when a string appears the 2nd, 4th, 6th, etc. time
	 * in the array, append the string to the result. Return the empty string if no string appears a 2nd time.
	 */
	public String wordAppend(String[] strings) {
		Map<String, Integer> map = new HashMap<>();
		return Arrays.stream(strings)
				.filter(s -> map.merge(s, 1, Integer::sum) % 2 == 0) // update map and filter if even
				.collect(Collectors.joining());
	}

	/**
	 * Given an array of strings, return a Map<String, Boolean> where each different string is a key and its value is true
	 * if that string appears 2 or more times in the array.
	 */
	public Map<String, Boolean> wordMultiple(String[] strings) {
		return Arrays.stream(strings).collect(Collectors.toMap(s -> s, s -> false, (a, b) -> true));
	}

	/**
	 * We'll say that 2 strings "match" if they are non-empty and their first chars are the same.
	 * Loop over and then return the given array of non-empty strings as follows: if a string matches an earlier string
	 * in the array, swap the 2 strings in the array. When a position in the array has been swapped, it no longer matches
	 * anything. Using a map, this can be solved making just one pass over the array. More difficult than it looks.
	 */
	public String[] allSwap(String[] strings) {
		Map<Character, Integer> map = new HashMap<>();
		java.util.stream.IntStream.range(0, strings.length).forEach(i ->
				map.compute(strings[i].charAt(0), (k, v) -> {
					if(v == null) return i;
					Collections.swap(Arrays.asList(strings), i, v);
					return null;
				}));
		return strings;
	}

	/**
	 * We'll say that 2 strings "match" if they are non-empty and their first chars are the same.
	 * Loop over and then return the given array of non-empty strings as follows: if a string
	 * matches an earlier string in the array, swap the 2 strings in the array. A particular first char
	 * can only cause 1 swap, so once a char has caused a swap, its later swaps are disabled. Using a map,
	 * this can be solved making just one pass over the array. More difficult than it looks.
	 */
	public String[] firstSwap(String[] strings) {
		Map<Character, Integer> map = new HashMap<>();
		java.util.stream.IntStream.range(0, strings.length).forEach(i ->
				map.compute(strings[i].charAt(0), (key, value) -> {
					if(value == null) return i;
					Collections.swap(Arrays.asList(strings), i, value == -1 ? i : value); // swap with itself if already swapped
					return -1;
				}));
		return strings;
	}
}
