import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class SAP {

    private static final class Node {
        int distance;
        int vertex;

        Node(int d, int v) {
            distance = d;
            vertex = v;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            Node node = (Node) obj;
            return this.vertex == node.vertex;
        }

        @Override
        public int hashCode() {
            return 17 * ((Integer) vertex).hashCode();
        }

        Iterable<Node> moveNext(Digraph G) {
            List<Node> nextNodes = new LinkedList<>();
            for (int nextV : G.adj(this.vertex)) {
                nextNodes.add(new Node(this.distance + 1, nextV));
            }
            return nextNodes;
        }
    }

    private final static int NOT_FOUND = -1;
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        List<Integer> v_s = new LinkedList<>();
        List<Integer> w_s = new LinkedList<>();
        v_s.add(v);
        w_s.add(w);

        return this.length(v_s, w_s);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        List<Integer> v_s = new LinkedList<>();
        List<Integer> w_s = new LinkedList<>();
        v_s.add(v);
        w_s.add(w);
        return this.ancestor(v_s, w_s);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int[] visitedVertexLeft = new int[this.G.V()];
        Arrays.fill(visitedVertexLeft, NOT_FOUND);
        int[] visitedVertexRight = new int[this.G.V()];
        Arrays.fill(visitedVertexRight, NOT_FOUND);

        int ancestor = SAP.bfs(G, v, w, visitedVertexLeft, visitedVertexRight);
        if (ancestor == NOT_FOUND) return NOT_FOUND;

        return visitedVertexLeft[ancestor] + visitedVertexRight[ancestor];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int[] visitedVertexLeft = new int[this.G.V()];
        Arrays.fill(visitedVertexLeft, NOT_FOUND);
        int[] visitedVertexRight = new int[this.G.V()];
        Arrays.fill(visitedVertexRight, NOT_FOUND);

        return SAP.bfs(G, v, w, visitedVertexLeft, visitedVertexRight);
    }

//    private static int bfs(Digraph G, int v, int w, int[] visitedVertex) {
//        LinkedQueue<Node> queue = new LinkedQueue<Node>();
//        if (visitedVertex.length != G.V())
//            throw new IllegalArgumentException("Visited Vertex size and Graph vertex size not consistent");
//
//        Arrays.fill(visitedVertex, NOT_FOUND);
//
//        queue.enqueue(new Node(0, v));
//        queue.enqueue(new Node(0, w));
//
//        while (!queue.isEmpty()) {
//            Node node = queue.dequeue();
//            int vertex = node.vertex;
//            if (visitedVertex[vertex] != NOT_FOUND) {
//                visitedVertex[vertex] += node.distance;
//                return vertex;
//            } else {
//                visitedVertex[vertex] = node.distance;
//                for (Node nextNode : node.moveNext(G)) {
//                    queue.enqueue(nextNode);
//                }
//            }
//        }
//        return NOT_FOUND;
//    }

    private static int bfs(Digraph G, Iterable<Integer> v, Iterable<Integer> w, int[] visitedVertexLeft, int[] visitedVertexRight) {
        LinkedQueue<Node> queueLeft = new LinkedQueue<>();
        LinkedQueue<Node> queueRight = new LinkedQueue<>();

        for (int item : v) queueLeft.enqueue(new Node(0, item));
        for (int item : w) queueRight.enqueue(new Node(0, item));

        int shortestLength = Integer.MAX_VALUE;
        int ancestor = NOT_FOUND;

        while (!queueLeft.isEmpty() || !queueRight.isEmpty()) {
            if (!queueLeft.isEmpty()) {
                Node left = queueLeft.dequeue();
                int lVertex = left.vertex;
                if (visitedVertexLeft[lVertex] == NOT_FOUND) visitedVertexLeft[lVertex] = left.distance;
                visitedVertexLeft[lVertex] = Math.min(left.distance, visitedVertexLeft[lVertex]);
                if (visitedVertexRight[lVertex] != NOT_FOUND) {
                    int newLength = visitedVertexLeft[lVertex] + visitedVertexRight[lVertex];
                    if (shortestLength > newLength) {
                        shortestLength = newLength;
                        ancestor = lVertex;
                    }
                } else {
                    for (Node nextNode : left.moveNext(G)){
                        if (visitedVertexLeft[nextNode.vertex]==NOT_FOUND)
                            queueLeft.enqueue(nextNode);
                    }
                }
            }

            if (!queueRight.isEmpty()) {
                Node right = queueRight.dequeue();
                int rVertex = right.vertex;
                if (visitedVertexRight[rVertex] == NOT_FOUND) visitedVertexRight[rVertex] = right.distance;
                visitedVertexRight[rVertex] = Math.min(right.distance, visitedVertexRight[rVertex]);
                if (visitedVertexLeft[rVertex] != NOT_FOUND) {
                    int newLength = visitedVertexLeft[rVertex] + visitedVertexRight[rVertex];
                    if (shortestLength > newLength) {
                        shortestLength = newLength;
                        ancestor = rVertex;
                    }
                } else {
                    for (Node nextNode : right.moveNext(G)){
                        if (visitedVertexRight[nextNode.vertex]==NOT_FOUND)
                            queueRight.enqueue(nextNode);
                    }
                }
            }
        }
        return ancestor;
    }


    // do unit testing of this class
    public static void main(String[] args) {
        testDiGraph();
        testMultiSAPAncestor(args[0]);

        testSmallSAPAncestor();

        testSAPAncestor(args[0]);
    }

    private static void testDiGraph(){
        In fileIn = new In("data/wordnet/digraph3.txt");
        if (!fileIn.exists()) throw new IllegalArgumentException();
        Digraph G = new Digraph(fileIn);
        SAP sap = new SAP(G);

        int ancestor, length;
        List<Integer> v, w;
        v = new LinkedList<>();
        v.add(10);
        w = new LinkedList<>();
        w.add(7);
        ancestor = sap.ancestor(v, w);
        length = sap.length(v, w);
        System.out.println(ancestor);
        System.out.println(length);
        assert length == 3;

        v = new LinkedList<>();
        v.add(7);
        w = new LinkedList<>();
        w.add(11);
        ancestor = sap.ancestor(v, w);
        length = sap.length(v, w);
        System.out.println(ancestor);
        System.out.println(length);
        assert length == 3;

//        ancestor = sap.ancestor(10, 7);
//        length = sap.length(10, 7);
//        //assert ancestor == 3;
//        System.out.println(ancestor);
//        System.out.println(length);
//        assert length == 3;
    }

    private static void testMultiSAPAncestor(String filename) {
        In fileIn = new In(filename);
        if (!fileIn.exists()) throw new IllegalArgumentException();
        Digraph G = new Digraph(fileIn);
        SAP sap = new SAP(G);

        int ancestor, length;
        List<Integer> v = new LinkedList<>();
        v.add(13);
        v.add(23);
        v.add(24);
        List<Integer> w = new LinkedList<>();
        w.add(6);
        w.add(16);
        w.add(17);
        ancestor = sap.ancestor(v, w);
        length = sap.length(v, w);
        assert ancestor == 3;
        assert length == 4;

        v = new LinkedList<>();
        v.add(13);
        v.add(23);
        v.add(24);
        v.add(1);
        w = new LinkedList<>();
        w.add(6);
        w.add(16);
        w.add(17);
        ancestor = sap.ancestor(v, w);
        length = sap.length(v, w);
        assert ancestor == 0;
        assert length == 3;

    }

    private static void testSAPAncestor(String filename) {
        In fileIn = new In(filename);
        if (!fileIn.exists()) throw new IllegalArgumentException();
        Digraph G = new Digraph(fileIn);
        SAP sap = new SAP(G);

        //int length = sap.length(v, w);
        int ancestor, length;
        ancestor = sap.ancestor(13, 16);
        length = sap.length(13, 16);
        assert ancestor == 3;
        assert length == 4;

        ancestor = sap.ancestor(13, 17);
        length = sap.length(13, 17);
        assert ancestor == 0;
        assert length == 8;

        ancestor = sap.ancestor(17, 17);
        length = sap.length(17, 17);
        assert ancestor == 17;
        assert length == 0;

        ancestor = sap.ancestor(17, 17);
        length = sap.length(17, 17);
        assert ancestor == 17;
        assert length == 0;

        ancestor = sap.ancestor(13, 22);
        length = sap.length(13, 22);
        assert ancestor == 3;
        assert length == 5;


        //Add a node 16->3
        sap.G.addEdge(16, 3);
        ancestor = sap.ancestor(13, 22);
        length = sap.length(13, 22);
        assert ancestor == 3;
        assert length == 4;

    }

    private static void testSmallSAPAncestor() {
        In fileIn = new In("data/wordnet/digraph1.txt");
        if (!fileIn.exists()) throw new IllegalArgumentException();
        Digraph G = new Digraph(fileIn);
        SAP sap = new SAP(G);

        int ancestor, length;
        ancestor = sap.ancestor(1, 6);
        length = sap.length(1, 6);
        assert ancestor == -1;
        assert length == -1;

        ancestor = sap.ancestor(3, 11);
        length = sap.length(3, 11);
        assert ancestor == 1;
        assert length == 4;

        ancestor = sap.ancestor(9, 12);
        length = sap.length(9, 12);
        assert ancestor == 5;
        assert length == 3;

        ancestor = sap.ancestor(7, 2);
        length = sap.length(7, 2);
        assert ancestor == 0;
        assert length == 4;
    }

}
