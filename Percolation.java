
```sh
import java.io.File;
import java.util.Scanner;

public class Percolation {

	private static final boolean SITE_BLOCKED = false;
	private static final boolean SITE_OPEN = true;
	private boolean[] sites; // sites[i] = state of site i
	private int mN; // remember the input N
	private int topIdx; // idx of the special top
	private int bottomIdx; // idx of the speical bottom
	private WeightedQuickUnionUF uf, ufEnd;

	// create N-by-N grid, with all sites blocked
	public Percolation(int N) {
		if (N <= 0)
			throw new IllegalArgumentException("N must be >0");
		sites = new boolean[N * N + 2];
		for (int i = 0; i < N * N + 2; i++) {
			sites[i] = SITE_BLOCKED;
		}

		mN = N;
		topIdx = N * N;
		bottomIdx = N * N + 1;
		//System.out.println(sites.length);
		sites[topIdx] = SITE_OPEN;
		sites[bottomIdx] = SITE_OPEN;
		uf = new WeightedQuickUnionUF((N * N) + 2);
		ufEnd = new WeightedQuickUnionUF((N * N) + 2);

	}

	private void checkIndex(int i, int j) {
		if (i < 1 || i > mN)
			throw new IndexOutOfBoundsException("row index i out of bounds");
		if (j < 1 || j > mN)
			throw new IndexOutOfBoundsException("column index j out of bounds");
	}

	// open site(row i, column j) if it is not open already
	public void open(int i, int j) {
		int row = i - 1;
		int col = j - 1;
		checkIndex(i, j);
		if (isOpen(i, j))
			 return;
			sites[xy(row, col)] = SITE_OPEN;
		if (row == 0) {
			uf.union(topIdx, xy(row, col));
			ufEnd.union(topIdx, xy(row, col));
		}
		if (row == mN - 1) {
			ufEnd.union(bottomIdx, xy(row-1, col));
			if (isOpen(i - 1, j)) {
				uf.union(xy(row, col), xy(row - 1, col));
			}
		}
		if (row > 0 && row < mN - 1) {
			if (isOpen(i - 1, j)) {
				uf.union(xy(row, col), xy(row - 1, col));
				ufEnd.union(xy(row, col), xy(row - 1, col));
			}
			if (isOpen(i + 1, j)) {
				uf.union(xy(row, col), xy(row + 1, col));
				ufEnd.union(xy(row, col), xy(row + 1, col));
			}
		}
		if (col == 0) {
			if (isOpen(i, j + 1)) {
				uf.union(xy(row, col), xy(row, col + 1));
				ufEnd.union(xy(row, col), xy(row, col + 1));
			}
		}
		if (col == mN - 1) {
			if (isOpen(i, j - 1)) {
				uf.union(xy(row, col), xy(row, col - 1));
				ufEnd.union(xy(row, col), xy(row, col - 1));
			}
		}
		if (col > 0 && col < mN - 1) {
			if (isOpen(i, j - 1)) {
				uf.union(xy(row, col), xy(row, col - 1));
				ufEnd.union(xy(row, col), xy(row, col - 1));
			}
			if (isOpen(i, j + 1)) {
				uf.union(xy(row, col), xy(row, col + 1));
				ufEnd.union(xy(row, col), xy(row, col + 1));
			}
		}
	}

	private int xy(int i, int j) {
		return (i * mN) + j;
	}

	// is site(row i, column j) open?
	public boolean isOpen(int i, int j) {
		checkIndex(i, j);
		int row = i - 1;
		int col = j - 1;
		return sites[xy(row, col)];

	}

	// is site(row i, column j) full?
	public boolean isFull(int i, int j) {
		int row = i - 1;
		int col = j - 1;
		return uf.connected(topIdx, xy(row, col)) && isOpen(i, j);
	}

	// does the system percolate?
	public boolean percolates() {
		if(uf.connected(topIdx, bottomIdx)|| ufEnd.connected(topIdx, bottomIdx)){
			return true;	
		}else{
			return false;
		}
		//return uf.connected(topIdx, bottomIdx)
			//	|| ufEnd.connected(topIdx, bottomIdx);
	}

	// test client(optional)
	public static void main(String[] args) {
		Scanner in;
		int N = 0;
		long start = System.currentTimeMillis();

		try {
			// get input file from argument
			in = new Scanner(new File(args[0]), "UTF-8");
		} catch (Exception e) {
			System.out.println("invalid or no input file ");
			return;
		}

		N = in.nextInt(); // N-by-N percolation system
		System.out.printf("N = %d\n", N);

		// repeatedly read in sites to open and draw resulting system
		Percolation perc = new Percolation(N);

		while (in.hasNext()) {
			int i = in.nextInt(); // get i for open site (i,j)
			int j = in.nextInt(); // get j for open site (i,j)
			perc.open(i, j); // open site (i,j)
			System.out.printf("open(%d, %d)\n", i, j);
		}
		if (perc.percolates()) {
			System.out.println("This system percolates");
		} else {
			System.out.println("This system does NOT percolate");
		}

		double time = (System.currentTimeMillis() - start) / 1000.0;
		System.out.println("running time = " + time + " ms");
	}
}
