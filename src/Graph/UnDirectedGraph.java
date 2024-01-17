package Graph;

import java.util.Arrays;
import java.util.Stack;
//https://csacademy.com/app/graph_editor/


// Implement depth-first search in an undirected graph without using recursion.
class GraphDfs {
    private final static int INVALID = -1;
    private boolean[] marked;
    private int[] edgedTo;

    private UnDirectedGraph graph;

    public GraphDfs(UnDirectedGraph graph, int v) {
        marked = new boolean[graph.V()];
        edgedTo = new int[graph.V()];
        Arrays.fill(marked, false);
        Arrays.fill(edgedTo, INVALID);
        this.graph = graph;
    }

    private void dfs(UnDirectedGraph g, int v) {
        Stack<Integer> stack = new Stack<>();
        stack.add(v);

        int newVertex = INVALID;
        while (!stack.isEmpty()) {
            newVertex = stack.pop();
            if (!isValidVertex(newVertex)) throw new IllegalArgumentException();
            this.marked[newVertex] = true;

            for (int cv : graph.adj(newVertex)) {
                if (!isValidVertex(cv)) throw new IllegalArgumentException();
                if (this.marked[cv]) continue;
                this.edgedTo[cv] = newVertex;
                stack.push(cv);
            }
        }
    }

    private boolean isValidVertex(int v) {
        if (v < 0 || v >= graph.V()) {
            return false;
        }
        return true;
    }

    public boolean isConnected(int v){
        return this.marked[v];
    }

    public int connectedFrom(int v){
        return this.edgedTo[v];
    }

}

class Bag<E> {
    private Node first;
    private int size;

    private class Node {
        E item;
        Node next;
    }

    public void add(E item) {
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        size++;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Iterable<E> iterator() {
        return new Iterable<E>() {
            @Override
            public java.util.Iterator<E> iterator() {
                return new java.util.Iterator<E>() {
                    Node current = first;

                    @Override
                    public boolean hasNext() {
                        return current != null;
                    }

                    @Override
                    public E next() {
                        E item = current.item;
                        current = current.next;
                        return item;
                    }
                };
            }
        };
    }
}

public class UnDirectedGraph {

    private final int V;
    private int E;
    private final Bag<Integer>[] adj;

    public UnDirectedGraph(int V) {
        this.V = V;
        this.E = 0;
        adj = (Bag<Integer>[]) new Bag[V];

        for (int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }

    public int V() {
        return this.V;
    }

    public int E() {
        return this.E;
    }

    public void addEdge(int v, int w) {
        if (v < 0 || v >= V) throw new IllegalArgumentException();
        if (w < 0 || w >= V) throw new IllegalArgumentException();
        E++;
        adj[v].add(w);
        adj[w].add(v);
    }

    public Iterable<Integer> adj(int v) {
        if (v < 0 || v >= V) throw new IllegalArgumentException();
        return adj[v].iterator();
    }

    public int degree(int v) {
        if (v < 0 || v >= V) throw new IllegalArgumentException();
        return adj[v].size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(V);
        sb.append(" vertices, ");
        sb.append(E);
        sb.append(" edges\n");
        for (int v = 0; v < V; v++) {
            sb.append(v);
            sb.append(": ");
            for (int w : adj[v].iterator()) {
                sb.append(w);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        UnDirectedGraph G = new UnDirectedGraph(6);
        G.addEdge(0, 2);
        G.addEdge(0, 4);
        G.addEdge(0, 5);
        G.addEdge(1, 4);
        G.addEdge(1, 5);
        G.addEdge(2, 3);
        G.addEdge(2, 4);
        G.addEdge(4, 5);
        System.out.println(G.toString());
    }
}
