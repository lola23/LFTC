import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grammar {

    private List<String> N;                     // non-terminals
    private List<String> E;                     // terminals
    private String S;                           // start
    private Map<String, List<List<String>>> P;  // productions

    public Grammar() {
        this.P = new HashMap<>();
    }

    public void readFromFile() throws IOException {
        File file = new File("grammar_test_1.in");

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String q = reader.readLine();
        this.N = Utils.trim(Utils.split(q));

        String e = reader.readLine();
        this.E = Utils.trim(Utils.split(e));

        this.S = reader.readLine();

        String p;
        while ((p = reader.readLine()) != null) {
            List<String> T = Arrays.asList(p.split("->"));
            String k = T.get(0).trim();
            List<String> all = Utils.trim(Arrays.asList(T.get(1).strip().split("\\|")));
            List<List<String>> splitted = new ArrayList<>();
            for (String s : all) {
                splitted.add(Utils.trim(Arrays.asList(s.split(" "))));
            }
            List<List<String>> values = this.P.getOrDefault(k, null);
            if (values != null) {
                values.addAll(splitted);
            } else {
                values = new ArrayList<>(splitted);
                this.P.put(k, values);
            }
        }
    }

    public List<String> getN() {
        return N;
    }

    public List<String> getE() {
        return E;
    }

    public String getS() {
        return S;
    }

    public Map<String, List<List<String>>> getP() {
        return P;
    }

    public  Map<String, List<List<String>>> getRhsProductions(String nonTerminal) {
        Map<String, List<List<String>>> P = new HashMap<>();

        for (String key : this.P.keySet()) {
            List<List<String>> values = this.P.get(key);
            List<List<String>> containingLists = new ArrayList<>();
            for (List<String> list : values) {
                if (list.contains(nonTerminal)) {
                    containingLists.add(list);
                }
            }
            if (!containingLists.isEmpty()) {
                P.put(key, containingLists);
            }
        }
        return  P;
    }

    public Map<String, List<List<String>>> getProductionsInWhichNTIsOnTheRight(String nt) {
        Map<String, List<List<String>>> result = new HashMap<>();
        P.forEach((k,v) -> {
            for(List<String> productionRule: v) {
                if(productionRule.contains(nt)) {
                    List<List<String>> tempResult = result.getOrDefault(k, new ArrayList<>());
                    tempResult.add(productionRule);
                    result.put(k, tempResult);
                }
            }
        });
        return result;
    }
}
