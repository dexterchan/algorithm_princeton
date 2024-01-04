package SubString;

class KarpHash {
    final static int BASE = 256;
    final static int MODULO = 997;
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

    public String findPalindromeInLength(String str, int length) {
        if (length <= 1) return null;
        int keyLength = length / 2;
        String reverseStr = new StringBuilder(str).reverse().toString();

        long[] hashValues = new long[str.length()];
        long[] hashReverseValues = new long[str.length()];

        hashValues = calculateHashValues(str, keyLength, hashValues);
        hashReverseValues = calculateHashValues(reverseStr, keyLength, hashReverseValues);

        for (int i=keyLength-1;i<str.length()-keyLength*2;i++){
            int j = str.length() - i;
            if (hashValues[i] == hashReverseValues[j]){
                return str.substring(i-keyLength, j+keyLength);
            }
        }
        return findPalindromeInLength(str, length / 2);
    }

    private long[] calculateHashValues(String str, int keyLength, long[] hashValueOutput) {
        if (hashValueOutput.length != str.length()) throw new IllegalArgumentException();
        KarpHash karpHash = new KarpHash(keyLength);

        hashValueOutput[keyLength - 1] = karpHash.calculateHash(str.substring(0, keyLength));
        for (int i = keyLength; i < str.length() - keyLength * 2; i++) {
            hashValueOutput[i] = karpHash.addNewCharOnRight(hashValueOutput[i - 1], str.charAt(i));
            hashValueOutput[i] = karpHash.removeOldCharOnLeft(hashValueOutput[i], str.charAt(i - keyLength));
        }
        return hashValueOutput;
    }

    public static void main(String[] args) {
        testSamples();
        testKarpHashReversed();
        testKarpHash();

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

    private static void testSamples() {
        PalindromeFinder finder = new PalindromeFinder();
        String sample1 = "abcdeedcbakg";

        finder.findPalindromeInLength(sample1, sample1.length());

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
