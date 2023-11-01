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
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] visitedVertex = new int[this.G.V()];
        Arrays.fill(visitedVertex, NOT_FOUND);
        int ancestor = SAP.bfs(G, v, w, visitedVertex);
        if (ancestor == NOT_FOUND) return NOT_FOUND;

        return visitedVertex[ancestor];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] visitedVertex = new int[this.G.V()];
        Arrays.fill(visitedVertex, NOT_FOUND);
        return SAP.bfs(G, v, w, visitedVertex);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;
    }

    private static int bfs(Digraph G, int v, int w, int[] visitedVertex) {
        LinkedQueue<Node> queue = new LinkedQueue<Node>();
        if (visitedVertex.length != G.V())
            throw new IllegalArgumentException("Visited Vertex size and Graph vertex size not consistent");

        Arrays.fill(visitedVertex, NOT_FOUND);

        queue.enqueue(new Node(0, v));
        queue.enqueue(new Node(0, w));

        while (!queue.isEmpty()) {
            Node node = queue.dequeue();
            int vertex = node.vertex;
            if (visitedVertex[vertex] != NOT_FOUND) {
                visitedVertex[vertex] += node.distance;
                return  vertex;
            }else{
                visitedVertex[vertex] = node.distance;
                for(Node nextNode: node.moveNext(G)){
                    queue.enqueue(nextNode);
                }
            }
        }
        return NOT_FOUND;
    }

    // do unit testing of this class
    public static void main(String[] args) {


        In fileIn = new In(args[0]);
        if (!fileIn.exists()) throw new IllegalArgumentException();
        Digraph G = new Digraph(fileIn);
        SAP sap = new SAP(G);
        testSAPAncestor(sap);
    }

    private static void testSAPAncestor(SAP sap) {
        //int length = sap.length(v, w);
        int ancestor, length;
        ancestor= sap.ancestor(13, 16);
        assert ancestor==3;

        ancestor= sap.ancestor(13, 17);
        assert ancestor==0;

        ancestor= sap.ancestor(17, 17);
        assert ancestor==17;

        length = sap.length(13,16);
        assert length == 4;


    }
}
