import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {

     static Grammar grammar = new Grammar();
     static LL_1 ll1 = new LL_1(grammar);

     public static void main(String[] args) throws IOException {

          grammar.readFromFile();
          ll1.FIRST();
          ll1.FOLLOW();
          ll1.generateParseTable();

          List<String> seq = new ArrayList<>();
          List<String> p1 = new ArrayList<>();
          List<String> p2 = new ArrayList<>();
          List<String> p3 = new ArrayList<>();

          readFromSeq(seq);
          readFromPIF(p1, p2, p3);

          while (true) {
               printMenu();

               Scanner scanner = new Scanner(System.in);
               System.out.print("Option: ");

               String option = scanner.nextLine();

               switch (option) {
                    case "1": {
                         printNonTerminals();
                         break;
                    }
                    case "2": {
                         printTerminals();
                         break;
                    }
                    case "3": {
                         printProductions();
                         break;
                    }
                    case "4": {
                         String nonTerminal = scanner.nextLine();
                         printProductionsForANonTerminal(nonTerminal.trim());
                         break;
                    }
                    case "5": {
                         System.out.println(ll1.getParseTable());
                         break;
                    }
                    case "6": {
                         List<String> w = Arrays.asList(scanner.nextLine().replace("\n", "").split(" "));
                         parseSequence(w);
                         break;
                    }
                    case "7": {
                         parseSequence(seq);
                         break;
                    }
                    case "8": {
                         System.out.println("Select problem");
                         String menu = "";
                         menu += "1. p1\n";
                         menu += "2. p2\n";
                         menu += "3. p3\n";
                         System.out.println(menu);
                         String p = scanner.nextLine();
                         switch (p) {
                              case "1": {
                                   parseSequence(p1);
                                   break;
                              }
                              case "2": {
                                   parseSequence(p2);
                                   break;
                              }
                              case "3": {
                                   parseSequence(p3);
                                   break;
                              }
                              default: {
                                   return;
                              }
                         }
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

     private static void readFromSeq(List<String> seq) {
          try {
               File fileSeq = new File("seq.in");
               Scanner myReader = new Scanner(fileSeq);
               String data = myReader.nextLine();
               String[] sequence = data.split(" ");
               seq.addAll(Arrays.asList(sequence));
          } catch (FileNotFoundException e) {
               System.out.println("An error occurred.");
               e.printStackTrace();
          }
     }

     public static void readFromPIF(List<String> p1, List<String> p2, List<String> p3) {
          try {
               File filePifOut = new File("pif.out");
               Scanner myReader = new Scanner(filePifOut);
               String data = myReader.nextLine();
               String[] sequence = data.split(" ");
               p1.addAll(Arrays.asList(sequence));
               data = myReader.nextLine();
               sequence = data.split(" ");
               p2.addAll(Arrays.asList(sequence));
               data = myReader.nextLine();
               sequence = data.split(" ");
               p3.addAll(Arrays.asList(sequence));
               myReader.close();
          } catch (FileNotFoundException e) {
               System.out.println("An error occurred.");
               e.printStackTrace();
          }
     }

     public static void parseSequence(List<String> w) {
          boolean result = ll1.parse(w);
          if (result) {
               Stack<String> pi = ll1.getPi();
               System.out.println(pi);
               System.out.println(displayProductions(pi));
               System.out.println("Sequence " + w + " accepted.");
          } else {
               Stack<String> pi = ll1.getPi();
//            System.out.println(pi);
//            System.out.println(displayProductions(pi));
               System.out.println("Sequence " + w + " is not accepted.");
          }
     }

     private static String displayProductions(Stack<String> pi) {
          StringBuilder sb = new StringBuilder();

          for (String productionIndexString : pi) {
               if (productionIndexString.equals("~")) {
                    continue;
               }
               Integer productionIndex = Integer.parseInt(productionIndexString);
               ll1.getProductionsNumbered().forEach((key, value) -> {
                    if (productionIndex.equals(value))
                         sb.append(value).append(": ").append(key.getKey()).append(" -> ").append(key.getValue()).append("\n");
               });
          }

          return sb.toString();
     }

     private static void printMenu() {
          System.out.println("------------ MENU ------------");
          String menu = "";
          menu += "1. Display non-terminals\n";
          menu += "2. Display terminals\n";
          menu += "3. Display all productions\n";
          menu += "4. Display all productions for a non-terminal\n";
          menu += "5. Display parse table\n";
          menu += "6. Parse sequence\n";
          menu += "7. Parse seq.in for g1.in\n";
          menu += "8. Parse pif.out for g2.in\n";
          menu += "0. Exit";
          System.out.println(menu);
          System.out.println("------------------------------");
     }

     private static void printNonTerminals() {
          System.out.println("N: " + grammar.getN());
     }

     private static void printTerminals() {
          System.out.println("E: " + grammar.getE());
     }

     private static void printProductions() {
          System.out.println("P: ");
          for (String key : grammar.getP().keySet()) {
               System.out.println(buildString(key));
          }
     }

     private static void printProductionsForANonTerminal(String nonTerminal) {
          System.out.println("Productions for the non-terminal:");
          System.out.println(buildString(nonTerminal));
     }

     private static String buildString(String key) {
          List<List<String>> value = grammar.getP().get(key);
          if (value != null) {
               StringBuilder all = new StringBuilder();
               for (List<String> list : value) {
                    for (String s : list) {
                         all.append(s);
                    }
                    all.append(" | ");
               }
               return "\t" + key + " -> " + all.substring(0, all.length() - 2);
          } else return "NonTerminal not found\n";
     }
}
