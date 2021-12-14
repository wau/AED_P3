package aed.tables;


import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

public class Treap<Key extends Comparable<Key>,Value> {

    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    private int size;
    private Node root;
    protected Random r;

    public class MinPriorityQueue<T extends Comparable<T>> implements Iterable<T>{

        UnrolledLinkedList<T> list;
        int qsize;
        int blockSize = 880;//770
        private MinPriorityQueue<T> tMinPriorityQueue;



        public Iterator<T> iterator() {
            return new MinPriorityQueueIterator();
        }

        private class MinPriorityQueueIterator implements Iterator<T>  {
            MinPriorityQueue<T> copy;

            MinPriorityQueueIterator() {
                this.copy = shallowCopy();
                //System.out.println(Arrays.toString(this.copy.getElements()));
            }


            @Override
            public boolean hasNext() {
                return !copy.isEmpty();
            }

            @Override
            public T next() {
                return copy.removeMin();
            }

        }

        public class Node<T> {
            private T[] items; // elementos do bloco
            private int counter; // n de elementos do array do bloco
            private Node next;
            private int start;
            private int where2Add;
            @SuppressWarnings("unchecked")
            public Node() {

                this.items = (T[]) new Comparable[blockSize];
                this.counter = 0;
                this.next = null;
                this.start = 0;
                this.where2Add = 0;
            }
            public int size() {
                return this.counter;
            }

            public T getItem(int idx) {

                return this.items[idx];
            }
            public void setItem(int idx, T item) {

                this.items[idx] = item;
            }
            public void addInNode(T item) {

                this.items[where2Add] = item;
                this.where2Add++;
                this.counter++;
            }
            public T removeFirst() {
                T result = this.items[this.start];
                this.items[this.start] = null;
                if (start<blockSize-1)
                    this.start++;
                this.counter--;
                return result;
            }


            public void removeSince(int idx, int startSize) {
                for (int i = idx; i < startSize; i++) {
                    this.items[i] = null;
                    this.counter--;
                    //   this.where2Add--;
                }
            }

            public Node shallowCopy() {
                Node newNode = new Node();

                for (int i = 0; i < blockSize; i++) {
                    newNode.items[i] = this.items[i];
                }

                newNode.counter = this.counter;
                newNode.start = this.start;
                newNode.where2Add = this.where2Add;

                if (this.next != null)
                    newNode.next = this.next.shallowCopy();
                return newNode;
            }
        }




        @SuppressWarnings("unchecked")
        public class UnrolledLinkedList<T extends Comparable<T>> {
            private Node first;
            private Node last;
            private int nNodes;
            private int blockSize;
            public UnrolledLinkedList(int blockSize) {
                this.first = new Node();
                this.last = this.first;
                this.blockSize = blockSize;
                this.nNodes = 1;
            }

            ////////////////


            public Object[][] getArrayOfBlocks() {

                Object[][] result = (Object[][])new Object[this.nNodes][this.blockSize];

                Node currentnode = this.first;
                int counter = 0;
                while (currentnode != null && counter < this.nNodes)
                {
                    // for (int i = 0; i < currentnode.size(); i++)
                    for (int i = 0; i < blockSize; i++)
                    {
                        result[counter][i] = currentnode.items[i];
                    }

                    currentnode = currentnode.next;
                    counter++;
                }
                return result;
            }


            void rightShift(Node start, int startIdx) {

                //    T last = (T) start.items[start.where2Add];

                // shift right

                for( int index =start.where2Add-1; index >= startIdx ; index-- )
                {
                    start.items[index+1] = start.items[index];

                    start.items[index] = null;
                }

            }


            public Node middleNode(Node start, int n) {

                int i = 0;
                while (i < n/2)
                {
                    start = start.next;
                    i++;
                }
                return start;
            }
            private class rMoveHalf {
                Node node;
                int idx;

                rMoveHalf(Node node, int idx) {
                    this.node = node;
                    this.idx = idx;
                }
            }

            private rMoveHalf moveHalf(Node node, int idx) {
                Node result;
                Node newNode = new Node();

                int test = Math.max(node.start, (blockSize / 2));

                for (int i = test; i < blockSize; i++) {
                    T citem = (T) node.getItem(i);
                    newNode.addInNode(citem);

                    node.items[i] = null;
                    node.counter--;
                }

                //  node.removeSince(test, blockSize);
                node.where2Add = test;

                Node next = node.next;
                node.next = newNode;

                newNode.next = next;


                this.nNodes++;

                if (idx >= test) {
                    return new rMoveHalf(node.next, idx - test);
                }
                else {
                    return new rMoveHalf(node, idx);
                }
            }

            public UnrolledLinkedList<T> shallowCopy() {
                UnrolledLinkedList<T> newList = new UnrolledLinkedList<T>(this.blockSize);

                newList.first = this.first.shallowCopy();
                newList.nNodes = this.nNodes;
                newList.blockSize = this.blockSize;


                return newList;
            }



            @SuppressWarnings("unchecked")
            public void addWithNode(Node currentnode, int idx, T item) {
                if (currentnode.where2Add >= blockSize) {
                    rMoveHalf r  = moveHalf(currentnode, idx);
                    rightShift(r.node, r.idx); // right shift first
                    r.node.setItem(r.idx, item);
                    r.node.counter++;
                    r.node.where2Add++;

                } else {
                    rightShift(currentnode, idx); // right sh
                    currentnode.setItem(idx, item);
                    currentnode.counter++;
                    currentnode.where2Add++;
                }
            }
        }
        public MinPriorityQueue()
        {
            this.list = new UnrolledLinkedList<T>(blockSize);
            //    this.list.first = null;
            this.qsize = 0;
            //TODO: implement
        }


        private class NodePlusIndex {
            private Node node;
            private int idx;
            private boolean has0or1element;

            NodePlusIndex(Node node, int idx, boolean has0or1element) {
                this.node = node;
                this.idx = idx;
                this.has0or1element = has0or1element;
            }

        }

        public <T extends Comparable<T>> int binarySearch(T[] arr, int low, int high, T item)
        {
            int mid = low + (high - low) / 2;

            if (high >= low) {

                int cmp = arr[mid].compareTo(item);

                if (cmp >= 0)
                    return binarySearch(arr, low, mid - 1, item);

                // if (cmp < 0)
                return binarySearch(arr, mid + 1, high, item);
            }
            return mid;
        }

        @SuppressWarnings("unchecked")
        public NodePlusIndex completeSearch(T item)
        {
            Node currentnode = list.first;
            Node prev = list.first;

            while (currentnode != null)
            {
                int cmpFirst = item.compareTo((T) currentnode.items[currentnode.start]);
                int cmpLast =  item.compareTo((T) currentnode.items[currentnode.where2Add-1]);

                boolean canCompare = currentnode.size() > 1;

                if ((canCompare && cmpFirst >= 0 && cmpLast <= 0) || (currentnode.where2Add < blockSize && cmpFirst <= 0) || (currentnode.where2Add < blockSize && cmpLast >= 0 && currentnode.next == null)) {
                    return new NodePlusIndex(currentnode, binarySearch((T[]) currentnode.items, currentnode.start, currentnode.where2Add-1, item), false);
                }
                else{
                    prev = currentnode;
                    currentnode = currentnode.next;
                }
            }

            Node newNode = new Node();
            this.list.nNodes++;
            prev.next = newNode;

            return new NodePlusIndex(newNode, binarySearch((T[]) newNode.items, newNode.start, newNode.where2Add-1, item), true);
        }

        //needed for testing purposes only
        public Object[] getElements() {

            Object[] result = new Object[qsize];
            int r = 0;

            Node currentnode = this.list.first;

            int counter = 0;

            while (currentnode != null && counter < this.list.nNodes)
            {
                // for (int i = 0; i < currentnode.size(); i++)
                for (int i = currentnode.start; i < currentnode.where2Add; i++)
                {
                    result[r++] = currentnode.items[i];
                }

                currentnode = currentnode.next;
                counter++;
            }
            return result;
        }

        public MinPriorityQueue<T> shallowCopy()
        {
            MinPriorityQueue<T> pQueue = new MinPriorityQueue<T>();
            pQueue.list = this.list.shallowCopy();

            pQueue.blockSize = this.list.blockSize;
            pQueue.qsize = this.qsize;

            return pQueue;
        }

        @SuppressWarnings("unchecked")
        public void insert(T element)
        {
            if (!isEmpty()) {
                NodePlusIndex v = completeSearch(element);

                if (v.has0or1element) {
                    v.node.addInNode(element);
                }
                else
                    this.list.addWithNode(v.node, v.idx, element);
            }
            else
                this.list.addWithNode(this.list.first, 0, element);

            this.qsize++;
        }

        @SuppressWarnings("unchecked")
        public T peekMin()
        {
            if (!isEmpty()) {

                return (T)this.list.first.items[this.list.first.start];
            }
            return null;
        }

        public T removeMin()
        {
            T result = null;
            if (!isEmpty()) {

                result = (T) this.list.first.removeFirst();
                if (this.list.nNodes > 1 && this.list.first.counter <= 0) {
                    this.list.first = this.list.first.next;
                    this.list.nNodes--;
                }
                else if (this.list.nNodes == 1 && this.list.first.counter <= 0) {
                    this.list.first = new Node(); // re initialize
                }

                qsize--;
            }
            return result;
        }

        public boolean isEmpty()
        {
            //TODO: implement
            //  return true;
            return qsize == 0;
        }

        public int size()
        {
            //TODO: implement
            return qsize;
        }

    }

    MinPriorityQueue<Key> pq = new MinPriorityQueue<Key>();


    private class Node {
        private Key key;
        private Value value;
        private Node left;
        private Node right;
        private int priority;
        private int size;

        public Node(Key k, Value v) {
            this.key = k;
            this.value = v;
            this.priority = r.nextInt(1000);

            this.left = null;
            this.right = null;
        }

        public Node(Key k, Value v, int size, int priority)
        {
            this.key = k;
            this.value = v;
            this.priority = priority;

            this.size = 1;
        }

        public Node(Key k, Value v, int customPriority) {
            this.key = k;
            this.value = v;
            this.priority = customPriority;//r.nextInt(1000);

            this.left = null;
            this.right = null;
        }

        public void update() {
              size = getSize(left) + getSize(right) + 1;
        }



        public Node shallowCopy() {
            Node newNode = new Node(this.key, this.value, this.priority);
            newNode.size = this.size;// this.size();

            if (this.right != null)
                newNode.right = this.right.shallowCopy();

            if (this.left != null)
                newNode.left = this.left.shallowCopy();

            return newNode;
        }

        public String toString()
        {
            return "[k:" + this.key + " v:" + this.value + " p:" + this.priority + " s:" + this.size + "]";
        }

        public boolean isLeaf() { return this.left == null && this.right == null; }

        public boolean hasNoNulls() { return this.left != null && this.right != null; }

        public boolean getNotNull() { return (this.left != null) ? true : false; } // left != null: true, right != null: false
                                                                                    // rotateright, rotateleft
    }
    public Treap() {
        this.size = 0;
        this.r = new Random();

    }
    public Treap(Random r) {
        this.size = 0;
        this.r = r;
    }
    public int size() {
        return getSize(this.root);
    }

    private int getSize(Node root) {
        return (root != null) ? root.size : 0;
    }

    /*private void updateSize(Node n) {
        if (n != null)  n.size = getSize(n.left) + getSize(n.right)+ 1;
    }*/


    ////
    public boolean containsKey(Key k) {
        return containsH(this.root, k);
    }
    private boolean containsH(Node n, Key k) { //contains helper function
        if (n == null) return false;

        int cmp = k.compareTo(n.key);
        if (cmp < 0) return containsH(n.left,k);
        else if(cmp > 0) return containsH(n.right,k);
        else return true;
    }
    ////

    public Value get(Key k) {
        return getH(this.root, k);
    }
    private Value getH(Node n, Key k) {
        if(n == null) return null;
        int cmp = k.compareTo(n.key);
        if (cmp < 0) return getH(n.left,k);
        else if(cmp > 0) return getH(n.right,k);
        else return n.value;
    }


    private Node rotateLeft(Node root) {
        Node newR = root.right;

        Node swap = newR.left;
        newR.left = root;
        root.right = swap;

        root.update();
        newR.update();

        return newR;
    }



    private Node rotateRight(Node root) {
        Node newR = root.left;
        Node swap = newR.right;
        newR.right = root;
        root.left = swap;

        root.update();
        newR.update();


        return newR;
    }


    public void put(Key k, Value v) {
        this.root = putH(this.root, k, v, r.nextInt());
    }

    public void put(Key k, Value v, int priority) {
        this.root = putH(this.root, k, v, priority);
    }

    //precisa ter o return node para conseguir atualizar o Node recursivamente
    public Node putH(Node n, Key k, Value v, int priority) {

        /*printTreap(this.root, 0);
        System.out.println();
        System.out.println();*/

        if (n == null) { return new Node(k, v, 1, priority);}


      //  updateSize(n);

        int cmpr = k.compareTo(n.key);
        if (cmpr > 0) {
            n.right = putH(n.right, k, v, priority);

            if (n.right.priority > n.priority)
                n = rotateLeft(n);
        }
        else if (cmpr < 0) {
            n.left = putH(n.left, k, v, priority);

            if (n.left.priority > n.priority)
                n = rotateRight(n);
        }
        else  {
            n.value = v;
        }
        n.update();
        return n;
    }


    public Key min() {
        if (this.root != null) {
            Node n = this.root;
            Key min = n.key;

            while (n.left != null) {
                   min = n.left.key;
                   n = n.left;
            }
            return min;
        }
        return null;
    }
    public void deleteMin() {
        //deleteMinH(this.root, null);
        delete(min());
    }

    public void deleteMinH(Node n, Node father) {
        if (n.left != null)
            deleteMinH(n.left, n);
        else {
            n = null;
            if (father != null)  {father.left = null; father.update();}
            else { root = root.right; root.update();}
        }

    }


    public Treap<Key, Value> shallowCopy() {
        Treap<Key, Value> newTreap = new Treap<Key, Value>(this.r);

        newTreap.root = this.root.shallowCopy();
        newTreap.size = size();

        return newTreap;

    }
  /*  public Key max() {

    }

    public void deleteMax() {

    }

   */
   /* public void deleteMin() {
        if (size > 0) {
            Node n = this.root;
            Node father = null;
            Key min = n.key;

            while (n.left != null) {
                father = n;
                n = n.left;
            }
            //go to last node
            if (size == 1) {

            }
            else {

            }
            father.left = null;
            n = null;
        }
    }*/



    public void delete(Key k) {

//        this.root = deleteH(this.root, k, null, -1);

        this.root = deleteH(k, this.root);

      //  this.root = removeH(k, this.root);
        //tem que atualizar a root sempre.


     //   while ()
    }


    private Node deleteH(Key key, Node current) {
        if (current == null) return null;

        /*
        printTreap(this.root, 0, current);
        System.out.println();
        System.out.println();
        System.out.println();*/

        int cmpr = key.compareTo(current.key);

        if (cmpr < 0) {
            current.left = deleteH(key, current.left);
        }
        else if (cmpr > 0) {
            current.right = deleteH(key, current.right);
           // return current;
        }
        else { //node to be deleted
            current.priority = Integer.MIN_VALUE;

            if (current.hasNoNulls()) {
                Node nRoot;
                if (current.left.priority > current.right.priority) {
                    nRoot = current.left;
                    rotateRight(current);
                    nRoot.right = deleteH(key, current.right);
                }
                else {
                    nRoot = current.right;
                    rotateLeft(current);
                    nRoot.left = deleteH(key, current.left);
                }
                nRoot.update();
                return nRoot;
            }
            else if (current.left != null) { current.left.update(); return current.left; }
            else if (current.right != null) { current.right.update(); return current.right; }
            else { this.size--; return null; }//is a leaf
        }
        current.update();
        return current;
    }

   /* public void deleteH(Node n, Key k, Node father, int where) {
        printTreap(this.root, 0, n);
        System.out.println();
        System.out.println();

        if(n == null) return;
        int cmp = k.compareTo(n.key);
        if (cmp < 0) deleteH(n.left,k, n, LEFT);
        else if(cmp > 0) deleteH(n.right, k, n, RIGHT);
        else {

            if (n.isLeaf()) {
                n = null;

                if (where == RIGHT && father.right != null)
                    father.right = null;
                else if (where == LEFT && father.left != null)
                    father.left = null;
                return;
            }

            n.priority = Integer.MIN_VALUE; 

            if (n.hasNoNulls()) {
                if (n.right.priority > n.left.priority) {//right up
                    n = rotateLeft(n);
                    deleteH(n.left, k, n, LEFT);
                }
                else {
                    n = rotateRight(n);
                    deleteH(n.right, k, n, RIGHT);
                }
            }
            else  {
                if (n.right != null) {
                    n = rotateLeft(n);
                    deleteH(n.left, k, n, LEFT);
                }
                else if (n.left != null) {
                    n = rotateRight(n);
                    deleteH(n.right, k, n, RIGHT);

                }
            };
        }
    }*/


    //helper method that uses the treap to build an array with a heap structure
    private void fillHeapArray(Node n, Object[] a, int position)
    {
        if(n == null) return;

        if(position < a.length)
        {
            a[position] = n;
            fillHeapArray(n.left,a,2*position);
            fillHeapArray(n.right,a,2*position+1);
        }
    }



    public Treap[] split(Key k) {
        return null;
    }

    public Key max() {
        return maxH(this.root);
    }

    public Key maxH(Node root) {

        if (root == null) { return null;}
        else {
            if(root.right == null) {return root.key;}
            else return maxH(root.right);
        }
    }


    public void deleteMax() {
       /* if (this.root != null) {
            deleteMaxH(this.root, null);
            this.root.update();

        }*/
        delete(max());
    }

    public void deleteMaxH(Node n, Node father) {
        if (n.right != null)
            deleteMaxH(n.right, n);
        else {
            n = null;
            if (father != null)  {father.right = null; father.update();}
            else {root = root.right; root.update();}
        }
    }

    public int rank(Key k ){
        return 0;
    }

    public int size(Key min, Key max) {
        return 0;

    }

    public Key select(int n) {
        return null;
    }

    public Iterable<Key> keys(Key min, Key max) {
        return null;
    }

    public Iterable<Key> keys() {
        return pq;
    }

    public Iterable<Integer> priorities() {
        return null;
    }

    public Iterable<Value> values() {
        return null;
    }

    //if you want to use a different organization that a set of nodes with pointers, you can do it, but you will have to change
    //this method to be consistent with your implementation
    private Object[] getHeapArray()
    {
        //we only store the first 31 elements (position 0 is not used, so we need a size of 32), to print the first 5 rows of the treap
        Object[] a = new Object[32];
        fillHeapArray(this.root,a,1);
        return a;
    }

    private void printCentered(String line)
    {
        //assuming 120 characters width for a line
        int padding = (120 - line.length())/2;
        if(padding < 0) padding = 0;
        String paddingString = "";
        for(int i = 0; i < padding; i++)
        {
            paddingString +=" ";
        }

        System.out.println(paddingString + line);
    }

    //this is used by some of the automatic tests in Mooshak. It is used to print the first 5 levels of a Treap.
    //feel free to use it for your own tests
    public void printTreapBeginning() {
        Object[] heap = getHeapArray();
        int size = size();
        int printedNodes = 0;
        String nodeToPrint;
        int i = 1;
        int nodes;
        String line;

        //only prints the first five levels
        for (int depth = 0; depth < 5; depth++) {
            //number of nodes to print at a particular depth
            nodes = (int) Math.pow(2, depth);
            line = "";
            for (int j = 0; j < nodes && i < heap.length; j++) {
                if (heap[i] != null) {
                    nodeToPrint = heap[i].toString();
                    printedNodes++;
                } else {
                    nodeToPrint = "[null]";
                }
                line += " " + nodeToPrint;
                i++;
            }

            printCentered(line);
            if (i >= heap.length || printedNodes >= size) break;
        }
    }//helper method that uses the treap to build an array with a heap structure


    public void printTreap(Node root, int space, Node where) {

        final int height = 15;

        if (root == null) {
            return;
        }

        space += height;

        printTreap(root.right, space, where);
        System.lineSeparator();

        for (int i = height; i < space; i++) {
            System.out.print(' ');
        }

        if (root.key != where.key)
            System.out.println(root.key + "(" + root.priority + ")");
        else
            System.out.println("\u001B[41m"+ root.key + "(" + root.priority + ")" + "\u001B[0m");

        printTreap(root.left, space, where);
    }

    public static void main(String[] args) {
        Treap<Integer, Integer> treap = new Treap<>();

        treap.put(0, 20);
        treap.put(30, 10);
        treap.put(-1, 0);
        treap.put(-30, 0);
        treap.put(25, 0);

        treap.put(15, 0);
        treap.put(15, 0);



        //treap.printTreap(treap.root, 0, treap.root);
        treap.printTreapBeginning();


//       treap.deleteMax();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(treap.get(30));




        treap.pq.insert(20);
        treap.pq.insert(-1);
        treap.pq.insert(30);
        treap.pq.insert(5);
        treap.pq.insert(-70);

    //    System.out.println(treap.pq);

        for (Integer k: treap.keys()) {
            System.out.println(k);
        }




      /*  treap.delete(700);
        treap.delete(-35);
        treap.delete(-2);
        treap.delete(850);*/

       // treap.printTreapBeginning();

        //treap.printTreap(treap.root, 0, treap.root);

        Treap<Integer, Integer> copy = treap.shallowCopy();

    }

}