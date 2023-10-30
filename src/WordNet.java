import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashSet;
import java.util.Stack;
import java.util.List;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordNet {
    private static final int INVALID_STATE = -1;

    private static class DiGraphCycle {
        private boolean[] marked;
        private int[] edgeTo;
        private Stack<Integer> cycle = new Stack<>();

        DiGraphCycle(Digraph g) {
            if (hasParallelEdges(g)) return;

            marked = new boolean[g.V()];
            edgeTo = new int[g.V()];

            for (int v = 0; v < g.V(); v++) {
                resetTraverseInfo(g);
                dfs(g, v);
            }

        }

        private void resetTraverseInfo(Digraph g) {

            for (int i = 0; i < g.V(); i++) {
                marked[i] = false;
                edgeTo[i] = WordNet.INVALID_STATE;
            }
        }

        private boolean hasParallelEdges(Digraph g) {
            //It is special case for detecting parallel edge in undirected graph
            marked = new boolean[g.V()];
            for (int v = 0; v < g.V(); v++) {
                for (int w : g.adj(v)) {
                    if (marked[w]) {
                        cycle = new Stack<>();
                        cycle.push(v);
                        cycle.push(w);
                        cycle.push(v);
                        return true;
                    }
                    marked[w] = true;
                }
                for (int w : g.adj(v)) marked[w] = false;
            }
            return false;
        }

        public boolean hasCycle() {
            return this.cycle != null && !this.cycle.isEmpty();
        }

        public Iterator<Integer> cycle() {
            List<Integer> l = new LinkedList<>();
            Stack<Integer> s = new Stack<>();
            while (!this.cycle.isEmpty()) {
                int element = this.cycle.pop();
                s.push(element);
                l.add(element);
            }
            this.cycle = s;

            return l.iterator();
        }

        private void dfs(Digraph g, int v) {
            for (int w : g.adj(v)) {
                //Short circuit if cycle was found
                if (hasCycle()) return;

                if (!this.marked[w]) {
                    this.marked[w] = true;
                    this.edgeTo[w] = v;
                    dfs(g, w);
                } else {// if (w != p) {
                    Stack<Integer> new_cycle = new Stack<>();
                    int x = v;
                    while(x!=INVALID_STATE && x!=w ){
                        new_cycle.push(x);
                        x = edgeTo[x];
                    }

                    if (x != INVALID_STATE) {
                        new_cycle.push(w);
                        new_cycle.push(v);
                        Iterator<Integer> itr = new_cycle.iterator();
                        while (itr.hasNext()) this.cycle.push(itr.next());
                    }

                }
            }
        }
    }

    private static class SymbolTable {
        TreeMap<String, List<Node>> map = new TreeMap<>();
        private int max_node_num = 0;

        public Iterable<Node> put(String key, Node value) {
            List<Node> values = null;
            if (!this.map.containsKey(key)) {
                values = new LinkedList<>();
                this.map.put(key, values);
            } else {
                values = this.map.get(key);
            }
            values.add(value);
            max_node_num = Math.max(max_node_num, value.num);
            return values;
        }

        public Iterable<Node> get(String key) {
            return this.map.get(key);
        }

        public int size() {
            return map.size();
        }

        public int getNumNodes() {
            return this.max_node_num + 1;
        }
    }

    private static class Node {
        int num;
        String description;

        Node(int num, String description) {
            if (num < 0) throw new IllegalArgumentException();
            this.num = num;
            this.description = description;
        }
    }

    private Digraph digraph;

    SymbolTable symbolTables = new SymbolTable();

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        this.symbolTables = WordNet.readSynsetsFile(synsets);
        this.digraph = WordNet.createDiGraph(this.symbolTables, hypernyms);
        //this.readHyperNyms(hypernyms);

    }

    private static Digraph createDiGraph(SymbolTable st, String hypernyms) {
        int num_vertex = st.getNumNodes();

        Digraph digraph = new Digraph(num_vertex);
        //1. Read all edges
        In fileIn = new In(hypernyms);
        if (!fileIn.exists()) throw new IllegalArgumentException();
        int count = 0;
        while (!fileIn.isEmpty()) {
            count++;
            String line = fileIn.readLine();
            String[] numbers = line.split(",");
            if (numbers.length < 1) {
                throw new IllegalArgumentException("line" + count + ":edge entry format problem");
            }
            if (numbers.length == 1) {
                continue;
            }
            int v = Integer.parseInt(numbers[0]);
            for (int j = 1; j < numbers.length; j++) {
                int w = 0;
                try {
                    w = Integer.parseInt(numbers[j]);
                    digraph.addEdge(v, w);
                } catch (NumberFormatException ne) {
                    throw new NumberFormatException("line" + count + "-" + numbers[j] + ":w number format problem");
                } catch (IllegalArgumentException ie) {
                    throw new IllegalArgumentException("line" + count + "-" + numbers[j] + ie.getMessage());
                }

            }
        }
        fileIn.close();

        //Check if root exits
        int num_outdegree_zero = 0;
        for (int i = 0; i < num_vertex; i++) {
            if (digraph.outdegree(i) == 0) {
                System.out.println("Root vertex=" + i);
                num_outdegree_zero++;
            }

        }
        System.out.println("Detected " + num_outdegree_zero + " root vertex");
        if (num_outdegree_zero != 1) throw new IllegalArgumentException("No root found");

        //Check if cyclic found
        DiGraphCycle cycle_detector = new DiGraphCycle(digraph);
        if (cycle_detector.hasCycle()) {
            throw new IllegalArgumentException("Cycle found");
        }
        return digraph;
    }

    private static SymbolTable readSynsetsFile(String synets) {

        Pattern pattern = Pattern.compile("^(\\d+),([^,]*),(.+)");
        SymbolTable symTable = new SymbolTable();
        //1. Read all vertexs
        In fileIn = new In(synets);
        if (!fileIn.exists()) throw new IllegalArgumentException();
        int count = 0;
        while (!fileIn.isEmpty()) {

            String line = fileIn.readLine();
            Matcher matcher = pattern.matcher(line);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("line" + (count + 1) + ":(" + line + ") not matching" + pattern.pattern());
            }

            int inx = Integer.parseInt(matcher.group(1));
            String token = matcher.group(2);
            String description = matcher.group(3);
            symTable.put(token, new Node(inx, description));
            assert count == inx;
            count++;
        }
        System.out.println("Read " + count + " vertex, Max Node number=" + symTable.getNumNodes());
        assert symTable.getNumNodes() == count;
        fileIn.close();
        return symTable;
    }

    private Set<Integer> getNounSynset(){
        Iterable<Node> nouns = this.symbolTables.get("noun");
        Iterator<Node> itr= nouns.iterator();
        HashSet<Integer> nouns_nums = new HashSet<>();
        while(itr.hasNext()) nouns_nums.add(itr.next().num);
        return nouns_nums;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return null;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {

        Iterable<Node> symNums = this.symbolTables.get(word);
        Set<Integer> nounSet = this.getNounSynset();
        
        for(Node node: symNums){
            Stack<Integer> nounStackTrace = new Stack<>();
            nounStackTrace.push(node.num);
            boolean isNoun = this.dfs_check_ancestor(node.num, nounSet, nounStackTrace);
            if(isNoun){
                printStackTrace(nounStackTrace);
                return true;
            }
        }

        return false;
    }

    private void printStackTrace(Stack<Integer> stack){
        while(!stack.isEmpty()){
            System.out.println(stack.pop());
        }
    }

    private boolean dfs_check_ancestor(int v, Set<Integer> ancestors, Stack<Integer> path){

        for (int ancestor:this.digraph.adj(v)){
            if (ancestors.contains(ancestor)){
                path.push(ancestor);
                return true;
            }else{
                boolean found = dfs_check_ancestor(ancestor, ancestors, path);
                if (found){
                    path.push(ancestor);
                }
            }
        }
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        testCycleGraph();

        //var symboltables = testReadSymbSet(args[0]);
        //var Digraph = testCreateDigraph(symboltables, args[1]);

        testNoun(args[0], args[1]);

    }

    private static WordNet testNoun(String synsets, String hypernyms){
        WordNet wordNet= new WordNet(synsets, hypernyms);

        assert wordNet.isNoun("apple");

        assert  !wordNet.isNoun("running");

        return wordNet;
    }

    private static SymbolTable testReadSymbSet(String file) {
        SymbolTable st = WordNet.readSynsetsFile(file);
        System.out.println("Number of " + st.size());
        assert st.size() > 0;
        return st;
    }

    private static Digraph testCreateDigraph(SymbolTable st, String hyper) {
        Digraph graph = WordNet.createDiGraph(st, hyper);
        assert graph != null;
        return graph;
    }

    private static void printCycle(DiGraphCycle cycle){
        Iterator<Integer> c_itr = cycle.cycle();
        while (c_itr.hasNext()) {
            System.out.print(c_itr.next() + ",");
        }
        System.out.println();
    }
    private static void testCycleGraph() {
        Digraph triangle, arbitary;
        DiGraphCycle cycle;

        arbitary = new Digraph(3);
        arbitary.addEdge(1, 2);
        arbitary.addEdge(2, 1);
        arbitary.addEdge(2, 0);
        cycle = new DiGraphCycle(arbitary);
        assert cycle.hasCycle();
        printCycle(cycle);


        arbitary = new Digraph(3);
        arbitary.addEdge(0, 1);
        arbitary.addEdge(1, 0);
        cycle = new DiGraphCycle(arbitary);
        assert cycle.hasCycle();
        printCycle(cycle);

        arbitary = new Digraph(3);
        arbitary.addEdge(0, 1);
        arbitary.addEdge(1, 2);
        arbitary.addEdge(2, 1);
        cycle = new DiGraphCycle(arbitary);
        assert cycle.hasCycle();
        printCycle(cycle);


        arbitary = new Digraph(8);
        arbitary.addEdge(0, 2);
        arbitary.addEdge(2, 3);
        arbitary.addEdge(3, 4);
        //arbitary.addEdge(4, 2);
        arbitary.addEdge(4, 5);
        arbitary.addEdge(1, 6);
        arbitary.addEdge(6, 7);
        arbitary.addEdge(7, 6);
        arbitary.addEdge(7, 5);
        cycle = new DiGraphCycle(arbitary);
        assert cycle.hasCycle();
        printCycle(cycle);

        triangle = new Digraph(3);
        triangle.addEdge(0, 1);
        triangle.addEdge(1, 2);
        triangle.addEdge(2, 0);
        cycle = new DiGraphCycle(triangle);
        assert cycle.hasCycle();
        printCycle(cycle);

        triangle = new Digraph(3);
        triangle.addEdge(0, 1);
        triangle.addEdge(1, 2);
        cycle = new DiGraphCycle(triangle);
        assert !cycle.hasCycle();

        arbitary = new Digraph(5);
        arbitary.addEdge(0, 1);
        arbitary.addEdge(0, 2);
        arbitary.addEdge(1, 2);
        arbitary.addEdge(4, 1);
        cycle = new DiGraphCycle(arbitary);
        assert !cycle.hasCycle();


        //example: https://www.baeldung.com/cs/detecting-cycles-in-directed-graph
        arbitary = new Digraph(5);
        arbitary.addEdge(0, 1);
        arbitary.addEdge(0, 2);
        arbitary.addEdge(1, 2);
        arbitary.addEdge(2, 3);
        arbitary.addEdge(3, 4); //cause index -1
        arbitary.addEdge(4, 1);
        cycle = new DiGraphCycle(arbitary);
        assert cycle.hasCycle();
        printCycle(cycle);

    }
}