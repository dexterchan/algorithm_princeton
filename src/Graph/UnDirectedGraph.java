package Graph;
import java.util.Stack;
//https://csacademy.com/app/graph_editor/

class Bag<E>{
    private Node first;
    private int size;

    private class Node{
        E item;
        Node next;
    }

    public void add(E item){
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        size++;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public Iterable<E> iterator(){
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

    public UnDirectedGraph(UnDirectedGraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            Stack<Integer> reverse = new Stack<Integer>();
            for (int w : G.adj[v].iterator())
                reverse.push(w);
            for (int w : reverse)
                adj[v].add(w);
        }
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
        G.addEdge(1,4);
        G.addEdge(1,5);
        G.addEdge(2,3);
        G.addEdge(2,4);
        G.addEdge(4,5);
        System.out.println(G.toString());
    }
}
