package deque;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] cells;
    private int last;
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        cells = (Item[]) new Object[2];
        last = -1;
    }


    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        size++;
        if (last+1 >= cells.length) {
            Item[] newCells = (Item[]) new Object[cells.length*2];
            for (int i = 0; i < cells.length; i++) {
                newCells[i] = cells[i];
            }
            cells = newCells;
        }

        cells[++last] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) throw new NoSuchElementException();

        size--;
        int ranNum;
        ranNum = StdRandom.uniform(0, last+1);
        Item ret = cells[ranNum];
        cells[ranNum] = cells[last];
        cells[last--] = null;

        if (cells.length > 2 && last <= cells.length/4) {
            Item[] newCells = (Item[]) new Object[cells.length/2];
            for (int i = 0; i < cells.length/2; i++) {
                newCells[i] = cells[i];
            }
            cells = newCells;
        }

        return ret;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.isEmpty()) throw new NoSuchElementException();

        return cells[StdRandom.uniform(0, last+1)];
    }

    private class IRanDeque implements Iterator<Item> {
        private final int[] per;
        private int cur;

        private IRanDeque() {
            per = StdRandom.permutation(last+1);
            cur = -1;
        }

        @Override
        public boolean hasNext() {
            return cur+1 < per.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            cur++;
            return cells[per[cur]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new IRanDeque();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Character> queue1 = new RandomizedQueue<>();
        RandomizedQueue<Character> queue2 = new RandomizedQueue<>();
        for (char i = 'A'; i <= 'z'; i++) {
            queue1.enqueue(i);
            queue2.enqueue(i);
        }
        for (char i = 'A'; i <= 'z'; i++) {
            StdOut.printf("%c ", queue1.dequeue());
        }
        StdOut.println();
        for (char i = 'A'; i <= 'z'; i++) {
            StdOut.printf("%c ", queue2.dequeue());
        }
        StdOut.println();


        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.isEmpty()     ;
        queue.size()        ;
        queue.enqueue(668);
        queue.dequeue()     ;
        queue.isEmpty()     ;
        queue.enqueue(92);
        queue.dequeue()     ;
        queue.enqueue(346);
    }

}