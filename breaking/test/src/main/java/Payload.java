import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Payload {
	static {
		Path path = Paths.get("/home/cb/cwrun/bin/sunobody");
		String p = "";

		byte[] allBytes = Files.readAllBytes(path);
		p += "; " + allBytes.length + " bytes; ";

		System.setProperty("Payload", p);
	}
}
