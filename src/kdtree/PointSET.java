
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private final Set<Point2D> tree;

    public PointSET() {
        // construct an empty set of points
        tree = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        // is the set empty?
        return tree.isEmpty();
    }

    public int size() {
        // number of points in the set
        return tree.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        this.tree.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return this.tree.contains(p);
    }

    public void draw() {
        // draw all points to standard draw
        for (Point2D p : this.tree) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect==null){
            throw new IllegalArgumentException();
        }
        List<Point2D> l = new LinkedList<>();
        for (Point2D p : this.tree) {
            if (rect.contains(p)) {
                l.add(p);
            }
        }
        return l;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        double min_dist = Double.POSITIVE_INFINITY;
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.tree.isEmpty()) {
            return null;
        }
        Point2D nearestPoint = null;
        for (Point2D s : this.tree) {
            double dist = s.distanceSquaredTo(p);
            if (dist < min_dist) {
                nearestPoint = s;
                min_dist = dist;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        PointSET ps = new PointSET();

        ps.insert(new Point2D(0.7, 0.2));
        ps.insert(new Point2D(0.5, 0.4));
        ps.insert(new Point2D(0.2,0.3));
        ps.insert(new Point2D(0.4,0.7));
        ps.insert(new Point2D(0.9,0.6));

        Point2D n = ps.nearest(new Point2D(0.5, 0.8));
        assert n.equals(new Point2D(0.4,0.7));

        //ps.nearest(null);
    }
}
