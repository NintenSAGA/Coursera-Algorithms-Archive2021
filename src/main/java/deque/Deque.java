package deque;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private LinkedNode first;
    private LinkedNode last;
    private int size;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    private class LinkedNode {
        private final Item val;
        private LinkedNode next;
        private LinkedNode prev;
        private LinkedNode(Item val) {
            this.val = val;
            this.next = null;
            this.prev = null;
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (this.isEmpty()) {
            addEmpty(item);
        } else {
            LinkedNode node = new LinkedNode(item);
            node.next = first;
            first.prev = node;
            first = node;
        }

        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (this.isEmpty()) {
            addEmpty(item);
        } else {
            LinkedNode node = new LinkedNode(item);
            last.next = node;
            node.prev = last;
            last = node;
        }

        size++;
    }

    private void addEmpty(Item item) {
        LinkedNode node = new LinkedNode(item);
        first = node;
        last = node;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            Item ret = first.val;
            first = first.next;
            size--;

            if (size == 0) {
                first = null;
                last = null;
            } else first.prev = null;

            return ret;
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            Item ret = last.val;
            last = last.prev;
            size--;

            if (size == 0) {
                first = null;
                last = null;
            } else last.next = null;

            return ret;
        }
    }

    private class IDeque implements Iterator<Item> {
        LinkedNode cur;

        private IDeque() {
            cur = null;
        }

        @Override
        public boolean hasNext() {
            if (cur == null) return first != null;
            return cur.next != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            if (cur == null) cur = first;
            else cur = cur.next;

            return cur.val;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new IDeque();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addLast("EAKZJDNBJO");
        for (String i: deque) {
            StdOut.println(i);
        }
    }

}