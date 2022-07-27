package begin;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champ = null;
        for (int i = 1;!StdIn.isEmpty();i++) {
            String next = StdIn.readString();
            if (StdRandom.bernoulli((double)1/i)) {
                champ = next;
            }
        }

        StdOut.println(champ);
    }
}
