package boggle;

import java.util.Arrays;
import java.util.TreeSet;

public class BoggleSolver
{
    private final MyTST<Boolean> tst;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();

        tst = new MyTST<>();

        for (String word : dictionary) {
            tst.put(word, true);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();

        TreeSet<String> ret = new TreeSet<>();
        int cols = board.cols();
        int rows = board.rows();
        myDFS dfs = new myDFS(ret, board);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                dfs.dfs(null, col, row, -1);
            }
        }

        return ret;
    }

    private class myDFS {
        private final boolean[][] marked;
        private final StringBuilder sb;
        private final TreeSet<String> ret;
        private final BoggleBoard board;
        private final int rows;
        private final int cols;

        private myDFS(TreeSet<String> ret, BoggleBoard board) {
            rows = board.rows();
            cols = board.cols();
            this.marked = new boolean[board.rows()][board.cols()];
            for (boolean[] r : marked) Arrays.fill(r, false);
            this.board = board;
            this.ret = ret;
            this.sb = new StringBuilder();
        }

        private void dfs(MyTST.Node<Boolean> node, int col, int row, int d) {
            if (row < 0 || row >= rows || col < 0 || col >= cols)
                return;
            if (marked[row][col])
                return;

            char c = board.getLetter(row, col);
            sb.append(c);

            if (c == 'Q') {
                sb.append('U');
            }

            String word = sb.toString();

            MyTST.Node<Boolean> nxt = tst.hasPrefix(node, word, d);
            if (nxt != null) {
                marked[row][col] = true;

                if (word.length() >= 3 && nxt.val != null)
                    ret.add(word);

                for (int[] dir : new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1},
                        {1, 1}, {-1, -1}, {-1, 1}, {1, -1}}) {
                    int dc = dir[0];
                    int dr = dir[1];
                    if (c != 'Q') dfs(nxt, col+dc, row+dr, d+1);
                    else dfs(nxt, col+dc, row+dr, d+2);
                }
            }

            if (c == 'Q') sb.deleteCharAt(sb.length()-1);
            sb.deleteCharAt(sb.length()-1);
            marked[row][col] = false;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException();

        if (word.length() < 3 || !tst.contains(word)) return 0;

        switch (word.length()) {
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }
}