import java.util.Arrays;

@SuppressWarnings("unused")
public class Demo {
	/**
	 * We'll say that an array "likes" a particular value if at least half of the elements in the array
	 * differ from the value by 2 or less. (This is an example problem where the solution code is provided.
	 * Click the <b>Go</b> button to run the problem. JavaBat has many practice coding problems like this
	 * with immediate feedback.)
	 */
	public boolean arrayLike(int[] nums, int value) {
		return Arrays.stream(nums)
				.filter(n -> Math.abs(n - value) < 3)
				.count() >= (nums.length + 1) / 2;
	}
}
