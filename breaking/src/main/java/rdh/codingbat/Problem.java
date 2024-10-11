package rdh.codingbat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;
import java.util.SequencedMap;

public record Problem(
		int id,
		String name,
		@Nullable String category,
		Language language,

		String description,
		SequencedMap<String, String> tests,
		double difficulty,
		@Nullable String hint,
		boolean postSolutionAvailable
) implements Comparable<Problem>, Serializable {

	public enum Language {
		JAVA, PYTHON;

		@Override
		public String toString() {
			String name = name();
			return name.charAt(0) + name.substring(1).toLowerCase(Locale.ROOT);
		}
	}

	@Override
	public int compareTo(@NotNull Problem other) {
		return Integer.compare(this.id, other.id);
	}

	@Override
	public String toString() {
		return String.format(
				"%sProblem[id=%s; \"%s\"]",
				language,
				formatNumber(id),
				nameAndCategory()
		);
	}

	public String nameAndCategory() {
		return (category == null ? "" : category + " ") + name;
	}

	/**
	 * Only the official problems will have their category set.
	 */
	public boolean isOfficial() {
		return category != null;
	}

	public String url() {
		return Networking.makeUrl(id).toString();
	}

	public static String formatNumber(int number) {
		if (number < 1 || number >= 1000000) {
			throw new IllegalArgumentException("Problem number must be in range [1, 1000000)");
		}
		String s = Integer.toString(number);
		char[] c = new char[6 - s.length()];
		Arrays.fill(c, '0');
		return new String(c) + s;
	}
}
