package StackAndQueue;

import utility.NumberArrayCreator;

public class QueueLinkedList <T>{
    private class Node{
        T value;
        Node next;
        Node(T value){
            this.value = value;
        }
    }

    private Node first = null;
    private Node last = null;
    private int length = 0;

    public boolean isEmpty(){
        return first == null;
    }
    public boolean add (T value){
        Node new_last = new Node(value);
        Node old_last = last;
        last = new_last;

        if (this.isEmpty()){
            first = new_last;
        }else {
            old_last.next = new_last;
        }

        this.length += 1;
        return true;
    }

    public T remove(){
        if (first == null){
            throw new IllegalStateException("Empty queue");
        }
        T value = first.value;
        first = first.next;
        this.length -= 1;
        if (this.isEmpty()){
            this.last = null;
        }
        return value;
    }

    public int length(){
        return length;
    }

    public static void main(String [] args) throws Exception {
        NumberArrayCreator creator = new NumberArrayCreator(1);
        int[] list = creator.create_number_array(10, 1, 100);


        QueueLinkedList<Integer> queue = new QueueLinkedList<>();
        for (int i : list){
            System.out.print(i + ",");
            queue.add(i);
        }
        System.out.println("");

        if (queue.length() != list.length) throw new RuntimeException();

        for (int i=0; i < list.length; i++){
            if (list[i] != queue.remove()) throw new RuntimeException();
        }
        if (queue.length() != 0 ) throw new RuntimeException();

        queue.add(5);
        queue.add(10);
        if (queue.remove()!=5) throw new RuntimeException();
        if (queue.remove()!=10) throw new RuntimeException();
    }
}
