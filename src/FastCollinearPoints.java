import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    private List<Point[]> _collinear;
    private static final int MIN_POINTS = 4;

    private static final double SLOPE_EQUAL_RANGE = 0.000001;

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

        _collinear = findCollinearPoints(immutablePoints);
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

    private static List<Point[]> findCollinearPointsImproved(Point[] points, Comparator<Point> comparator) {
        List<Point[]> collinearPoints = new ArrayList<>();

        final int p = 0;

        for (int i = 0; i < points.length; i++) {
            Point refPoint = points[i];
            //Sort the aux array with point[i] slope order
            Arrays.sort(points, refPoint.slopeOrder());
            //By exploiting self slope to be -ve infinity
            //we compare slope from position 0
            // Find the start and end positon of each segment
            //by windowing
            int start = 1, end = start+1;
            while(end < points.length){
                double refSlope = refPoint.slopeTo(points[start]);
                double cmpSlope = refPoint.slopeTo(points[end]);
                if (equalSlope(refSlope, cmpSlope)){
                    end++;
                }else{
                    if (end - start >= MIN_POINTS - 1) {
                        Point[] _points = copyPoints(points, refPoint, start, end);
                        collinearPoints.add(_points);
                    }
                    start = end;
                    end++;
                }
            }
        }
    }

    private static List<Point[]> findCollinearPoints(Point[] points) {
        List<Point[]> collinearPoints = new ArrayList<>();



        Point[] aux = new Point[points.length - 1];
        for (int i = 0; i < points.length; i++) {
            Point refPoint = points[i];

            //copy other points into aux
            for (int j = 0, c = 0; j < points.length; j++) {
                if (i != j) {
                    aux[c++] = points[j];
                }
            }

            //Sort the aux array with point[i] slope order
            Arrays.sort(aux, refPoint.slopeOrder());
//            Arrays.stream(aux).forEach(
//                    p->System.out.print(Optional.ofNullable(p).orElseThrow().toString())
//            );
//            System.out.println("<-points:"+ refPoint.toString());
//            Arrays.stream(aux).forEach(
//                    p -> System.out.print(p.slopeTo(refPoint)+",")
//            );
//            System.out.println("<--slopes:" + refPoint.toString());

            int startPt = 0;
            //Scan the slope
            for (int cnt = 0; cnt < aux.length; cnt++) {
                double refSlope = refPoint.slopeTo(aux[startPt]);
                double cmpSlope = refPoint.slopeTo(aux[cnt]);
                if (!equalSlope(refSlope, cmpSlope)) {
                    //Copy Collinar point candidates
                    if (cnt - startPt >= MIN_POINTS - 1) {
                        if (filterCollinratPointsCandidate(aux, startPt, cnt, refPoint)) {
                            Point[] _points = copyPoints(aux, refPoint, startPt, cnt);
                            collinearPoints.add(_points);
                        }
                    }
                    startPt = cnt;
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

    private static boolean filterCollinratPointsCandidate(Point[] aux, int start, int end, Point refPoint) {
        //Find the smallest point in aux
        //Cost ~N
        Point smallestPoint = aux[start];

//        for (int i=start;i<end;i++){
//            System.out.print(aux[i].toString()+",");
//        }
//        System.out.println("candidate:"+refPoint);

        for (int i = start + 1; i < end; i++) {
            if (smallestPoint.compareTo(aux[i]) > 0) {
                smallestPoint = aux[i];
            }
        }
        if (smallestPoint.compareTo(refPoint) < 0) {
            return false;
        }
//        System.out.println("SmallestPoint:"+smallestPoint.toString()+"candidate:"+refPoint.toString()+"passed"+smallestPoint.compareTo(refPoint) );
//        System.out.println("candidate:"+refPoint+"passed");
        return true;
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
        LineSegment[] segments = new LineSegment[this._collinear.size()];
        int s = 0;
        // the line segments
        for (Point[] pt : this._collinear) {
            //Find max Point and min Point
            int max_inx = pt.length - 1;
            int min_inx = 0;
            assert pt.length >= MIN_POINTS;

            for (int j = 0; j < pt.length; j++) {
                if (pt[max_inx].compareTo(pt[j]) < 0) {
                    max_inx = j;
                }
                if (pt[min_inx].compareTo(pt[j]) > 0) {
                    min_inx = j;
                }
            }
            segments[s++] = new LineSegment(
                    pt[min_inx], pt[max_inx]
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
