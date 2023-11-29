package SubString;

public class SubStringTest {
    private static final int R = 256;

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

    public static int boyleCountFromRight(String pat, String text) {
        int i, j;
        int N = text.length();
        int M = pat.length();
        Boyle_Heuristic heuristic = new Boyle_Heuristic(pat);

//        int skip = 0;
//        for (i = 0, j = 0; i < N && j < M; i+=skip) {
//            skip = 0;
//            char c = text.charAt(i + j);
//            if ( c == pat.charAt(j)) j++;
//            else {
//                skip = Math.max(1, j- heuristic.skipCharacters(c);
//                j = 0;
//            }
//        }
        if (j == M) return i;
        else return -1;
    }

    public static int rabinKarpSubStringSearch(String pat, String text) {
        int N = text.length();

        RabinKarpHash hashCalc = new RabinKarpHash(pat, pat.length());
        long refHash = hashCalc.getPatternHash();
        long curHash = hashCalc.calculateHash(text);
        final int M = hashCalc.getHashLength();
        for (int i = hashCalc.getHashLength(); i < N; i++) {
            curHash = hashCalc.updateHash(curHash, text.charAt(i - M), text.charAt(i));
            if (curHash == refHash) return i - M + 1;
        }

        return -1;
    }

    public static void main(String args[]) {
        String text = "ABACADABRAC";
        String pat = "ADABR";
        assert (bruteForceSubstringSearch(pat, text)) == 4;
        pat = "BRAC";
        assert (kmpDfaSubstringSearch(pat, text)) == 7;

        pat = "AAAAA";
        assert (kmpDfaSubstringSearch(pat, text)) == -1;

        pat = "ADABR";
        assert (boyleCountFromRight(pat, text)) == 4;

        pat = "BRAC";
        System.out.println(boyleCountFromRight(pat,text));
        assert (boyleCountFromRight(pat, text)) == 7;


        pat = "AAAAA";
        assert (boyleCountFromRight(pat, text)) == -1;

        pat = "BRAC";
        assert (rabinKarpSubStringSearch(pat, text) == 7);

        pat = "ACAD";
        assert (rabinKarpSubStringSearch(pat, text) == 2);

        pat = "AAAA";
        assert (rabinKarpSubStringSearch(pat, text) == -1);
    }

}