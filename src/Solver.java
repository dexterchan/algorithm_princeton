import edu.princeton.cs.algs4.MinPQ;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Solver {


    private Node solvedNode = null;

    private Board initialBoard = null;


    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.initialBoard = initial;

        int max_steps = initial.dimension() * initial.dimension() * 1000000;
        this.solvedNode = solvePuzzle(initial, max_steps);

    }

    private static Node solvePuzzle(Board initialBoard, int max_steps) {
        Board twinBoard = initialBoard.twin();
        MinPQ<Node> priorityQ = new MinPQ<>();
        priorityQ.insert(new Node(initialBoard, 0, null));
        priorityQ.insert(new Node(twinBoard, 0, null));

        int steps = 0;
        while (!priorityQ.isEmpty() && steps++ < max_steps) {
            Node minNode = priorityQ.delMin();
            Board theBoard = minNode.board;

//            System.out.println("Steps:" + steps);
//            System.out.println(theBoard.toString());

            //Check if reach the goal
            if (theBoard.isGoal()) {
                //System.out.println("Finish with steps:" + steps);
                return minNode;
            }

            for (Board newBoard : minNode.board.neighbors()) {
                Node prevNode = minNode.previousNode;
                boolean skipThisBoard = false;
                while (prevNode != null) {
                    if (newBoard.equals(prevNode.board)) {
                        skipThisBoard = true;
                        break;
                    }
                    prevNode = prevNode.previousNode;
                }
                if (skipThisBoard) {
                    continue;
                }
//                if (minNode.previousNode != null && newBoard.equals(minNode.previousNode.board)) {
//                    //skip duplicated run
//                    continue;
//                }
                priorityQ.insert(new Node(newBoard, minNode.moves + 1, minNode));
            }

        }

        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (this.solvedNode == null) {
            throw new IllegalArgumentException("Failed to determine solvable");
        }
        Iterator<Board> itr = this.getMoveHistory();
        Board firstBoard = itr.next();
        return firstBoard.equals(this.initialBoard);
    }

    private Iterator<Board> getMoveHistory() {
        Stack<Board> stack = new Stack<>();
        List<Board> queue = new LinkedList<>();
        Node n = this.solvedNode;
        while (n != null) {
            stack.push(n.board);
            n = n.previousNode;
        }
        while (!stack.isEmpty()) {
            queue.add(stack.pop());
        }

        return queue.iterator();
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
        if (!this.isSolvable()) {
            return null;
        }
        LinkedList<Board> steps = new LinkedList<>();
        for (Iterator<Board> it = this.getMoveHistory(); it.hasNext(); ) {
            steps.add(it.next());
        }
        return steps;
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

    public static void main(String[] args) {
        int[][] titles = null;
        Board board = null;
        Solver solver = null;
        Iterable<Board> iter = null;

        titles = new int[][]{
                {7, 8, 5},
                {4, 0, 2},
                {3, 6, 1}
        };
        board = new Board(titles);
        solver = new Solver(board);
        assert solver.isSolvable();
        System.out.println(solver.moves());

        titles = new int[][]{
//                {0, 1, 3},
//                {4, 2, 5},
//                {7, 8, 6}
                {2, 1, 3},
                {4, 5, 6},
                {8, 7, 0}
//                {1, 0, 3},
//               {4, 2, 5},
//                {7, 8, 6}
        };
        board = new Board(titles);
        solver = new Solver(board);


        int i = 0;
//        for (Iterator<Board> b = solver.getMoveHistory(); b.hasNext(); i++) {
//            Board bd = b.next();
//            System.out.println("Steps" + i);
//            System.out.println(bd.toString());
//        }
        assert solver.isSolvable();
        assert solver.moves() == solver.solvedNode.moves;
        assert solver.solution() != null;
        assert solver.solution() instanceof Iterable;


        titles = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {8, 7, 0}
        };
        board = new Board(titles);
        solver = new Solver(board);
        assert !solver.isSolvable();
        assert solver.moves() == -1;
        assert solver.solution() == null;


    }
}
