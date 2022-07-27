package collinearpoints;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] segments;
    private int segNum;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point p: points) if (p == null) throw new IllegalArgumentException();

        int n = points.length;
        Point[] oPoints = Arrays.copyOf(points, n);
        Point[] slopeSorted = new Point[n];
        Queue<LineSegment> temp = new Queue<>();
        segNum = 0;

        Arrays.sort(oPoints);
        for (int i = 0; i < n; i++) {
            if (i < n-1 && oPoints[i].slopeTo(oPoints[i+1]) == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();
            slopeSorted[i] = oPoints[i];
        }

        for (Point p: oPoints) {
            Arrays.sort(slopeSorted, p.slopeOrder());
            for (int i = 1; i < n-2; i++) {
                if (slopeSorted[i].equals(slopeSorted[i-1])) throw new IllegalArgumentException();
                if (p.slopeTo(slopeSorted[i]) == p.slopeTo(slopeSorted[i+1])
                && p.slopeTo(slopeSorted[i+1]) == p.slopeTo(slopeSorted[i+2])) {
                    int o = i;
                    i += 2;
                    while (i < n-1 && p.slopeTo(slopeSorted[i]) == p.slopeTo(slopeSorted[i+1])) i++;
                    Point[] sub = Arrays.copyOfRange(slopeSorted, o, i+2);
                    sub[i-o+1] = p;
                    Arrays.sort(sub);
                    if (sub[0] == p) {
                        temp.enqueue(new LineSegment(p, sub[i-o+1]));
                        segNum++;
                    }
                    i--;
                }
            }
        }

        segments = new LineSegment[segNum];
        for (int i = 0; i < segNum; i++) segments[i] = temp.dequeue();
    }

    // the number of line segments
    public int numberOfSegments() {
        return segNum;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segNum);
    }
}