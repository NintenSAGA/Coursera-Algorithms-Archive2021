package percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double stddev;
    private final int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        
        double[] thresholds = new double[trials];
        this.trials = trials;

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                try {
                    percolation.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }

            thresholds[i] = (double) percolation.numberOfOpenSites()/(n*n);
        }

        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean-(CONFIDENCE_95*stddev)/Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean+(CONFIDENCE_95*stddev)/Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats stat = new PercolationStats(n, t);

        StdOut.printf("%-24s= %f\n", "mean", stat.mean());
        StdOut.printf("%-24s= %.16f\n", "stddev", stat.stddev());
        StdOut.printf("%-24s= [%.16f, %.16f]\n", "95% confidence interval",
                stat.confidenceLo(), stat.confidenceHi());
    }

}