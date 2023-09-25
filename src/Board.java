import java.util.LinkedList;
import java.util.List;


public class Board {
    private int dimension;
    private int[][] tiles;

    private static final int INVALID = -1;
    private int hamming_distance = INVALID;
    private int manhattan_distance = INVALID;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException("tiles is null");
        int n = tiles.length;
        for (int i = 0; i < n; i++) {
            if (tiles[i].length != n) throw new IllegalArgumentException("tiles is not n-by-n");
        }
        this.dimension = n;
        this.tiles = tiles;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        if (this.hamming_distance != INVALID) {
            return hamming_distance;
        }
        int count = 0;
        int n = dimension;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                if (tile != 0 && tile != i * n + j + 1) count++;
            }
        }
        this.hamming_distance = count;
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (this.manhattan_distance != INVALID) {
            return this.manhattan_distance;
        }
        int count = 0;
        int n = dimension;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                if (tile != 0) {
                    int distX = Math.abs((tile - 1) / n - i);
                    int distY = Math.abs((tile - 1) % n - j);
                    //wrong implementation
//                    int ref = i * n + j + 1;
//                    int diff = Math.abs(tile - ref);
//                    int dist = diff / n + diff % n;
                    count += (distX + distY);
                }
            }
        }
        this.manhattan_distance = count;
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int n = dimension;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int ref = (i!=n-1 || j != n-1) ? i * n + j + 1 : 0 ;
                if (this.tiles[i][j] != ref){
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;

        if (y == null) return false;

        if (!(y instanceof Board)) return false;

        if (((Board) y).dimension() != dimension) return false;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] != ((Board) y).tiles[i][j]) return false;
            }
        }

        return true;
    }

    private static Board swap(Board board, int old_x, int old_y, int new_x, int new_y) {
        Board newBoard = board.replicate();
        int value = newBoard.tiles[new_x][new_y];
        newBoard.tiles[new_x][new_y] = newBoard.tiles[old_x][old_y];
        newBoard.tiles[old_x][old_y] = value;
        return newBoard;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighborslst = new LinkedList<>();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (this.tiles[i][j] == 0) {
                    //it is the blank
                    //check neighor right move
                    if ((i - 1) >= 0) {
                        neighborslst.add(swap(this, i - 1, j, i, j));
                    }
                    //check neighor down move
                    if (j - 1 >= 0) {
                        neighborslst.add(swap(this, i, j - 1, i, j));
                    }
                    //check neighor left move
                    if (i + 1 < dimension) {
                        neighborslst.add(swap(this, i + 1, j, i, j));
                    }
                    //check neighor up move
                    if (j + 1 < dimension) {
                        neighborslst.add(swap(this, i, j + 1, i, j));
                    }
                }
            }
        }

        return neighborslst;
    }

    private Board replicate() {
        int[][] newBoard = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                newBoard[i][j] = tiles[i][j];
            }
        }
        return new Board(newBoard);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int first_x = 0;
        int first_y = 0;
        int second_x = 0;
        int second_y = 0;

        boolean first_found = false;
        first_x = 0;
        while (first_x < dimension && !first_found) {
            for (first_y = 0; first_y < dimension; first_y++) {
                if (this.tiles[first_x][first_y] != 0) {
                    first_found = true;
                    break;
                }
            }
            if (!first_found) {
                first_x++;
            }
        }
        boolean second_found = false;
        second_x = first_x;

        while (second_x < dimension && !second_found) {
            second_y = (second_x == first_x) ? first_y + 1 : 0;
            for (; second_y < dimension; second_y++) {
                if (this.tiles[second_x][second_y] != 0) {
                    second_found = true;
                    break;
                }
            }
            if (!second_found) {
                second_x++;
            }
        }

        //System.out.println("SWAP " + first_x + "," + first_y + "," + second_x + "," + second_y);
        return swap(this, first_x, first_y, second_x, second_y);

    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] titles = null;
        Board board = null;
        Iterable<Board> iter = null;

        board = new Board(new int[][]{
                {1, 0},
                {2, 3}
        });
        assert board.manhattan() == 2 + 1;

        board = new Board(
                new int[][]{
                        {5, 8, 7},
                        {1, 4, 6},
                        {3, 0, 2}
                }
        );
        assert board.manhattan() == 17;

        board = new Board(
                new int[][]{
                        {0, 1, 5},
                        {6, 2, 7},
                        {8, 4, 3}
                }
        );
        assert board.manhattan() == 14;

        board = new Board(
                new int[][]{
                        {14, 7, 0, 3},
                        {2, 9, 8, 13},
                        {15, 11, 10, 1},
                        {5, 12, 6, 4}
                }
        );
        assert board.manhattan() == 38;

        board = new Board(new int[][]{{0, 1}, {2, 3},});
        assert board.twin().equals(new Board(new int[][]{{0, 2}, {1, 3}}));
//        iter = board.neighbors();
//        iter.forEach(
//                b -> System.out.println(b.toString())
//        );

        board = new Board(new int[][]{{1, 0}, {2, 3},});
//        board.neighbors().forEach(
//                b -> System.out.println(b.toString())
//        );

        board = new Board(new int[][]{{1, 2}, {0, 3},});
        board.neighbors().forEach(b -> System.out.println(b.toString()));

        board = new Board(new int[][]{{1, 2}, {3, 0},});
        board.neighbors().forEach(b -> System.out.println(b.toString()));


        Board board3 = new Board(new int[][]{{1, 0}, {3, 2}});
        assert board3.twin().equals(new Board(new int[][]{{3, 0}, {1, 2}}));


        titles = new int[][]{{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
        board = new Board(titles);
        iter = board.neighbors();
        iter.forEach(b -> System.out.println(b.toString()));


        titles = new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};

        board = new Board(titles);
        System.out.println("Check center:" + board.manhattan());
        assert board.manhattan() == 10;
        iter = board.neighbors();
        iter.forEach(b -> System.out.println(b.toString()));


        titles = new int[][]{{1, 8, 3}, {4, 5, 6}, {7, 2, 0}};
        board = new Board(titles);
        System.out.println("Check right bottom corner:" + board.manhattan());
        iter = board.neighbors();
        iter.forEach(b -> System.out.println(b.toString()));

        System.out.println("Test twin");
        board = new Board(new int[][]{{1, 8, 3}, {4, 5, 6}, {7, 2, 0}});
        System.out.println(board.twin().toString());
        assert board.twin().equals(new Board(new int[][]{{8, 1, 3}, {4, 5, 6}, {7, 2, 0}}));

        LinkedList<Board> testHash = new LinkedList<>();
        testHash.add(board);
        Board board2 = new Board(new int[][]{{1, 8, 3}, {4, 5, 6}, {7, 2, 0}});

        assert board.equals(board2);
        assert testHash.contains(board2);


        board = new Board(new int[][]{{1, 2, 3}, {4, 5, 6}, {8, 7, 0}});
        assert board.twin().equals(new Board(new int[][]{{2, 1, 3}, {4, 5, 6}, {8, 7, 0}}));


    }
}
