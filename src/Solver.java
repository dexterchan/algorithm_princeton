
import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedList;
import java.util.Stack;

public class Solver {

    MinPQ<Node> priorityQ = new MinPQ<>();
    Node solvedNode = null;

    boolean is_solvable = false;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        Board twinBoard = initial.twin();

        priorityQ.insert(new Node(initial, 0, null));
        priorityQ.insert(new Node(twinBoard, 0, null));

        int max_steps = initial.dimension() * initial.dimension() * 100;
        this.solvedNode = solvePuzzle(priorityQ, max_steps);



    }

    private static Node solvePuzzle(MinPQ<Node> priorityQ, int max_steps) {
        int steps = 0;
        while (!priorityQ.isEmpty() && steps++ < max_steps) {
            Node minNode = priorityQ.delMin();
            Board theBoard = minNode.board;

            //Check if reach the goal
            if (theBoard.isGoal()) {
                return minNode;
            }

            for (Board newBoard : minNode.board.neighbors()) {
                if (minNode.previousNode != null && newBoard.equals(minNode.previousNode.board)) {
                    //skip duplicated run
                    continue;
                }
                priorityQ.insert(new Node(newBoard, minNode.moves + 1, minNode));
            }

        }
        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (this.solvedNode == null){
            throw new IllegalArgumentException("Failed to determine solvable");
        }

        return this.is_solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!this.isSolvable()) {
            return -1;
        }
        return this.solvedNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Stack<Board> stack = new Stack<>();
        LinkedList<Board> lst = new LinkedList<>();

        return lst;
    }


    private static class Node implements Comparable<Node> {
        int moves = 0;
        Node previousNode = null;
        Board board = null;

        Node(Board board, int moves, Node previous) {
            this.board = board;
            this.moves = moves;
            previousNode = previous;
        }

        int manhatten_priority() {
            return board.manhattan() + moves;
        }

        int hamming_priority() {
            return board.hamming() + moves;
        }

        @Override
        public int compareTo(Node o) {
            return this.manhatten_priority() - o.manhatten_priority();
        }
    }
}
