import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class FiniteAutomata {

    private List<String> Q;
    private List<String> E;
    private String q0;
    private List<String> F;
    private Map<Pair<String, String>, ArrayList<String>> S;

    public FiniteAutomata() {
        this.S = new HashMap<>();
    }

    public boolean isDFA() {
        for (ArrayList<String> t : this.S.values()) {
            if (t.size() > 1) {
                return false;
            }
        }
        return true;
    }

    public boolean accept(String sequence) {
        String current_state = this.q0;
        char[] chars = sequence.toCharArray();
        for (char aChar : chars) {
            Pair<String, String> pair = new Pair<String, String>(current_state, "" + aChar);
            if (!this.S.containsKey(pair)) {
                return false;
            } else {
                current_state = this.S.get(pair).get(0);
            }
        }
        return true;
    }

    public void readFromFile() throws IOException {
        File file = new File("E:\\An III\\LFTC\\Labs\\Lab_07\\data\\fa.in");

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String q = reader.readLine();
        List<String> Q = Utils.split(q);
        this.Q = Utils.refactor(Q);

        String e = reader.readLine();
        List<String> E = Utils.split(e);
        this.E = Utils.refactor(E);

        String qo = reader.readLine();
        List<String> q0 = Utils.refactor(Utils.split(qo));
        this.q0 = q0.get(0);

        String f = reader.readLine();
        List<String> F = Utils.split(f);
        this.F = Utils.refactor(F);

        reader.readLine();

        String s;
        while ((s = reader.readLine()) != null) {
            List<String> T = Arrays.asList(s.split("->"));
            List<String> t = Arrays.asList(T.get(0).split(","));
            Pair<String, String> k = new Pair<>(t.get(0).strip(), t.get(1).strip());

            ArrayList<String> values = this.S.getOrDefault(k, null);
            if (values != null) {
                values.add(T.get(1).strip());
            } else {
                values = new ArrayList<>();
                values.add( T.get(1).strip());
                this.S.put(k, values);
            }
        }
    }

    public List<String> getQ() {
        return Q;
    }

    public List<String> getE() {
        return E;
    }

    public String getQ0() {
        return q0;
    }

    public List<String> getF() {
        return F;
    }

    public Map<Pair<String, String>, ArrayList<String>> getS() {
        return S;
    }
}


