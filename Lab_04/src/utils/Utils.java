package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Utils {

    public static String removeWhitespaces(String line) {
        return line.trim().replaceAll(" {2,}", " ");
    }

    public static void generateOutput(List<String> result, String program) throws IOException {
        Files.write(Paths.get("src/output/" + program + ".out"), result);
    }
}
