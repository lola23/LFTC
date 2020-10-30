import java.util.*;
import java.util.regex.Pattern;

public class Language {

    public static List<String> operators = Arrays.asList("+", "-", "*", "/", "%", ">", ">=", "<", "<=", "=", "!=","and", "or", "not", "is");
    public static List<String> separators = List.of("(", ")", "[", "]", "{", "}", ";", ",", " ", "\n", "\t");
    public static List<String> reservedWords = Arrays.asList(
            "begin_appy",
            "end_appy",
            "ify",
            "elseify",
            "elsy",
            "loopy",
            "to",
            "sparkle",
            "inty",
            "booly",
            "arry"
    );

    public static boolean isOperator(String s) {
        return operators.contains(s);
    }

    public static boolean isSeparator(String s) {
        return separators.contains(s);
    }

    public static boolean isReservedWord(String s) {
        return reservedWords.contains(s);
    }

    public static boolean isIdentifier(String s){
        Pattern pattern = Pattern.compile("[A-Za-z][A-Za-z0-9]*");
        if (s.length() > 8) {
            return false;
        }
        return (pattern.matcher(s).matches());
    }

    public static boolean isConstant(String s) {
        return (isBoolean(s) || isInteger(s));
    }

    public static boolean isInteger(String s) {
        Pattern pattern = Pattern.compile("[-]?([0]|[1-9][0-9]*)");
        return (pattern.matcher(s).matches());
    }

    public static boolean isBoolean(String s) {
        Pattern pattern = Pattern.compile("true|false");
        return (pattern.matcher(s).matches());
    }

}
