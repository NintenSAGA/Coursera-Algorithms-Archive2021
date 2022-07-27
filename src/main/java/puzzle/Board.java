package puzzle;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private final int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null || tiles[0] == null) throw new IllegalArgumentException();
        this.dimension = tiles.length;
        this.tiles = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int tile = tiles[i][j];
                this.tiles[i][j] = tile;
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n", dimension));
        for (int[] row: tiles) {
            for (int tile : row) {
                sb.append(String.format("%2d ", tile));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int countHamming = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int tile = tiles[i][j];
                if (tile != 0) {
                    // hamming
                    if (tile != dimension*i + j + 1) countHamming++;
                }
            }
        }
        return countHamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int countManhattan = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int tile = tiles[i][j];
                if (tile != 0) {
                    // manhattan
                    countManhattan += Math.abs(i - (tile-1) / dimension)
                            + Math.abs(j - ((tile % dimension) == 0 ? dimension : tile % dimension) + 1);
                }
            }
        }
        return countManhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int or = 0, oc = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] == 0) {
                    // blank
                    or = i;
                    oc = j;
                }
            }
        }

        Queue<Board> ret = new Queue<>();
        int[][] newTiles;

        for (int[] dir: new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}}) {
            int dr = or + dir[0], dc = oc + dir[1];
            if (dr < dimension && dc < dimension && dr >= 0 && dc >= 0) {
                newTiles = new int[dimension][];
                for (int i = 0; i < dimension; i++) newTiles[i] = Arrays.copyOf(tiles[i], dimension);
                newTiles[or][oc] = newTiles[dr][dc];
                newTiles[dr][dc] = 0;
                ret.enqueue(new Board(newTiles));
            }
        }

        return ret;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        boolean exitFlag = false;
        int[][] newTiles = new int[dimension][];
        for (int i = 0; i < dimension; i++) newTiles[i] = Arrays.copyOf(tiles[i], dimension);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension-1; j++) {
                if (newTiles[i][j] != 0 && newTiles[i][j+1] != 0) {
                    int temp = newTiles[i][j];
                    newTiles[i][j] = newTiles[i][j+1];
                    newTiles[i][j+1] = temp;
                    exitFlag = true;
                    break;
                }
            }
            if (exitFlag) break;
        }
        return new Board(newTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
//        Board board = new Board(new int[][]{
//                {8, 1, 3},
//                {4, 0, 2},
//                {7, 6, 5}
//        });
//        StdOut.println(board.equals("ssss"));
//        Queue<Board> q = new Queue<>();
//        q.enqueue(board);
//        for (Board b: board.neighbors()) q.enqueue(b);
//        q.enqueue(board.twin());
//
//        for (Board b: q) StdOut.printf("%smanhattan = %d\nhamming = %d\n\n", b, b.manhattan, b.hamming);
    }
}