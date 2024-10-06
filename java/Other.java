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
}
