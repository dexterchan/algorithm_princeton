package StackAndQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node{
        Item value;
        Node next;
        Node(Item value){
            if (value == null){
                throw new IllegalArgumentException("value cannot be null");
            }
            this.value = value;
            next = null;
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
        Node lastFirst = this.first;
        Node newFirst = new Node(item);
        this.first = newFirst;
        newFirst.next = lastFirst;

        if (this.isEmpty()){
            this.last = newFirst;
        }
        this.size += 1;
    }

    // add the item to the back
    public void addLast(Item item){
        Node lastLast = this.last;

        Node newLast = new Node(item);
        this.last = newLast;
        if (this.isEmpty()){
            this.first = newLast;
        }else
            lastLast.next = newLast;

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

        if (this.isEmpty()){
            this.last = null;
        }
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
        this.last = null;

        return value;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return null;
    }

    // unit testing (required)
    public static void main(String[] args){

    }

}