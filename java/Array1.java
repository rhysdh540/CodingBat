import java.util.Arrays;

@SuppressWarnings("unused")
public class Array1 {
	/**
	 * Given an array of ints, return true if 6 appears as either the first or last element in the array.
	 * The array will be length 1 or more.
	 */
	public boolean firstLast6(int[] nums) {
		return nums[0] == 6 || nums[nums.length - 1] == 6;
	}

	/**
	 * Given an array of ints, return true if the array is length 1 or more,
	 * and the first element and the last element are equal.
	 */
	public boolean sameFirstLast(int[] nums) {
		return nums.length > 0 && nums[0] == nums[nums.length - 1];
	}

	/**
	 * Return an int array length 3 containing the first 3 digits of pi, {3, 1, 4}.
	 */
	public int[] makePi() {
		return new int[]{3, 1, 4};
	}

	/**
	 * Given 2 arrays of ints, a and b, return true if they have the same first element or they have the same last element.
	 * Both arrays will be length 1 or more.
	 */
	public boolean commonEnd(int[] a, int[] b) {
		return a[0] == b[0] || a[a.length - 1] == b[b.length - 1];
	}

	/**
	 * Given an array of ints length 3, return the sum of all the elements.
	 */
	public int sum3(int[] nums) {
		return Arrays.stream(nums).sum(); // technically shorter than just adding them
	}

	/**
	 * Given an array of ints length 3, return an array with the elements "rotated left" so {1, 2, 3} yields {2, 3, 1}.
	 */
	public int[] rotateLeft3(int[] nums) {
		return new int[]{nums[1], nums[2], nums[0]};
	}

	/**
	 * Given an array of ints length 3, return a new array with the elements in reverse order,
	 * so {1, 2, 3} becomes {3, 2, 1}.
	 */
	public int[] reverse3(int[] nums) {
		return new int[]{nums[2], nums[1], nums[0]};
	}

	/**
	 * Given an array of ints length 3, figure out which is larger, the first or last element in the array,
	 * and set all the other elements to be that value. Return the changed array.
	 */
	public int[] maxEnd3(int[] nums) {
		return Arrays.stream(nums).map(i -> Math.max(nums[0], nums[2])).toArray();
	}

	/**
	 * Given an array of ints, return the sum of the first 2 elements in the array.
	 * If the array length is less than 2, just sum up the elements that exist, returning 0 if the array is length 0.
	 */
	public int sum2(int[] nums) {
		return Arrays.stream(nums).limit(2).sum();
	}

	/**
	 * Given 2 int arrays, a and b, each length 3, return a new array length 2 containing their middle elements.
	 */
	public int[] middleWay(int[] a, int[] b) {
		return new int[]{a[1], b[1]};
	}

	/**
	 * Given an array of ints, return a new array length 2 containing the first and last elements from the original array.
	 * The original array will be length 1 or more.
	 */
	public int[] makeEnds(int[] nums) {
		return new int[]{nums[0], nums[nums.length - 1]};
	}

	/**
	 * Given an int array length 2, return true if it contains a 2 or a 3.
	 */
	public boolean has23(int[] nums) {
		return Arrays.stream(nums).anyMatch(i -> i / 2 == 1);
	}

	/**
	 * Given an int array length 2, return true if it does not contain a 2 or 3.
	 */
	public boolean no23(int[] nums) {
		return Arrays.stream(nums).noneMatch(i -> i / 2 == 1);
	}

	/**
	 * Given an int array, return a new array with double the length where its last element is the same as the original array,
	 * and all the other elements are 0. The original array will be length 1 or more.
	 * Note: by default, a new int array contains all 0's.
	 */
	public int[] makeLast(int[] nums) {
		return java.util.stream.IntStream.concat(
				Arrays.stream(new int[2 * nums.length - 1]),
				Arrays.stream(new int[]{nums[nums.length - 1]})
		).toArray();
	}

	/**
	 * Given an int array, return true if the array contains 2 twice, or 3 twice.
	 * The array will be length 0, 1, or 2.
	 */
	public boolean double23(int[] nums) {
		return Arrays.stream(nums).filter(i -> i == 2).count() == 2
				|| Arrays.stream(nums).filter(i -> i == 3).count() == 2;
	}

	/**
	 * Given an int array length 3, if there is a 2 in the array immediately followed by a 3,
	 * set the 3 element to 0. Return the changed array.
	 */
	public int[] fix23(int[] nums) { // not the most elegant solution but i couldn't make it work with streams
		return nums[0] == 2 && nums[1] == 3 ?
				new int[]{2, 0, nums[2]} :
				nums[1] == 2 && nums[2] == 3 ?
						new int[]{nums[0], 2, 0} : nums;
	}

	/**
	 * Start with 2 int arrays, a and b, of any length. Return how many of the arrays have 1 as their first element.
	 */
	public int start1(int[] a, int[] b) {
		return (a.length > 0 && a[0] == 1 ? 1 : 0) + (b.length > 0 && b[0] == 1 ? 1 : 0);
	}

	/**
	 * Start with 2 int arrays, a and b, each length 2. Consider the sum of the values in each array.
	 * Return the array which has the largest sum. In event of a tie, return a.
	 */
	public int[] biggerTwo(int[] a, int[] b) {
		return a[0] + a[1] >= b[0] + b[1] ? a : b;
	}

	/**
	 * Given an array of ints of even length, return a new array length 2 containing the middle two elements from the original array.
	 * The original array will be length 2 or more.
	 */
	public int[] makeMiddle(int[] nums) {
		return new int[]{nums[nums.length / 2 - 1], nums[nums.length / 2]};
	}

	/**
	 * Given 2 int arrays, each length 2, return a new array length 4 containing all their elements.
	 */
	public int[] plusTwo(int[] a, int[] b) {
		return new int[]{a[0], a[1], b[0], b[1]};
	}

	/**
	 * Given an array of ints, swap the first and last elements in the array.
	 * Return the modified array. The array length will be at least 1.
	 */
	public int[] swapEnds(int[] nums) {
		return nums.length < 2 ? nums :
				java.util.stream.IntStream.concat(Arrays.stream(new int[]{nums[nums.length - 1]}),
						java.util.stream.IntStream.concat(Arrays.stream(nums, 1, nums.length - 1),
								Arrays.stream(new int[]{nums[0]}))).toArray();
	}

	/**
	 * Given an array of ints of odd length, return a new array length 3 containing the elements from the middle of the array.
	 * The array length will be at least 3.
	 */
	public int[] midThree(int[] nums) {
		return new int[]{nums[nums.length / 2 - 1], nums[nums.length / 2], nums[nums.length / 2 + 1]};
	}

	/**
	 * Given an array of ints of odd length, look at the first, last, and middle values in the array and return the largest.
	 * The array length will be a least 1.
	 */
	public int maxTriple(int[] nums) {
		return Math.max(Math.max(nums[0], nums[nums.length / 2]), nums[nums.length - 1]);
	}

	/**
	 * Given an int array of any length, return a new array of its first 2 elements.
	 * If the array is smaller than length 2, use whatever elements are present.
	 */
	public int[] frontPiece(int[] nums) {
		return Arrays.stream(nums).limit(2).toArray();
	}

	/**
	 * We'll say that a 1 immediately followed by a 3 in an array is an "unlucky" 1.
	 * Return true if the given array contains an unlucky 1 in the first 2 or last 2 positions in the array.
	 */
	public boolean unlucky1(int[] nums) {
		return nums.length > 1 &&
				(nums[0] == 1 && nums[1] == 3 ||
						nums[1] == 1 && nums[2] == 3 ||
						nums[nums.length - 2] == 1 && nums[nums.length - 1] == 3);
	}

	/**
	 * Given 2 int arrays, a and b, return a new array length 2 containing, as much as will fit,
	 * the elements from a followed by the elements from b. The arrays may be any length, including 0,
	 * but there will be 2 or more elements available between the 2 arrays.
	 */
	public int[] make2(int[] a, int[] b) {
		return java.util.stream.IntStream.concat(Arrays.stream(a), Arrays.stream(b))
				.limit(2)
				.toArray();
	}

	/**
	 * Given 2 int arrays, a and b, of any length, return a new array with the first element of each array.
	 * If either array is length 0, ignore that array.
	 */
	public int[] front11(int[] a, int[] b) {
		return java.util.stream.IntStream.concat(
						Arrays.stream(a).limit(1),
						Arrays.stream(b).limit(1))
				.toArray();
	}
}
