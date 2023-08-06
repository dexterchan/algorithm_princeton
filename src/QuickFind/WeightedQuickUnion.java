package QuickFind;

import QuickFind.QuickFindUnion;

public class WeightedQuickUnion extends QuickFindUnion{
    protected int[] sz;
    public WeightedQuickUnion(int N){
        super(N);
        sz = new int[N];
        for (int i = 0;i<N;i++){
            sz[i] = 1;
        }
    }

    /*
    It leads to find complexity improve from O(N) to O(logN)
    * */
    @Override
    public void union(int p, int q){
        if (this.connected(p, q))
            return;
        int i = this.root(p);
        int j = this.root(q);
        if (this.sz[i] < this.sz[j]){
            this.id[i] = j;
            this.sz[j] += this.sz[i];
        }else{
            this.id[j] = i;
            this.sz[i] += this.sz[j];
        }
    }

    public static void main (String [] args){
        QuickFindUnion qf = new WeightedQuickUnion(10);
        qf.union(0,5);
        qf.union(1,2);
        qf.union(3,4);
        qf.union(5,6);
        qf.union(2,7);
        qf.union(3,8);
        qf.union(4,9);
        System.out.println(qf.toString());
        qf.union(5, 9);
        System.out.println(qf.toString());
    }

}
