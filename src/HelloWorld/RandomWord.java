package HelloWorld;
//import StdIn
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

class StdInput {
    static Scanner in = new Scanner(System.in);

    public static boolean isEmpty(){
        return !in.hasNextLine();
    }

    public static String readString(){
        return  in.next();
    }

}

public class RandomWord {
    public static void main(String[] args){
        //Read words from standard input
        //and print a random one.
        var words = new ArrayList<String>();

        try {
            while (!StdInput.isEmpty()) {
                words.add(StdInput.readString());
            }
        }catch (NoSuchElementException ne){
        }finally {
            var k = (int)(Math.random() * words.size());
            System.out.println(words.get(k));

        }
    }
}
