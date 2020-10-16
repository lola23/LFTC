import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.pow;

public class HashTable {

    private Map<Integer, ArrayList<String>> table;

    public HashTable() {
        this.table = new HashMap<>();
    }

    // Returns the info associated with the key
    public ArrayList<String> search(int key) {
        return table.getOrDefault(key, null);
    }

    // Adds an element to the corresponding slot
    public void add(String value) {

        int key = this.hash(value);
        ArrayList<String> values = this.search(key);
        if (values != null && !values.contains(value)) {
            values.add(0, value);
        } else if (values == null) {
            values = new ArrayList<>();
            values.add(value);
            table.put(key, values);
        }
    }

    // Returns they key and the position the element is in the corresponding list to the key | null
    public ArrayList<Integer> getId(String value) {
        int key = this.hash(value);
        ArrayList<String> values = search(key);
        if (values != null && values.contains(value)) {
                ArrayList<Integer> pair = new ArrayList<>();
                pair.add(0, key);
                pair.add(1, value.indexOf(value));
                return pair;
        }
        return null;
    }

    // Hash function used
    public int hash(String value) {
        int sum = 0;
        int n = value.length();
        for (int i = 0; i < n; i++) {
            sum += value.charAt(i);
        }
        return sum / n;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for( int key : table.keySet()) {
            s.append(key).append(' ').append(search(key)).append("\n");
        }
        return s.toString();
    }
}
