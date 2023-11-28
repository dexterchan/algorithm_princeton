package SubString;

public class KMP_DFA {
    final static int CHAR_SIZE = 256; //ASCII
    final int[][] dfa;
    final String pattern;

    public KMP_DFA(String pattern) {
        this.pattern = pattern;
        dfa = new int[CHAR_SIZE][pattern.length()];

        resetDfa(dfa);
        fillTransition(dfa, this.pattern);
    }

    private static int[][] resetDfa(int[][] dfa) {
        for (int i = 0; i < dfa.length; i++) {
            for (int j = 0; j < dfa[i].length; j++) dfa[i][j] = 0;
        }
        return dfa;
    }

    private static int[][] fillTransition(int[][] dfa, String pattern) {
        int match_case = getPatternIndex(pattern, 0);
        dfa[match_case][0] = 1;
        for (int j = 1, X = 0; j < pattern.length(); j++) {
            for (int c = 0; c < CHAR_SIZE; c++) {
                dfa[c][j] = dfa[c][X];
            }
            match_case = getPatternIndex(pattern, j);
            dfa[match_case][j] = j + 1;
            X = dfa[match_case][X];
        }
        return dfa;
    }

    public int getNexState(int stage, char state){
        return this.dfa[state][stage];
    }

    private static int getPatternIndex(String pattern, int i) {
        if (i < 0 || i >= pattern.length()) throw new IllegalArgumentException();
        return getState(pattern.charAt(i));
    }

    private static final int getState(char c) {
        if (c < 'A' || c > 'Z') throw new IllegalArgumentException();
        return (int) c;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.pattern);
        sb.append("\n");
        for (int i = 'A'; i < this.dfa.length && i <= 'Z'; i++) {
            for (int j = 0; j < this.dfa[i].length; j++) {
                sb.append(this.dfa[i][j]);
                sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        KMP_DFA dfa = new KMP_DFA("ABABAC");
        System.out.println(dfa.toString());
    }
}
