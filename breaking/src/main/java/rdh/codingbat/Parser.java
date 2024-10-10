package rdh.codingbat;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.Escaper;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import rdh.codingbat.Problem.Language;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Parser {
	private final int numThreads;

	private final InputStream in;
	private final Path outputJson, outputBin;
	private final Map<Integer, Problem> foundProblems = new ConcurrentSkipListMap<>();
	private final ThreadGroup group = new ThreadGroup("Parsers");

	public Parser(int numThreads, Path input, Path outputJson, Path outputBin) {
		this.numThreads = numThreads;
		this.outputJson = outputJson;
		this.outputBin = outputBin;

		if(Files.exists(outputBin)) {
			try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(outputBin))) {
				((Set<Problem>) ois.readObject()).forEach(p -> foundProblems.put(p.id(), p));
			} catch (Throwable e) {
				throw Networking.unchecked(e);
			}
		}

		try {
			this.in = Files.newInputStream(input);
		} catch (IOException e) {
			throw Networking.unchecked(e);
		}
	}

	public void run() throws IOException {
		Files.walk(Path.of("data/failedProblems"))
				.filter(Files::isRegularFile)
				.forEach(p -> {
					try {
						Files.delete(p);
					} catch (IOException e) {
						throw Networking.unchecked(e);
					}
				});

		Thread shutdownHook = new Thread(() -> {
			group.interrupt();
			System.out.println("Stopped after parsing " + foundProblems.size() + " problems");
			write();
			try {
				in.close();
			} catch (IOException e) {
				throw Networking.unchecked(e);
			}
		});

		System.out.println("Starting with " + foundProblems.size() + " problems");

		Runtime.getRuntime().addShutdownHook(shutdownHook);

		for(int i = 0; i < numThreads; i++) {
			Thread thread = new Thread(group, this::runTask);
			thread.setName("Spammer-" + i);
			thread.setUncaughtExceptionHandler((t, e) -> {
				System.err.println("Error in thread " + t.getName());
				e.printStackTrace();
				group.interrupt();
			});
			thread.start();
		}

		while(group.activeCount() > 0) {
			int size = foundProblems.size();
			if(size % 100 == 0) {
				synchronized(System.out) {
					System.out.print("Parsed " + foundProblems.size() + " problems\r");
					System.out.flush();
				}
			}
		}

		Runtime.getRuntime().removeShutdownHook(shutdownHook);

		write();
		try {
			in.close();
		} catch (IOException e) {
			throw Networking.unchecked(e);
		}
	}

	private void write() {
		JsonGrammar g = JsonGrammar.builder()
				.bareRootObject(true)
				.build();

		Jankson jankson = Jankson.builder()
				.allowBareRootObject()
				.build();

		try(BufferedWriter bw = Files.newBufferedWriter(outputJson)) {
			JsonArray element = (JsonArray) jankson.toJson(foundProblems.values().toArray());

			element.replaceAll(prob -> {
				JsonObject obj = (JsonObject) prob;
				JsonObject tests = (JsonObject) obj.get("tests");
				obj.put("tests", tests.entrySet().stream()
						.collect(JsonObject::new,
								(o, e) -> o.put(Escaper.escapeString(e.getKey()),
										e.getValue()),
								JsonObject::putAll));
				return obj;
			});

			element.toJson(bw, g, 0);
		} catch (IOException e) {
			throw Networking.unchecked(e);
		}

		try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(outputBin))) {
			oos.writeObject(new ConcurrentSkipListSet<>(foundProblems.values()));
		} catch (IOException e) {
			throw Networking.unchecked(e);
		}
	}

	private void runTask() {
		int problemNumber = getNextProblem();

		while(problemNumber != -1) {
			try {
				InputStream stream = Networking.getProblemStream(problemNumber);
				Problem problem = parseProblem(stream, problemNumber);
				foundProblems.put(problemNumber, problem);
			} catch (IOException e) {
				throw Networking.unchecked(e);
			}

			problemNumber = getNextProblem();
		}
	}

	private int getNextProblem() {
		while(true) {
			byte[] buf = new byte[6];
			synchronized(in) {
				try {
					int read = in.read(buf);
					if(read == -1) {
						return -1;
					}

					if(read != 6) {
						throw new IllegalArgumentException("Invalid problem number: " + new String(buf, 0, read));
					}

					int c = in.read();
					if(c != '\n' && c != '\r' && c != -1) {
						throw new IllegalArgumentException("Invalid problem formatting: " + new String(buf));
					}

				} catch (IOException e) {
					throw Networking.unchecked(e);
				}
			}

			int result = 0;
			for(int i = 0; i < 6; i++) {
				byte b = buf[i];
				if(b < '0' || b > '9') {
					throw new IllegalArgumentException("Invalid problem number: " + new String(buf));
				}
				result = result * 10 + b - '0';
			}
			if(foundProblems.containsKey(result)) continue;
			return result;
		}
	}

	@SuppressWarnings("DataFlowIssue")
	public static Problem parseProblem(InputStream in, int id) {
		Document d = null;
		try {
			d = Jsoup.parse(in, StandardCharsets.UTF_8.name(), Networking.makeUrl(id).toString());

			Element body = getOnly(
					d.body().select(new Evaluator.Class("tabc"))
			);

			String language = body.select("nav").select("ul").select("li#selected").select("a").text();

			body = getOnly(body.select("div.tabin").select("div.indent"));

			Element firstChild = body.firstElementChild();
			boolean hasCategory = firstChild.is(new Evaluator.Tag("a"));

			String category = !hasCategory ? null :
					firstChild.selectFirst(new Evaluator.Tag("span")).text();

			Element nameContainer = body.child(hasCategory ? 1 : 0);
			while(nameContainer.childrenSize() == 1) {
				// sometimes it's wrapped in a <p> or more
				nameContainer = nameContainer.firstElementChild();
			}

			String name = nameContainer.text();

			Element problemBody = body
					.select("table")
					.select("tr")
					.select("td").first();

			String description = getOnly(problemBody
					.select("div.minh")
					.select("p.max2")).text();

			SequencedMap<String, String> tests = parseTests(problemBody, name);

			String hint = parseHint(
					problemBody.children().select("p:not([*])").first()  // TODO does this .first() work for everything?
							.select("button.gray")
			);

			double difficulty = findDifficulty(body);

			return new Problem(
					id,
					name,
					category,
					Language.valueOf(language.toUpperCase(Locale.ROOT)),
					description,
					tests,
					difficulty,
					hint
			);
		} catch (Exception e) {
			try {
				Files.createDirectories(Path.of("data/failedProblems"));
				Files.writeString(Path.of("data/failedProblems/" + id + ".html"), d.outerHtml());
			} catch (IOException ex) {
				e.addSuppressed(ex);
			}
			throw Networking.unchecked(e);
		}
	}

	private static @NotNull SequencedMap<String, String> parseTests(Element problemBody, String name) {
		Pattern pattern = Pattern.compile(name + "(\\(.*?\\)) →");
		String text = problemBody.ownText();

		Matcher matcher = pattern.matcher(text);

		SequencedMap<String, String> tests = new LinkedHashMap<>();

		int index = 0;
		while(matcher.find()) {
			String input = sanitizeInput(matcher.group(1));
			String output = text.substring(index, matcher.start()).trim();
			index = matcher.end();

			tests.put(input, output);
		}
		return tests;
	}

	private static String parseHint(Elements buttons) {
		buttons = buttons.select("button:containsOwn(Hint)");
		if(buttons.isEmpty()) return null;
		String onClick = getOnly(buttons).attr("onclick");

		final String start = "document.getElementById(\"results\").innerHTML=\"<b>Hint:</b><br>\"+unescape(\"";
		final String end = "\"); sendRemark(\"h1\");";

		if(!onClick.startsWith(start)) {
			return null;
		}

		return URLDecoder.decode(
				onClick.substring(start.length(), onClick.length() - end.length()),
				StandardCharsets.UTF_8);
	}

	private static double findDifficulty(Element body) {
		final String prefix = "Difficulty: ";

		for(Element p : body.children().select("p:not([*])")) {
			Elements fonts = p.select("font[color=\"gray\"]");
			for(Element font : fonts) {
				String text = font.text();


				if(text.startsWith(prefix)) {
					return Double.parseDouble(text.substring(prefix.length()));
				}
			}
		}

		return -1;
	}

	private static String sanitizeInput(String input) {
		input = input.trim();
		if(input.charAt(0) != '(') {
			throw new IllegalStateException();
		} else {
			input = input.substring(1);
		}

		if(input.charAt(input.length() - 1) != ')') {
			throw new IllegalStateException();
		} else {
			input = input.substring(0, input.length() - 1);
		}

		if(input.isBlank()) input = "()";

		return input;
	}

	private static Element getOnly(Elements elements) {
		if(elements.size() != 1) {
			throw new IllegalStateException("found " + elements.size() + " elements instead of 1\n"
					+ elements.outerHtml());
		}

		return elements.first();
	}

	public static void main(String[] args) throws IOException {
		Parser parser = new Parser(4,
				Path.of("data/problemIDs.txt"),
				Path.of("data/problems.json5"),
				Path.of("data/problems.bin")
		);
		parser.run();
	}
}
