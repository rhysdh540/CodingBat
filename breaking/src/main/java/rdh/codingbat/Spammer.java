package rdh.codingbat;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

public final class Spammer {
	private final int numThreads;
	private final int endingNum;

	private final Path file;
	private final AtomicInteger i;
	private final Set<Integer> foundProblems = new ConcurrentSkipListSet<>();
	private final ThreadGroup group = new ThreadGroup("Spammers");
	private final Writer writer;

	private static final String NO_PROBLEM_FOUND_MESSAGE = "Error: could not find problem";

	public Spammer(
		int startingNum,
		int endingNum,
		int numThreads,
		Path file
	) {
		this.numThreads = numThreads;
		this.endingNum = endingNum;
		this.file = file;
		this.i = new AtomicInteger(startingNum);

		try {
			this.writer = Files.newBufferedWriter(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Spammer(
		int startingNum,
		int numThreads
	) {
		this(startingNum, 1_000_000, numThreads, Paths.get("data/problemIDs.txt"));
	}

	public void run() throws IOException {
		Thread shutdownHook = new Thread(() -> {
			group.interrupt();
			System.out.println("Stopped on problem " + i.get());
		});
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		// load existing problems
		Files.readAllLines(file).stream()
			.map(Integer::parseInt)
			.forEach(foundProblems::add);

		for(int j = 0; j < numThreads; j++) {
			Thread thread = new Thread(group, this::runTask);
			thread.setName("Spammer-" + j);
			thread.start();
		}

		while(group.activeCount() > 0) {
			synchronized(System.out) {
				System.out.print("On problem " + i.get() + "; found " + foundProblems.size() + " total\r");
				System.out.flush();
			}
		}

		Runtime.getRuntime().removeShutdownHook(shutdownHook);
		writer.close();
	}

	private void runTask() {
		while(true) {
			int problemNumber = i.getAndIncrement();
			if(problemNumber >= endingNum) return;

			if(exists(problemNumber)) {
				foundProblems.add(problemNumber);
				write(problemNumber);
			}
		}
	}

	private void write(int problemNumber) {
		Writer w = writer;
		if(w == null) return;

		synchronized(w) {
			try {
				w.write(problemNumber + "\n");
				w.flush();
			} catch(Exception e) {
				throw Networking.unchecked(e);
			}
		}
	}

	private boolean exists(int problemNumber) {
		final String notFound = NO_PROBLEM_FOUND_MESSAGE;
		try(InputStream is = Networking.getProblemStream(problemNumber)) {
			byte[] buffer = new byte[notFound.length()];
			if(is.read(buffer) != notFound.length()) return false;
			for(int j = 0; j < notFound.length(); j++) {
				if(buffer[j] != notFound.charAt(j)) return true;
			}
			return false;
		} catch (IOException e) {
			throw Networking.unchecked(e);
		}
	}

	public static void main(String[] args) {
		Spammer spammer = new Spammer(100_000, 300_000, 3, Paths.get("data/problemIDs.txt"));

		try {
			spammer.run();
		} catch (IOException e) {
			throw Networking.unchecked(e);
		}
	}
}
