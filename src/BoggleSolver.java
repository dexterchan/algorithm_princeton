import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//https://coursera.cs.princeton.edu/algs4/assignments/boggle/specification.php
public class BoggleSolver {

    private final Tries<String> _tries;

    private static class Tries<Value> {
        private static final int NUM_CHR = 26;
        private final Node node = new Node(null);

        private final Pattern pattern = Pattern.compile("^[A-Z]+$");

        private Node currentPosition;

        public Tries() {
            reset();
        }

        private final static class Node {
            private Object value;
            private final Node[] next;

            private Node(Object value) {
                this.value = value;
                this.next = new Node[NUM_CHR];
            }

            private Node() {
                this(null);
            }

            public Object getValue() {
                return this.value;
            }

            public void setValue(Object value) {
                this.value = value;
            }

            public boolean isNUll() {
                return this.value == null;
            }

            public Node getNode(int i) {
                if (!isValidNodeBoundary(i)) throw new IllegalArgumentException("INvalid boundary " + i);
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

            public boolean hasChild(char c) {
                int inx = c - 'A';
                return this.next[inx] != null;
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
                if (entries > 0) {
                    sb.delete(0, 1);
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

        public void reset() {
            this.currentPosition = this.node;
        }

        public boolean move(char c) {
            if (!isMoveable()) return false;
            int nextPosition = getNextNodeIndex(c);
            this.currentPosition = this.currentPosition.next[nextPosition];
            return true;
        }

        private boolean isMoveable() {
            return this.currentPosition != null;
        }

        private static void assignNewChildNode(Node node, String key, int at) {
            int nextNodeIndex = getNextNodeIndex(key, at);
            node.setNode(new Node(), nextNodeIndex);
        }

        private static <Value> void recursivePutTries(Node node, String key, Value value, int remain) {
            if (remain == 0) {
                node.setValue(value);
                return;
            }
            ;

            int at = key.length() - remain;
            int nodeIndex = getNextNodeIndex(key, at);
            Node childNode = node.getNode(nodeIndex);
            if (childNode == null) {
                assignNewChildNode(node, key, at);
                childNode = node.getNode(nodeIndex);
            }
            int new_remain = remain - getNodeLength(key, at);

            recursivePutTries(childNode, key, value, new_remain);

        }

        private static int getNodeLength(String key, int at) {
            if (!isValidateKeyAt(key, at)) throw new IllegalArgumentException();
            if (key.charAt(at) != 'Q') return 1;

            int nextCharAt = at + 1;
            if (!isValidateKeyAt(key, nextCharAt)) return 1;
            if (key.charAt(nextCharAt) == 'U') return 2;

            return 1;
        }

        private static int getNextNodeIndex(String key, int at) {
            char c = key.charAt(at);
            return getNextNodeIndex(c);
        }

        private static int getNextNodeIndex(char c) {
            if (c < 'A' || c > 'Z') throw new IllegalArgumentException("character should be A-Z");
            return c - (int) 'A';
        }

        private static boolean isValidateKeyAt(String key, int at) {
            if (key == null || key.isEmpty()) return false;
            if (at < 0 || at >= key.length()) return false;
            return true;
        }

        public Object getCurrentValue() {
            if (this.currentPosition == null) return null;

            return this.currentPosition.getValue();
        }

        public String toString() {
            return this.node.toString();
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this._tries = new Tries<>();
        for (String word : dictionary) {
            String wordUpper = word.toUpperCase();
            this._tries.put(wordUpper, wordUpper);
        }

    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> raw_results = this.travseBoard(board);

        return raw_results.stream().filter(word -> calculateValidStringScore(word) > 0).collect(Collectors.toList());

    }


    private Set<String> travseBoard(BoggleBoard board) {
        boolean[][] visited = new boolean[board.rows()][board.cols()];
        Set<String> finalResults = new HashSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                char c = board.getLetter(i, j);
                Tries.Node root = this._tries.node;
                if (root.hasChild(c)) {
                    resetVisitedRecords(visited, board.rows(), board.cols());
                    finalResults = searchWords(board, this._tries.node, i, j, visited, finalResults);
                }
            }
        }
        return finalResults;
    }

    private static boolean[][] resetVisitedRecords(boolean[][] visited, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                visited[i][j] = false;
            }
        }
        return visited;
    }

    private Set<String> searchWords(BoggleBoard board, Tries.Node node, int row, int col, boolean[][] visited, Set<String> results) {
        if (visited[row][col]) {
            return results;
        }
        visited[row][col] = true;

        char c = board.getLetter(row, col);
        if (!node.hasChild(c)) {
            visited[row][col] = false;
            return results;
        }
        Tries.Node nextNode = node.getNode(Tries.getNextNodeIndex(c));

        if (!nextNode.isNUll()) {
            results.add((String) nextNode.getValue());
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int n_i = row + i;
                int n_j = col + j;
                if (!isValidBoardDimension(board, n_i, n_j)) continue;
                if (visited[n_i][n_j]) continue;
                results = searchWords(board, nextNode, n_i, n_j, visited, results);
            }
        }
        visited[row][col] = false;

        return results;
    }


    private static boolean isValidBoardDimension(BoggleBoard board, int m, int n) {
        if (m < 0 || m >= board.rows()) return false;
        if (n < 0 || n >= board.cols()) return false;
        return true;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int matchNumChar = this.matchString(word);
        if (matchNumChar == 0) return 0;
        return calculateValidStringScore(word);
    }

    private int calculateValidStringScore(String word) {
        int score = 0;
        int numfOfChars = word.length();
        if (numfOfChars >= 3 && numfOfChars <= 4) {
            return 1;
        }
        if (numfOfChars >= 8) {
            return 11;
        }
        switch (numfOfChars) {
            case 5:
                score = 2;
                break;
            case 6:
                score = 3;
                break;
            case 7:
                score = 5;
                break;
            default:
                score = 0;
        }
        return score;
    }

    private int matchString(String word) {
        _tries.reset();
        String upperWord = word.toUpperCase();
        int matchNumChar = 0;
        for (int i = 0; i < upperWord.length(); i++) {
            char c = upperWord.charAt(i);
            boolean moveable = _tries.move(c);
            if (!moveable) return 0;
            if (c == 'Q') i++; //skip next U
            matchNumChar++;
        }
        if (_tries.getCurrentValue() == null || !_tries.getCurrentValue().equals(upperWord)) {
            //System.out.println("Not matching although matched " + upperWord);
            return 0;
        }

        return matchNumChar;
    }


    public static void main(String[] args) {
        testQfollowU();
        testBoggleSolver();
        testAccuracy();

        testPerformance("data/boggle/dictionary-algs4.txt", 10);
//        testTries("data/boggle/dictionary-algs4.txt");
        testBoardRead();
    }

    private static void testQfollowU() {
        String[] dictionary;
        BoggleBoard board;
        BoggleSolver solver;
        Iterable<String> resultItr;
        Set<String> resultSet;

        //Test Tries : BUQSHA
        Tries<String> tries = new Tries<>();
        tries.put("ABC", "ABC_value");
        tries.put("QUEEN", "QUEEN_value");
        tries.put("BUQSHA", "BUQSHA_value");
        tries.put("COREQ", "COREQ_value");
        //System.out.println(tries);

        String[] dictionary1 = {"ABC", "QUEEN", "BUQSHA", "COREQ", "QQQ"};
        solver = new BoggleSolver(dictionary1);
        System.out.println(solver.matchString("QQQ"));
        assert solver.matchString("ABC") == 3;
        assert solver.matchString("QUEEN") == 4;
        //assert solver.matchString("BUQSHA") == 6;
        char[][] data = {{'Q','U','I','T'},
                {'Q','E','C','X'},
                {'Q','R','O','C'},
                {'A','Q','U','A'}};
        board = new BoggleBoard(data);
        resultSet = solver.travseBoard(board);
        assert resultSet.contains("COREQ");
        assert resultSet.contains("QQQ");


        board = new BoggleBoard("data/boggle/board4x4.txt");
        In streamIn = new In("data/boggle/dictionary-yawl.txt");
        dictionary = streamIn.readAllStrings();
        solver = new BoggleSolver(dictionary);

        resultSet = solver.travseBoard(board);
        assert resultSet.contains("UNIT");


//        for (String w: resultSet){
//            System.out.println(w);
//        }

    }

    private static void testAccuracy() {
        BoggleBoard board;
        BoggleSolver solver;
        Set resultSet;
        Iterable<String> resultItr;
        String[] dictionary = {"USE", "ABC"};

        In streamIn = new In("data/boggle/dictionary-algs4.txt");
        dictionary = streamIn.readAllStrings();


        //char[][] data = {{'T', 'Y', 'N', 'U'}, {'E', 'D', 'S', 'E'}};
        char[][] data = {{'N', 'U'}, {'S', 'E'}};
        board = new BoggleBoard(data);
        solver = new BoggleSolver(dictionary);
        assert solver.scoreOf("USE") != 0;
        //assert solver.scoreOf("SI") != 0;
        resultSet = solver.travseBoard(board);
        assert resultSet.contains("USE");
        assert !resultSet.contains("ABC");


        board = new BoggleBoard("data/boggle/board4x4.txt");

        solver = new BoggleSolver(dictionary);
        assert solver.scoreOf("USE") != 0;

        resultSet = solver.travseBoard(board);
        assert resultSet.contains("USE");
        assert resultSet.contains("SI");

        resultItr = solver.getAllValidWords(board);
        boolean foundSi = false;
        for (String s : resultItr) {
            if (s.equals("SI")) foundSi = true;
        }
        assert !foundSi;

        char[][] data2 = {{'Y', 'N', 'U'}, {'D', 'S', 'E'}};
        board = new BoggleBoard(data2);
        solver = new BoggleSolver(dictionary);
        resultSet = solver.travseBoard(board);
        assert resultSet.contains("USE");

    }

    private static void testPerformance(String fileName, int dim) {
        In fileStream = new In(fileName);
        String[] dictionary = fileStream.readAllStrings();
        int test_num = 100;
        BoggleSolver solver = new BoggleSolver(dictionary);
        Stopwatch stopwatch = new Stopwatch();
        for (int i = 0; i < test_num; i++) {
            BoggleBoard board = new BoggleBoard(dim, dim);
            Iterable<String> results = solver.getAllValidWords(board);
            //printResults(results);
        }
        double totalTime = stopwatch.elapsedTime();
        System.out.println("Average calculation time:" + totalTime / test_num);

    }

    private static void printResults(Iterable<String> results) {
        for (String s : results) {
            System.out.print(s + ",");
        }
        System.out.println();
    }

    private static void testBoggleSolver() {
        String[] dictionary = {"APPLEPIE", "ORANGE", "BANANA", "UMBELLA", "quarrel", "queen"};
        BoggleSolver solver = new BoggleSolver(dictionary);
        int score = 0;

        System.out.println(solver._tries);
        score = solver.matchString("quarrel");
        assert score == "quarrel".length() - 1;
        score = solver.scoreOf("quarrel");
        assert score == 5;

        score = solver.matchString("APPLE");
        assert score == 0;
        score = solver.matchString("APPLEPIE");
        assert score == "APPLEPIE".length();

        score = solver.matchString("queen");
        assert score == "queen".length() - 1;
        score = solver.scoreOf("queen");
        assert score == 2;


        BoggleBoard board = null;
        Iterable<String> matchedStrings = null;
        board = new BoggleBoard("data/boggle/board-points5.txt");
        String[] dictionary2 = {"TNG", "SNG", "APPLE", "ORANGE"};
        solver = new BoggleSolver(dictionary2);
        matchedStrings = solver.travseBoard(board);
        for (String s : matchedStrings) {
            System.out.println(s);
        }

        char[][] data = {{'A', 'B'}, {'C', 'D'}};
        board = new BoggleBoard(data);
        String[] dictionary3 = {"AB", "APPLE", "ORANGE"};
        solver = new BoggleSolver(dictionary3);
        matchedStrings = solver.travseBoard(board);
        for (String s : matchedStrings) {
            System.out.println(s);
        }


    }

    private static void testTries(String fileName) {
        Tries<String> tries = new Tries<>();
        tries.put("ABC", "ABC_value");
        tries.put("BBA", "BBA_value");
        tries.put("BBD", "BBD_value");
        tries.put("BBD", "BBD2_value");
        tries.put("QUBBD", "QUBBD_value");
        System.out.println(tries);

        Tries<String> testTries = new Tries<>();
        In fileStream = new In(fileName);
        String[] dictionary = fileStream.readAllStrings();
        for (String s : dictionary) {
            testTries.put(s, s);
        }
    }


    private static void testBoardRead() {
        BoggleBoard board;
//        char[][] data = {{'N', 'U'}, { 'S', 'E'}};
//        board = new BoggleBoard(data);
//        System.out.println(board);

        char[][] data2 = {{'Y', 'N', 'U'}, {'D', 'S', 'E'}};
        board = new BoggleBoard(data2);
        System.out.println(board);

//        board = new BoggleBoard("data/boggle/board4x4.txt");
//        System.out.println(board);
//
//        board = new BoggleBoard("data/boggle/board-16q.txt");
//        System.out.println(board);
//
//        board = new BoggleBoard("data/boggle/board-aqua.txt");
//        System.out.println(board);
    }
}
