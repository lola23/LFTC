package domain;

import java.util.ArrayList;
import java.util.List;

public class Pif {

    private List<Pair<Integer, Pair<Integer, Integer>>> info;

    public Pif() {
        this.info = new ArrayList<>();
    }

    public List<Pair<Integer, Pair<Integer, Integer>>> getInfo() {
        return info;
    }

    public void add(Pair<Integer, Pair<Integer, Integer>> item) {
        this.info.add(item);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for( Pair<Integer, Pair<Integer, Integer>> item : info) {
            s.append(item.toString()).append("\n");
        }
        return s.toString();
    }
}
