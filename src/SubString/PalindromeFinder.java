package SubString;

import java.util.Arrays;

class KarpHash {
    final static int BASE = 256;
    final static int MODULO = 997;

    final static int NOT_FOUND = -1;
    final int rbase_power;
    final int patternLength;


    public KarpHash(int patternLength) {
        this.patternLength = patternLength;
        this.rbase_power = calculateHashPower(patternLength);
    }

    private static int calculateHashPower(int patternLength) {
        int power = 1;
        for (int i = 0; i < patternLength - 1; i++) {
            power = (power * BASE) % MODULO;
        }
        return power;
    }

    public long calculateHash(String key) {
        long hash = 0;
        hash = calculateKeyHash(key);

        for (int j = 0; j < this.patternLength - key.length(); j++) {
            hash = (BASE * hash) % MODULO;
        }
        return hash;
    }

    private long calculateKeyHash(String key) {
        long hash = 0;
        for (int j = 0; j < key.length(); j++) {
            hash = (BASE * hash + key.charAt(j)) % MODULO;
        }
        return hash;
    }

    long calculateHashReversed(String key) {
        StringBuilder sb = new StringBuilder(key);
        sb.reverse();
        return calculateHash(sb.toString());
    }


    public long removeOldCharOnLeft(long oldHash, char leftChar) {
        long newHash;
        newHash = (oldHash + MODULO - (leftChar * rbase_power) % MODULO) % MODULO;
        return newHash;
    }

    public long addNewCharOnRight(long oldHash, char rightChar) {
        long newHash;
        newHash = (oldHash * BASE + rightChar) % MODULO;
        return newHash;
    }

}

public class PalindromeFinder {
    final static int NOT_FOUND = -1;

    public String findPalindromeInLength(String str) {
        if (str.length() <= 1) return null;

        String reverseStr = new StringBuilder(str).reverse().toString();
        long[] hashValueOutput = new long[str.length()];
        long[] hashValueRevOutput = new long[str.length()];

        String result = this.bfsPalindromeSearch(str, reverseStr, str.length(), hashValueOutput, hashValueRevOutput);


        return result;
    }

    private String bfsPalindromeSearch(String str, String revStr, int searchLength, long[] hashs, long[] hashRevs) {
        if (searchLength <= 1) return null;
        Arrays.fill(hashs, NOT_FOUND);
        Arrays.fill(hashRevs, NOT_FOUND);

        int keyLength = searchLength == 3 ? 2 : searchLength / 2;

        this.calculateHashValues(str, keyLength, hashs);
        this.calculateHashValues(revStr, keyLength, hashRevs);

        String candidateEven = foundPalindromeEvenCase(str, hashs, hashRevs, keyLength);
        String candidateOdd = foundPalindromeOddCase(str, hashs, hashRevs, keyLength);

        if (candidateEven != null && candidateOdd != null) {
            if (candidateEven.length() > candidateOdd.length()) return candidateEven;
            else return candidateOdd;
        }
        if (candidateEven != null && candidateOdd == null) return candidateEven;
        if (candidateEven == null && candidateOdd != null) return candidateOdd;

        int nextSearchLength = searchLength > 3 ? searchLength / 2 : searchLength - 1;
        return bfsPalindromeSearch(str, revStr, nextSearchLength, hashs, hashRevs);

    }

    private static String getSearchResult(String str, int found, boolean isEven, int keyLength) {

        int begin = found - keyLength + 1;
        int end = isEven ? found + keyLength : found + keyLength - 1;
        while (begin - 1 >= 0 && (end + 1) < str.length()) {
            if (str.charAt(begin - 1) == str.charAt(end + 1)) {
                begin--;
                end++;
            } else break;
        }
        return str.substring(begin, end + 1);
    }

    private long[] calculateHashValues(String str, int keyLength, long[] hashValueOutput) {
        if (hashValueOutput.length != str.length()) throw new IllegalArgumentException();
        if (keyLength <= 0) return hashValueOutput;
        KarpHash karpHash = new KarpHash(keyLength);
        String firstSubStr = str.substring(0, keyLength);
        hashValueOutput[keyLength - 1] = karpHash.calculateHash(firstSubStr);
        for (int i = keyLength; i < str.length() - keyLength + 1; i++) {
            char newChar = str.charAt(i);
            char oldChar = str.charAt(i - keyLength);
            hashValueOutput[i] = karpHash.removeOldCharOnLeft(hashValueOutput[i - 1], oldChar);
            hashValueOutput[i] = karpHash.addNewCharOnRight(hashValueOutput[i], newChar);
        }
        return hashValueOutput;
    }


    public static void main(String[] args) {

        testBfsPalindromeSample();

        testNonPalindromSamples("abcdee", 3);
        testNonPalindromSamples("psabcdedcba", 11);

        testPalindromeSamples("psabcdedcba", 9, 6, 6);

        testKarpHash();
        testPalindromeSamples("abcdeedcba", 10, 4, 5);
        testPalindromeSamples("psabcdeedcba", 10, 6, 7);
        testPalindromeSamples("abcdeedcbaps", 10, 4, 5);


        testPalindromeSamples("abcdedcba", 9, 4, 4);

        testPalindromeSamples("abcdedcbaps", 9, 4, 4);

        testPalindromeSamples("abcdeedcba", 2, 4, 5);
        testPalindromeSamples("abcdee", 2, 4, 5);

        testPalindromeSamples("aa", 2, 0, 1);
        testPalindromeSamples("aa", 1, 0, 0);


        //testNonPalindromSamples("a", 1);
        testNonPalindromSamples("ab", 2);
        testNonPalindromSamples("ab", 1);
        testNonPalindromSamples("abcdefg", 7);


        testKarpHashReversed();


    }

    private static void testBfsPalindromeSample() {
        PalindromeFinder finder = new PalindromeFinder();
        String result;

        result = finder.findPalindromeInLength("abcdedcbarabcdeedcba");
        assert result.equals("edcbarabcde");

        result = finder.findPalindromeInLength("abcdedcbarsabcdeedcba");
        assert result.equals("abcdeedcba");

        result = finder.findPalindromeInLength("abcdeedcba");
        assert result.equals("abcdeedcba");

        result = finder.findPalindromeInLength("abcdee");
        assert result.equals("ee");

        result = finder.findPalindromeInLength("aabbaaccddeeddcc");
        assert result.equals("ccddeeddcc");


        result = finder.findPalindromeInLength("lxabcdbayxded");
        assert result.equals("ded");

        result = finder.findPalindromeInLength("xxabcdedcbayxabcdeedcba");
        assert result.equals("abcdeedcba");

        result = finder.findPalindromeInLength("psabcdedcba");
        assert result.equals("abcdedcba");


        result = finder.findPalindromeInLength("abcdedcba");
        assert result.equals("abcdedcba");

        result = finder.findPalindromeInLength("xxabcdedcbayx");
        assert result.equals("abcdedcba");


        result = finder.findPalindromeInLength("ktabcdeedcbaxxabcdedcbayx");
        assert result.equals("edcbaxxabcde");


    }

    private static void testPalindromeSamples(String sample, int totalLength, int e1, int e2) {
        PalindromeFinder finder = new PalindromeFinder();
        int strLength = sample.length();
        boolean isEven = totalLength % 2 == 0;
        int keyLength = isEven ? totalLength / 2 : totalLength / 2 + 1;


        String revSample = new StringBuilder(sample).reverse().toString();
        long[] hashValueOutput = new long[sample.length()];
        long[] hashValueRevOutput = new long[sample.length()];
        Arrays.fill(hashValueOutput, NOT_FOUND);
        Arrays.fill(hashValueRevOutput, NOT_FOUND);

        finder.calculateHashValues(sample, keyLength, hashValueOutput);
        finder.calculateHashValues(revSample, keyLength, hashValueRevOutput);
        assert hashValueOutput[e1] == hashValueRevOutput[strLength - 1 - e2];

        //Loop through the result
        int found = NOT_FOUND;
        found = isEven ? isPalindromeFoundEvenCase(hashValueOutput, hashValueRevOutput) : isPalindromeFoundOddCase(hashValueOutput, hashValueRevOutput);

        if (isEven) {
            assert found == e1;
            assert found + 1 == e2;
        } else {
            assert found == e1;
            assert found == e2;
        }
    }

    private static String foundPalindromeEvenCase(String str, long[] hashValues, long[] hashRevValues, int keyLength) {
        int found = NOT_FOUND;
        int len = hashValues.length;
        String longestString = null;
        int longStringLen = 0;
        for (int i = 0; i < len && len - i - 2 >= 0; i++) {
            if (hashValues[i] != NOT_FOUND && hashValues[i] == hashRevValues[len - 1 - i - 1]) {
                found = i;
                String tmpPalindromeString = getLongestPalindromeString(str, found, keyLength, true);
                if (tmpPalindromeString.length() > longStringLen) {
                    longStringLen = tmpPalindromeString.length();
                    longestString = tmpPalindromeString;
                }
            }
        }
        return longestString;
    }

    private static String foundPalindromeOddCase(String str, long[] hashValues, long[] hashRevValues, int keyLength) {
        int found = NOT_FOUND;
        int len = hashValues.length;
        String longestString = null;
        int longStringLen = 0;
        for (int i = 0; i < len && len - i - 1 >= 0; i++) {
            if (hashValues[i] != NOT_FOUND && hashValues[i] == hashRevValues[len - 1 - i]) {
                found = i;
                String tmpPalindromeString = getLongestPalindromeString(str, found, keyLength, false);
                if (tmpPalindromeString.length() > longStringLen) {
                    longStringLen = tmpPalindromeString.length();
                    longestString = tmpPalindromeString;
                }
            }
        }
        return longestString;
    }

    private static String getLongestPalindromeString(String str, int found, int keyLength, boolean isEven) {
        int begin = found - keyLength + 1;
        int end = found + (isEven ? keyLength : keyLength - 1);
        while (begin - 1 >= 0 && (end + 1) < str.length()) {
            if (str.charAt(begin - 1) == str.charAt(end + 1)) {
                begin--;
                end++;
            } else break;
        }
        return str.substring(begin, end + 1);
    }


    private static int isPalindromeFoundEvenCase(long[] hashValues, long[] hashRevValues) {
        int found = NOT_FOUND;
        int len = hashValues.length;
        for (int i = 0; i < len && len - i - 2 >= 0; i++) {
            if (hashValues[i] != NOT_FOUND && hashValues[i] == hashRevValues[len - 1 - i - 1]) {
                found = i;
                return found;
            }
        }
        return found;
    }

    private static int isPalindromeFoundOddCase(long[] hashValues, long[] hashRevValues) {
        int found = NOT_FOUND;
        int len = hashValues.length;
        for (int i = 0; i < len && len - i - 1 >= 0; i++) {
            if (hashValues[i] != NOT_FOUND && hashValues[i] == hashRevValues[len - 1 - i]) {
                found = i;
                return found;
            }
        }
        return found;
    }

    private static void testNonPalindromSamples(String sample, int totalLength) {
        PalindromeFinder finder = new PalindromeFinder();

        boolean isEven = totalLength % 2 == 0;
        int keyLength = isEven || totalLength == 1 ? totalLength / 2 : totalLength / 2 + 1;

        String revSample = new StringBuilder(sample).reverse().toString();

        long[] hashValueOutput = new long[sample.length()];
        long[] hashValueRevOutput = new long[sample.length()];
        Arrays.fill(hashValueOutput, NOT_FOUND);
        Arrays.fill(hashValueRevOutput, NOT_FOUND);

        finder.calculateHashValues(sample, keyLength, hashValueOutput);
        finder.calculateHashValues(revSample, keyLength, hashValueRevOutput);

        //Loop through the result
        int found = NOT_FOUND;
        found = isEven ? isPalindromeFoundEvenCase(hashValueOutput, hashValueRevOutput) : isPalindromeFoundOddCase(hashValueOutput, hashValueRevOutput);
        assert found == NOT_FOUND;


    }

    private static void testKarpHash() {
        String pattern = "ABCDE";
        long r;
        KarpHash karpHash = new KarpHash(pattern.length());
        long orgHash = karpHash.calculateHash(pattern);
        long newHash = karpHash.calculateHash("BCDE");

        r = karpHash.removeOldCharOnLeft(orgHash, 'A');
        assert (r * KarpHash.BASE) % KarpHash.MODULO == newHash;
        r = karpHash.addNewCharOnRight(r, 'F');
        newHash = karpHash.calculateHash("BCDEF");
        assert r == newHash;


        pattern = "abcde";
        orgHash = karpHash.calculateHash(pattern);
        String testPattern = "psabc";
        r = karpHash.calculateHash(testPattern);
        r = karpHash.removeOldCharOnLeft(r, 'p');
        r = karpHash.addNewCharOnRight(r, 'd');
        r = karpHash.removeOldCharOnLeft(r, 's');
        r = karpHash.addNewCharOnRight(r, 'e');
        assert orgHash == r;
    }

    private static void testKarpHashReversed() {
        String pattern = "ABCDE";
        String revPattern = new StringBuilder(pattern).reverse().toString();
        long orgHash, refHash;
        KarpHash karpHash = new KarpHash(pattern.length());
        orgHash = karpHash.calculateHashReversed(pattern);
        refHash = karpHash.calculateHash(revPattern);
        assert orgHash == refHash;
    }


//    private static void testKarpHash2() {
//        String pattern = "XYZAB";
//        long r, newHash;
//        KarpHash karpHash = new KarpHash(pattern.length());
//        long orgHash = karpHash.calculateHash(pattern);
//        r = karpHash.removeOldCharOnRight(orgHash, 'B');
//        newHash = karpHash.calculateHash("XYZA");
//        assert r == newHash;
//        newHash = karpHash.calculateKeyHash("XYZA");
//        assert newHash == newHash;
//
//    }
}
