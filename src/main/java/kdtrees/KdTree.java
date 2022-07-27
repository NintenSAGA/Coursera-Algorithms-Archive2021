package kdtrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static class Node {
        private static final boolean VERTICAL = true;
        private static final boolean LEVEL = false;
        private final Point2D point;
        private final Node prev;
        private final boolean dir;
        private final RectHV leftRect;
        private final RectHV rightRect;
        private Node left;
        private Node right;

        private Node(Point2D point, Node prev, RectHV rect) {
            if (point == null) throw new IllegalArgumentException();

            this.point = point;
            this.left = null;
            this.right = null;
            this.prev = prev;
            this.dir = (prev == null || prev.dir == LEVEL) ? VERTICAL : LEVEL;

            if (this.dir == VERTICAL) {
                leftRect = new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
                rightRect = new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
                rightRect = new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
            }
        }
    }

    private static final boolean LEFT = true;
    private static final boolean RIGHT = false;
    private Node head;
    private int size;

    // construct an empty set of points
    public KdTree() {
        head = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return head == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (head == null) {
            head = new Node(p, null, new RectHV(0, 0, 1, 1));
            size++;
            return;
        }

        Node prev = null, cur = head;
        boolean dir = false;
        while (cur != null) {
            Point2D curP = cur.point;
            if (curP.equals(p)) break;
            prev = cur;
            dir = cur.dir ? p.x() < curP.x() : p.y() < curP.y();
            cur = dir ? cur.left : cur.right;
        }

        if (cur == null) {
            if (dir == LEFT) {
                prev.left = new Node(p, prev, prev.leftRect);
            } else {
                prev.right = new Node(p, prev, prev.rightRect);
            }
            size++;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (head == null) {
            return false;
        }

        Node cur = head;
        boolean dir = false;
        while (cur != null) {
            Point2D curP = cur.point;
            if (curP.equals(p)) return true;
            dir = cur.dir ? p.x() < curP.x() : p.y() < curP.y();
            cur = dir ? cur.left : cur.right;
        }

        return false;
    }


    // draw all points to standard draw
    public void draw() {
        Queue<Node> toVisit = new Queue<>();
        toVisit.enqueue(head);

        while (!toVisit.isEmpty()) {
            Node cur = toVisit.dequeue();
            if (cur == null) continue;

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            cur.point.draw();

            StdDraw.setPenRadius();
            if (cur.dir == Node.VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                Node n = cur.prev;
                double yMin = 0, yMax = 1;
                while (n != null) {
                    if (n.dir != cur.dir) {
                        if (n.point.y() > cur.point.y()) {
                            yMax = Math.min(yMax, n.point.y());
                        } else {
                            yMin = Math.max(yMin, n.point.y());
                        }
                    }
                    n = n.prev;
                }
                StdDraw.line(cur.point.x(), yMin, cur.point.x(), yMax);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                Node n = cur.prev;
                double xMin = 0, xMax = 1;
                while (n != null) {
                    if (n.dir != cur.dir) {
                        if (n.point.x() > cur.point.x()) {
                            xMax = Math.min(xMax, n.point.x());
                        } else {
                            xMin = Math.max(xMin, n.point.x());
                        }
                    }
                    n = n.prev;
                }
                StdDraw.line(xMin, cur.point.y(), xMax, cur.point.y());
            }

            toVisit.enqueue(cur.left);
            toVisit.enqueue(cur.right);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Queue<Node> toVisit = new Queue<>();
        Queue<Point2D> ret = new Queue<>();
        toVisit.enqueue(head);
        while (!toVisit.isEmpty()) {
            Node cur = toVisit.dequeue();
            if (cur == null) continue;

            if (rect.contains(cur.point)) ret.enqueue(cur.point);

            if ((cur.dir && cur.point.x() <= rect.xmax()) || (!cur.dir && cur.point.y() <= rect.ymax()))
                toVisit.enqueue(cur.right);
            if ((cur.dir && cur.point.x() > rect.xmin()) || (!cur.dir && cur.point.y() > rect.ymin()))
                toVisit.enqueue(cur.left);
        }

        return ret;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return new dfs(p).result();
    }

    private class dfs {
        double minDis;
        Point2D desP;
        Point2D p;
        double px, py;

        private dfs(Point2D p) {
            this.p = p;
            this.desP = null;
            this.px = p.x();
            this.py = p.y();
            minDis = Double.MAX_VALUE;
            search(head);
        }

        private void search(Node n) {
            if (n == null) return;
            double x = n.point.x(),
                    y = n.point.y();
            double dis = p.distanceTo(n.point);
            if (dis < minDis) {
                minDis = dis;
                desP = n.point;
            }

            if (n.dir == Node.VERTICAL) {
                if (px < x) {
                    search(n.left);
                    if (n.rightRect.distanceTo(p) < minDis) search(n.right);
                } else {
                    search(n.right);
                    if (n.leftRect.distanceTo(p) < minDis) search(n.left);
                }
            } else {
                if (py < y) {
                    search(n.left);
                    if (n.rightRect.distanceTo(p) < minDis) search(n.right);
                } else {
                    search(n.right);
                    if (n.leftRect.distanceTo(p) < minDis) search(n.left);
                }
            }
        }
        
        private Point2D result() {
            return desP;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
