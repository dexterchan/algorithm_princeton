package SubString;

public class SubStringTest {


    public static int bruteForceSubstringSearch(String pat, String text) {
        int i = 0, N = text.length();
        int j = 0, M = pat.length();

        for (i = 0, j = 0; i < N && j < M; i++) {
            if (text.charAt(i) == pat.charAt(j)) j++;
            else {
                i -= j;
                j = 0;
            }
        }
        if (j == M) return i - M;
        else return -1;
    }


    public static int kmpDfaSubstringSearch(String pat, String text) {
        int N = text.length();
        int M = pat.length();
        int i, j;
        KMP_DFA dfa = new KMP_DFA(pat);
        for (i = 0, j = 0; i < N && j < M; i++) {
            char c = text.charAt(i);
            j = dfa.getNexState(j, c);
        }
        if (j == M) return i - M;
        else return -1;
    }

    public static void main(String args[]) {
        String text = "ABACADABRAC";
        String pat = "ADABR";
        assert (bruteForceSubstringSearch(pat, text))==4;
        pat = "BRAC";
        assert (kmpDfaSubstringSearch(pat, text)) == 7;

        pat = "AAAAA";
        assert (kmpDfaSubstringSearch(pat, text)) == -1;

    }

}