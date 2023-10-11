import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String args[]){
        int cnt = 0;
        String championString = null;
        while(!StdIn.isEmpty()){
            String newString = StdIn.readString();
            if (StdRandom.bernoulli(1.0/(double)(++cnt))){
                championString = newString;
            }
        }
        System.out.println(championString);

    }
}
