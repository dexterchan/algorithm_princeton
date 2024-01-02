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
        if (length<=1) return null;
        int keyLength = length / 2;
        String reverseStr = new StringBuilder(str).reverse().toString();

        String key = str.substring(0, keyLength);
        String reverseKey = reverseStr.substring(0, keyLength);

        KarpHash karpHash = new KarpHash(key.length());
        long hashSeed = karpHash.calculateHash(key);
        long hashReverseSeed = karpHash.calculateHash(reverseKey);

        long[] hashSeedValues = new long[str.length()];
        long[] hashSeedReverseValues = new long[str.length()];

        hashSeedValues[keyLength - 1] = hashSeed;
        hashSeedReverseValues[str.length() - keyLength] = hashReverseSeed;
        for (int i = 0; i < str.length() - keyLength * 2; i++) {
            long h = karpHash.addNewCharOnRight(hashSeedValues[i + keyLength - 1], str.charAt(i + keyLength));
            hashSeedValues[i + keyLength] = karpHash.removeOldCharOnLeft(h, str.charAt(i));

            h = karpHash.addNewCharOnRight(hashSeedReverseValues[str.length() - keyLength - i], reverseStr.charAt(i + keyLength));
            hashSeedReverseValues[str.length() - keyLength - i - 1] = karpHash.removeOldCharOnLeft(h, reverseStr.charAt(i));
        }

        return findPalindromeInLength(str, length/2);
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
