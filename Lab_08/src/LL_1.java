
import java.util.*;

public class LL_1 {

    private Grammar grammar;
    private Map<String, List<String>> FIRST;
    private Map<String, List<String>> FOLLOW;

    public LL_1(Grammar grammar) {
        this.grammar = grammar;
        this.FIRST =  new HashMap<>();
        this.FOLLOW =  new HashMap<>();
    }

    public List<String> first(String nonTerminal) {

        List<String> terminals = grammar.getE();
        Map<String, List<List<String>>> productions = grammar.getP();

        List<List<String>> all = productions.get(nonTerminal);
        List<String> firsts = new ArrayList<>();
        if (this.FIRST.containsKey(nonTerminal)) {
            return this.FIRST.get(nonTerminal);
        } else {
            for (List<String> list : all) {

                String first = list.get(0);
                if (terminals.contains(first)) {
                    firsts.add(first);
                } else {
                    firsts.addAll(first(first));
                }
            }
        }
        return firsts;
    }

    public void FIRST() {

        List<String> nonTerminals = grammar.getN();
        List<String> terminals = grammar.getE();

        for (String terminal : terminals) {
            List<String> ft = Collections.singletonList(terminal);
            this.FIRST.put(terminal, ft);
        }

        for (String nonTerminal : nonTerminals) {
            this.FIRST.put(nonTerminal, first(nonTerminal));
        }
        System.out.println(this.FIRST);
    }

    public void FOLLOW() {

        List<String> nonTerminals = grammar.getN();

        for (String nonTerminal : nonTerminals) {
            this.FOLLOW.put(nonTerminal, follow(nonTerminal));
        }
        System.out.println(this.FOLLOW);
    }

    private List<String> follow(String nonTerminal) {
        return null;
    }

}
