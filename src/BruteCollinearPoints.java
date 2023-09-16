//https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<Point[]> _collinear;

    private static final double INVALID_SLOPE = Double.NEGATIVE_INFINITY;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        if (points == null) {
            throw new IllegalArgumentException();
        }
        if (hasDuplicate(points)) {
            throw new IllegalArgumentException();
        }
        Point[] immutablePoints = points.clone();
        //1) sort the points first
        immutablePoints = sortPoints(immutablePoints);

        //2) find collinear points
        this._collinear = findCollinearPoints(immutablePoints);


    }

    private static boolean hasDuplicate(Point[] points){
        Point lastPoint = null;
        for (int i = 0; i < points.length; i++) {
            if (lastPoint != null && lastPoint.compareTo(points[i]) == 0) {
                return true;
            }
            lastPoint = points[i];
        }
        return false;
    }

    private static ArrayList<Point[]> findCollinearPoints(Point[] points) {
        ArrayList<Point[]> _collinear = new ArrayList<>();
        //The cost is ~ N^4
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        //System.out.println(i + "," + j + "," + k + "," + l);
                        double slopeij = points[i].slopeTo(points[j]);
                        double slopejk = points[j].slopeTo(points[k]);
                        double slopekl = points[k].slopeTo(points[l]);
                        if (Double.compare(slopeij, slopejk)==0 &&
                                Double.compare(slopejk, slopekl)==0 ) {
                            if (slopeij == INVALID_SLOPE) {
                                //System.out.println("Invalid slope"+points[i].toString()+","+points[j].toString()+","+points[k].toString()+","+points[l].toString());
                                continue;
                            }

                            Point[] p = {points[i], points[j], points[k], points[l]};
                            _collinear.add(p);
                        }
                    }
                }
            }
        }
        return _collinear;
    }

    private static Point[] sortPoints(Point[] points) {
        Arrays.sort(points);
        return points;
    }

    public int numberOfSegments() {
        // the number of line segments
        return this._collinear.size();
    }

    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[this.numberOfSegments()];

        int cnt = 0;
        while (cnt < this.numberOfSegments()) {
            Point[] points = this._collinear.get(cnt);
//            for (Point pt : points) {
//                System.out.print(pt.toString() + ",");
//            }
//            System.out.println();
            segments[cnt++] = new LineSegment(
                    points[0], points[points.length - 1]
            );

        }
        return segments;
    }

    private static Point[] readPoints(String path) {
        In in = new In(path);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        return points;
    }

    public static void main(String[] args) {
        Point[] points = readPoints(args[0]);
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        LineSegment[] lineSegments = brute.segments();
        for (LineSegment s : lineSegments) {
            System.out.println("Segments:" + s.toString());
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
//        for (Point p : points){
//            p.draw();
//        }
        for (LineSegment s : lineSegments) {
            s.draw();
        }
    }
}
