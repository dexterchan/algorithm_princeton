package Collinear; /******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    private final double POSITIVE_MAX_SLOPE = Double.POSITIVE_INFINITY;
    private final double NEGATIVE_MAX_SLOPE = Double.NEGATIVE_INFINITY;

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (that == null) {
            throw new NullPointerException("Point is null");
        }

        if (this.compareTo(that) == 0) {
            return NEGATIVE_MAX_SLOPE;
        }

        if (that.x == this.x) {
            return this.POSITIVE_MAX_SLOPE;
        }

        if (this.y == that.y) {
            return 0;
        } else {
            return ((double) (that.y - this.y)) / (that.x - this.x);
        }


    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        if (that == null) {
            throw new NullPointerException("Point is null");
        }

        if (this.y != that.y) {
            return this.y - that.y;
        }
        return this.x - that.x;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                double slope1 = Point.this.slopeTo(o1);
                double slope2 = Point.this.slopeTo(o2);
                return Double.compare(slope1, slope2);
            }
        };
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

//        Arrays.sort(points);
//        for (Point p : points)
//            System.out.println(p.toString());
        Point p, q, r;

        p = new Point(0, 2);
        q = new Point(0, 5);
        r = new Point(0, 2);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;
        assert p.slopeTo(r) == Double.NEGATIVE_INFINITY;
        assert p.slopeOrder().compare(q, r) == 1;


        //Test symmetry
        p = new Point(193, 293);
        q = new Point(193, 115);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;
        assert q.slopeTo(p) == Double.POSITIVE_INFINITY;

        p = new Point(1, 1);
        q = new Point(1, 1);
        assert p.slopeTo(q) == Double.NEGATIVE_INFINITY;

        p = new Point(332, 313);
        q = new Point(332, 229);
        r = new Point(403, 111);
        assert p.slopeOrder().compare(q, r) == 1;

        p = new Point(1, 1);
        q = new Point(1, 100);
        r = new Point(1, 100);
        assert p.slopeOrder().compare(q, r) == 0;

        p = new Point(1, 1);
        q = new Point(1, -100);
        r = new Point(1, 100);
        assert p.slopeOrder().compare(q, r) == 0;


//        // draw the points
//        StdDraw.enableDoubleBuffering();
//        StdDraw.setXscale(0, 32768);
//        StdDraw.setYscale(0, 32768);
//        for (Point p : points) {
//            p.draw();
//        }
//
//        StdDraw.show();
    }


}