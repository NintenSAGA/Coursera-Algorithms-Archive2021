package burrowwheeler;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int[] ascii = new int[256];
        for (int i = 0; i < 256; i++) ascii[i] = i;

        while (!BinaryStdIn.isEmpty()) {
            int c = BinaryStdIn.readChar();
            BinaryStdOut.write(ascii[c], 8);
            if (ascii[c] != 0) {
                for (int i = 0; i < 256; i++) {
                    if (ascii[i] < ascii[c])
                        ascii[i]++;
                }
                ascii[c] = 0;
            }
        }

        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        int[] ascii = new int[256];
        for (int i = 0; i < 256; i++) ascii[i] = i;

        while (!BinaryStdIn.isEmpty()) {
            int c = BinaryStdIn.readInt(8);
            BinaryStdOut.write((char) ascii[c]);
            if (c != 0) {
                int temp = ascii[c];
                System.arraycopy(ascii, 0, ascii, 1, c);
                ascii[0] = temp;
            }
        }

        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        switch (args[0]) {
            case "-":
                encode();
                return;
            case "+":
                decode();
                return;
            default:
        }
    }

}

//    Performance requirements.
//        The running time of BinaryStdOutth move-to-front encoding and decoding
//        must be proportional to n R (or better) in the worst case and
//        proportional to n + R (or better) on inputs that arise when compressing
//        typical English text, where n is the number of characters in the
//        input and R is the alphabet size. The amount of memory used by BinaryStdOutth
//        move-to-front encoding and decoding must be proportional to n + R (or better)
//        in the worst case.

