package utility;

import java.util.Random;

public class NumberArrayCreator {
    private Random random;
    public NumberArrayCreator(int seed) {
        //Set seed for random number generator
        this.random = new Random(seed);
    }
    public  int[] create_number_array(int size, int upper_bound, int lower_bound) {
        //Create an array of N numbers between lower_bound and upper_bound
        int[] data = new int[size];
        int range = upper_bound - lower_bound;

        //generate N number of distinct number between lower_bound and upper_bound
        for (int i = 0; i < size; i++) {
            //Generate random number with this.seed
            data[i] = (int) (random.nextDouble() * range) + lower_bound;
        }
        return data;
    }
}
