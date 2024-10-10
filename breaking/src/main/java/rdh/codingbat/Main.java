package rdh.codingbat;

import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		Path path = Path.of("data/problems.bin");
		Set<Problem> problems = getProblems(path);

		System.out.println(problems.size() + " problems loaded");
		System.out.println(problems.stream().filter(p -> p.language() == Problem.Language.JAVA).count() + " Java problems");
		System.out.println(problems.stream().filter(p -> p.language() == Problem.Language.PYTHON).count() + " Python problems");

		long r = problems.stream()
				.filter(p -> p.language() == Problem.Language.JAVA)
				.filter(p -> p.category() != null)
				.filter(p -> p.category().contains("Recursion"))
				.count();
		System.out.println(r + " Java recursion problems");
	}

	public static Set<Problem> getProblems(Path path) {
		try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
			return (Set<Problem>) ois.readObject();
		} catch (Throwable e) {
			throw Networking.unchecked(e);
		}
	}
}
