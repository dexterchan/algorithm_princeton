import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordNet;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }         // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        if (nouns == null) throw new IllegalArgumentException();

        int inputSize = nouns.length;

        int[][] distMatrix = new int[inputSize][];
        for (int i = 0; i < inputSize; i++) distMatrix[i] = new int[inputSize];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                distMatrix[i][j] = 0;
            }
        }

        for (int i = 0; i < inputSize; i++) {
            for (int j = i + 1; j < inputSize; j++) {
                String nounA = nouns[i];
                String nounB = nouns[j];
                int dist = wordNet.distance(nounA, nounB);
                distMatrix[i][j] = dist;
                distMatrix[j][i] = dist;
            }
        }

        final int LONGEST_DIST = Integer.MAX_VALUE / inputSize;
        int longestDist = 0;
        int candidate = -1;
        for (int i = 0; i < inputSize; i++) {
            int dist = 0;
            for (int j = 0; j < inputSize; j++) {
                dist += (distMatrix[i][j]==-1)?LONGEST_DIST:distMatrix[i][j];
            }
            if (dist > longestDist) {
                candidate = i;
                longestDist = dist;
            }
        }

        if (candidate == -1)
            return null;


        return nouns[candidate];
    }

    public static void main(String[] args) {
        test_outcast_8(args);
        readfiles(args);

    }  // see test client below

    private static void test_outcast_8(String[] args){
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        String data = "water soda bed orange_juice milk apple_juice tea coffee";
        String [] nouns = data.split(" ");
        outcast.outcast(nouns);
    }
    private static void readfiles(String[] args){
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}