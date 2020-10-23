import domain.Pair;
import domain.Pif;
import domain.SymbolTable;
import utils.IllegalNameException;
import utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Scanner {

    private static final String TOKENS_IN = "src/input/tokens.in";
    private static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    private List<String> codes;
    private SymbolTable STI;
    private SymbolTable STC;
    private Pif PIF;

    public Scanner() {
        codes = new ArrayList<>();
        STI = new SymbolTable();
        STC = new SymbolTable();
        PIF = new Pif();
        this.getCodes();
    }

    public Pif getPIF() {
        return PIF;
    }

    public SymbolTable getSTI() {
        return STI;
    }

    public SymbolTable getSTC() {
        return STC;
    }

    public List<String> run(Path path) {
        // prepare list of potential errors
        List<String> errors = new ArrayList<>();

        // parse each line of the file
        int lineNr = 1;
        try {
            for (String line : Files.readAllLines(path)) {
                line = Utils.removeWhitespaces(line);

                if (!line.equals("")) {

                    // tokenize line
                    ArrayList<String> tokens = new ArrayList<>(Arrays.asList(line.split(" ")));
                    ArrayList<Integer> specialSeparators = new ArrayList<>(Arrays.asList(17, 18, 19, 20, 21, 22, 31, 33));
                    for (int i = 17; i < codes.size(); i++) {
                        String separator = codes.get(i);
                        if (specialSeparators.contains(i)) {
                            separator = "\\" + codes.get(i);
                        }
                        tokens = this.tokenizeLine(tokens, separator);
                    }

                    // identify tokens and log exceptions
                    for (String token : tokens) {
                        try {
                            identify(token);
                        }  catch (IllegalNameException e) {
                            errors.add("Line " + lineNr + ": Illegal token \"" + token + "\".");
                        }
                    }
                }
                lineNr++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errors.size() == 0) {
            errors.add("Lexically correct");
        }

        return errors;
    }

    private ArrayList<String> tokenizeLine(ArrayList<String> tokens, String separator) {
        ArrayList<String> newTokens = new ArrayList<>();
        int i = 0;
        for(String token : tokens) {
            if (token.equals("-") && Language.isInteger(tokens.get(i + 1))) {
                newTokens.add("-" + tokens.get(i + 1));
            } else if (i != 0 && tokens.get(i - 1).equals("-") && Language.isInteger(token)) { continue; }
            else
                {
                if (!token.equals(">=") && !token.equals("<=") && !token.equals("!=")) {
                    newTokens.addAll(Arrays.asList(token.split(String.format(WITH_DELIMITER, separator))));
                }
                else {
                    newTokens.add(token);
                }
            }
            i++;
        }
        return newTokens;
    }

    private void identify(String token) throws IllegalNameException {
        if (codes.contains(token)) {    // if separator/operator/reserved
            addToPIF(token);
        } else {
            if (Language.isConstant(token)) {
                addToST(token, STC);
            } else {
                if (Language.isIdentifier(token)) {
                    addToST(token, STI);
                } else {
                    throw new IllegalNameException(token);
                }
            }
            addToPIF(token);
        }
    }

    private void addToPIF(String token) {
        Pair<Integer, Integer> def = new Pair<>(-1, -1);
        if (Language.isReservedWord(token) || Language.isOperator(token) || Language.isSeparator(token)) {
            this.PIF.add(new Pair<>(codes.indexOf(token), def));
        }
        else {
            if (Language.isConstant(token)) {
                this.PIF.add(new Pair<>(0, STC.getId(token)));
            }
            else if (Language.isIdentifier(token)) {
                this.PIF.add(new Pair<>(1, STI.getId(token)));
            }
            else {
                System.out.println("Lexical Error "+ token + " is not defined.");
            }
        }
    }

    private void addToST(String token, SymbolTable ST) {
        if (ST.getId(token) == null) {
            ST.add(token);
        }
    }

    private void getCodes() {
        try {
            codes.addAll(Files.readAllLines(Paths.get(TOKENS_IN)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
