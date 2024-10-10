package rdh.codingbat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
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
				Networking.formatProblemNumber(id),
				nameAndCategory()
		);
	}

	public String nameAndCategory() {
		return (category == null ? "" : category + " ") + name;
	}

	public boolean isOfficial() {
		return category != null;
	}

	public String url() {
		return Networking.makeUrl(id).toString();
	}
}
