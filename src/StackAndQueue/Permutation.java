package StackAndQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args){
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);

        try {
            while (!StdIn.isEmpty()) {
                String read_str = StdIn.readString();
                queue.enqueue(read_str);

            }
        }catch (NoSuchElementException ne){
        }finally {
            int cnt = 0;
            Iterator<String> itr = queue.iterator();
            while (cnt < k && itr.hasNext()){
                System.out.println(itr.next());
                cnt++;
            }

        }
    }
}
