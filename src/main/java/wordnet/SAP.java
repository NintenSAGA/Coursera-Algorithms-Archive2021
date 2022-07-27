package wordnet;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.digraph = new Digraph(G);
    }

    private void nullCheck(Object v) {
        if (v == null) throw new IllegalArgumentException();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v),
                bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int ancestor = ancestor(bfsV, bfsW);
        return ancestor != -1 ? bfsV.distTo(ancestor) + bfsW.distTo(ancestor) : -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v),
                bfsW = new BreadthFirstDirectedPaths(digraph, w);
        return ancestor(bfsV, bfsW);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        nullCheck(v);
        nullCheck(w);

        if (!v.iterator().hasNext() || !w.iterator().hasNext()) return -1;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v),
                bfsW = new BreadthFirstDirectedPaths(digraph, w);

        int ancestor = ancestor(bfsV, bfsW);
        return ancestor != -1 ? bfsV.distTo(ancestor) + bfsW.distTo(ancestor) : -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        nullCheck(v);
        nullCheck(w);

        if (!v.iterator().hasNext() || !w.iterator().hasNext()) return -1;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v),
                bfsW = new BreadthFirstDirectedPaths(digraph, w);

        return ancestor(bfsV, bfsW);
    }

    private int ancestor(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {
        int dis;
        int minDis = Integer.MAX_VALUE;
        int ret = -1;

        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.distTo(i) != Integer.MAX_VALUE && bfsW.distTo(i) != Integer.MAX_VALUE) {
                dis = bfsV.distTo(i) + bfsW.distTo(i);
                if (dis < minDis) {
                    minDis = dis;
                    ret = i;
                }
            }
        }

        return ret;
    }

    private static Iterable<Integer> arrayToIt(int[] a) {
        Queue<Integer> queue = new Queue<>();
        for (int i : a) queue.enqueue(i);
        return queue;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph-wordnet.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        StdOut.printf("length = %d, ancestor = %d\n",
                sap.length(SAP.arrayToIt(new int[]{202, 2801, 6684, 10085, 47799, 51449, 54100, 59958, 61785, 67235, 67468}),
                        SAP.arrayToIt(new int[]{34327, 42873, 64295})),
                sap.ancestor(SAP.arrayToIt(new int[]{202, 2801, 6684, 10085, 47799, 51449, 54100, 59958, 61785, 67235, 67468}),
                        SAP.arrayToIt(new int[]{34327, 42873, 64295})));

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
