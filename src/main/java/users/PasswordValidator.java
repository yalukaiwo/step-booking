package users;

public class PasswordValidator {
    // UPDATE: CHANGED ALL HELPER METHODS TO PRIVATE
    public static boolean checkPassword(String s){
     return length(s) && CapitalLetters(s)
               && SmallLetters(s) && Symbols(s)
               && Numbers(s);
    }

    private static boolean length(String string){
        return string.length()>=8;
    }

    private static boolean Capital(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private  static boolean Small(char c) {
        return c >= 'a' && c <= 'z';
    }

    private static boolean Number(char c) {
        return c >= '0' && c <= '9';
    }

    private static  boolean Symbol(char c) {
        return c == ')' ||c == '(' || c == '$' || c=='&' || c=='*' || c=='!' ;
    }

    private static boolean SmallLetters(String str){
        for (Character c: str.toCharArray()) {
            if(Small( c)) return  true;
        }
        return  false;
    }

    private static boolean CapitalLetters(String str){
        for (Character c: str.toCharArray()) {
            if(Capital( c)) return  true;
        }
        return  false;
    }

    private static boolean Symbols(String str){
        for (Character c: str.toCharArray()) {
            if(Symbol(c)) return  true;
        }
        return  false;
    }

    private static boolean Numbers(String str){
        for (Character c: str.toCharArray()) {
            if(Number( c)) return  true;
        }
        return  false;
    }
}
