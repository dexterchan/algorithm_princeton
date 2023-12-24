package SubString;


class TandemRepeatFinder {
    private final int CHAR_SIZE;
    private final int strLength;
    private final int END_STATE_MACHINE;
    private int bestStart = 0;
    private int bestEnd = 0;
    private int[][] dfa;

    private int X;
    private final char SMALLEST_CHAR;
    private final char LARGEST_CHAR;
    private final String str;

    private final int INVALID_POS = Integer.MIN_VALUE;

    TandemRepeatFinder(String str, String base, char smallestChar, char largestChar) {
        this.str = str;
        this.SMALLEST_CHAR = smallestChar;
        this.LARGEST_CHAR = largestChar;
        this.CHAR_SIZE = largestChar - smallestChar + 1;

        strLength = str.length();
        END_STATE_MACHINE = base.length();
        dfa = create_dfa(base);
        findTandem();
    }

    public String getTandem() {
        if (bestStart >= bestEnd) return null;
        if (bestEnd - bestStart <= END_STATE_MACHINE) return null;
        return this.str.substring(bestStart, bestEnd + 1);
    }

    private int[][] create_dfa(String base) {
        String pattern = base;
        dfa = new int[CHAR_SIZE][END_STATE_MACHINE + 1];
        //reset DFA array
        for (int i = 0; i < CHAR_SIZE; i++) {
            for (int j = 0; j < dfa[i].length; j++) dfa[i][j] = 0;
        }

        int firstChar = getState(pattern.charAt(0));
        final int CYCLIC_STATE = END_STATE_MACHINE;
        dfa[firstChar][0] = 1;
        this.X = 0;
        fill_dfa(pattern);
        for (int j = 0; j < CHAR_SIZE; j++) dfa[j][CYCLIC_STATE] = getDfaCycleState(j, X);
        dfa[firstChar][CYCLIC_STATE] = 1;

        return dfa;
    }

    private int[][] fill_dfa(String base) {
        int c;
        final int BASE_LENGTH = base.length();
        for (int i = 1; i < BASE_LENGTH; i++) {
            for (int j = 0; j < CHAR_SIZE; j++) dfa[j][i] = getDfaCycleState(j, X);
            c = getState(base.charAt(i));
            dfa[c][i] = (i + 1);
            X = getDfaCycleState(c, X);
        }
        return dfa;
    }

    private int getDfaCycleState(int j, int i) {
        return dfa[j][i] % this.END_STATE_MACHINE;
    }


    /*
        Answer:
        setup KMP state machine from the base
        Mode: searching mode
        loop the state machine on the string for each character at i

        when detect next state=1 at i, enter Run mode
        start the counting of start=i,
        check if there is smooth transition,

        check exit criteria:
        keep running
        if smooth transition, continue until state=end of state machine
        if prev state = end of state machine and check the next state =1
        otherwise exit at i

        if i-start > recordLength, register lastEnd=i, lastStart=start
     */
    private int findTandem() {
        int k = 0;
        int nextStage = -1;
        boolean isSearching = true;
        int maxLen = 0;
        int lastStart = INVALID_POS, lastEnd = INVALID_POS;
        int stage;


        for (stage = 0; k < strLength; k++, stage = nextStage ) {
            int state = getState(str.charAt(k));
            nextStage = this.dfa[state][stage];
            if (nextStage == this.END_STATE_MACHINE) {
                int lastSegPos = getLastSegmentStart(k);
                if (lastSegPos != lastEnd + 1) lastStart = lastSegPos;
                lastEnd = k;
                System.out.println(lastStart + "," + lastEnd);
                int l = getSegmentLength(lastStart, lastEnd);
                if (maxLen < l) {
                    bestStart = lastStart;
                    bestEnd = lastEnd;
                }
            }

        }
        //Adjust bestEnd

        return bestEnd - bestStart;
    }

    private int getLastSegmentStart(int k) {
        return k - END_STATE_MACHINE + 1;
    }

    private int getSegmentLength(int start, int end) {
        return end - start + 1;
    }

    private int getState(char chr) {
        if (!isCharOutOfBound(chr)) throw new IllegalArgumentException();
        return chr - SMALLEST_CHAR;
    }

    private boolean isCharOutOfBound(char chr) {
        if (chr < this.SMALLEST_CHAR || chr > LARGEST_CHAR) return false;
        return true;
    }

}

public class SubStringInterview {

    /*
    Cyclic rotation of a string. A string s
        is a cyclic rotation of a string t
        if s and t have the same length and
        s consists of a suffix of t followed by a prefix of t.
        For example, "winterbreak" is a cyclic rotation of "breakwinter"
        (and vice versa).
        Design a linear-time algorithm to determine
        whether one string is a cyclic rotation of another.
     */
    /*
        Solution:
        Get KMP of both string
        check if length of t = length of s
        loop string t with s....
        if state ot t incrementing and reach the end of s at t[k]
        check if t[k+1:] match s[0: k]

     */
    public static boolean cyclicRotationOfString(String str1, String str2) {

        if (str1.length() <= 1) return false;
        if (str1.length() != str2.length()) return false;

        KMP_DFA dfa = new KMP_DFA(str2);


        int stage = 0;
        int strLength = str1.length();
        int k;
        boolean lastSmoothTransition = false;
        for (k = 1, stage = 0; k < strLength && stage < strLength; k++) {
            char c = str1.charAt(k);
            int nextStage = dfa.getNexState(stage, c);
            if (!smoothTransition(stage, nextStage)) {
                lastSmoothTransition = false;
            } else {
                lastSmoothTransition = true;
            }
            stage = nextStage;
        }
        if (!lastSmoothTransition) return false;
        if (stage == 0) return false;
        int startPos = strLength - stage;

        for (int i = 0; i < startPos; i++) {
            if (str1.charAt(i) != str2.charAt(stage + i)) {
                return false;
            }
        }

        return true;
    }


    /*
        Tandem repeat. A tandem repeat of a base string b within a string
        s is a substring of s consisting of at least one consecutive copy
        of the base string b.
        Given b and s, design an algorithm to find a tandem repeat of
        b within s of maximum length.
        Your algorithm should run in time proportional to
        M+N, where M is length of b and N is the length s.

        For example, if
        s is "abcabcababcaba" and
        b is "abcab", then "abcababcab" is the tandem substring of maximum length (2 copies).
     */

    public static String maxTandemRepeat(String str, String base) {
        TandemRepeatFinder tandem = new TandemRepeatFinder(str, base, 'a', 'd');
        return tandem.getTandem();
    }


    private static boolean smoothTransition(int stage, int nextStage) {
        return nextStage == stage + 1;
    }

    public static void main(String[] args) {
        //testCyclicRotationOfString();
        testTandem();
    }

    public static void testTandem() {
        String resultStr;
        resultStr = SubStringInterview.maxTandemRepeat("abcaccabaccaba", "abcab");
        assert resultStr == null;

        resultStr = SubStringInterview.maxTandemRepeat("abcadcababcaaa", "abcab");
        assert resultStr == null;

        resultStr = SubStringInterview.maxTandemRepeat("abcabcababcaba", "abcab");
        assert resultStr != null;
        System.out.println(resultStr);
        assert resultStr.equals("abcababcab");

        resultStr = SubStringInterview.maxTandemRepeat("abcabcababcaca", "abcab");
        assert resultStr == null;
        System.out.println(resultStr);
        //assert resultStr.equals("abcababcab");

        resultStr = SubStringInterview.maxTandemRepeat("abcabcababcada", "abcab");
        assert resultStr == null;


        resultStr = SubStringInterview.maxTandemRepeat("abcabcababcaaa", "abcab");
        assert resultStr == null;


        resultStr = SubStringInterview.maxTandemRepeat("abcababcababcabcababcaba", "abcab");
        assert resultStr != null;
        System.out.println(resultStr);
        assert resultStr.equals("abcababcababcab");
    }

    public static void testCyclicRotationOfString() {
        boolean result = false;
        result = SubStringInterview.cyclicRotationOfString("winterbreak", "breakwintet");
        assert (result == false);

        result = SubStringInterview.cyclicRotationOfString("winterbreak", "breakwinter");
        assert (result);

        result = SubStringInterview.cyclicRotationOfString("winterbreak", "breabwinter");
        assert (result == false);

        result = SubStringInterview.cyclicRotationOfString("ADBADBA", "DBAADBA");
        assert (result);

        result = SubStringInterview.cyclicRotationOfString("ADBADBA", "DBAADBA");
        assert (result);
    }
}
