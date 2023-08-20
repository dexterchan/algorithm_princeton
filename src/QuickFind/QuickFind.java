package QuickFind;

public class QuickFind {
    private int[] id;

    public QuickFind(int N){
        id = new int[N];
        for (int i=0;i<N;i++) {
            id[i] = i;
        }
    }

    public boolean connected(int i, int j){
        return this.id[i] == this.id[j];
    }


    public void union(int i , int j){
        if (!this.connected(i,j)) {
            int prev = this.id[j];
            int new_value = this.id[i];
            this.id[j] = new_value;

            //at most 2N+2 operation
            //iterate the old prev value and update by j
            for (int c = 0; c < this.id.length; c++) {
                if (this.id[c] == prev) {
                    this.id[c] = new_value;
                }
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<this.id.length;i++){
            if (i<10)
                sb.append(" ");
            sb.append(i);
            sb.append("|");
        }
        sb.append("\n");

        for (int i=0; i<this.id.length;i++){
            if (id[i]<10)
                sb.append(" ");
            sb.append(id[i]);
            sb.append("|");
        }
        sb.append("\n");

        return sb.toString();
    }
    public static void main(String [] args){
        QuickFind qf = new QuickFind(10);
        qf.union(0,5);
        qf.union(1,2);
        qf.union(3,4);
        qf.union(5,6);
        qf.union(2,7);
        qf.union(3,8);
        qf.union(4,9);
        qf.connected(8,9 );
        System.out.println(qf.toString());

    }
}
