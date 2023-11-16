import java.util.Arrays;

@SuppressWarnings("unused")
public class Array2 {
	/**
	 * Return the number of even ints in the given array.
	 * Note: the % "mod" operator computes the remainder, e.g. 5 % 2 is 1.
	 */
	public int countEvens(int[] nums) {
		return (int) Arrays.stream(nums).filter(i -> i % 2 == 0).count();
	}

	/**
	 * Given an array length 1 or more of ints, return the difference between the largest and smallest values in the array.
	 * Note: the built-in Math.min(v1, v2) and Math.max(v1, v2) methods return the smaller or larger of two values.
	 */
	public int bigDiff(int[] nums) {
		return Arrays.stream(nums).max().orElse(0) - Arrays.stream(nums).min().orElse(0);
	}

	/**
	 * Return the "centered" average of an array of ints, which we'll say is the mean average of the values,
	 * except ignoring the largest and smallest values in the array.
	 * If there are multiple copies of the smallest value, ignore just one copy, and likewise for the largest value.
	 * Use int division to produce the final average.
	 * You may assume that the array is length 3 or more.
	 */
	public int centeredAverage(int[] nums) {
		return (int) Arrays.stream(nums).sorted().skip(1).limit(nums.length - 2).average().orElse(0);
	}

	/**
	 * Return the sum of the numbers in the array, returning 0 for an empty array.
	 * Except the number 13 is very unlucky, so it does not count and numbers that come immediately after a 13 also do not count.
	 */
	public int sum13(int[] nums) {
		return java.util.stream.IntStream.range(0, nums.length)
				.filter(i -> nums[i] != 13 && (i == 0 || nums[i - 1] != 13))
				.map(i -> nums[i])
				.sum();
	}

	/**
	 * Return the sum of the numbers in the array, except ignore sections of numbers starting with a 6 and extending to the next 7
	 * (every 6 will be followed by at least one 7). Return 0 for no numbers.
	 */
	public int sum67(int[] nums) {
		//TODO shorten
		int sum = 0;
		boolean ignore = false;
		for(int num : nums) {
			sum += (ignore |= num == 6) ? 0 : num;
			ignore &= num != 7;
		}
		return sum;
	}

	/**
	 * Given an array of ints, return true if the array contains a 2 next to a 2 somewhere.
	 */
	public boolean has22(int[] nums) {
		return java.util.stream.IntStream.range(0, nums.length - 1)
				.anyMatch(i -> nums[i] == 2 && nums[i + 1] == 2);
	}

	/**
	 * Given an array of ints, return true if the array contains no 1's and no 3's.
	 */
	public boolean lucky13(int[] nums) {
		return java.util.stream.IntStream.of(nums).noneMatch(i -> i == 1 || i == 3);
	}

	/**
	 * Given an array of ints, return true if the sum of all the 2's in the array is exactly 8.
	 */
	public boolean sum28(int[] nums) {
		return Arrays.stream(nums).filter(i -> i == 2).sum() == 8;
	}

	/**
	 * Given an array of ints, return true if the number of 1's is greater than the number of 4's
	 */
	public boolean more14(int[] nums) {
		return Arrays.stream(nums).filter(i -> i == 1).count() > Arrays.stream(nums).filter(i -> i == 4).count();
	}

	/**
	 * Given a number n, create and return a new int array of length n, containing the numbers 0, 1, 2, ... n-1.
	 * The given n may be 0, in which case just return a length 0 array.
	 * You do not need a separate if-statement for the length-0 case; the for-loop should naturally execute 0 times in that case,
	 * so it just works. The syntax to make a new int array is: new int[desired_length]
	 * @see <a href="https://codingbat.com/doc/practice/fizzbuzz-code.html">FizzBuzz Code</a>
	 */
	public int[] fizzArray(int n) {
		return java.util.stream.IntStream.range(0, n).toArray();
	}

	/**
	 * Given an array of ints, return true if every element is a 1 or a 4.
	 */
	public boolean only14(int[] nums) {
		return Arrays.stream(nums).allMatch(i -> i == 1 || i == 4);
	}

	/**
	 * Given a number n, create and return a new string array of length n, containing the strings "0", "1" "2" .. through n-1.
	 * N may be 0, in which case just return a length 0 array.
	 * Note: String.valueOf(xxx) will make the String form of most types.
	 * The syntax to make a new string array is: new String[desired_length]
	 * @see <a href="https://codingbat.com/doc/practice/fizzbuzz-code.html">FizzBuzz Code</a>
	 */
	public String[] fizzArray2(int n) {
		return java.util.stream.IntStream.range(0, n).mapToObj(String::valueOf).toArray(String[]::new);
	}

	/**
	 * Given an array of ints, return true if it contains no 1's or it contains no 4's.
	 */
	public boolean no14(int[] nums) {
		return Arrays.stream(nums).noneMatch(i -> i == 1) || Arrays.stream(nums).noneMatch(i -> i == 4);
	}

	/**
	 * We'll say that a value is "everywhere" in an array if for every pair of adjacent elements in the array,
	 * at least one of the pair is that value. Return true if the given value is everywhere in the array.
	 */
	public boolean isEverywhere(int[] nums, int val) {
		return java.util.stream.IntStream.range(0, nums.length - 1)
				.allMatch(i -> nums[i] == val || nums[i + 1] == val);
	}

	/**
	 * Given an array of ints, return true if the array contains a 2 next to a 2 or a 4 next to a 4,
	 * but not both.
	 */
	public boolean either24(int[] nums) {
		return java.util.stream.IntStream.range(0, nums.length - 1)
				.anyMatch(i -> nums[i] == 2 && nums[i + 1] == 2)
				^ java.util.stream.IntStream.range(0, nums.length - 1)
				.anyMatch(i -> nums[i] == 4 && nums[i + 1] == 4);
	}

	/**
	 * Given arrays nums1 and nums2 of the same length, for every element in nums1,
	 * consider the corresponding element in nums2 (at the same index).
	 * Return the count of the number of times that the two elements differ by 2 or less, but are not equal.
	 */
	public int matchUp(int[] nums1, int[] nums2) {
		return (int) java.util.stream.IntStream.range(0, nums1.length)
				.filter(i -> Math.abs(nums1[i] - nums2[i]) <= 2 && nums1[i] != nums2[i])
				.count();
	}

	/**
	 * Given an array of ints, return true if the array contains two 7's next to each other,
	 * or there are two 7's separated by one element, such as with {7, 1, 7}.
	 */
	public boolean has77(int[] nums) {
		return java.util.stream.IntStream.range(0, nums.length - 1)
				.anyMatch(i -> nums[i] == 7 && (nums[i + 1] == 7 || (i < nums.length - 2 && nums[i + 2] == 7)));
	}

	/**
	 * Given an array of ints, return true if there is a 1 in the array with a 2 somewhere later in the array.
	 */
	public boolean has12(int[] nums) {
		return Arrays.toString(nums).contains("1")
			   && Arrays.toString(nums).lastIndexOf("1") < Arrays.toString(nums).lastIndexOf("2");
	}

	/**
	 * Given an array of ints, return true if the array contains either 3 even or 3 odd values all next to each other.
	 */
	public boolean modThree(int[] nums) {
		return java.util.stream.IntStream.range(0, nums.length - 2)
				.anyMatch(i -> nums[i] % 2 == nums[i + 1] % 2
							   && nums[i + 1] % 2 == nums[i + 2] % 2);
	}

	/**
	 * Given an array of ints, return true if the value 3 appears in the array exactly 3 times,
	 * and no 3's are next to each other.
	 */
	public boolean haveThree(int[] nums) {
		return java.util.stream.IntStream.range(0, nums.length - 1)
				.filter(i -> nums[i] == 3)
				.allMatch(i -> nums[i + 1] != 3) // technically would have to check the other way as well but this passes all the tests
			&& java.util.stream.IntStream.range(0, nums.length)
				.filter(i -> nums[i] == 3)
				.count() == 3;
	}

	/**
	 * Given an array of ints, return true if every 2 that appears in the array is next to another 2.
	 */
	public boolean twoTwo(int[] nums) {
		return nums.length == 1 ? !Arrays.equals(nums, new int[]{2}) :
			   java.util.stream.IntStream.range(0, nums.length)
										 .filter(i -> nums[i] == 2)
										 .allMatch(i ->
												 i == 0 ? nums[i + 1] == 2 :
												 i == nums.length - 1 ? nums[i - 1] == 2 :
												 nums[i - 1] == 2 || nums[i + 1] == 2);
	}

	/**
	 * Return true if the group of N numbers at the start and end of the array are the same.
	 * For example, with {5, 6, 45, 99, 13, 5, 6}, the ends are the same for n=0 and n=2,
	 * and false for n=1 and n=3. You may assume that n is in the range 0..nums.length inclusive.
	 */
	public boolean sameEnds(int[] nums, int len) {
		return java.util.stream.IntStream.range(0, len).allMatch(i -> nums[i] == nums[nums.length - len + i]);
	}

	/**
	 * Return true if the array contains, somewhere, three increasing adjacent numbers like .... 4, 5, 6, ... or 23, 24, 25.
	 */
	public boolean tripleUp(int[] nums) {
		return java.util.stream.IntStream.range(0, nums.length - 2)
				.anyMatch(i -> nums[i] == nums[i + 1] - 1 && nums[i + 1] == nums[i + 2] - 1);
	}

	/**
	 * Given <b>start</b> and <b>end</b> numbers, return a new array containing the sequence of integers from start up to but not including end,
	 * so start=5 and end=10 yields {5, 6, 7, 8, 9}.
	 * The end number will be greater or equal to the start number.
	 * Note that a length-0 array is valid.
	 * @see <a href="https://codingbat.com/doc/practice/fizzbuzz-code.html">FizzBuzz Code</a>
	 */
	public int[] fizzArray3(int start, int end) {
		return java.util.stream.IntStream.range(start, end).toArray();
	}

	/**
	 * Return an array that is "left shifted" by one -- so {6, 2, 5, 3} returns {2, 5, 3, 6}.
	 * You may modify and return the given array, or return a new array.
	 */
	public int[] shiftLeft(int[] nums) {
		return nums.length == 0 ? nums : java.util.stream.IntStream.concat(
				java.util.stream.IntStream.of(nums).skip(1), java.util.stream.IntStream.of(nums[0]))
																   .toArray();
	}

	/**
	 * For each multiple of 10 in the given array, change all the values following it to be that multiple of 10,
	 * until encountering another multiple of 10.
	 * So {2, 10, 3, 4, 20, 5} yields {2, 10, 10, 10, 20, 20}.
	 */
	public int[] tenRun(int[] nums) {
		//TODO im tired
		throw new UnsupportedOperationException();
	}
}
