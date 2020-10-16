import java.util.ArrayList;

public class SymbolTable {

    private HashTable hashTable;

    public SymbolTable() {
        this.hashTable = new HashTable();
    }

    public ArrayList<String> search(String value) {
        return this.hashTable.search(hash(value));
    }

    public void add(String value) {
        this.hashTable.add(value);
    }

    public ArrayList<Integer> getId(String value) {
        return this.hashTable.getId(value);
    }

    public int hash(String value) {
        return this.hashTable.hash(value);
    }
}
