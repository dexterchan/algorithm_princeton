import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    double[] results = null;
    int N;

    int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 0)
            throw new IllegalArgumentException();
        if (trials < 0)
            throw new IllegalArgumentException();

        results = new double[trials];
        this.N = n;
        this.trials = trials;

        for (int c = 0; c < trials; c++) {
            results[c] = run_experiment(n);
        }
    }

    private static double run_experiment(int N) {
        Percolation p = new Percolation(N);
        int total = N * N;


        for (int i = 0; i < total; i++) {
            //System.out.println("Running experiment: " + (i + 1));
            int row = 0;
            int col = 0;

            do {
                int step = StdRandom.uniformInt(total);
                row = step / N + 1;
                col = step % N + 1;
            } while (p.isOpen(row, col));

            p.open(row, col);

            if (p.percolates()) {
                double v = (double) (i + 1) / (double) total;
                System.out.println("Found precolate: " + (i + 1) + ":" + v);
                return v;
            }
        }
        return 0;
    }

    // sample mean of percolation threshold
    public double mean() {
        double val = 0;
        for (double r : this.results) {
            val += r;
        }
        return val / this.trials;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double val = 0;
        double mean = this.mean();
        for (double r : this.results) {
            val += (r - mean) * (r - mean);
        }
        double dev = val / (this.trials - 1);
        return Math.sqrt(dev);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double st = Math.sqrt(trials);
        return this.mean() - 1.96 * this.stddev() / st;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double st = Math.sqrt(trials);
        return this.mean() + 1.96 * this.stddev() / st;
    }


    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }
        int N = Integer.parseInt(args[0]);
        int trial = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(N, trial);

        StdOut.printf("mean                    = %f\n", stats.mean());
        StdOut.printf("std                    = %f\n", stats.stddev());
        StdOut.printf("confidenceLo                    = %f\n", stats.confidenceLo());
        StdOut.printf("confidenceHi                    = %f\n", stats.confidenceHi());

    }

}
