package SubString;

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
        int startPos = strLength - stage ;

        for (int i = 0; i < startPos; i++) {
            if (str1.charAt(i) != str2.charAt(stage + i)) {
                return false;
            }
        }

        return true;
    }

    private static boolean smoothTransition(int stage, int nextStage) {
        return nextStage == stage + 1;
    }

    public static void main(String[] args) {
        boolean result = false;
        result = SubStringInterview.cyclicRotationOfString("winterbreak", "breakwintet");
        System.out.println(result);

        result = SubStringInterview.cyclicRotationOfString("winterbreak", "breakwinter");
        System.out.println(result);

        result = SubStringInterview.cyclicRotationOfString("winterbreak", "breabwinter");
        System.out.println(result);

        result = SubStringInterview.cyclicRotationOfString("ADBADBA", "DBAADBA");
        System.out.println(result);

        result = SubStringInterview.cyclicRotationOfString("ADBADBA", "DBAADBA");
        System.out.println(result);
    }
}
