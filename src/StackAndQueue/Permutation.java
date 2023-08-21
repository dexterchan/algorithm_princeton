package StackAndQueue;
import StackAndQueue.RandomizedQueue;

import java.util.Iterator;
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

public class Permutation {
    public static void main(String[] args){
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);

        try {
            while (!StdInput.isEmpty()) {
                String read_str = StdInput.readString();
                String[] words = read_str.split(" ");
                for (String w : words){
                    if (queue.size()>=k)
                        break;
                    queue.enqueue(w);
                }
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
