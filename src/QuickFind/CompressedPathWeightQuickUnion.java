package QuickFind;

import QuickFind.WeightedQuickUnion;

public class CompressedPathWeightQuickUnion extends WeightedQuickUnion {
    public CompressedPathWeightQuickUnion(int N){
        super(N);
    }

    @Override
    protected int root (int v){
        while (this.id[v] !=v ){
            this.id[v] = this.id[this.id[v]]; //compress path is here!
            v = this.id[v];
        }
        return v;
    }

    public static void main(String[] args){
        QuickFindUnion qf = new CompressedPathWeightQuickUnion(10);
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

/*
largest_member
 */