import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private int _moves = -1;

    private boolean solvable = false;
    private List<Board> best_solution = null;

    private int max_steps = 0;

    private static class Node implements Comparable<Node> {
        int moves = 0;
        Board board;

        LinkedList<Node> history = new LinkedList<>();


        Node(Board board, int moves) {
            if (board == null) throw new IllegalArgumentException();
            this.board = board;
            this.moves = moves;
        }

        Node branchNewNode(Board board) {
            Node node = new Node(board, this.moves + 1);
            for (Node h : this.history) {
                node.history.add(h);
            }
            return node;
        }

        int manhatten_priority() {
            return board.manhattan() + moves;
        }

        int hamming_priority() {
            return board.hamming() + moves;
        }

        @Override
        public int compareTo(Node o) {
            if (o == null) {
                throw new NullPointerException("Node is null");
            }
            return this.moves - o.moves;
        }

        public static Comparator<Node> getManhattenComparator() {
            return new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    if (o1 == null || o2 == null) {
                        throw new IllegalArgumentException();
                    }
                    return o1.manhatten_priority() - o2.manhatten_priority();
                }
            };
        }

        public static Comparator<Node> getHammingComparator() {
            return new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    if (o1 == null || o2 == null) {
                        throw new IllegalArgumentException();
                    }
                    return o1.hamming_priority() - o2.hamming_priority();
                }
            };
        }

        void printHistory() {
            System.out.println("Total moves" + this.moves);
            int step = 0;
            for (Node h : this.history) {
                System.out.println("Step:" + step++);
                System.out.println(h.board.toString());
                System.out.println(h.board.manhattan());
            }
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.max_steps = initial.dimension() * initial.dimension() * 1000000;
        ArrayList<Node> nodes = this.solveThePuzzle(initial, this.max_steps);

        Board twinBoard = initial.twin();
        List<Node> twin_nodes = this.solveThePuzzle(twinBoard, this.max_steps);
        System.out.println("Number of solutions:" + nodes.size());
        System.out.println("Number of twin solutions:" + twin_nodes.size());


        if (!nodes.isEmpty() && twin_nodes.isEmpty()) {
            this.solvable = true;
        } else if (nodes.isEmpty() && !twin_nodes.isEmpty()) {
            this.solvable = false;
        } else {
            throw new IllegalArgumentException("Solvable status is undetermined");
        }

        Arrays.sort(nodes.toArray());

        if (this.solvable) {
            Node bestNode = nodes.get(0);
            this.best_solution = bestNode.history.stream().map(
                    n -> n.board
            ).collect(Collectors.toList());
            this._moves = bestNode.moves;
        }
    }

    private static ArrayList<Node> solveThePuzzle(Board broad, int max_steps) {
        LinkedList<Board> visitedBoardLst = new LinkedList<>();
        //Priority Queue
        MinPQ<Node> priorityQ = new MinPQ<>(Node.getManhattenComparator());
        priorityQ.insert(new Node(broad, 0));
        ArrayList<Node> solution = new ArrayList<>();
        int steps = 0;

        while (!priorityQ.isEmpty() && steps < max_steps) {
            steps++;
            Node minNode = priorityQ.delMin();
            if (visitedBoardLst.contains(minNode.board)) {
                continue;
            }
            Board theBoard = minNode.board;
            visitedBoardLst.add(theBoard);
            minNode.history.add(minNode);

            System.out.println("Running steps:" + steps + theBoard.toString());
            System.out.println("Hamming:" + theBoard.hamming());
            System.out.println("manhattan:" + theBoard.manhattan());
            //System.out.println("visitedBoardLst:"+visitedBoardLst.size());
            System.out.println("Solution length:" + solution.size());
            System.out.println("PriorityQueue length:" + priorityQ.size());

            //Check if it is goal
            if (theBoard.isGoal()) {
                solution.add(minNode);
                continue;
            }

            //Get next moves
            for (Board newBoard : minNode.board.neighbors()) {
                Node branchNode = minNode.branchNewNode(newBoard);
                priorityQ.insert(branchNode);
            }


        }
        return solution;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!this.isSolvable()) {
            return -1;
        }
        return _moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return best_solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] titles = null;
        Board board = null;
        Solver solver = null;
        Iterable<Board> iter = null;
//        titles = new int[][]{
//                {1, 0, 3},
//                {4, 2, 5},
//                {7, 8, 6}
//        };
////        titles = new int[][]{
////                {1, 0},
////                {3, 2}
////        };
//        board = new Board(titles);
//
//        solver = new Solver(board);
//        assert solver.solvable;
//        assert solver.moves() != -1;
//        assert solver.solution() != null;
//        System.out.println(solver.moves());


        titles = new int[][]{
                {0, 1, 3},
                {4, 2, 5},
                {7, 8, 6}
//                {2, 1, 3},
//                {4, 5, 6},
//                {8, 7, 0}
//                {1, 0, 3},
//               {4, 2, 5},
//                {7, 8, 6}
        };
//        titles = new int[][]{
//                {1, 0},
//                {3, 2}
//        };
        board = new Board(titles);
        List<Node> node = Solver.solveThePuzzle(board, 100);
        node.get(0).history.forEach(
                h -> System.out.println(h.board.toString())
        );
        //solver = new Solver(board);


//        titles = new int[][]{
//                {0 , 1 },
//                {2,  3},
//        };
//
//        board = new Board(titles);
//        System.out.println(board);
//        System.out.println(board.twin());
//        solver = new Solver(board);
//        assert !solver.solvable;
//        assert solver.moves() == -1;
//        assert solver.solution() == null;

    }

}
