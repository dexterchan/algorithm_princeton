import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

public class BoggleBoard {
    private char[][] board = null;
    private final int row;
    private final int col;

    // Initializes a random 4-by-4 Boggle board.
    // (by rolling the Hasbro dice)
    public BoggleBoard() {
        this(4, 4);
    }

    // Initializes a random m-by-n Boggle board.
    // (using the frequency of letters in the English language)
    public BoggleBoard(int m, int n) {
        this.row = m;
        this.col = n;
        if (m < 0) throw new IllegalArgumentException();
        if (n < 0) throw new IllegalArgumentException();

        this.board = new char[this.row][this.col];
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.row; j++) {
                this.board[i][j] = (char) (StdRandom.uniformInt(0, 26) + 'A');
            }
        }
    }

    // Initializes a Boggle board from the specified filename.
    public BoggleBoard(String filename) {

        In fileStream = new In(filename);
        if (!fileStream.exists()) throw new IllegalArgumentException(filename + " not exists");

        this.row = fileStream.readInt();
        this.col = fileStream.readInt();
        if (this.row < 0) throw new IllegalArgumentException();
        if (this.col < 0) throw new IllegalArgumentException();

        this.board = new char[this.row][this.col];
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                String readStr = fileStream.readString();
                if (readStr.length() == 2 && readStr.equals("Qu")) {
                    this.board[i][j] = 'Q';
                    continue;
                }
                if (readStr.length() == 1) {
                    this.board[i][j] = readStr.charAt(0);
                    continue;
                }
                throw new IllegalArgumentException("Invalid " + readStr + " at " + i + "," + j);
            }
        }
    }

    // Initializes a Boggle board from the 2d char array.
    // (with 'Q' representing the two-letter sequence "Qu")
    public BoggleBoard(char[][] a) {
        this.row = a.length;
        if (a == null || this.row == 0) throw new IllegalArgumentException();

        this.col = a[0].length;
        this.board = new char[this.row][this.col];
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.row; j++) {
                this.board[i][j] = a[i][j];
            }
        }
    }

    // Returns the number of rows.
    public int rows() {
        return this.row;
    }

    // Returns the number of columns.
    public int cols() {
        return this.col;
    }

    // Returns the letter in row i and column j.
    // (with 'Q' representing the two-letter sequence "Qu")
    public char getLetter(int i, int j) {
        isValidDimension(i, j);
        return this.board[i][j];
    }

    // Returns a string representation of the board.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.rows(); i++) {
            for (int j = 0; j < this.cols(); j++) {
                char c = this.getLetter(i, j);
                if (c == 'Q') {
                    sb.append("Qu ");
                } else {
                    sb.append(c);
                    sb.append("  ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    private void isValidDimension(int m, int n) {
        if (m < 0 || m >= this.row) throw new IllegalArgumentException();
        if (n < 0) throw new IllegalArgumentException();
    }
}

