package SubString;

public class Boyle_Heuristic {
    private static final int R = 256;
    private static final int NO_ENTRY = -1;
    private final String pattern;
    private final int[] heuristic = new int[R];

    public Boyle_Heuristic(String pattern) {
        this.pattern = pattern;
        resetHeuristic(heuristic);
        deriveHeuristic(heuristic, pattern);

    }

    private static final int[] resetHeuristic(int[] heuristic) {
        for (int i = 0; i < heuristic.length; i++) heuristic[i] = NO_ENTRY;
        return heuristic;
    }

    private static final int[] deriveHeuristic(int[] heuristic, String pattern) {
        for (int i = 0; i < pattern.length(); i++) {
            int state = getState(pattern, i);
            heuristic[state] = i;
        }
        return heuristic;
    }

    public int skipCharacters(char c) {
        int state = getState(c);
        return  this.heuristic[state];
    }

    private static final int getState(char c) {
        if (c < 'A' || c > 'Z') throw new IllegalArgumentException();
        return (int) c;
    }

    private static final int getState(String pattern, int at) {
        return pattern.charAt(at);
    }
}
