package SubString;

import utility.PrimeNumberFounder;

public class RabinKarpHash {
    final static int R_BASE = 256; //ASCII
    final static int MODULO = PrimeNumberFounder.getLargestPrimeNumber(1000);

    final int hashDigitLength;
    int R_BASE_hashDigitLength_POWER;
    final String pattern;

    final long patternHash;

    public RabinKarpHash(String pattern) {
        this.pattern = pattern;
        this.hashDigitLength = pattern.length();

        R_BASE_hashDigitLength_POWER = 1;
        for (int i = 0; i < hashDigitLength - 1; i++)
            R_BASE_hashDigitLength_POWER = (R_BASE_hashDigitLength_POWER * R_BASE) % MODULO;
        this.patternHash = calculateHash(this.pattern);
    }

    public long calculateHash(String key) {
        long hash = 0;
        for (int j = 0; j < this.hashDigitLength; j++) {
            int d = (j < key.length()) ? key.charAt(j) : 0;
            hash = (R_BASE * hash + d) % MODULO;
        }
        return hash;
    }

    public long updateHash(long oldHash, char oldHeadChar, char newTailChar) {
        long newHash;
        newHash = (oldHash + MODULO - (R_BASE_hashDigitLength_POWER * oldHeadChar) % MODULO) % MODULO;
        newHash = (newHash * R_BASE + newTailChar) % MODULO;
        return newHash;
    }

    public long getPatternHash(){
        return this.patternHash;
    }

    public int getHashLength(){
        return this.hashDigitLength;
    }
}
