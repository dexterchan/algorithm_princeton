package QuickFind;

public class QuickFindUnion {
    protected int[] id;
    public QuickFindUnion(int N){
        id = new int[N];
        for (int i=0;i<N;i++){
            id[i] = i;
        }
    }

    protected int root (int v){
        while (this.id[v] !=v ){
            v = this.id[v];
        }
        return v;
    }

    public boolean connected(int p, int q){
        return this.root(p) == this.root(q);
    }

    public void union(int p, int q){
        if (this.connected(p, q))
            return;
        int i = this.root(p);
        int j = this.root(q);
        this.id[i] = j;

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
    public static void main (String [] args){
        QuickFindUnion qf = new QuickFindUnion(10);
        qf.union(0,5);
        qf.union(1,2);
        qf.union(3,4);
        qf.union(5,6);
        qf.union(2,7);
        qf.union(3,8);
        qf.union(4,9);
        System.out.println(qf.toString());
    }
}
