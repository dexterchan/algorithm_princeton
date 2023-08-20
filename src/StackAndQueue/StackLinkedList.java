package StackAndQueue;
import utility.NumberArrayCreator;

public class StackLinkedList<T> {
    private class Node {
        T data;
        Node next = null;
        Node(T value){
            this.data = value;
        }
    }
    private Node top = null;
    private int size = 0;

    public T push(T value){
        Node n = new Node(value);
        n.next = top;
        top = n;
        this.size += 1;
        return value;
    }

    public T pop(){
        if (top == null){
            throw new IllegalStateException("Stack is empty");
        }
        T value = this.top.data;
        this.top = this.top.next;
        this.size -= 1;
        return value;
    }

    public int length(){
        return this.size;
    }

    public static void main(String [] args) throws Exception{
        NumberArrayCreator creator = new NumberArrayCreator(1);
        int[] list = creator.create_number_array(10, 1, 100);

        StackLinkedList<Integer> stack = new StackLinkedList<>();
        for (int i : list){
            stack.push(i);
        }
        if( stack.length() != list.length) throw new RuntimeException();

        for (int i=list.length-1;i>=0;i--){
            if (list[i] != stack.pop())
                throw new RuntimeException();
        }
    }
}


