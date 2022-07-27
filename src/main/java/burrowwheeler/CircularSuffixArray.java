package burrowwheeler;

import java.util.Arrays;

public class CircularSuffixArray {
    private final int length;
    private final FakeString[] index;

    private class FakeString implements Comparable<FakeString> {
        private final String s;
        private final int st;

        private FakeString(String s, int st) {
            this.s = s;
            this.st = st;
        }

        private int getSt() {
            return st;
        }

        private char charAt(int i) {
            if (i + st < length) {
                return s.charAt(i + st);
            } else {
                return s.charAt(i + st - length);
            }
        }

        @Override
        public int compareTo(FakeString o) {
            for (int i = 0; i < length; i++) {
                int ret = Character.compare(this.charAt(i), o.charAt(i));
                if (ret != 0)
                    return ret;
            }
            return 0;
        }
    }



    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();

        length = s.length();
        index = new FakeString[length];

        for (int i = 0; i < length; i++) {
            index[i] = new FakeString(s, i);
        }

        Arrays.sort(index);
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length) throw new IllegalArgumentException();

        return index[i].getSt();
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}