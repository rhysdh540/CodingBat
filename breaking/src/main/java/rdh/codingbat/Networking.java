package rdh.codingbat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class Networking {
	public static final String USER_AGENT = "CodingBatSpammer/1.0 (https://github.com/rhysdh540/CodingBat - sorry for any problems!)";

	public static URL makeUrl(int problemNumber) {
		try {
			return URI.create("https://codingbat.com/prob/p" + Problem.formatNumber(problemNumber)).toURL();
		} catch (MalformedURLException e) {
			throw unchecked(e);
		}
	}

	public static InputStream getProblemStream(int problemNumber) throws IOException {
		try {
			HttpURLConnection connection = (HttpURLConnection) makeUrl(problemNumber).openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(5000);
			connection.setConnectTimeout(5000);
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.connect();

			return connection.getInputStream();
		} catch (SocketTimeoutException e) {
			// we may have ddosed the codingbat server by accident, stop to be nice
			throw new IOException("Timeout on problem fetch; problem " + problemNumber, e);
		}
	}

	private static final DateTimeFormatter urlFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

	public static InputStream sendCode(Problem problem, @NotNull String code, @NotNull String email, @Nullable String sessionId)
			throws IOException {
		String req = "https://codingbat.com/run" +
				"?id=p" + Problem.formatNumber(problem.id()) +
				"&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8) +
				"&cuname=" + URLEncoder.encode(email, StandardCharsets.UTF_8) +
				"&owner=" +
				"&date=662394817" + // TODO: what is this?
				"&adate=" +
				ZonedDateTime.now(ZoneOffset.UTC).format(urlFormatter) + "z";

		try {
			URL url = URI.create(req).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			if(sessionId != null) {
				connection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
			}
			connection.connect();

			return connection.getInputStream();
		} catch (SocketTimeoutException e) {
			throw new IOException("Timeout on code submission; problem " + problem.id(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException unchecked(Throwable t) throws T {
		throw (T) t;
	}

	private Networking() {}
}
