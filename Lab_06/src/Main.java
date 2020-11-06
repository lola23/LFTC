import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    static FiniteAutomata fa = new FiniteAutomata();

    public static void main(String[] args) throws IOException {

        fa.readFromFile();

        while (true) {
            printMenu();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Option: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1": {
                    printSetOfStates();
                    break;
                }
                case "2": {
                    printAlphabet();
                    break;
                }
                case "3": {
                    printInitialState();
                    break;
                }
                case "4": {
                    printFinalStates();
                    break;
                }
                case "5": {
                    printTransitions();
                    break;
                }
                case "0": {
                    return;
                }
                default: {
                    System.out.println("Wrong option");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("------------ MENU ------------");
        String menu = "";
        menu += "1. Display set of states\n";
        menu += "2. Display alphabet\n";
        menu += "3. Display initial state\n";
        menu += "4. Display set of final states\n";
        menu += "5. Display all transitions\n";
        menu += "0. Exit";
        System.out.println(menu);
        System.out.println("------------------------------");
    }

    private static void printSetOfStates() {
        System.out.println("Q: " + fa.getQ());
    }

    private static void printAlphabet() {
        System.out.println("E: " + fa.getE());
    }

    private static void printInitialState() {
        System.out.println("q0: " + fa.getQ0());
    }

    private static void printFinalStates() {
        System.out.println("F: " + fa.getF());
    }

    private static void printTransitions() {
        System.out.println("S: ");
        for (Pair<String, String> p : fa.getS().keySet()) {
            String s1 = p.getKey();
            String r = p.getValue();
            ArrayList<String> s2 = fa.getS().get(p);
            System.out.println("\t" + "(" + s1 + ", " + r + ") -> " + s2);
        }
    }

}