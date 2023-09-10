import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
//import utility.Timer;

import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;

public class FastCollinearPoints {

    private List<Point[]> _collinear;
    private static final int MIN_POINTS = 4;

    private static final boolean SEARCH_IS_CONSISTENT = true;

    private static final double SLOPE_EQUAL_RANGE = 0.000001;

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null) {
            throw new IllegalArgumentException();
        }
        Point[] immutablePoints = copyImmutablePoints(points);

        _collinear = findCollinearPoints(immutablePoints);
    }

    private static Point[] copyImmutablePoints(Point[] points){
        Point[] copy = new Point[points.length];
        for(int i=0;i<points.length;i++){
            copy[i] = points[i];
        }
        return copy;
    }

    private static boolean equalSlope(double slope1, double slope2) {
        return Math.abs(slope1 - slope2) < SLOPE_EQUAL_RANGE;
    }

    private static List<Point[]> findCollinearPoints(Point[] points) {
        List<Point[]> collinearPoints = new ArrayList<>();
        //1) sort the pints first
        sortPoints(points, null);


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

        for (int i = start + 1; i < end && (!SEARCH_IS_CONSISTENT); i++) {
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
            for (int j = 0; j < pt.length && (!SEARCH_IS_CONSISTENT); j++) {
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
        //utility.Timer timer = new Timer();
        FastCollinearPoints fast = new FastCollinearPoints(points);
        LineSegment[] lineSegments = fast.segments();
        //System.out.println("Time elapsed: " + timer.elapsedTime());

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
