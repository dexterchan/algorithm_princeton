package Collinear;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    //private List<Point[]> _collinear;
    private List<LineSegment> _segments;
    private static final int MIN_POINTS = 3;

    private static final double SLOPE_EQUAL_RANGE = 0;

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null) {
            throw new IllegalArgumentException();
        }
        checkNull(points);
        if (hasDuplicate(points)) {
            throw new IllegalArgumentException();
        }
        Point[] immutablePoints = points.clone();

        _segments = findCollinearPointsImproved(immutablePoints);
    }

    private static void checkNull(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    private static boolean hasDuplicate(Point[] points) {
        sortPoints(points, null);
        Point lastPoint = null;
        for (int i = 0; i < points.length; i++) {
            if (lastPoint != null && lastPoint.compareTo(points[i]) == 0) {
                return true;
            }
            lastPoint = points[i];
        }
        return false;
    }

    private static List<LineSegment> findCollinearPointsImproved(Point[] points) {
        //List<Point[]> collinearPoints = new ArrayList<>();
        List<LineSegment> _segments = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            sortPoints(points, null);
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
            while (end < points.length) {
                while (end < points.length && Double.compare(refPoint.slopeTo(points[start]), refPoint.slopeTo(points[end])) == 0) {
                    end++;
                }
                //Filter the segments with less than 3 points
                //and remove duplicated segments
                if (end - start >= MIN_POINTS && refPoint.compareTo(points[start]) < 0) {
                    _segments.add(new LineSegment(refPoint, points[end - 1]));
                }
                start = end;
                end++;

            }
        }
        return _segments;
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
        if (comparator != null) Arrays.sort(points, comparator);
        else Arrays.sort(points);
        return points;
    }

    public int numberOfSegments() {
        // the number of line segments
        return this._segments.size();
    }

    public LineSegment[] segments() {
//        LineSegment[] segments = new LineSegment[this.numberOfSegments()];
//
//        // the line segments
//        for (int s = 0; s < segments.length; s++) {
//            Point[] points = this._collinear.get(s);
//            segments[s] = new LineSegment(points[0], points[points.length - 1]);
//        }
        return _segments.toArray(new LineSegment[_segments.size()]);
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
        assert lineSegments.length == fast.numberOfSegments();
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
        try{
            Point[] _points = new Point[]{new Point(1, 1), null, new Point(3, 3), new Point(4, 4)};
            FastCollinearPoints _fast = new FastCollinearPoints(_points);
        }catch (IllegalArgumentException e){
            System.out.println("IllegalArgumentException");
        }
    }
}
