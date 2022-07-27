package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF union;
    private final WeightedQuickUnionUF unionFull;
    private final boolean[][] status;
    private final int n;
    private int openNum = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        union = new WeightedQuickUnionUF(n*n+2);
        unionFull = new WeightedQuickUnionUF(n*n+2);
        status = new boolean[n+1][n+1];
        for (boolean[] row: status) {
            for (int i = 0; i < row.length; i++) row[i] = false;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        argumentCheck(row, col);

        if (!isOpen(row, col)) {
            status[row][col] = true;
            openNum++;

            if (row == 1) {
                union.union(0, unionCode(row, col));
                unionFull.union(0, unionCode(row, col));
            }

            if (row == n) {
                union.union(n*n+1, unionCode(row, col));
            }

            for (int[] dir: new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}}) {
                try {
                    int x = row+dir[0];
                    int y = col+dir[1];
                    if (status[x][y]) {
                        union.union(unionCode(x, y), unionCode(row, col));
                        unionFull.union(unionCode(x, y), unionCode(row, col));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        argumentCheck(row, col);
        return status[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        argumentCheck(row, col);
        return unionFull.find(0) == unionFull.find(unionCode(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openNum;
    }

    // does the system percolate?
    public boolean percolates() {
        return union.find(0) == union.find(n*n+1);
    }

    private int unionCode(int row, int col) {
        return col + (row-1)*n;
    }

    private void argumentCheck(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        // empty body
    }
}