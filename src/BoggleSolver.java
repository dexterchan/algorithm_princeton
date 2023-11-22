import edu.princeton.cs.algs4.In;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BoggleSolver {
    static class Tries<Value> {
        private static final int NUM_CHR = 26;
        private final Node node = new Node(null);

        private final Pattern pattern = Pattern.compile("^[A-Z]+$");

        private final static class Node {
            private Object value;
            private final Node[] next;

            private Node(Object value) {
                this.value = value;
                this.next = new Node[NUM_CHR];
            }

            private Node(){
                this(null);
            }

            public Object getValue() {
                return this.value;
            }

            public void setValue(Object value){
                this.value = value;
            }

            public boolean isNUll() {
                return this.value == null;
            }

            public Node getNode(int i) {
                if (!isValidNodeBoundary(i)) throw new IllegalArgumentException("INvalid boundary "+i);
                return this.next[i];
            }

            public void setNode(Node node, int i) {
                if (!isValidNodeBoundary(i)) throw new IllegalArgumentException("index i is " + i);
                this.next[i] = node;
            }

            private static boolean isValidNodeBoundary(int i) {
                if (i < 0 || i >= NUM_CHR)
                    return false;
                return true;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();

                int entries = 0;
                for (int i = 0; i < this.next.length; i++) {
                    if (this.next[i] != null) {
                        sb.append(",");
                        sb.append("\"");
                        sb.append((char) ('A' + i));
                        sb.append("\":");
                        sb.append(this.next[i].toString());
                        entries++;
                    }
                }
                if (entries>0){
                    sb.delete(0,1);
                }

                if (this.value != null) {
                    sb.append("\"value\":");
                    sb.append("\"");
                    sb.append(this.value);
                    sb.append("\"");
                    sb.insert(0, "{");
                    sb.append("}");
                }

                if (entries == 0) return sb.toString();

                sb.insert(0, "{");
                sb.append("}");

                return sb.toString();
            }
        }

        public void put(String key, Value value) {
            Matcher m = pattern.matcher(key);
            if (!m.matches()) throw new IllegalArgumentException("Invalid key A-Z");
            recursivePutTries(this.node, key, value, key.length());

        }

        private static void assignNewChildNode(Node node, String key, int at){
            int nextNodeIndex = getNextNodeIndex(key, at);
            node.setNode(new Node(), nextNodeIndex);
        }
        private static <Value> void recursivePutTries(Node node, String key, Value value, int remain) {
            if (remain == 0) {
                node.setValue(value);
                return;
            };

            int at = key.length() - remain;
            int nodeIndex = getNextNodeIndex(key, at);
            Node childNode = node.getNode(nodeIndex);
            if (childNode == null){
                assignNewChildNode(node, key, at);
                childNode = node.getNode(nodeIndex);
            }
            int new_remain = remain - getNodeLength(key, at);

            recursivePutTries(childNode, key, value, new_remain);

        }

        private static int getNodeLength(String key, int at) {
            if (!isValidateKeyAt(key, at)) throw new IllegalArgumentException();
            if (key.charAt(at) == 'Q') return 2;
            return 1;
        }

        private static int getNextNodeIndex(String key, int at) {
            int c = (int) (key.charAt(at));
            return c - (int) 'A';
        }

        private static boolean isValidateKeyAt(String key, int at) {
            if (key == null || key.isEmpty()) return false;
            char c = key.charAt(at);
            if (c != 'Q') return true;

            //last character is Q
            if (key.length() - 1 == at) throw new IllegalArgumentException("Q is last character");
            if (key.charAt(at + 1) != 'U') throw new IllegalArgumentException("Q should follow U");
            return true;
        }

        public String toString() {
            return this.node.toString();
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        return null;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return -1;
    }

    public static void main(String[] args) {
        testTries(args[0]);
        testBoardRead();
    }

    static void testTries(String fileName){
        Tries<String> tries = new Tries<>();
        tries.put("ABC", "ABC_value");
        tries.put("BBA", "BBA_value");
        tries.put("BBD", "BBD_value");
        tries.put("BBD", "BBD2_value");
        System.out.println(tries);

        Tries<String> testTries = new Tries<>();
        In fileStream = new In(fileName);
        String[] dictionary = fileStream.readAllStrings();
        for (String s: dictionary){
            testTries.put(s, s);
        }
    }

    static void testBoardRead(){
        BoggleBoard board = new BoggleBoard("data/boggle/board4x4.txt");
        System.out.println(board);

        board = new BoggleBoard("data/boggle/board-16q.txt");
        System.out.println(board);

        board = new BoggleBoard("data/boggle/board-aqua.txt");
        System.out.println(board);
    }
}
