package burrowswheeler;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);
        StringBuilder sb = new StringBuilder();
        int l = suffixArray.length();

        for (int i = 0; i < l; i++) {
            int tail = suffixArray.index(i);
            if (tail == 0) {
                BinaryStdOut.write(i, 32);
            }
            sb.append(s.charAt((suffixArray.index(i) - 1 + l) % l));
        }
        BinaryStdOut.write(sb.toString());

        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        long time = System.currentTimeMillis();

        int first = BinaryStdIn.readInt();
        char[] t = BinaryStdIn.readString().toCharArray();
        char[] h = indexCountingSort(t);
        int[] next = new int[t.length];
        Queue<Integer>[] radix = (Queue<Integer>[]) new Queue[256];

        for (int i = 0; i < h.length; i++) {
            char c = t[i];
            if (radix[c] == null)
                radix[c] = new Queue<>();
            radix[c].enqueue(i);
        }

        for (int i = 0; i < h.length; i++) {
            next[i] = radix[h[i]].dequeue();
        }

        StringBuilder sb = new StringBuilder();
        for (int j = 0, i = first; j < next.length; j++) {
            sb.append(h[i]);
            i = next[i];
        }

        BinaryStdOut.write(sb.toString());

        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static char[] indexCountingSort(char[] o) {
        int[] radix = new int[256+1];
        char[] ret = new char[o.length];

        for (int c : o)
            radix[c+1]++;
        for (int i = 1; i < 256+1; i++)
            radix[i] += radix[i-1];
        for (char c : o) {
            ret[radix[c]] = c;
            radix[c]++;
        }
        return ret;
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        switch (args[0]) {
            case "-":
                transform();
                return;
            case "+":
                long t = System.currentTimeMillis();
                inverseTransform();
                StdOut.println((double) (System.currentTimeMillis()-t)*0.001);
                return;
            default:
        }
    }

}