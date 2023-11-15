import java.util.stream.Collectors;
import java.util.Arrays;

@SuppressWarnings({"unused"})
public class Warmup2 {
	/**
	 * Given a string and a non-negative int n, return a larger string that is n copies of the original string.
	 */
	public String stringTimes(String str, int n) {
		//'\0' is the null character
		return new String(new char[n]).replace("\0", str);
	}

	/**
	 * Given a string and a non-negative int n, we'll say that the front of the string is the first 3 chars,
	 * or whatever is there if the string is less than length 3. Return n copies of the front;
	 */
	public String frontTimes(String str, int n) {
		return new String(new char[n]).replace("\0", str.substring(0, Math.min(3, str.length())));
	}

	/**
	 * Count the number of "xx" in the given string. We'll say that overlapping is allowed, so "xxx" contains 2 "xx".
	 */
	int countXX(String str) { // ngl i have no clue why this and doubleX aren't public
		return str.length() < 2 ? 0 : (str.startsWith("xx") ? 1 : 0) + countXX(str.substring(1));
	}

	/**
	 * Given a string, return true if the first instance of "x" in the string is immediately followed by another "x".
	 */
	boolean doubleX(String str) {
		return str.indexOf('x') + 1 < str.length() && str.charAt(str.indexOf('x') + 1) == 'x';
	}

	/**
	 * Given a string, return a new string made of every other char starting with the first, so "Hello" yields "Hlo".
	 */
	public String stringBits(String str) {
		//regex: '(.)' matches any character in group 1, '.' matches any other character
		//'$1' matches the first group (being the '(.)' from earlier)
		return str.replaceAll("(.).", "$1");
	}

	/**
	 * Given a non-empty string like "Code" return a string like "CCoCodCode".
	 */
	public String stringSplosion(String str) {
		return java.util.stream.IntStream.rangeClosed(1, str.length()).mapToObj(i -> str.substring(0, i)).collect(Collectors.joining());
	}

	/**
	 * Given a string, return the count of the number of times that a substring length 2 appears in the string
	 * and also as the last 2 chars of the string, so "hixxxhi" yields 1 (we won't count the end substring).
	 */
	public int last2(String str) {
		//TODO
		throw new UnsupportedOperationException();
	}

	/**
	 * Given an array of ints, return the number of 9's in the array.
	 */
	public int arrayCount9(int[] nums) {
		return Arrays.stream(nums).filter(i -> i == 9).toArray().length;
	}

	/**
	 * Given an array of ints, return true if one of the first 4 elements in the array is a 9. The array length may be less than 4.
	 */
	public boolean arrayFront9(int[] nums) {
		return Arrays.stream(nums).limit(4).anyMatch(i -> i == 9);
	}

	/**
	 * Given an array of ints, return true if the sequence of numbers 1, 2, 3 appears in the array somewhere.
	 */
	public boolean array123(int[] nums) {
		return Arrays.toString(nums).contains("1, 2, 3");
	}

	/**
	 * Given 2 strings, a and b, return the number of the positions where they contain the same length 2 substring.
	 * So "xxcaazz" and "xxbaaz" yields 3, since the "xx", "aa", and "az" substrings appear in the same place in both strings.
	 */
	public int stringMatch(String a, String b) {
		return java.util.stream.IntStream.rangeClosed(0, Math.min(a.length(), b.length()) - 2)
										 .map(i -> a.substring(i, i + 2).equals(b.substring(i, i + 2)) ? 1 : 0).sum();
	}

	/**
	 * Given a string, return a version where all the "x" have been removed. Except an "x" at the very start or end should not be removed.
	 */
	public String stringX(String str) {
		//regex: '(?<!^)' matches not the start of the string, 'x' matches itself, and '(?!$)' matches not at the end of the string
		return str.replaceAll("(?<!^)x(?!$)", "");
	}

	/**
	 * Given a string, return a string made of the chars at indexes 0,1, 4,5, 8,9 ... so "kittens" yields "kien".
	 */
	public String altPairs(String str) {
		//regex: '(..)' matches and captures the first two characters in group 1, '.?.?' matches the third and fourth characters if they exist, so length 3 strings ("yak") work
		return str.replaceAll("(..).?.?", "$1");
	}

	/**
	 * Suppose the string "yak" is unlucky. Given a string, return a version where all the "yak" are removed,
	 * but the "a" can be any char. The "yak" strings will not overlap.
	 */
	public String stringYak(String str) {
		//regex: 'y.k' matches 'y' followed by any character followed by 'k'
		return str.replaceAll("y.k", "");
	}

	/**
	 * Given an array of ints, return the number of times that two 6's are next to each other in the array.
	 * Also count instances where the second "6" is actually a 7.
	 */
	public int array667(int[] nums) {
		return java.util.stream.IntStream.rangeClosed(0, nums.length - 2)
										 .map(i -> (nums[i] == 6 && (nums[i + 1] == 6 || nums[i + 1] == 7)) ? 1 : 0).sum();
	}

	/**
	 * Given an array of ints, we'll say that a triple is a value appearing 3 times in a row in the array.
	 * Return true if the array does not contain any triples.
	 */
	public boolean noTriples(int[] nums) {
		return java.util.stream.IntStream.rangeClosed(0, nums.length - 3)
										 .noneMatch(i -> nums[i] == nums[i + 1] && nums[i + 1] == nums[i + 2]);
	}

	/**
	 * Given an array of ints, return true if it contains a 2, 7, 1 pattern: a value, followed by the value plus 5, followed by the value minus 1.
	 * Additionally the 271 counts even if the "1" differs by 2 or less from the correct value.
	 */
	public boolean has271(int[] nums) {
		return java.util.stream.IntStream.rangeClosed(0, nums.length - 3)
										 .anyMatch(i -> nums[i + 1] == nums[i] + 5 && Math.abs(nums[i + 2] - (nums[i] - 1)) <= 2);
	}
}
