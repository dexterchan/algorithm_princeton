import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class KdTree {

    private static final int DIMENSION = 2;
    private static final double MIN_DIM = 0;
    private static final double MAX_DIM = 1;

    private class Node {
        Point2D value;

        Node(Node parentNode, Point2D value) {
            this.value = value;
            this.dimension = 0;
            if (parentNode != null) {
                dimension = (parentNode.dimension + 1) % DIMENSION;
            }
        }

        int dimension;
        Node left;
        Node right;

        int size;

        void insertChild(Point2D value) {
            boolean insertLeft;
            if (dimension == 0) {
                //check 1st dimension - x
                insertLeft = value.x() < this.value.x();
            } else {
                //check 2nd dimension - x
                insertLeft = value.y() < this.value.y();
            }

            if (insertLeft) {
                if (left == null) {
                    this.left = new Node(this, value);
                } else {
                    this.left.insertChild(value);
                }
            } else {
                if (right == null) {
                    this.right = new Node(this, value);
                } else {
                    this.right.insertChild(value);
                }
            }
            this.size++;
        }

        Node searchNode(Point2D value) {
            if (this.value.equals(value)) {
                return this;
            }
            boolean searchLeft;
            if (this.dimension == 0) {
                searchLeft = value.x() < this.value.x();
            } else {
                searchLeft = value.y() < this.value.y();
            }

            if (searchLeft) {
                if (left == null) {
                    return null;
                } else {
                    return this.left.searchNode(value);
                }
            } else {
                if (right == null) {
                    return null;
                } else {
                    return this.right.searchNode(value);
                }
            }
        }

    }

    private Node root;

    public KdTree() {
        // construct an empty set of points
        this.root = null;
    }

    public boolean isEmpty() {
        // is the set empty?
        return (root == null);
    }

    public int size() {
        // number of points in the set
        if (this.root == null)
            return 0;
        return this.root.size + 1;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.root == null) {
            this.root = new Node(null, p);
        } else {
            if (!this.contains(p)) {
                this.root.insertChild(p);
            }
        }
    }

    public boolean contains(Point2D p) {
        // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (this.isEmpty()) {
            return false;
        }
        return (this.root.searchNode(p) != null);
    }

    public void draw() {
        // draw all points to standard draw
        if (this.isEmpty()) {
            return;
        }
        Node node = null;
        Stack<Node> stack = new Stack<Node>();
        stack.push(this.root);
        //DFS to traverse all points
        while (!stack.isEmpty()) {
            node = stack.pop();
            Point2D pt = node.value;
            StdDraw.point(pt.x(), pt.y());
            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> finalAns = new LinkedList<>();
        Stack<Node> stack = new Stack<>();
        stack.push(this.root);

        while (!stack.isEmpty()) {
            Node node = stack.pop();
            Point2D point = node.value;
            if (rect.contains(point)) {
                finalAns.add(point);
            }

            //Check left subtree
            if (node.left != null) {
                RectHV leftRect = createLeft(node.dimension, point);
                if (leftRect.intersects(rect)) {
                    stack.push(node.left);
                }
            }

            //Check right substree
            if (node.right != null) {
                RectHV rightRect = createRight(node.dimension, point);
                if (rightRect.intersects(rect)) {
                    stack.push(node.right);
                }
            }
        }
        return finalAns;
    }

    private static RectHV createLeft(int dimension, Point2D pt) {
        double xmin = MIN_DIM;
        double xmax = MAX_DIM;
        double ymin = MIN_DIM;
        double ymax = MAX_DIM;
        if (dimension == 0) {
            //coor-x
            xmax = pt.x();
        } else {
            //coor-y
            ymax = pt.y();
        }
        return new RectHV(xmin, ymin, xmax, ymax);
    }

    private static RectHV createRight(int dimension, Point2D pt) {
        double xmin = MIN_DIM;
        double xmax = MAX_DIM;
        double ymin = MIN_DIM;
        double ymax = MAX_DIM;
        if (dimension == 0) {
            //coor-x
            xmin = pt.x();
        } else {
            //coor-y
            ymin = pt.y();
        }
        return new RectHV(xmin, ymin, xmax, ymax);
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        return null;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
        KdTree kdTree = null;

        //Check contains
        testContains();
        testRange();
    }

    private static void testContains() {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));

        assert kdTree.size() == 5;
        assert kdTree.contains(new Point2D(0.9, 0.6));
        assert !kdTree.contains(new Point2D(0.9, 0.5));
    }

    private static void testRange() {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));

        RectHV searchRange = null;

        List<Point2D> ans = null;
        //case 1
        searchRange = new RectHV(0.1, 0.2, 0.6, 0.8);
        ans = (List<Point2D>) kdTree.range(searchRange);
        assert ans.size() == 3;
        assert ans.contains(new Point2D(0.2,0.3));
        assert ans.contains(new Point2D(0.4,0.7));
        assert ans.contains(new Point2D(0.5,0.4));

        //case 2
        searchRange = new RectHV(0.1, 0.2, 0.15, 0.25);
        ans = (List<Point2D>) kdTree.range(searchRange);
        assert ans.size() == 0;

        //case 3
        searchRange = new RectHV(0.6, 0.1, 0.95, 0.7);
        ans = (List<Point2D>) kdTree.range(searchRange);
        assert ans.size() == 2;
        assert ans.contains(new Point2D(.7,0.2));
        assert ans.contains(new Point2D(.9,0.6));

    }

}
