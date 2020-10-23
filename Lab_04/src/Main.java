import utils.Utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        final String path = "src/input/";
        final String program = "p1";
        Scanner scanner = new Scanner();

        Utils.generateOutput(scanner.run(Paths.get(path + program + ".in")), program);
        List<String> STs = new ArrayList<>();
        STs.add("--- STs are represented using a HashTable ---");
        STs.add("--- STI ---");
        STs.add(scanner.getSTI().toString());
        STs.add("--- STC ---");
        STs.add(scanner.getSTC().toString());
        Utils.generateOutput(STs, "ST");
        List<String> PIF = new ArrayList<>();
        PIF.add("--- PIF ---");
        PIF.add(scanner.getPIF().toString());
        Utils.generateOutput(PIF, "PIF");
    }

}