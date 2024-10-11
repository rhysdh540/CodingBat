package rdh.codingbat;

import rdh.codingbat.Problem.Language;

import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.zip.GZIPInputStream;

public class Main {
	public static void main(String[] args) {
		Path path = Path.of("data/problems.bin.gz");
		Set<Problem> problems = getProblems(path);

		System.out.println(problems.size() + " problems loaded");
		problems.parallelStream()
				.filter(p -> p.tests().isEmpty())

				.forEach(p -> {
					postEmptyProblem(p);
					System.out.println("Posted " + p.id() + " (" + p.url() + ")");
				});
	}

	private static void postEmptyProblem(Problem p) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

		StringBuilder urlBuilder = new StringBuilder("https://codingbat.com/run");
		urlBuilder.append("?id=p").append(Problem.formatNumber(p.id()));
		urlBuilder.append("&code=");
		if(p.language() == Language.PYTHON) {
			if(p.name().isEmpty()) {
				System.out.println("Skipping " + p.id() + " because it has no name and is python");
				return;
			}

			// java problems can be passed with no code
			urlBuilder.append(
					URLEncoder.encode("def " + p.name() + "():\n  pass", StandardCharsets.UTF_8)
			);
		}

		urlBuilder.append("&cuname=");
		urlBuilder.append(URLEncoder.encode(System.getenv("codingbat.email"), StandardCharsets.UTF_8));
		urlBuilder.append("&owner=&date=662394817&adate=");
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		urlBuilder.append(now.format(formatter)).append("z");

		try {
			URL url = URI.create(urlBuilder.toString()).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Cookie", "JSESSIONID=" + System.getenv("codingbat.sessionId"));
			connection.connect();

			int responseCode = connection.getResponseCode();
			String responseMessage = new String(connection.getInputStream().readAllBytes());

			if(responseCode != 200 || responseMessage.startsWith("Error:")) {
				System.out.println("Failed to post " + p.id() + " (response code " + responseCode + ")");
				System.out.println("  " + responseMessage);
			}
		} catch (Throwable e) {
			throw Networking.unchecked(e);
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
