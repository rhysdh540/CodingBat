import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Array3 {
	/**
	 * Consider the leftmost and righmost appearances of some value in an array.
	 * We'll say that the "span" is the number of elements between the two inclusive.
	 * A single value has a span of 1. Returns the largest span found in the given array.
	 * (Efficiency is not a priority.)
	 */
	public int maxSpan(int[] nums) {
		// take every value in the array, find the first and last index of that value, find the largest difference
		return Arrays.stream(nums).map(i -> // could add .distinct() here to make it more efficient but without is shorter
				Arrays.stream(nums).boxed().collect(Collectors.toList()).lastIndexOf(i) -
				Arrays.stream(nums).boxed().collect(Collectors.toList()).indexOf(i) + 1
		).max().orElse(0);
	}

	/**
	 * Return an array that contains exactly the same numbers as the given array,
	 * but rearranged so that every 3 is immediately followed by a 4.
	 * Do not move the 3's, but every other number may move.
	 * The array contains the same number of 3's and 4's, every 3 has a number after it that is not a 3,
	 * and a 3 appears in the array before any 4.
	 */
	public int[] fix34(int[] nums) {
		//TODO shorten
		List<Integer> list = Arrays.stream(nums).boxed().collect(Collectors.toList());
		for(int i = 0, j = list.indexOf(4); i < list.size(); i++)
			if(list.get(i) == 3) {
				list.set(i + 1, list.set(j, list.get(i + 1)));
				j = list.subList(j, list.size()).indexOf(4) + j;
			}
		return list.stream().mapToInt(k -> k).toArray();
	}

	/**
	 * (This is a slightly harder version of the fix34 problem.)
	 * Return an array that contains exactly the same numbers as the given array,
	 * but rearranged so that every 4 is immediately followed by a 5. Do not move the 4's,
	 * but every other number may move. The array contains the same number of 4's and 5's,
	 * and every 4 has a number after it that is not a 4.In this version,
	 * 5's may appear anywhere in the original array.
	 */
	public int[] fix45(int[] nums) {
		//TODO shorten
		List<Integer> list = Arrays.stream(nums).boxed().collect(Collectors.toList());
		for(int i = 0; i < list.size() - 1; i++)
			if(list.get(i) == 4 && list.get(i + 1) != 5)
				for(int j = 0; j < list.size(); j++)
					list.set(list.get(j) == 5 && j == 0 ? 0 : list.get(j) == 5 && list.get(j - 1) != 4 ? j : i + 1, list.set(i + 1, 5));
		return list.stream().mapToInt(i -> i).toArray();
	}

	/**
	 * Given a non-empty array, return true if there is a place to split the array
	 * so that the sum of the numbers on one side is equal to the sum of the numbers on the other side.
	 */
	public boolean canBalance(int[] nums) {
		return java.util.stream.IntStream.range(0, nums.length).anyMatch(i ->
				Arrays.stream(Arrays.copyOf(nums, i)).sum() ==
				Arrays.stream(Arrays.copyOfRange(nums, i, nums.length)).sum());
	}

	/**
	 * Given two arrays of ints sorted in increasing order, outer and inner,
	 * return true if all of the numbers in inner appear in outer.
	 * The best solution makes only a single "linear" pass of both arrays,
	 * taking advantage of the fact that both arrays are already in sorted order.
	 */
	public boolean linearIn(int[] outer, int[] inner) {
		return Arrays.stream(inner).allMatch(i -> Arrays.stream(outer).anyMatch(j -> j == i));
	}

	/**
	 * Given n>=0, create an array length n*n with the following pattern, shown here for n=3 : <br>
	 * {@code {0, 0, 1,    0, 2, 1,    3, 2, 1}} (spaces added to show the 3 groups).
	 */
	public int[] squareUp(int n) {
		return java.util.stream.IntStream.rangeClosed(1, n).flatMap(i ->
				java.util.stream.IntStream.range(0, n).map(j -> j < n - i ? 0 : n - j)
		).toArray();
	}

	/**
	 * Given n>=0, create an array with the pattern {@code {1,    1, 2,    1, 2, 3,    ... 1, 2, 3 .. n}}
	 * (spaces added to show the grouping). Note that the length of the array will be 1 + 2 + 3 ... + n,
	 * which is known to sum to exactly n*(n + 1)/2.
	 */
	public int[] seriesUp(int n) {
		return java.util.stream.IntStream.rangeClosed(1, n).flatMap(i ->
				java.util.stream.IntStream.rangeClosed(1, i)
		).toArray();
	}

	/**
	 * We'll say that a "mirror" section in an array is a group of contiguous elements such that somewhere in the array,
	 * the same group appears in reverse order. For example, the largest mirror section in
	 * {@code {1, 2, 3, 8, 9, 3, 2, 1}} is length 3 (the {@code {1, 2, 3}} part).
	 * Return the size of the largest mirror section found in the given array.
	 */
	public int maxMirror(int[] nums) {
		//TODO shorten
		int max = 0, size = 0;
		for(int start = 0; start < nums.length; start++)
			for(int begin = nums.length - 1; begin >= 0; begin--, max = Math.max(max, size), size = 0) {
				int i = start, j = begin;
				for(; i < nums.length && j >= 0 && nums[i] == nums[j]; size++, i++, j--) ;
			}
		return max;
	}

	/**
	 * Say that a "clump" in an array is a series of 2 or more adjacent elements of the same value.
	 * Return the number of clumps in the given array.
	 * @see String3#maxBlock(String) regexplanation
	 */
	@SuppressWarnings("RegExpSuspiciousBackref")
	public int countClumps(int[] nums) {
		return (int) Arrays.stream(Arrays.stream(nums).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString()
				.split("(?<=(.))(?!\\1)"))
				.filter(s -> s.length() > 1)
				.count();
	}
}
