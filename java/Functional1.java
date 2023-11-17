import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Functional1 {
	/**
	 * Given a list of integers, return a list where each integer is multiplied by 2.
	 */
	public List<Integer> doubling(List<Integer> nums) {
		return nums.stream().map(i -> i * 2).collect(Collectors.toList());
	}

	/**
	 * Given a list of integers, return a list where each integer is multiplied with itself.
	 */
	public List<Integer> square(List<Integer> nums) {
		return nums.stream().map(i -> i * i).collect(Collectors.toList());
	}

	/**
	 * Given a list of strings, return a list where each string has "*" added at its end.
	 */
	public List<String> addStar(List<String> strings) {
		return strings.stream().map(s -> s + "*").collect(Collectors.toList());
	}

	/**
	 * Given a list of strings, return a list where each string is replaced by
	 * 3 copies of the string concatenated together.
	 */
	public List<String> copies3(List<String> strings) {
		return strings.stream().map(s -> s + s + s).collect(Collectors.toList());
	}

	/**
	 * Given a list of strings, return a list where each string has "y" added at its start and end.
	 */
	public List<String> moreY(List<String> strings) {
		return strings.stream().map(s -> "y" + s + "y").collect(Collectors.toList());
	}

	/**
	 * Given a list of integers, return a list where each integer is added to 1 and the result is multiplied by 10.
	 */
	public List<Integer> math1(List<Integer> nums) {
		return nums.stream().map(i -> (i + 1) * 10).collect(Collectors.toList());
	}

	/**
	 * Given a list of non-negative integers, return an integer list of the rightmost digits. (Note: use %)
	 */
	public List<Integer> rightDigit(List<Integer> nums) {
		return nums.stream().map(i -> i % 10).collect(Collectors.toList());
	}

	/**
	 * Given a list of strings, return a list where each string is converted to lower case (Note: String toLowerCase() method).
	 */
	public List<String> lower(List<String> strings) {
		return strings.stream().map(String::toLowerCase).collect(Collectors.toList());
	}

	/**
	 * Given a list of strings, return a list where each string has all its "x" removed.
	 */
	public List<String> noX(List<String> strings) {
		return strings.stream().map(s -> s.replace("x", "")).collect(Collectors.toList());
	}
}
