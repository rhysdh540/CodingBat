@SuppressWarnings("unused")
public class Logic2 {
	/**
	 * We want to make a row of bricks that is goal inches long. We have a number of small bricks (1 inch each)
	 * and big bricks (5 inches each). Return true if it is possible to make the goal by choosing from the given bricks.
	 * This is a little harder than it looks and can be done without any loops.
	 *
	 * @see <a href="https://codingbat.com/doc/practice/makebricks-introduction.html">Introduction to MakeBricks</a>
	 */
	public boolean makeBricks(int small, int big, int goal) {
		// if we have "enough" big bricks:
		// goal / 5 is the number of big bricks we can use, so goal % 5 is the leftover

		// if we don't have enough, we'd use all of them (so big * 5 inches),
		// so the leftover is goal - big * 5
		// this could be <0 though (if we have too many big bricks), so we take the max of the two

		// and just compare with small
		return small >= Math.max(goal % 5, goal - big * 5);

		// another way of thinking about it:
		// the number of leftover will be (goal - <number of big we actually use> * 5)
		// so to find the number of big we actually use, it will either be all of them (if we don't have enough)
		// or the number of big we can use (goal / 5)
		// so take the minimum of those two, and plug into the formula
		// return goal - Math.min(big, goal / 5) * 5 <= small;
	}

	/**
	 * Given 3 int values, a b c, return their sum. However,
	 * if one of the values is the same as another of the values, it does not count towards the sum.
	 */
	public int loneSum(int a, int b, int c) {
		return a == b && b == c ? 0 : a == b ? c : a == c ? b : b == c ? a : a + b + c;
	}

	/**
	 * Given 3 int values, a b c, return their sum. However,
	 * if one of the values is 13 then it does not count towards the sum and values to its right do not count.
	 * So for example, if b is 13, then both b and c do not count.
	 */
	public int luckySum(int a, int b, int c) {
		return a == 13 ? 0
				: b == 13 ? a
				: (c == 13 ? (a + b) : (a + b + c));
	}

	/**
	 * Given 3 int values, a b c, return their sum. However,
	 * if any of the values is a teen -- in the range 13..19 inclusive -- then that value counts as 0,
	 * except 15 and 16 do not count as a teens. Write a separate helper "public int fixTeen(int n) {"
	 * that takes in an int value and returns that value fixed for the teen rule.
	 * In this way, you avoid repeating the teen code 3 times (i.e. "decomposition").
	 * Define the helper below and at the same indent level as the main noTeenSum().
	 */
	public int noTeenSum(int a, int b, int c) {
		return fixTeen(a) + fixTeen(b) + fixTeen(c);
	}

	// required helper method
	public int fixTeen(int n) {
		return n == 15 || n == 16 || n < 13 || n > 19 ? n : 0;
	}

	/**
	 * For this problem, we'll round an int value up to the next multiple of 10 if its rightmost digit is 5 or more,
	 * so 15 rounds up to 20. Alternately, round down to the previous multiple of 10 if its rightmost digit
	 * is less than 5, so 12 rounds down to 10. Given 3 ints, a b c, return the sum of their rounded values.
	 * To avoid code repetition, write a separate helper "public int round10(int num) {" and call it 3 times.
	 * Write the helper entirely below and at the same indent level as roundSum().
	 */
	public int roundSum(int a, int b, int c) {
		return round10(a) + round10(b) + round10(c);
	}

	// required helper method
	public int round10(int i) {
		return Math.round(i / 10.0f) * 10;
	}

	/**
	 * Given three ints, a b c, return true if one of b or c is "close" (differing from a by at most 1),
	 * while the other is "far", differing from both other values by 2 or more.
	 * Note: Math.abs(num) computes the absolute value of a number.
	 */
	public boolean closeFar(int a, int b, int c) {
		return Math.abs(a - b) < 2 && Math.abs(a - c) >= 2 && Math.abs(b - c) > 1
				|| Math.abs(a - c) < 2 && Math.abs(a - b) > 1 && Math.abs(b - c) > 1;
	}

	/**
	 * Given 2 int values greater than 0, return whichever value is nearest to 21 without going over.
	 * Return 0 if they both go over.
	 */
	public int blackjack(int a, int b) {
		return Math.max(a > 21 ? 0 : a, b > 21 ? 0 : b);
	}

	/**
	 * Given three ints, a b c, one of them is small, one is medium and one is large.
	 * Return true if the three values are evenly spaced, so the difference between small and medium
	 * is the same as the difference between medium and large.
	 */
	public boolean evenlySpaced(int a, int b, int c) {
		// smallest + largest == 2 * middle
		return Math.min(a, Math.min(b, c)) + Math.max(a, Math.max(b, c))
				== 2 * Math.max(Math.min(a, b), Math.min(Math.max(a, b), c));
	}

	/**
	 * We want make a package of goal kilos of chocolate. We have small bars (1 kilo each) and big bars (5 kilos each).
	 * Return the number of small bars to use, assuming we always use big bars before small bars.
	 * Return -1 if it can't be done.
	 */
	public int makeChocolate(int small, int big, int goal) {
		// slight hack to make this one line; we assign `big` to the number of small bars we need (see makeBricks)
		// and then just a ternary
		return small < (big = Math.max(goal % 5, goal - big * 5)) ? -1 : big;
	}
}
