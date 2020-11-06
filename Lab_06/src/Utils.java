import java.util.Arrays;
import java.util.List;

public class Utils {

    public static List<String> split(String s) {
        return Arrays.asList(s.strip().split(" "));
    }

    public static List<String> refactor(List<String> s) {
        return s.subList(2, s.size());
    }
}
