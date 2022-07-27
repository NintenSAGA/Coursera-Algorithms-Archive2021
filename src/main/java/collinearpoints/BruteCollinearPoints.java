package collinearpoints;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] segments;
    private int segNum;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point p: points) if (p == null) throw new IllegalArgumentException();

        segNum = 0;
        int n = points.length;
        Point[] oPoints = Arrays.copyOf(points, n);
        Arrays.sort(oPoints);

        for (int i = 0; i < n-1; i++) if (oPoints[i].slopeTo(oPoints[i+1]) == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();

        Queue<LineSegment> temp = new Queue<>();

        for (int i = 0; i < n-3; i++) {
            boolean breakFlag = false;
            for (int j = i+1; j < n-2; j++) {
                double slope1 = oPoints[i].slopeTo(oPoints[j]);
                for (int k = j+1; k < n-1;k++) {
                    double slope2 = oPoints[j].slopeTo(oPoints[k]);
                    if (slope1 != slope2) continue;
                    for (int l = k+1; l < n; l++) {
                        double slope3 = oPoints[k].slopeTo(oPoints[l]);
                        if (slope1 == slope3) {
                            temp.enqueue(new LineSegment(oPoints[i], oPoints[l]));
                            segNum++;
                            breakFlag = true;
                            break;
                        }
                    }
                    if (breakFlag) break;
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