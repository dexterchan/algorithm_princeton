import edu.princeton.cs.algs4.StdDraw;

public class Point2D_notneed implements Comparable<Point2D_notneed> {
    private final double x;
    private final double y;

    public Point2D_notneed(double x, double y) {
        // construct the point (x, y)
        this.x = x;
        this.y = y;
    }

    public double x() {
        // x-coordinate
        return this.x;
    }

    public double y() {
        // y-coordinate
        return this.y;
    }

    public double distanceTo(Point2D_notneed that) {
        // Euclidean distance between two points
        if (that == null)
            throw new IllegalArgumentException();

        return Math.sqrt(this.distanceSquaredTo(that));
    }

    public double distanceSquaredTo(Point2D_notneed that) {
        // square of Euclidean distance between two points
        if (that == null)
            throw new IllegalArgumentException();

        double distX = this.x - that.x;
        double distY = this.y - that.y;
        return distX * distX + distY * distY;
    }

    public int compareTo(Point2D_notneed that) {
        // for use in an ordered symbol table
        if (that == null)
            throw new IllegalArgumentException();
        if (this.x - that.x < 0) {
            return -1;
        } else if (this.x - that.x > 0) {
            return 1;
        }

        if (this.y - that.y < 0)
            return -1;
        else if (this.y - that.y > 0)
            return 1;
        else
            return 0;
    }

    public boolean equals(Object that) {
        // does this point equal that object?
        if (that == null) {
            throw new IllegalArgumentException();
        }

        if (!(that instanceof Point2D_notneed)) {
            return false;
        }

        Point2D_notneed thatPoint = (Point2D_notneed) that;
        return (this.x == thatPoint.x) && (this.y == thatPoint.y);
    }

    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    public String toString() {
        // string representation
        return "(" + this.x + "," + this.y + ")";
    }
}
