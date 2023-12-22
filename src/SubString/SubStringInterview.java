package SubString;


class TandemRepeatFinder{
    final KMP_DFA kmp_dfa;
    int lastStart = 0, lastEnd = 0;
    int recordLength = 0;
    int stage = 0;
    int lastStage = 0;
    final int strLength;
    final int END_STATE_MACHINE;

    final String str;

    TandemRepeatFinder(String str, String base){
        this.str = str;
        kmp_dfa = new KMP_DFA(base);
        strLength = str.length();
        END_STATE_MACHINE = base.length();
    }

    private void findTandem(){

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
    public static String maxTandemRepeat(String str, String base) {
        KMP_DFA kmp_dfa = new KMP_DFA(base);
        int lastStart = 0, lastEnd = 0;
        int recordLength = 0;
        int stage = 0;
        int lastStage = 0;
        final int strLength = str.length();
        final int END_STATE_MACHINE = base.length();
        boolean searchMode = true;

        int i;
        for (i = 0; i < strLength; i++) {
            char c = str.charAt(i);
            int nextStage = kmp_dfa.getNexState(stage, c);

        }
//            if(searchMode){
//                if(nextStage==1){
//                    searchMode = false;
//                    lastStart = i;
//                }
//            }else{
//                if(nextStage==END_STATE_MACHINE){
//                    if(i==strLength-1){
//                        lastEnd = i;
//                        if(lastEnd-lastStart>recordLength){
//                            recordLength = lastEnd-lastStart;
//                        }
//                    }else{
//                        if(kmp_dfa.getNexState(nextStage, str.charAt(i+1))==1){
//                            lastEnd = i;
//                            if(lastEnd-lastStart>recordLength){
//                                recordLength = lastEnd-lastStart;
//                            }
//                        }else{
//                            searchMode = true;
//                            stage = 0;
//                        }
//                    }
//                }else{
//                    if(smoothTransition(stage, nextStage)){
//                        stage = nextStage;
//                    }else{
//                        searchMode = true;
//                        stage = 0;
//                    }
//                }
//        }
//    }


        return null;
    }


    private static boolean smoothTransition(int stage, int nextStage) {
        return nextStage == stage + 1;
    }

    public static void main(String[] args) {
        testCyclicRotationOfString();
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
