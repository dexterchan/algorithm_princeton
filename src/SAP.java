import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SAP {
    private Digraph graph;
    private final int INVALID = -1;

    public SAP(Digraph g) {
        this.graph = new Digraph(g);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        this.checkInput(v);
        this.checkInput(w);

        BfsTransverse bfs = new BfsTransverse(
                this.graph,
                this.getIterable(v),
                this.getIterable(w)
        );
        return bfs.getLength();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        this.checkInput(v);
        this.checkInput(w);

        BfsTransverse bfs = new BfsTransverse(
                this.graph,
                this.getIterable(v),
                this.getIterable(w)
        );

        return bfs.getAncestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (!checkInputs(v)) return -1;
        if (!checkInputs(w)) return -1;

        BfsTransverse bfs = new BfsTransverse(this.graph, v, w);
        return bfs.getLength();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!checkInputs(v)) return -1;
        if (!checkInputs(w)) return -1;

        BfsTransverse bfs = new BfsTransverse(this.graph, v, w);
        return bfs.getAncestor();
    }



    private void checkInput(int v) {
        if (v < 0 || v >= graph.V() ) {
            throw new IllegalArgumentException();
        }
    }

    private boolean checkInputs(Iterable<Integer> v){
        int count = 0;
        if (v == null) throw new IllegalArgumentException();
        for(Integer i: v){
            if (i == null) throw new IllegalArgumentException();
            this.checkInput(i);
            count++;
        }
        if (count == 0 ){
            return false;
        }
        return true;
    }

    private Iterable<Integer> getIterable(int single) {
        List<Integer> itr = new LinkedList<>();
        itr.add(single);
        return itr;
    }

    public static void main(String[] args) {
        testStrangeInput();

        testDiGraph();

        testMultiSAPAncestor(args[0]);

        testSmallSAPAncestor();

        testSAPAncestor(args[0]);
    }

    private static void testStrangeInput(){
        int ancestor, length;
        List<Integer> v, w;
        v = new LinkedList<>();
        w = new LinkedList<>();
        In fileIn = new In("data/wordnet/digraph1.txt");
        if (!fileIn.exists()) throw new IllegalArgumentException();
        Digraph G = new Digraph(fileIn);
        SAP sap = new SAP(G);

        ancestor = sap.ancestor(v, w);
        length = sap.length(v, w);

        v = new LinkedList<>();
        w = new LinkedList<>();
        v.add(0);
        v.add(3);
        v.add(null);
        v.add(8);
        v.add(9);
        w.add(5);
        w.add(12);
        try {
            ancestor = sap.ancestor(v, w);
            length = sap.length(v, w);
        }catch (IllegalArgumentException ie){}


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
        sap.graph.addEdge(16, 3);
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

class BfsTransverse {
    private final int numVertexes;
    private final BreadthFirstDirectedPaths v, w;

    private static final int INVALID = -1;
    private int ancestor = INVALID;
    private int minDist = INVALID;

    BfsTransverse(final Digraph g, final Iterable<Integer> vs, final Iterable<Integer> ws) {
        numVertexes = g.V();
        v = new BreadthFirstDirectedPaths(g, vs);
        w = new BreadthFirstDirectedPaths(g, ws);
        this.getAncest();
    }

    private void getAncest() {
        //brutal method to explore all potential ancestors
        for (int i = 0; i < numVertexes; i++) {
            if (hasIntersection(i)) {
                if (minDist == INVALID || minDist > this.getDist(i)) {
                    this.minDist = this.getDist(i);
                    this.ancestor = i;
                }
            }
        }
    }

    public int getDist(final int t) {
        return this.v.distTo(t) + this.w.distTo(t);
    }

    private boolean hasIntersection(int p) {
        return v.hasPathTo(p) && w.hasPathTo(p);
    }

    public int getAncestor() {
        return this.ancestor;
    }


    public int getLength() {
        return this.minDist;
    }

}