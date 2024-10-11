package rdh.codingbat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;

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
