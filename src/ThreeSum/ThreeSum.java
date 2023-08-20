package ThreeSum;

import utility.Timer;
import utility.NumberArrayCreator;

import java.util.Arrays;

public class ThreeSum {
    public ThreeSum() {
    }

    public static int brutal_force_count_sum_zero(int[] data) {
        //Count sum of 3 numbers in data is equal to zero
        // Time complexity = ~(N^3)
        int counter = 0;
        int N = data.length;
        System.out.println("Length of data is: " + N);
        if (data.length < 3) {
            throw new IndexOutOfBoundsException("data array should be longer than 3");
        }
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                for (int k = j + 1; k < N; k++) {

                    var sum = data[i] + data[j] + data[k];
                    if (sum == 0) {
                        //System.out.println("Found 3 numbers sum to zero: " + i + " + " + j + " + " + k);
                        counter += 1;
                    }
                }
            }
        }
        return counter;
    }

    public static int sorted_count_sum_zero(int [] data){
        //Sort data array data
        //Count sum of 3 numbers in data is equal to zero
        // Time complexity = ~(N^2 * log N)
        int counter = 0;
        int N = data.length;
        System.out.println("Length of data is: " + N);
        if (data.length < 3) {
            throw new IndexOutOfBoundsException("data array should be longer than 3");
        }
        //Sort data array
        Arrays.sort(data);
        //System.out.println("Sorted data array is: " + Arrays.toString(data));
        //Find 3 numbers sum to zero
        for (int i=0; i< N; i++){
            for (int j= i + 1 ; j < N; j++){
                //Find k such that data[k] = -(data[i] + data[j])
                int k = Arrays.binarySearch(data, -(data[i] + data[j]));
                if (k > j){
                    //System.out.println("Found 3 numbers sum to zero: " + i + " + " + j + " + " + k);
                    counter += 1;
                }
            }
        }
        return counter;
    }

    public static void main(String[] args) {
        //create an integer array of [10, -20, -10, 30, 40, 50, -20, 10]
        int[] data = {10, -20, -10, 30, 40, 50, -20, 10};
        NumberArrayCreator numberArrayCreator = new NumberArrayCreator(1);
        data = numberArrayCreator.create_number_array(1000, 100, -100);
        //System.out.println("data array is: " + Arrays.toString(data));

        Timer timer = new Timer();
        var counter = ThreeSum.brutal_force_count_sum_zero(data);
        System.out.println("Time elapsed: " + timer.elapsedTime());
        System.out.println("Number of 3 numbers sum to zero is: " + counter);

        timer = new Timer();
        var counter2 = ThreeSum.sorted_count_sum_zero(data);
        System.out.println("Time elapsed: " + timer.elapsedTime());
        System.out.println("Number of 3 numbers sum to zero is: " + counter2);
        assert counter == counter2;

    }
}
