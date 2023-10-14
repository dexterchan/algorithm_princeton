public class Percolation {
    private class QuickFind {
        int[] id = null;
        int[] sz = null;

        QuickFind(int n) {
            this.id = new int[n];
            this.sz = new int[n];
            for (int i = 0; i < n; i++) {
                this.id[i] = i;
                this.sz[i] = 1;
            }
        }

        int find(int value) {
            int inx = value;
            while (this.id[inx] != inx) {
                this.id[inx] = this.id[this.id[inx]];//compress path here!
                inx = this.id[inx];
            }
            return inx;
        }

        boolean is_connected(int x, int y) {
            return this.find(x) == this.find(y);
        }

        void union(int x, int y) {
            if (this.is_connected(x, y)) {
                return;
            }

            int rootX = this.find(x);
            int rootY = this.find(y);

            if (this.sz[rootX] < this.sz[rootY]) {
                //merge to Y
                this.id[rootX] = rootY;
                this.sz[rootY] += this.sz[rootX];
            } else {
                this.id[rootY] = rootX;
                this.sz[rootX] += this.sz[rootY];
            }
        }
    }

    static int address(int row, int col, int N) {
        return (row - 1) * N + (col - 1);
    }

    private int size;
    private QuickFind quickFind;


    private boolean[] grid = null;
    private int numOpen = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.size = n;
        this.quickFind = new QuickFind(n * n);
        this.grid = new boolean[n * n];

        for (int i = 0; i < n * n; i++) {
            this.grid[i] = false;
        }
        // the row and column indices are integers between 1 and n, where (1, 1) is the upper-left site
    }

    private boolean checkRowColValid(int row, int col) {
        if (row < 0 || row > this.size)
            return false;
        if (col < 0 || col > this.size)
            return false;
        return true;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!this.checkRowColValid(row, col)) {
            throw new IllegalArgumentException();
        }

        int i = address(row, col, this.size);

        if (grid[i]) {
            return;
        }

        this.grid[i] = true;
        this.numOpen++;

        //Check connected point
        if (row - 1 >= 1) {
            //Up
            if (isOpen(row - 1, col)) {
                this.quickFind.union(address(row - 1, col, this.size), i);
            }
        }

        if (col + 1 <= this.size) {
            //right
            if (isOpen(row, col + 1)) {
                this.quickFind.union(address(row, col + 1, this.size), i);
            }
        }

        if (row + 1 <= this.size) {
            //down
            if (isOpen(row + 1, col)) {
                this.quickFind.union(address(row + 1, col, this.size), i);
            }
        }

        if (col - 1 >= 1) {
            //left
            if (isOpen(row, col - 1)) {
                this.quickFind.union(address(row, col - 1, this.size), i);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!this.checkRowColValid(row, col)) {
            throw new IllegalArgumentException();
        }

        int i = address(row, col, this.size);
        return this.grid[i];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!this.checkRowColValid(row, col)) {
            throw new IllegalArgumentException();
        }

        return !this.isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        boolean[] buffer = new boolean[this.size * this.size];
        //Scan upper row
        //Reduce the time complexity to N
        //Trade off: memory grow to N^2
        for (int i=1;i<=this.size;i++){
            int uaddress = address(1, i, this.size);
            int v = this.quickFind.find(uaddress);
            buffer[v] = true;
        }
        for (int i=1;i<=this.size;i++){
            int laddress = address(this.size, i, this.size);
            int v = this.quickFind.find(laddress);
            if (buffer[v]) return true;
        }
//
//        for (int uc = 1; uc <= this.size; uc++) {
//            for (int lc = 1; lc <= this.size; lc++) {
//                int uAddress = address(1, uc, this.size);
//                int lAddress = address(this.size, lc, this.size);
//                if (this.quickFind.is_connected(uAddress, lAddress)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation pl = new Percolation(3);

        assert !pl.percolates();

        pl.open(2, 2);
        assert !pl.percolates();

        pl.open(1, 1);
        assert !pl.percolates();

        assert pl.numberOfOpenSites() == 2;

        pl.open(3, 3);
        assert !pl.percolates();

        pl.open(1, 2);
        assert !pl.percolates();

        pl.open(3, 2);
        assert pl.percolates();
    }
}