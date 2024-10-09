import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

public final class Spammer {
	// configuration
	private static final int startingNum = 623616;
	private static final int numThreads = 5; // WARNING: do not increase to large numbers
	private static final int endingNum = 1_000_000;

	// state
	private static final AtomicInteger i = new AtomicInteger(startingNum);
	private static final Set<Integer> foundProblems = new ConcurrentSkipListSet<>();
	private static final ThreadGroup group = new ThreadGroup("Spammers");
	private static Writer writer = null;


	// stuff
	private static final Path FILE = Paths.get("problems.txt");
	private static final String NO_PROBLEM_FOUND_MESSAGE = "Error: could not find problem";
	private static final String USER_AGENT = "CodingBatSpammer/1.0 (https://github.com/rhysdh540/CodingBat - sorry for any problems!)";

	public static void main(String[] args) throws IOException {
		Thread shutdownHook = new Thread(() -> System.out.println("Stopped on problem " + i.get()));
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		writer = Files.newBufferedWriter(FILE);

		// load existing problems
		Files.readAllLines(FILE).stream()
			.map(Integer::parseInt)
			.forEach(foundProblems::add);

		for(int j = 0; j < numThreads; j++) {
			Thread thread = new Thread(group, Spammer::runTask);
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
	}

	private static void runTask() {
		while(true) {
			int problemNumber = i.getAndIncrement();
			if(problemNumber >= endingNum) return;

			if(exists(problemNumber)) {
				foundProblems.add(problemNumber);
				write(problemNumber);
			}
		}
	}

	private static void write(int problemNumber) {
		Writer w = writer;
		if(w == null) return;

		synchronized(w) {
			try {
				w.write(problemNumber + "\n");
				w.flush();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean exists(int problemNumber) {
		try {
			URL url = new URL(generateUrl(problemNumber));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(5000);
			connection.setConnectTimeout(5000);
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.connect();

			final String notFound = NO_PROBLEM_FOUND_MESSAGE;

			try(InputStream is = connection.getInputStream()) {
				byte[] buffer = new byte[notFound.length()];
				if(is.read(buffer) != notFound.length()) return false;

				for(int j = 0; j < notFound.length(); j++) {
					if(buffer[j] != notFound.charAt(j)) return true;
				}

				return false;
			}
		} catch (SocketTimeoutException e) {
			// we may have ddosed the codingbat server by accident, stop to be nice
			group.interrupt();
			System.err.println("\nTimeout on problem " + problemNumber);
			System.exit(1);
			throw new AssertionError();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static String generateUrl(int problemNumber) {
		if(problemNumber < 1 || problemNumber >= 1000000) {
			throw new IllegalArgumentException("Problem number must be between 1 and 999999");
		}

		String s = Integer.toString(problemNumber);
		char[] c = new char[6 - s.length()];
		Arrays.fill(c, '0');
		return "https://codingbat.com/prob/p" + new String(c) + s;
	}
}
