import java.util.Arrays;

@SuppressWarnings("unused")
public class String3 {
	/**
	 * Given a string, count the number of words ending in 'y' or 'z' --
	 * so the 'y' in "heavy" and the 'z' in "fez" count, but not the 'y' in "yellow" (not case sensitive).
	 * We'll say that a y or z is at the end of a word if there is not an alphabetic letter immediately following it.
	 * (Note: Character.isLetter(char) tests if a char is an alphabetic letter.)
	 */
	public int countYZ(String str) {
		//regex: splits the string into words (only letters)
		// then maps to 1 the word ends with y, z, Y, or Z or 0
		return Arrays.stream(str.split("[^a-zA-Z]"))
				.mapToInt(s -> s.matches(".*[yzYZ]$") ? 1 : 0)
				.sum();
	}

	/**
	 * Given two strings, <b>base</b> and <b>remove</b>, return a version of the base string
	 * where all instances of the remove string have been removed (not case sensitive).
	 * You may assume that the remove string is length 1 or more. Remove only non-overlapping instances,
	 * so with "xxx" removing "xx" leaves "x".
	 */
	public String withoutString(String base, String remove) {
		//regex: (?i) makes the match case-insensitive
		return base.replaceAll("(?i)" + remove, "");
	}

	/**
	 * Given a string, return true if the number of appearances of "is" anywhere in the string
	 * is equal to the number of appearances of "not" anywhere in the string (case sensitive).
	 */
	public boolean equalIsNot(String str) { // same logic as catDog from String-2
		return str.split("is", -1).length == str.split("not", -1).length;
	}

	/**
	 * We'll say that a lowercase 'g' in a string is "happy" if there is another 'g' immediately to its left or right.
	 * Return true if all the g's in the given string are happy.
	 */
	public boolean gHappy(String str) {
		//regex: (?<!g) matches anything but a g before g
		// (?!g) does the same but after
		return !str.matches(".*(?<!g)g(?!g).*");
	}

	/**
	 * We'll say that a "triple" in a string is a char appearing three times in a row.
	 * Return the number of triples in the given string. The triples may overlap.
	 */
	public int countTriple(String str) {
		return java.util.stream.IntStream.range(0, str.length())
				.filter(i -> i + 2 < str.length() && str.charAt(i) == str.charAt(i + 1) && str.charAt(i) == str.charAt(i + 2))
				.toArray().length;
	}

	/**
	 * Given a string, return the sum of the digits 0-9 that appear in the string, ignoring all other characters.
	 * Return 0 if there are no digits in the string.
	 * (Note: Character.isDigit(char) tests if a char is one of the chars '0', '1', .. '9'.
	 * Integer.parseInt(string) converts a string to an int.)
	 */
	public int sumDigits(String str) {
		return str.chars()
				.filter(Character::isDigit)
				.map(Character::getNumericValue)
				.sum();
	}

	/**
	 * Given a string, return the longest substring that appears at both the beginning and end of the string without overlapping.
	 * For example, sameEnds("abXab") is "ab".
	 */
	public String sameEnds(String string) {
		return java.util.stream.IntStream.range(0, string.length() / 2 + 1)
				.filter(i -> string.substring(0, i).equals(string.substring(string.length() - i)))
				.mapToObj(i -> string.substring(0, i))
				.max(java.util.Comparator.comparingInt(String::length))
				.orElse("");
	}

	/**
	 * Given a string, look for a mirror image (backwards) string at both the beginning and end of the given string.
	 * In other words, zero or more characters at the very begining of the given string,
	 * and at the very end of the string in reverse order (possibly overlapping).
	 * For example, the string "abXYZba" has the mirror end "ab".
	 */
	public String mirrorEnds(String str) {
		return java.util.stream.IntStream.range(0, str.length())
				.mapToObj(i -> str.substring(0, i + 1))
				.filter(s -> str.endsWith(new StringBuilder(s).reverse().toString()))
				.max(java.util.Comparator.comparingInt(String::length))
				.orElse("");
	}

	/**
	 * Given a string, return the length of the largest "block" in the string.
	 * A block is a run of adjacent chars that are the same.
	 */
	@SuppressWarnings("RegExpSuspiciousBackref") //idk how but it works
	public int maxBlock(String str) {
		//regex: (?<=(.)) matches anything but a letter before the current letter and captures it into group 1
		// (?!\1) asserts that the next character is not the same as the one in group 1
		return Arrays.stream(str.split("(?<=(.))(?!\\1)"))
				.mapToInt(String::length)
				.max().orElse(0);
	}

	/**
	 * Given a string, return the sum of the numbers appearing in the string, ignoring all other characters.
	 * A number is a series of 1 or more digit chars in a row.
	 * (Note: Character.isDigit(char) tests if a char is one of the chars '0', '1', .. '9'.
	 * Integer.parseInt(string) converts a string to an int.)
	 */
	public int sumNumbers(String str) {
		//regex: splits the string into numbers
		return Arrays.stream(str.split("[^0-9]+"))
					 .filter(s -> !s.isEmpty())
					 .mapToInt(Integer::parseInt)
					 .sum();
	}

	/**
	 * Given a string, return a string where every appearance of the lowercase word "is" has been replaced with "is not".
	 * The word "is" should not be immediately preceeded or followed by a letter -- so for example the "is" in "this" does not count.
	 * (Note: Character.isLetter(char) tests if a char is a letter.)
	 */
	public String notReplace(String str) {
		//regex: (?<!\w) matches anything but a letter before "is"
		// (?!\w) does the same but after
		return str.replaceAll("(?<!\\w)is(?!\\w)", "is not");
	}
}
