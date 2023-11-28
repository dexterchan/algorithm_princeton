package utility;

public class PrimeNumberFounder {
    public static int getLargestPrimeNumber(int N) {
        int upperLimit = (int) (Math.sqrt(N));
        boolean[] divisble = new boolean[N];

        if (N <= 2) return N;


        int i = 2;
        while (i <= upperLimit) {
            if (!divisble[i]) {
                for (int j = 2; j <= N/2 && j * i <N; j++)
                    divisble[j * i] = true;
                i += 1;
            } else i++;
        }
        int greatestPrime = 2;

        for (i = N - 1; i >= 0; i--) {
            if (!divisble[i]) {
                greatestPrime = i;
                break;
            }
        }

        return greatestPrime;
    }

    public static void main(String[] args) {
        System.out.println(getLargestPrimeNumber(1000000));
        assert getLargestPrimeNumber(1000000) == 999983;
    }
}
