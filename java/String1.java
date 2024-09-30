@SuppressWarnings("unused")
public class String1 {
	/**
	 * Given a string name, e.g. "Bob", return a greeting of the form "Hello Bob!".
	 */
	public String helloName(String name) {
		return "Hello " + name + "!";
	}

	/**
	 * Given two strings, a and b, return the result of putting them together in the order abba,
	 * e.g. "Hi" and "Bye" returns "HiByeByeHi".
	 */
	public String makeAbba(String a, String b) {
		return a + b + b + a;
	}

	/**
	 * The web is built with HTML strings like "<i>Yay</i>" which draws Yay as italic text.
	 * In this example, the "i" tag makes <i> and </i> which surround the word "Yay".
	 * Given tag and word strings, create the HTML string with tags around the word, e.g. "<i>Yay</i>".
	 */
	public String makeTags(String tag, String word) {
		return String.format("<%s>%s</%s>", tag, word, tag);
	}

	/**
	 * Given an "out" string length 4, such as "<<>>", and a word, return a new string where the word is in the middle
	 * of the out string, e.g. "<<word>>". Note: use str.substring(i, j) to extract the String starting at index i
	 * and going up to but not including index j.
	 */
	public String makeOutWord(String out, String word) {
		return out.substring(0, 2) + word + out.substring(2);
	}

	/**
	 * Given a string, return a new string made of 3 copies of the last 2 chars of the original string.
	 * The string length will be at least 2.
	 */
	public String extraEnd(String str) {
		return "___".replace("_", str.substring(str.length() - 2));
	}

	/**
	 * Given a string, return the string made of its first two chars, so the String "Hello" yields "He".
	 * If the string is shorter than length 2, return whatever there is, so "X" yields "X", and the empty string ""
	 * yields the empty string "". Note that str.length() returns the length of a string.
	 */
	public String firstTwo(String str) {
		return str.substring(0, Math.min(str.length(), 2));
	}

	/**
	 * Given a string of even length, return the first half. So the string "WooHoo" yields "Woo".
	 */
	public String firstHalf(String str) {
		return str.substring(0, str.length() / 2);
	}

	/**
	 * Given a string, return a version without the first and last char, so "Hello" yields "ell".
	 * The string length will be at least 2.
	 */
	public String withoutEnd(String str) {
		return str.substring(1, str.length() - 1);
	}

	/**
	 * Given 2 strings, a and b, return a string of the form short+long+short,
	 * with the shorter string on the outside and the longer string on the inside.
	 * The strings will not be the same length, but they may be empty (length 0).
	 */
	public String comboString(String a, String b) {
		return a.length() < b.length() ? a + b + a : b + a + b;
	}

	/**
	 * Given 2 strings, return their concatenation, except omit the first char of each.
	 * The strings will be at least length 1.
	 */
	public String nonStart(String a, String b) {
		return a.substring(1) + b.substring(1);
	}

	/**
	 * Given a string, return a "rotated left 2" version where the first 2 chars are moved to the end.
	 * The string length will be at least 2.
	 */
	public String left2(String str) {
		return str.substring(2) + str.substring(0, 2);
	}

	/**
	 * Given a string, return a "rotated right 2" version where the last 2 chars are moved to the start.
	 * The string length will be at least 2.
	 */
	public String right2(String str) {
		return str.substring(str.length() - 2) + str.substring(0, str.length() - 2);
	}

	/**
	 * Given a string, return a string length 1 from its front, unless front is false,
	 * in which case return a string length 1 from its back. The string will be non-empty.
	 */
	public String theEnd(String str, boolean front) {
		return front ? str.substring(0, 1) : str.substring(str.length() - 1);
	}

	/**
	 * Given a string, return a version without both the first and last char of the string.
	 * The string may be any length, including 0.
	 */
	@SuppressWarnings("SpellCheckingInspection") // the website misspells it too
	public String withouEnd2(String str) {
		return str.length() > 2 ? str.substring(1, str.length() - 1) : "";
	}

	/**
	 * Given a string of even length, return a string made of the middle two chars,
	 * so the string "string" yields "ri". The string length will be at least 2.
	 */
	public String middleTwo(String str) {
		return str.substring(str.length() / 2 - 1, str.length() / 2 + 1);
	}

	/**
	 * Given a string, return true if it ends in "ly".
	 */
	public boolean endsLy(String str) {
		return str.endsWith("ly");
	}

	/**
	 * Given a string and an int n, return a string made of the first and last n chars from the string.
	 * The string length will be at least n.
	 */
	public String nTwice(String str, int n) {
		return str.substring(0, n) + str.substring(str.length() - n);
	}

	/**
	 * Given a string and an index, return a string length 2 starting at the given index.
	 * If the index is too big or too small to define a string length 2, use the first 2 chars.
	 * The string length will be at least 2.
	 */
	public String twoChar(String str, int index) {
		return index + 2 > str.length() || index < 0 ? str.substring(0, 2) : str.substring(index, index + 2);
	}

	/**
	 * Given a string of odd length, return the string length 3 from its middle, so "Candy" yields "and".
	 * The string length will be at least 3.
	 */
	public String middleThree(String str) {
		return str.substring(str.length() / 2 - 1, str.length() / 2 + 2);
	}

	/**
	 * Given a string, return true if "bad" appears starting at index 0 or 1 in the string,
	 * such as with "badxxx" or "xbadxx" but not "xxbadxx". The string may be any length, including 0.
	 * Note: use .equals() to compare 2 strings.
	 */
	public boolean hasBad(String str) {
		// regex: .? is an optional character, so it matches either nothing or the one character before bad
		// bad is just bad
		// and .* is any number of any characters after bad
		return str.matches(".?bad.*");
	}

	/**
	 * Given a string, return a string length 2 made of its first 2 chars.
	 * If the string length is less than 2, use '@' for the missing chars.
	 */
	public String atFirst(String str) {
		return (str + "@@").substring(0, 2);
	}

	/**
	 * Given 2 strings, a and b, return a new string made of the first char of a and the last char of b,
	 * so "yo" and "java" yields "ya". If either string is length 0, use '@' for its missing char.
	 */
	public String lastChars(String a, String b) {
		return (a + "@").charAt(0) + ("@" + b).substring(b.length());
	}

	/**
	 * Given two strings, append them together (known as "concatenation") and return the result.
	 * However, if the concatenation creates a double-char, then omit one of the chars, so "abc" and "cat" yields "abcat".
	 */
	public String conCat(String a, String b) {
		return b.isEmpty() ? a : a.endsWith(b.substring(0, 1)) ? a + b.substring(1) : a + b;
	}

	/**
	 * Given a string of any length, return a new string where the last 2 chars, if present, are swapped,
	 * so "coding" yields "codign".
	 */
	public String lastTwo(String str) {
		return str.length() > 1 ? str.substring(0, str.length() - 2) + str.charAt(str.length() - 1) + str.charAt(str.length() - 2) : str;
	}

	/**
	 * Given a string, if the string begins with "red" or "blue" return that color string,
	 * otherwise return the empty string.
	 */
	public String seeColor(String str) {
		return str.startsWith("red") ? "red" : str.startsWith("blue") ? "blue" : "";
	}

	/**
	 * Given a string, return true if the first 2 chars in the string also appear at the end of the string,
	 * such as with "edited".
	 */
	public boolean frontAgain(String str) {
		return str.length() > 1 && str.endsWith(str.substring(0, 2));
	}

	/**
	 * Given two strings, append them together (known as "concatenation") and return the result.
	 * However, if the strings are different lengths, omit chars from the longer string
	 * so it is the same length as the shorter string. So "Hello" and "Hi" yield "loHi". The strings may be any length.
	 */
	public String minCat(String a, String b) {
		return a.substring(Math.max(0, a.length() - b.length())) + b.substring(Math.max(0, b.length() - a.length()));
	}

	/**
	 * Given a string, return a new string made of 3 copies of the first 2 chars of the original string.
	 * The string may be any length. If there are fewer than 2 chars, use whatever is there.
	 */
	public String extraFront(String str) {
		return "___".replace("_", str.substring(0, Math.min(str.length(), 2)));
	}

	/**
	 * Given a string, if a length 2 substring appears at both its beginning and end,
	 * return a string without the substring at the beginning, so "HelloHe" yields "lloHe".
	 * The substring may overlap with itself, so "Hi" yields "". Otherwise, return the original string unchanged.
	 */
	public String without2(String str) { // similar logic to frontAgain
		return str.length() > 1 && str.endsWith(str.substring(0, 2)) ? str.substring(2) : str;
	}

	/**
	 * Given a string, return a version without the first 2 chars. Except keep the first char if it is 'a'
	 * and keep the second char if it is 'b'. The string may be any length. Harder than it looks.
	 */
	public String deFront(String str) {
		return (str.startsWith("a") ? "a" : "") + (str.startsWith("b", 1) ? "b" : "") + str.substring(2);
	}

	/**
	 * Given a string and a second "word" string, we'll say that the word matches the string
	 * if it appears at the front of the string, except its first char does not need to match exactly.
	 * On a match, return the front of the string, or otherwise return the empty string.
	 * So, so with the string "hippo" the word "hi" returns "hi" and "xip" returns "hip".
	 * The word will be at least length 1.
	 */
	public String startWord(String str, String word) {
		return str.startsWith(word.substring(1), 1) ? str.substring(0, word.length()) : "";
	}

	/**
	 * Given a string, if the first or last chars are 'x', return the string without those 'x' chars,
	 * and otherwise return the string unchanged.
	 */
	public String withoutX(String str) {
		return "x".equals(str) ? "" :
				str.startsWith("x") ?
						(str.endsWith("x") ? str.substring(1, str.length() - 1) : str.substring(1)) :
						(str.endsWith("x") ? str.substring(0, str.length() - 1) : str);
	}

	/**
	 * Given a string, if one or both of the first 2 chars is 'x', return the string without those 'x' chars,
	 * and otherwise return the string unchanged. This is a little harder than it looks.
	 */
	public String withoutX2(String str) {
		//regex: ^(x(.)|(.)x) matches either x followed by any character or any character followed by x at the start of the string
		// the non-x character gets saved to capturing group 2 or 3 (the other will be empty, so we can use $2$3 to get the non-x character)
		// the ^x case is handled separately for the second x
		return str.replaceAll("^(x(.)|(.)x)", "$2$3").replaceAll("^x", "");
	}
}
