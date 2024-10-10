package rdh.codingbat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

public final class Networking {
	public static final String USER_AGENT = "CodingBatSpammer/1.0 (https://github.com/rhysdh540/CodingBat - sorry for any problems!)";

	public static URL makeUrl(int problemNumber) {
		try {
			return URI.create("https://codingbat.com/prob/p" + formatProblemNumber(problemNumber)).toURL();
		} catch (MalformedURLException e) {
			throw unchecked(e);
		}
	}

	public static String formatProblemNumber(int problemNumber) {
		if(problemNumber < 1 || problemNumber >= 1000000) {
			throw new IllegalArgumentException("Problem number must be between 1 and 999999");
		}
		String s = Integer.toString(problemNumber);
		char[] c = new char[6 - s.length()];
		Arrays.fill(c, '0');
		return new String(c) + s;
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
			System.err.println("\nTimeout on problem " + problemNumber);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException unchecked(Throwable t) throws T {
		throw (T) t;
	}

	private Networking() {}
}
