package rdh.codingbat;

import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.zip.GZIPInputStream;

public class Main {
	public static void main(String[] args) {
		Path path = Path.of("data/problems.bin.gz");
		Set<Problem> problems = getProblems(path);

		System.out.println(problems.size() + " problems loaded");


		var result = problems.stream()
				.parallel()
//				.filter(Problem::isOfficial)
//				.filter(p -> p.id() > 200_000)
				.filter(Predicate.not(Problem::isOfficial))
				.filter(p -> p.id() < 200_000)
				.toList();

		System.out.println(result.size() + " problems found");
		for(Problem p : result) {
			printProblemAndUrl(p);
		}
	}

	private static void printProblemAndUrl(Problem p) {
		System.out.println("  - " + p.nameAndCategory() + " (" + p.url() + ")");
	}

	public static <T> Collector<T, List<T>, Optional<T>> randomElement() {
		return Collector.of(
				ArrayList::new,
				List::add,
				(a, b) -> {
					a.addAll(b);
					return a;
				},
				list -> list.isEmpty() ? Optional.empty()
						: Optional.of(list.get((int) (Math.random() * list.size()))),
				Collector.Characteristics.UNORDERED, Collector.Characteristics.CONCURRENT
		);
	}

	@SuppressWarnings("unchecked")
	public static Set<Problem> getProblems(Path path) {
		try(ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(Files.newInputStream(path)))) {
			return (Set<Problem>) ois.readObject();
		} catch (Throwable e) {
			throw Networking.unchecked(e);
		}
	}
}
