package StackAndQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;
import utility.NumberArrayCreator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private class Node {
        Item value;
        Node next;

        Node(Item value) {
            this.value = value;
            this.next = null;
        }
    }

    private Node first = null;
    private Node last = null;

    private boolean changed_value = false;
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.first == null;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    private Iterator<Item> last_sample_iterator = null;

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldLast = this.last;

        Node newLast = new Node(item);
        this.last = newLast;

        if (this.isEmpty()) {
            this.first = newLast;
        } else {
            oldLast.next = newLast;
        }
        this.size += 1;
        changed_value = true;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Randomized Queue is empty");
        }
        Item value = this.first.value;
        Node oldFirst = this.first;
        this.first = oldFirst.next;

        if (this.isEmpty()) {
            this.last = null;
        }

        this.size -= 1;
        changed_value = true;
        return value;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        //obtain the first iterator
        if (this.isEmpty())
            throw new NoSuchElementException("Randomized Queue is empty");
        if (this.last_sample_iterator == null || this.changed_value || !this.last_sample_iterator.hasNext())
            this.last_sample_iterator = this.iterator();
        this.changed_value = false;
        return this.last_sample_iterator.next();
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            Item[] _buffer = null;
            int inx = 0;

            {
                int total_size = RandomizedQueue.this.size();
                RandomizedQueue.Node ref = RandomizedQueue.this.first;
                _buffer = (Item[]) new Object[total_size];

                for (int i = 0; ref != null; i++) {
                    _buffer[i] = (Item) ref.value;
                    ref = ref.next;
                }
                //Shuffle the list
                StdRandom.shuffle(_buffer);
                System.out.println("Called StdRandom");
//                for (int i = 1 ; i<total_size; i++){
//                    //Pick a random index (0,i-1)
//                    int j = random.nextInt(i);
//                    swap_value(_buffer, i, j);
//                }
            }

            void swap_value(Item[] a, int i, int j) {
                Item tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }

            @Override
            public boolean hasNext() {
                return inx < _buffer.length;
            }

            @Override
            public Item next() {
                return _buffer[inx++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

    }

    // unit testing (required)
    public static void main(String[] args) {
        testQueueLoading();
        testRandomLoading();
    }

    private static void testQueueLoading() {
        NumberArrayCreator creator = new NumberArrayCreator(1);
        int[] list = creator.create_number_array(10, 1, 100);
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i : list) {
            queue.enqueue(i);
        }
        int count_items = 0;
        //verify queue
        for (int i : list) {
            count_items += 1;
            assert queue.dequeue() == i : "Queue not matching";
        }
        assert count_items == list.length;
        assert queue.isEmpty();
    }

    private static void testRandomLoading() {
        int[] list = new int[100];
        for (int i = 0; i < list.length; i++) list[i] = i;

        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        for (int i : list) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 100; i++) {
            queue.sample();
        }


        int counter = 0;
        for (int i : queue) {
            assert i != list[counter++]: i + "=" + list[counter];
        }

    }

}
