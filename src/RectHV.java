public class RectHV {
    private final Point2D upperLeft;
    private final Point2D lowerRight;

    public RectHV(double xmin, double ymin,
                  double xmax, double ymax) {
        // construct the rectangle [xmin, xmax] x [ymin, ymax]
        // throw an IllegalArgumentException if (xmin > xmax) or (ymin > ymax)

        if ((xmin > xmax) || (ymin > ymax)) {
            throw new IllegalArgumentException("(xmin > xmax) or (ymin > ymax)");
        }
        this.upperLeft = new Point2D(xmin, ymax);
        this.lowerRight = new Point2D(xmax, ymin);

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
        RectHV leftRect = this.upperLeft.compareTo(that.upperLeft) < 0 ? this : that;
        RectHV rightRect = leftRect.equals(this) ? that : this;
        //Find overlapping along x-coordinates
        if (rightRect.xmin() > leftRect.xmax() ){
            return false;
        }

        return true;
    }

    public double distanceTo(Point2D p)            // Euclidean distance from point p to closest point in rectangle

    public double distanceSquaredTo(Point2D p)     // square of Euclidean distance from point p to closest point in rectangle

    public boolean equals(Object that)              // does this rectangle equal that object?

    public void draw()                           // draw to standard draw

    public String toString()                       // string representation
}
