package StackAndQueue;
import utility.NumberArrayCreator;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {

    private final class Node{
        Item value;
        Node next;
        Node prev;

        Node(Item value){
            if (value == null){
                throw new IllegalArgumentException("value cannot be null");
            }
            this.value = value;
            next = prev = null;
        }

        void updateNodeAhead(Node newHeadNode){
            Node lastHeadNode = this.prev;
            this.prev = newHeadNode;
            if (newHeadNode != null ) {
                newHeadNode.prev = lastHeadNode;
                newHeadNode.next = this;
            }
            if (lastHeadNode!=null)
                lastHeadNode.next = newHeadNode;

        }

        void updateNodeBehind(Node node){
            Node lastNextNode = this.next;
            this.next = node;
            if (node != null) {
                node.next = lastNextNode;
                node.prev = this;
            }
            if (lastNextNode != null)
                lastNextNode.prev = node;
        }

        void removeMyself(){
            Node lastNextNode = this.next;
            Node lastPrevNode = this.prev;

            if (lastNextNode!=null)
                lastNextNode.prev = lastPrevNode;
            if (lastPrevNode!=null)
                lastPrevNode.next = lastNextNode;
            this.next = null;
            this.prev = null;
        }

        @Override
        public String toString(){
            String add = (this.next!=null)?this.next.toString():"";
            return this.value+"->"+ add;
        }
    }

    private Node first;
    private Node last;

    private int size;

    // construct an empty deque
    public Deque(){

    }

    // is the deque empty?
    public boolean isEmpty(){
        return this.first == null;
    }

    // return the number of items on the deque
    public int size(){
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item){
        if (item == null)
            throw new IllegalArgumentException();
        Node lastFirst = this.first;
        Node newFirst = new Node(item);

        if (lastFirst != null)
            lastFirst.updateNodeAhead(newFirst);

        if (this.isEmpty()){
            this.last = newFirst;
        }

        this.first = newFirst;
        this.size += 1;
    }

    // add the item to the back
    public void addLast(Item item){
        if (item == null)
            throw new IllegalArgumentException();
        Node lastLast = this.last;

        Node newLast = new Node(item);
        this.last = newLast;
        if (this.isEmpty()){
            this.first = newLast;
        }else
            lastLast.updateNodeBehind(newLast);

        this.size += 1;
    }

    // remove and return the item from the front
    public Item removeFirst(){
        if (this.isEmpty()){
            throw new NoSuchElementException("Dequeue is empty");
        }
        Node old_first = this.first;
        Item value = old_first.value;
        this.first = old_first.next;

        old_first.removeMyself();

        if (this.isEmpty()){
            this.last = null;
        }
        this.size -= 1;
        return value;
    }

    // remove and return the item from the back
    //We need double linked list here to keep remove last worst cost is a constant
    public Item removeLast(){
        if (this.isEmpty()){
            throw new NoSuchElementException("Dequeue is empty");
        }
        Node old_last = this.last;
        Item value = old_last.value;

        Node prev_Node = old_last.prev;
        this.last = prev_Node;
        old_last.removeMyself();

        if (prev_Node == null){
            this.first = null;
        }
        this.size -= 1;
        return value;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new Iterator<Item>() {
            Node ref = Deque.this.first;

            @Override
            public boolean hasNext() {
                return ref != null;
            }

            @Override
            public Item next() {
                if (ref == null){
                    throw new NoSuchElementException();
                }
                Item v = ref.value;
                ref = ref.next;
                return v;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // unit testing (required)
    public static void main(String[] args){
        testAddFirstElement();
        testAddFirstLastElement();
        testAddSeqQueue();
//        testAddSeqStack();
//        testStackQueueCombine();
    }

    private static  void testAddFirstElement(){
        System.out.println("testAddFirstElement");
        Deque<Integer> queue = new Deque<>();
        queue.addFirst(100);
        assert queue.size()==1:" Not valid";
        int count_size = 0;
        for (int i : queue){
            count_size += 1;
            //System.out.println(i);
        }
        assert queue.removeFirst() == 100: "value should be 100";
        assert queue.isEmpty(): "Not empty";

        count_size = 0;
        queue.addFirst(200);

        for (int i : queue){
            count_size += 1;
            System.out.println(i);
        }
        assert count_size == 1;
        assert queue.removeLast() == 200: "value should be 200";
        assert queue.isEmpty() && queue.size() == 0;

        queue.addFirst(200);
        queue.addFirst(100);
        count_size = 0;
        for (int i : queue){
            count_size += 1;
            //System.out.print(i + ",");
        }
        System.out.println();

    }

    private static void testAddFirstLastElement(){
        System.out.println("testAddFirstLastElement");
        Deque<Integer> queue = new Deque<>();
        int[] values = {100, 200};
        queue.addLast(values[1]);
        assert !queue.isEmpty();
        queue.addFirst(values[0]);
        int count_items = 0;

        for (int i : queue){
//            System.out.print(i);
//            System.out.print(",");
            count_items++;
        }
        System.out.println();
        assert count_items == values.length;
        assert queue.removeLast() == values[values.length-1];

        count_items = 0;
        for (int i : queue){
//            System.out.print(i);
//            System.out.print(",");
            count_items++;
        }
        assert count_items == 1;

        queue.addLast(300);
        assert queue.removeLast() == 300;

        queue.addFirst(20);
        assert queue.removeLast() == 100;
        assert queue.removeFirst() == 20;
        System.out.println();
    }

    private static void testAddSeqQueue(){
        System.out.println("testAddSeqQueue");
        NumberArrayCreator creator = new NumberArrayCreator(1);
        int[] list = creator.create_number_array(1000, 1, 100);

        int count_num=0;

        Deque<Integer> queue = new Deque<>();
        for (int i : list){
            queue.addLast(i);
        }
        System.out.println("queue");
        for (int i : queue){
            //System.out.print(i+",");
        }
        System.out.println();
        //Now verify if the queue works
        for (int i: list){
            count_num += 1;
            assert queue.removeFirst() == i;
        }
        assert count_num == list.length;
        assert queue.isEmpty();
    }

//    public static void testAddSeqStack(){
//        System.out.println("testAddSeqStack");
//        NumberArrayCreator creator = new NumberArrayCreator(1);
//        int[] list = creator.create_number_array(1000, 1, 100);
//        Stack<Integer> refStack = new Stack<>();
//        Deque<Integer> queue = new Deque<>();
//        //Now verify if the stack works
//        for (int i : list){
//            refStack.add(i);
//            queue.addFirst(i);
//            //System.out.println(queue.first);
//        }
//        System.out.println("stack");
//        assert  !queue.isEmpty();
//        assert queue.size() == refStack.size();
//
//        while (!refStack.isEmpty()){
//            assert refStack.pop() == queue.removeFirst();
//        }
//        assert queue.isEmpty();
//    }

//    public static void testStackQueueCombine(){
//        System.out.println("testStackQueueCombine");
//        NumberArrayCreator creator = new NumberArrayCreator(1);
//        int[] list = creator.create_number_array(1000, 1, 100);
//
//        Deque<Integer> queue = new Deque<>();
//
//        //queue
//        for (int i : list){
//            queue.addLast(i);
//        }
//        for (int i : list){
//            assert queue.removeFirst() == i : "Queue not matching";
//        }
//        assert queue.isEmpty();
//
//        //stack
//        Stack<Integer> refStack = new Stack<>();
//        for (int i : list){
//            queue.addFirst(i);
//            refStack.add(i);
//        }
//        while(!refStack.isEmpty()){
//            assert refStack.pop() == queue.removeFirst() : "Stack not matching";
//        }
//        assert queue.isEmpty();
//
//    }





}