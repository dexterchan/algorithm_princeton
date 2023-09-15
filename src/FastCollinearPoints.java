import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    private List<Point[]> _collinear;
    private static final int MIN_POINTS = 3;

    private static final double SLOPE_EQUAL_RANGE = 0;

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null) {
            throw new IllegalArgumentException();
        }
        if (hasDuplicate(points)) {
            throw new IllegalArgumentException();
        }
        Point[] immutablePoints = points.clone();
        //1) sort the points first
        immutablePoints = sortPoints(immutablePoints, null);

        _collinear = findCollinearPointsImproved(immutablePoints);
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

    private static boolean equalSlope(double slope1, double slope2) {
//        if (slope1 == Double.NEGATIVE_INFINITY || slope2 == Double.NEGATIVE_INFINITY) {
//            return false;
//        }
        return Math.abs(slope1 - slope2) < SLOPE_EQUAL_RANGE;
    }

    private static List<Point[]> findCollinearPointsImproved(Point[] points) {
        List<Point[]> collinearPoints = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            sortPoints(points,null);
            Point refPoint = points[i];
            //Sort the aux array with point[i] slope order
            sortPoints(points, refPoint.slopeOrder());
//            Arrays.stream(points).forEach(
//                    pp->System.out.print(pp.toString())
//            );
//            System.out.println("<-points:"+ refPoint.toString());
//            Arrays.stream(points).forEach(
//                    pp -> System.out.print(pp.slopeTo(refPoint)+",")
//            );
            //System.out.println("<--slopes:" + refPoint.toString());
            //By exploiting self slope to be -ve infinity
            //we compare slope from position 0
            // Find the start and end position of each segment
            //by windowing
            int start = 1, end = 2;
            while(end < points.length){
                double refSlope = refPoint.slopeTo(points[start]);
                double cmpSlope = refPoint.slopeTo(points[end]);
                if (Double.compare(refSlope, cmpSlope)==0){
                    end++;
                }else{
                    if (end - start >= MIN_POINTS && refPoint.compareTo(points[start]) < 0 ) {
                        Point[] _points = copyPoints(points, refPoint, start, end);
                        collinearPoints.add(_points);
                    }
                    start = end;
                    end++;
                }
            }
        }
        return collinearPoints;
    }


    private static Point[] copyPoints(Point[] aux, Point refPoint, int start, int end) {
        Point[] _points = new Point[end - start + 1];
        _points[0] = refPoint;
        for (int i = 1; i <= end - start; i++) {
            _points[i] = aux[start + i];
        }
        return _points;
    }

    private static Point[] sortPoints(Point[] points, Comparator<Point> comparator) {
        if (comparator != null)
            Arrays.sort(points, comparator);
        else
            Arrays.sort(points);
        return points;
    }

    public int numberOfSegments() {
        // the number of line segments
        return this._collinear.size();
    }

    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[this.numberOfSegments()];

        // the line segments
        for (int s=0 ; s < segments.length; s++) {
            Point[] points = this._collinear.get(s);
            segments[s] = new LineSegment(
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
        FastCollinearPoints fast = new FastCollinearPoints(points);
        LineSegment[] lineSegments = fast.segments();
        for (LineSegment s : lineSegments) {
            System.out.println("Segments:" + s.toString());
        }

        // draw the points
//        StdDraw.enableDoubleBuffering();
//        StdDraw.setXscale(0, 32768);
//        StdDraw.setYscale(0, 32768);
//        for (Point p : points){
//            p.draw();
//        }
//        for (LineSegment s : lineSegments) {
//            s.draw();
//        }
    }
}
