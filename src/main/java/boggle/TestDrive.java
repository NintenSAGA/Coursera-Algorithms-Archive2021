package boggle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Timer;

public class TestDrive {
    public static void main(String[] args) {
        String[] myArg = "dictionary-algs4.txt board4x4.txt".split(" ");
//        In in = new In(myArg[0]);
        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
//        BoggleBoard board = new BoggleBoard();
        BoggleBoard board = new BoggleBoard(myArg[1]);

//        int score = 0;
//        for (String word : solver.getAllValidWords(board)) {
//            StdOut.println(word);
//            score += solver.scoreOf(word);
//        }
//        StdOut.println("Score = " + score);

        long st = System.currentTimeMillis();
        long count = 0;
        while (System.currentTimeMillis() - st < 5000) {
            solver.getAllValidWords(board);
            count++;
        }

        StdOut.println((double) count/5);
    }

}
