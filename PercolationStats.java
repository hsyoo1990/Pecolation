import java.util.Random;

public class PercolationStats {

	private int mT; // number of experiments to run
	private double mMean; // mean of percolation threshold
	private double mStddev; // standard deviation of percolation threshold

	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T) {

		if (N <= 0 || T <= 0)
			throw new java.lang.IllegalArgumentException(
					"N and T must be greater than 1");
		mT = T;

		Random random = new Random(System.currentTimeMillis());

		mMean = 0;
		mStddev = 0;

		for (int x = 0; x < mT; x++) {
			Percolation pc = new Percolation(N);
			int cnt = 0;
			while (true) {
				if (!pc.percolates()) {
					int i = random.nextInt(N) + 1;
					int j = random.nextInt(N) + 1;
					if (!pc.isOpen(i, j)) {
						pc.open(i, j);
						cnt++;
					}
				} else {
					break;
				}
			}
			mMean = mMean + (double) cnt / (N * N);
			mStddev = mStddev + Math.pow((double) cnt / (N * N), 2);
		}
		mMean = mMean / T;
		mStddev = mStddev / T;

	}

	// sample mean of percolation threshold
	public double mean() {
		return mMean;
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return mStddev;
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return mMean - 1.96 * mStddev / Math.sqrt(mT);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return mMean + 1.96 * mStddev / Math.sqrt(mT);
	}

	public static void main(String[] args) // test client(described below)
	{
		PercolationStats percStats;
		int N = 0;
		int T = 0;
		long start = System.currentTimeMillis();
		double time;

		// you must get two inputs N and T
		if (args.length > 0) {
			try {
				N = Integer.parseInt(args[0]); // get N
				T = Integer.parseInt(args[1]); // get T
			} catch (NumberFormatException e) {
				System.err.println("Argument" + args[0] + " and " + args[1]
						+ " must be integers.");
				return;
			}
		}

		// run experiment
		percStats = new PercolationStats(N, T);

		// print result
		System.out.println("mean                    = " + percStats.mean());
		System.out.println("stddev                  = " + percStats.stddev());
		System.out.println("95% confidence interval = "
				+ percStats.confidenceLo() + ", " + percStats.confidenceHi());

		time = (System.currentTimeMillis() - start) / 1000.0;
		System.out.printf("total time = %f ms (N = %d, T = %d)\n", time, N, T);
	}
}
