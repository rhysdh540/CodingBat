import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class String2 {
	/**
	 * Given a string, return a string where for every char in the original, there are two chars.
	 */
	public String doubleChar(String str) {
		return str.chars().mapToObj(c -> (char) c + "" + (char) c).collect(Collectors.joining());
	}

	/**
	 * Return the number of times that the string "hi" appears anywhere in the given string.
	 */
	public int countHi(String str) {
		return str.split("hi", -1).length - 1;
	}

	/**
	 * Return true if the string "cat" and "dog" appear the same number of times in the given string.
	 */
	public boolean catDog(String str) {
		return str.split("cat", -1).length == str.split("dog", -1).length;
	}

	/**
	 * Return the number of times that the string "code" appears anywhere in the given string,
	 * except we'll accept any letter for the 'd', so "cope" and "cooe" count.
	 */
	public int countCode(String str) {
		//regex: co.e matches 'co' + any char + 'e'
		// the -1 in split is to include trailing empty strings
		return str.split("co.e", -1).length - 1;
	}

	/**
	 * Given two strings, return true if either of the strings appears at the very end of the other string,
	 * ignoring upper/lower case differences (in other words, the computation should not be "case sensitive").
	 */
	public boolean endOther(String a, String b) {
		return a.toLowerCase().endsWith(b.toLowerCase()) || b.toLowerCase().endsWith(a.toLowerCase());
	}

	/**
	 * Return true if the given string contains an appearance of "xyz" where the xyz is not directly preceeded by a period (.).
	 * So "xxyz" counts but "x.xyz" does not.
	 */
	public boolean xyzThere(String str) {
		//regex: (optional: any chars + anything that isn't a period) + xyz + any chars
		// the first part is optional if the xyz is at the start of the string
		return str.matches("(.*[^.])?xyz.*");
	}

	/**
	 * Return true if the given string contains a "bob" string, but where the middle 'o' char can be any char.
	 */
	public boolean bobThere(String str) {
		//regex: any chars + b + any char + b + any chars
		return str.matches(".*b.b.*");
	}

	/**
	 * We'll say that a String is xy-balanced if for all the 'x' chars in the string,
	 * there exists a 'y' char somewhere later in the string. So "xxy" is balanced, but "xyx" is not.
	 * One 'y' can balance multiple 'x's. Return true if the given string is xy-balanced.
	 */
	public boolean xyBalance(String str) {
		return str.indexOf('x') == -1 || str.lastIndexOf('x') < str.lastIndexOf('y');
	}

	/**
	 * Given two strings, a and b, create a bigger string made of the first char of a, the first char of b,
	 * the second char of a, the second char of b, and so on. Any leftover chars go at the end of the result.
	 */
	public String mixString(String a, String b) {
		return a.isEmpty() ? b : b.isEmpty() ? a : a.charAt(0) + "" + b.charAt(0) + mixString(a.substring(1), b.substring(1));
	}

	/**
	 * Given a string and an int n, return a string made of n repetitions of the last n characters of the string.
	 * You may assume that n is between 0 and the length of the string, inclusive.
	 */
	public String repeatEnd(String str, int n) {
		return new String(new char[n]).replace("\0", str.substring(str.length() - n));
	}

	/**
	 * Given a string and an int n, return a string made of the first n characters of the string,
	 * followed by the first n-1 characters of the string, and so on.
	 * You may assume that n is between 0 and the length of the string, inclusive (i.e. n >= 0 and n <= str.length()).
	 */
	public String repeatFront(String str, int n) {
		return n == 0 ? "" : str.substring(0, n) + repeatFront(str, n - 1);
	}

	/**
	 * Given two strings, <b>word</b> and a separator <b>sep</b>, return a big string made of <b>count</b> occurrences of the word,
	 * separated by the separator string.
	 */
	public String repeatSeparator(String word, String sep, int count) {
		return count == 0 ? "" : count < 2 ? word : word + sep + repeatSeparator(word, sep, count - 1);
	}

	/**
	 * Given a string, consider the prefix string made of the first N chars of the string.
	 * Does that prefix string appear somewhere else in the string?
	 * Assume that the string is not empty and that N is in the range 1..str.length().
	 */
	public boolean prefixAgain(String str, int n) {
		return str.lastIndexOf(str.substring(0, n)) != 0;
	}

	/**
	 * Given a string, does "xyz" appear in the middle of the string? To define middle, we'll say that the number of
	 * chars to the left and right of the "xyz" must differ by at most one. This problem is harder than it looks.
	 */
	public boolean xyzMiddle(String str) { // completely illegible
		return str.length() >= 3 && (str.length() % 2 == 0 && ("xyz".equals(str.substring(str.length() / 2 - 2, str.length() / 2 + 1))) || "xyz".equals(str.substring(str.length() / 2 - 1, str.length() / 2 + 2)));
	}

	/**
	 * A sandwich is two pieces of bread with something in between. Return the string that is between the first and last
	 * appearance of "bread" in the given string, or return the empty string "" if there are not two pieces of bread.
	 */
	public String getSandwich(String str) { // definitely a way to use regex here but I don't know how
		return str.indexOf("bread") == str.lastIndexOf("bread") ? "" : str.substring(str.indexOf("bread") + 5, str.lastIndexOf("bread"));
	}

	/**
	 * Returns true if for every '*' (star) in the string, if there are chars both immediately before and after the star,
	 * they are the same.
	 */
	public boolean sameStarChar(String str) {
		return str.length() < 3
				|| (str.charAt(1) != '*' || str.charAt(0) == str.charAt(2))
				&& sameStarChar(str.substring(1));
	}

	/**
	 * Given a string, compute a new string by moving the first char to come after the next two chars, so "abc" yields "bca".
	 * Repeat this process for each subsequent group of 3 chars, so "abcdef" yields "bcaefd".
	 * Ignore any group of fewer than 3 chars at the end.
	 */
	public String oneTwo(String str) {
		return str.length() < 3 ? "" : str.substring(1, 3) + str.charAt(0) + oneTwo(str.substring(3));
	}

	/**
	 * Look for patterns like "zip" and "zap" in the string -- length-3, starting with 'z' and ending with 'p'.
	 * Return a string where for all such words, the middle letter is gone, so "zipXzap" yields "zpXzp".
	 */
	public String zipZap(String str) {
		return str.replaceAll("z.p", "zp");
	}

	/**
	 * Return a version of the given string, where for every star (*) in the string the star and the chars immediately
	 * to its left and right are gone. So "ab*cd" yields "ad" and "ab**cd" also yields "ad".
	 */
	public String starOut(String str) {
		//regex: .? optionally matches any character (in case the * is at the start of the string)
		// \\*+ matches a * one or more times
		// .? like the beginning of the expression, in case of string end
		return str.replaceAll(".?\\*+.?", "");
	}

	/**
	 * Given a string and a non-empty <b>word</b> string, return a version of the original String where all chars have been
	 * replaced by pluses ("+"), except for appearances of the word string which are preserved unchanged.
	 */
	public String plusOut(String str, String word) {
		//regex: (?!(%s)) matches anything but word
		// the whole stream thing makes it so it will match anywhere from the middle of the string too (so if word is "xy" then it will match "xy" and "y")
		// the . at the end is the thing we are replacing with the +
		return str.replaceAll(String.format("(?!(%s)).",
				java.util.stream.IntStream.range(0, word.length())
						.mapToObj(i -> word.substring(i)
								.replace("+", "\\+")) // escape +
						.collect(Collectors.joining("|"))), "+");
	}

	/**
	 * Given a string and a non-empty word string, return a string made of each char just before and just after every
	 * appearance of the word in the string. Ignore cases where there is no char before or after the word, and a char
	 * may be included twice if it is between two words.
	 */
	public String wordEnds(String str, String word) {
		return java.util.stream.IntStream.range(0, str.length() - word.length() + 1)
				.filter(i -> str.startsWith(word, i))
				.mapToObj(i -> new String(new char[]{i > 0 ? str.charAt(i - 1) : ' ', i + word.length() < str.length() ? str.charAt(i + word.length()) : ' '}))
				.collect(Collectors.joining()).replace(" ", "");
	}
}
