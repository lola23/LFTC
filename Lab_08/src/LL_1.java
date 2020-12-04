
import javafx.util.Pair;

import java.util.*;


public class LL_1 {

    private Grammar grammar;
    private Map<String, List<String>> FIRST;
    private Map<String, Set<String>> FOLLOW;
    private Map<Pair<String, String>, Pair<List<String>, Integer>> parseTable;
    private Map<Pair<String, List<String>>, Integer> numberedProductions = new HashMap<>();
    private Stack<String> alpha = new Stack<>();
    private Stack<String> beta = new Stack<>();
    private Stack<String> pi = new Stack<>();

    public LL_1(Grammar grammar) {
        this.grammar = grammar;
        this.FIRST = new HashMap<>();
        this.FOLLOW = new HashMap<>();
        this.parseTable = new HashMap<>();
    }

    public Stack<String> getPi() {
        return pi;
    }

    public List<String> first(String nonTerminal) {

        List<String> terminals = grammar.getE();
        Map<String, List<List<String>>> productions = grammar.getP();

        List<List<String>> all = productions.get(nonTerminal);

        if (all == null)
            return Collections.singletonList(nonTerminal);

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

        Map<String, Set<String>> previousIteration = new HashMap<>();
        Map<String, Set<String>> currentIteration;
        List<String> nonTerminals = grammar.getN();
        for (String n : nonTerminals)
            previousIteration.put(n, new HashSet<>());
        previousIteration.get(grammar.getS()).add("$");
        currentIteration = copy(previousIteration);


        while (true) {
            for (String n : nonTerminals) {
                //we get a dictionary with the productions that have n in the rhs
                Map<String, List<List<String>>> productions = grammar.getProductionsInWhichNTIsOnTheRight(n);
                for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
                    for (List<String> prod : entry.getValue()) {
                        //for each production that has n in rhs we go from n to the end
                        int index = prod.indexOf(n) + 1;
                        for (int i = index; i < prod.size(); i++) {
                            //we add all first of what is after n
                            List<String> first = first(prod.get(i));
                            currentIteration.get(n).addAll(first);
                            //if first contains ~ then we don't add ~ to current iteration, but add all elems from prev iteration of lhs
                            if (first.contains("~")) {
                                currentIteration.get(n).remove("~");
                                currentIteration.get(n).addAll(previousIteration.get(entry.getKey()));
                            }
                        }
                        //if nothing follows n then we add everything from prev iteration of the lhs
                        if (index == prod.size()) {
                            currentIteration.get(n).addAll(previousIteration.get(entry.getKey()));
                        }
                    }
                }
            }
            if (currentIteration.equals(previousIteration))
                break;
            else {
                previousIteration = copy(currentIteration);
            }
        }
        this.FOLLOW = currentIteration;
        System.out.println(this.FOLLOW);
    }

    public static HashMap<String, Set<String>> copy(Map<String, Set<String>> original) {
        HashMap<String, Set<String>> copy = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : original.entrySet()) {
            Set<String> temp = new HashSet<>(entry.getValue());
            copy.put(entry.getKey(), temp);
        }
        return copy;
    }

    public void numberProductions() {
        int index = 1;
        for (Map.Entry<String, List<List<String>>> entry : grammar.getP().entrySet()) {
            for (List<String> rule : entry.getValue())
                numberedProductions.put(new Pair<>(entry.getKey(), rule), index++);
        }
    }
    
    public void generateParseTable() {
        numberProductions();

        List<String> columnSymbols = new LinkedList<>(grammar.getE());
        columnSymbols.add("$");

        // M(a, a) = pop
        // M($, $) = acc

        parseTable.put(new Pair<>("$", "$"), new Pair<>(Collections.singletonList("acc"), -1));
        for (String terminal: grammar.getE())
            parseTable.put(new Pair<>(terminal, terminal), new Pair<>(Collections.singletonList("pop"), -1));

//        1) M(A, a) = (α, i), if:
//            a) a ∈ first(α)
//            b) a != ~
//            c) A -> α production with index i
//
//        2) M(A, b) = (α, i), if:
//            a) ~ ∈ first(α)
//            b) whichever b ∈ follow(A)
//            c) A -> α production with index i

        numberedProductions.forEach((key, value) -> {
            String rowSymbol = key.getKey();
            List<String> rule = key.getValue();
            Pair<List<String>, Integer> parseTableValue = new Pair<>(rule, value);

            for (String columnSymbol : columnSymbols) {
                Pair<String, String> parseTableKey = new Pair<>(rowSymbol, columnSymbol);

                // if our column-terminal is exactly first of rule
                if (rule.get(0).equals(columnSymbol) && !columnSymbol.equals("~"))
                    parseTable.put(parseTableKey, parseTableValue);

                    // if the first symbol is a non-terminal and it's first contain our column-terminal
                else if (grammar.getN().contains(rule.get(0)) && FIRST.get(rule.get(0)).contains(columnSymbol)) {
                    if (!parseTable.containsKey(parseTableKey)) {
                        parseTable.put(parseTableKey, parseTableValue);
                    }
                }
                else {
                    // if the first symbol is ~ then everything if FOLLOW(rowSymbol) will be in parse table
                    if (rule.get(0).equals("~")) {
                        for (String b : FOLLOW.get(rowSymbol))
                            parseTable.put(new Pair<>(rowSymbol, b), parseTableValue);

                    // if ~ is in FIRST(rule)
                    } else {
                        Set<String> firsts = new HashSet<>();
                        for (String symbol : rule)
                            if (grammar.getN().contains(symbol))
                                firsts.addAll(FIRST.get(symbol));
                        if (firsts.contains("~")) {
                            for (String b : FIRST.get(rowSymbol)) {
                                if (b.equals("~"))
                                    b = "$";
                                parseTableKey = new Pair<>(rowSymbol, b);
                                if (!parseTable.containsKey(parseTableKey)) {
                                    parseTable.put(parseTableKey, parseTableValue);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public boolean parse(List<String> w) {
        alpha.clear();
        alpha.push("$");
        pushAll(w, alpha);

        beta.clear();
        beta.push("$");
        beta.push(grammar.getS());

        pi.clear();
        pi.push("~");

        boolean go = true;
        boolean result = true;

        while (go) {
            String betaHead = beta.peek();
            String alphaHead = alpha.peek();

            if (betaHead.equals("$") && alphaHead.equals("$")) {
                return result;
            }

            Pair<String, String> heads = new Pair<>(betaHead, alphaHead);
            Pair<List<String>, Integer> parseTableEntry = parseTable.get(heads);

            if (parseTableEntry == null) {
                heads = new Pair<>(betaHead, "~");
                parseTableEntry = parseTable.get(heads);
                if (parseTableEntry != null) {
                    beta.pop();
                    continue;
                }
            }

            if (parseTableEntry == null) {
                go = false;
                result = false;
            } else {
                List<String> production = parseTableEntry.getKey();
                Integer productionPos = parseTableEntry.getValue();

                if (productionPos == -1 && production.get(0).equals("acc")) {
                    go = false;
                } else if (productionPos == -1 && production.get(0).equals("pop")) {
                    beta.pop();
                    alpha.pop();
                } else {
                    beta.pop();
                    if (!production.get(0).equals("~")) {
                        pushAll(production, beta);
                    }
                    pi.push(productionPos.toString());
                }
            }
        }

        return result;
    }

    private void pushAll(List<String> sequence, Stack<String> stack) {
        for (int i = sequence.size() - 1; i >= 0; i--) {
            stack.push(sequence.get(i));
        }
    }

    public boolean containsKey(Pair<String, String> key) {
        boolean result = false;
        for (Pair<String, String> currentKey : parseTable.keySet()) {
            if (currentKey.getKey().equals(key.getKey()) && currentKey.getValue().equals(key.getValue())) {
                result = true;
            }
        }

        return result;
    }

    public String getParseTable() {
        StringBuilder s = new StringBuilder();
        parseTable.forEach((k, v) -> {
            s.append(k.toString()).append(" -> ").append(v.toString()).append('\n');
        });
        return s.toString();
    }

    public Map<Pair<String, List<String>>, Integer> getProductionsNumbered() {
        return numberedProductions;
    }
}
