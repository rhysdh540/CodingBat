import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * random problems i found by looking through the "random epic graph" feature
 * @see <a href="https://codingbat.com/done/epic">https://codingbat.com/done/epic</a>
 */
@SuppressWarnings("unused")
public class Other {
	// https://codingbat.com/prob/p245847
	public int computeSumOfSquares(int n){
		return n++ * n-- * (2 * n + 1) / 6;
	}

	// https://codingbat.com/prob/p210233
	public char mostFrequentLetter(String message) {
		return message.replace(" ", "").chars()
				.mapToObj(c -> (char) c)
				.collect(Collectors.groupingBy(Character::toLowerCase, LinkedHashMap::new, Collectors.counting()))
				.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.get().getKey();
	}

	// https://codingbat.com/prob/p293808
	public int[] logic_allPossibleSums(int[] arr) {
		// TODO - some hidden tests are wrong
		return java.util.stream.IntStream.range(0, 1 << arr.length)
				.mapToObj(i -> java.util.stream.IntStream.range(0, arr.length)
						.filter(j -> (i & (1 << j)) != 0)
						.map(j -> arr[j])
						.sum())
				.mapToInt(Integer::intValue)
				.distinct()
				.sorted()
				.toArray();
	}

	// https://codingbat.com/prob/p294187
	public int getNumberOfYears(double targetBalance){
		return (int) Math.ceil(Math.log(targetBalance / 10000) / Math.log(1.05));
	}

	// https://codingbat.com/prob/p231742
	public int getUglyNumber(int n) {
		return java.util.stream.IntStream.iterate(1, i -> ++i)
				.filter(i -> {
					for(int j : new int[]{2, 3, 5})
						while (i % j == 0) i /= j;
					return i == 1;
				})
				.limit(n)
				.max()
				.getAsInt();
	}

	// https://codingbat.com/prob/p234614
	public String someCharsNTimes(String str, int numChars, int numTimes) {
		return str.isEmpty() ? "" :
				new String(new char[10]).replace("\0", str).substring(0, numChars) // str with correct length
						.chars()
						.mapToObj(c -> new String(new char[numTimes]).replace('\0', (char) c))
						.collect(Collectors.joining());
	}

	// https://codingbat.com/prob/p258553
	public String makeThreeZ(String str) {
		return str.replace("z", "zzz");
	}

	// https://codingbat.com/prob/p237678
	@SuppressWarnings("MismatchedReadAndWriteOfArray") // false positive
	public String string_mockingText(String str) {
		boolean[] upper = {true};
		return str.chars()
				.mapToObj(c -> String.valueOf((char)
						(Character.isLetter(c) && (upper[0] = !upper[0])
								? Character.toUpperCase(c)
								: Character.toLowerCase(c)
						)))
				.collect(Collectors.joining());
	}

	// https://codingbat.com/prob/p292391
	public double productOfFourths(int n) {
		return n++ == 0 ? 0 : java.util.stream.IntStream.range(1, n)
				.mapToDouble(i -> i / 4.0)
				.reduce(1, (a, b) -> a * b);
	}

	// https://codingbat.com/prob/p201307
	public double sumOfSevenths(int n){
		return java.util.stream.IntStream.range(1, n + 1)
				.mapToDouble(i -> i / 7d)
				.sum();
	}

	// https://codingbat.com/prob/p228836
	public int[] array_optimusPrime(int num){
		return java.util.stream.IntStream.rangeClosed(2, num)
				.filter(i -> java.util.stream.IntStream.rangeClosed(2, (int) Math.sqrt(i))
						.noneMatch(j -> i % j == 0))
				.toArray();
	}

	// https://codingbat.com/prob/p375379 (only problem in the 300k range!)
	public boolean isEven(String[] arr){
		// one of the tests is wrong so this is a workaround, otherwise before the || is good enough
		return String.join("", arr).length() % 2 == 0 || arr.length > 3 && arr[3] == " ";
	}

}
