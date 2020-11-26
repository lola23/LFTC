
import java.util.*;
import java.util.stream.Collectors;

public class LL_1 {

    private Grammar grammar;
    private Map<String, List<String>> FIRST;
    private Map<String, Set<String>> FOLLOW;

    public LL_1(Grammar grammar) {
        this.grammar = grammar;
        this.FIRST =  new HashMap<>();
        this.FOLLOW =  new HashMap<>();
    }

    public List<String> first(String nonTerminal) {

        List<String> terminals = grammar.getE();
        Map<String, List<List<String>>> productions = grammar.getP();

        List<List<String>> all = productions.get(nonTerminal);

        if(all == null)
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

        Map <String, Set<String>> previousIteration = new HashMap<>();
        Map <String, Set<String>> currentIteration;
        List<String> nonTerminals = grammar.getN();
        for(String n: nonTerminals)
            previousIteration.put(n, new HashSet<>());
        previousIteration.get(grammar.getS()).add("~");
        currentIteration = copy(previousIteration);


        while(true) {
            for(String n: nonTerminals) {
                //we get a dictionary with the productions that have n in the rhs
                Map<String, List<List<String>>> productions = grammar.getProductionsInWhichNTIsOnTheRight(n);
                for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
                    for(List<String> prod: entry.getValue()) {
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
                        if(index == prod.size()) {
                            currentIteration.get(n).addAll(previousIteration.get(entry.getKey()));
                        }
                    }
                }
            }
            if(currentIteration.equals(previousIteration))
                break;
            else {
                previousIteration = copy(currentIteration);
            }
        }
        this.FOLLOW = currentIteration;
        System.out.println(this.FOLLOW);
    }


    public static HashMap<String, Set<String>> copy(Map<String, Set<String>> original)
    {
        HashMap<String, Set<String>> copy = new HashMap<>();
        for (Map.Entry<String , Set<String>> entry : original.entrySet())
        {
            Set<String> temp = new HashSet<>();
            temp.addAll(entry.getValue());
            copy.put(entry.getKey(), temp);
        }
        return copy;
    }
}
