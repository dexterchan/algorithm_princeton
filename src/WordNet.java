import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

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
            //if (hasParallelEdges(g)) return;

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
        TreeMap<Integer, String> v_map = new TreeMap<>();
        private int max_node_num = 0;

        public SymbolTable(){}
        public SymbolTable (SymbolTable symbolTable){
            SymbolTable newTable = new SymbolTable();
            newTable.max_node_num = this.max_node_num;
            for (Integer key: this.v_map.keySet()){
                newTable.v_map.put(key, this.v_map.get(key));
            }
            for (String key: this.map.keySet()){
                List<Node> newValues = new LinkedList<>();
                this.map.get(key).forEach(
                        node -> newValues.add(new Node(node))
                );
                newTable.map.put(key, newValues);
            }

        }

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

        public String put(int key, String raw_input){
            this.v_map.put(key, raw_input);
            return raw_input;
        }

        public Iterable<Node> get(String key) {
            return this.map.get(key);
        }

        public String get(int vertex){
            return this.v_map.get(vertex);
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
        Node(Node node){
            this.num = node.num;
            this.description = node.description;
        }
    }

    private Digraph digraph = null;

    private SymbolTable symbolTables = null;

    private SAP sap = null;

//    public WordNet(){}

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        this.symbolTables = WordNet.readSynsetsFile(synsets);
        this.digraph = WordNet.createDiGraph(this.symbolTables, hypernyms);
        this.sap = new SAP(this.digraph);
        //this.readHyperNyms(hypernyms);

    }

//    public WordNet(WordNet wordNet){
//        this.symbolTables = new SymbolTable(wordNet.symbolTables);
//        this.digraph = new Digraph(wordNet.digraph);
//    }

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
        //System.out.println("Detected " + num_outdegree_zero + " root vertex");
        if (num_outdegree_zero != 1) throw new IllegalArgumentException("No root found");

        //Check if cyclic found
        DirectedCycle cycled = new DirectedCycle(digraph);
        if (cycled.hasCycle()){
            throw  new IllegalArgumentException("Cycle found");
        }


//        DiGraphCycle cycle_detector = new DiGraphCycle(digraph);
//        if (cycle_detector.hasCycle()) {
//            throw new IllegalArgumentException("Cycle found");
//        }
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

            symTable.put(inx, token);

            String [] tokens = token.split(" ");
            for (String t: tokens) symTable.put(t, new Node(inx, description));
            //assert count == inx; no longer consistent
            count++;
        }
        //System.out.println("Read " + count + " vertex, Max Node number=" + symTable.getNumNodes());
        //assert symTable.getNumNodes() == count;
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
        return this.symbolTables.map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if(word == null) throw new IllegalArgumentException();
        return this.symbolTables.get(word) != null;
    }

    private static void printStackTrace(Stack<Integer> stack){
        while(!stack.isEmpty()){
            System.out.println(stack.pop());
        }
    }



    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!this.isNoun(nounA)) throw new IllegalArgumentException();
        if (!this.isNoun(nounB)) throw new IllegalArgumentException();
        Iterable<Integer> vertexAs = this.getVertexs(nounA);
        Iterable<Integer> vertexBs = this.getVertexs(nounB);

        return sap.length(vertexAs, vertexBs);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!this.isNoun(nounA)) throw new IllegalArgumentException();
        if (!this.isNoun(nounB)) throw new IllegalArgumentException();
        Iterable<Integer> vertexAs = this.getVertexs(nounA);
        Iterable<Integer> vertexBs = this.getVertexs(nounB);
        int inx = sap.ancestor(vertexAs, vertexBs);

        if (inx == INVALID_STATE){
            throw new IllegalArgumentException();
        }

        return this.symbolTables.get(inx);
    }

    private Iterable<Integer> getVertexs(String noun){
        if (!this.isNoun(noun)) throw new IllegalArgumentException();
        List<Integer> vertexs = new LinkedList<>();
        this.symbolTables.get(noun).forEach(
                n->vertexs.add(n.num)
        );
        return vertexs;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        //testCycleGraph();
        testDiGraph();
        //var symboltables = testReadSymbSet(args[0]);
        //var Digraph = testCreateDigraph(symboltables, args[1]);

        testNoun(args[0], args[1]);

    }



    private static void testDiGraph(){
        String inputFile = "data/wordnet/digraph25.txt";

        In fileIn = new In(inputFile);
        if (!fileIn.exists()) throw new IllegalArgumentException();
        int count = 0;
        int vertex = fileIn.readInt();
        int edge = fileIn.readInt();
        assert vertex == 25;
        assert edge == 24;
        fileIn.readLine();
        Digraph digraph = new Digraph(vertex);
        for (int i=0;i<edge;i++){
            int v = fileIn.readInt();
            int w = fileIn.readInt();
            digraph.addEdge(v, w);
        }

        Set<Integer> numSet = new HashSet<>();
        numSet.add(1);
        Stack<Integer> path = new Stack<>();


    }

    private static WordNet testNoun(String synsets, String hypernyms){
        WordNet wordNet= new WordNet(synsets, hypernyms);

        assert wordNet.isNoun("gerund");
        assert wordNet.isNoun("anamorphosis");

        assert  wordNet.isNoun("running");
        assert  !wordNet.isNoun("sdafads");

        int dist = wordNet.distance("apple", "orange");
        assert dist != INVALID_STATE;
        System.out.println(dist);
        String ancestor = wordNet.sap("apple", "orange");
        assert ancestor!=null;


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