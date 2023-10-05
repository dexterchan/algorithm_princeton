import edu.princeton.cs.algs4.StdDraw;

public class RectHV {
    private final Point2D upperLeft;
    private final Point2D lowerRight;

    private final Point2D lowerLeft;

    private final Point2D upperRight;

    public RectHV(double xmin, double ymin, double xmax, double ymax) {
        // construct the rectangle [xmin, xmax] x [ymin, ymax]
        // throw an IllegalArgumentException if (xmin > xmax) or (ymin > ymax)

        if ((xmin > xmax) || (ymin > ymax)) {
            throw new IllegalArgumentException("(xmin > xmax) or (ymin > ymax)");
        }
        this.upperLeft = new Point2D(xmin, ymax);
        this.lowerRight = new Point2D(xmax, ymin);
        this.lowerLeft = new Point2D(xmin, ymin);
        this.upperRight = new Point2D(xmax, ymax);

    }

    public double xmin() {
        // minimum x-coordinate of rectangle
        return this.upperLeft.x();
    }

    public double ymin() {
        // minimum y-coordinate of rectangle
        return this.lowerRight.y();
    }

    public double xmax() {
        // maximum x-coordinate of rectangle
        return this.lowerRight.x();
    }

    public double ymax() {
        // maximum y-coordinate of rectangle
        return this.upperLeft.y();
    }

    public boolean contains(Point2D p) {
        // does this rectangle contain the point p (either inside or on boundary)?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.xmin() <= p.x() && p.x() <= this.xmax()) {
            if (this.ymin() <= p.y() && p.y() <= this.ymax()) {
                return true;
            }
        }
        return false;
    }

    public boolean intersects(RectHV that) {
        // does this rectangle intersect that rectangle (at one or more points)?
        if (that == null) {
            throw new IllegalArgumentException();
        }
        RectHV leftRect = this.upperLeft.compareTo(that.upperLeft) < 0 ? this : that;
        RectHV rightRect = leftRect.equals(this) ? that : this;
        //Find overlapping along x-coordinates
        if (rightRect.xmin() > leftRect.xmax()) {
            return false;
        }

        double lowY1 = leftRect.ymin();
        double highY1 = leftRect.ymax();
        double lowY2 = rightRect.ymin();
        //windowing to find overlap
        if (leftRect.ymin() > rightRect.ymin()) {
            lowY1 = rightRect.ymin();
            highY1 = rightRect.ymax();
            lowY2 = leftRect.ymin();
        }
        assert lowY1 <= lowY2;
        if (highY1 <= lowY2) {
            return false;
        }
        return true;
    }

    public double distanceTo(Point2D p) {
        // Euclidean distance from point p to closest point in rectangle
        return Math.sqrt(this.distanceSquaredTo(p));
    }

    public double distanceSquaredTo(Point2D p) {
        // square of Euclidean distance from point p to closest point in rectangle
        if (p == null) {
            throw new IllegalArgumentException();
        }
        //check if p in rectangle
        if (this.contains(p)) {
            return 0;
        }

        //Check if p projects on horizonal line
        if (p.x() >= this.xmin() && p.x() < this.xmax()) {
            double d1 = this.ymin() - p.y();
            double d2 = this.ymax() - p.y();
            return Math.min(d1 * d1, d2 * d2);
        }
        //Check if p projects on vertical line
        if (p.y() >= this.ymin() && p.y() < this.ymax()) {
            double d1 = this.xmin() - p.x();
            double d2 = this.xmax() - p.x();
            return Math.min(d1 * d1, d2 * d2);
        }

        double minDistance = p.distanceSquaredTo(this.upperLeft);
        minDistance = Math.min(minDistance, p.distanceSquaredTo(this.lowerRight));
        minDistance = Math.min(minDistance, p.distanceSquaredTo(this.lowerLeft));
        minDistance = Math.min(minDistance, p.distanceSquaredTo(this.upperRight));
        return minDistance;
    }

    public boolean equals(Object that) {
        // does this rectangle equal that object?
        if (that == null) {
            throw new IllegalArgumentException();
        }
        if (!(that instanceof RectHV)) {
            throw new IllegalArgumentException();
        }
        RectHV r = (RectHV) that;
        return this.upperLeft.equals(r.upperLeft) && this.lowerRight.equals(r.lowerRight);
    }

    public void draw() {
        // draw to standard draw
        StdDraw.filledRectangle(this.upperLeft.x(), this.upperLeft.y(), this.lowerRight.x() - this.upperLeft.x(), this.lowerRight.y() - this.upperLeft.y());
    }

    public String toString() {
        // string representation
        return "(" + this.upperLeft + "," + this.lowerRight + ")";
    }

    public static void main(String[] args) {
        //Testing intersects
        RectHV h1 = new RectHV(0.4, 0.3, 0.8, 0.6);
        RectHV h2 = new RectHV(0.7, 0.35, 1, 0.5);

        assert h1.intersects(h2);

        System.out.println(h1.distanceTo(new Point2D(0, 0)));

        assert !h1.intersects(new RectHV(-0.1, -0.5, 0.3, 0.8));
        assert h1.intersects(new RectHV(-0.1, -0.5, 0.5, 0.8));
        assert h1.intersects(new RectHV(-0.1, -0.5, 0.8, 0.8));
        assert !h1.intersects(new RectHV(-0.1, -0.5, 0.8, 0.3));
        assert !h1.intersects(new RectHV(-0.1, -0.5, 0.8, 0.29));

    }
}
