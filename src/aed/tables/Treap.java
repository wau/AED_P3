package aed.tables;

import java.util.Random;

public class Treap<Key extends Comparable<Key>,Value> {
    
    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    private int size;
    private Node root;
    protected Random r;

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
        }

        public Node(Key k, Value v, int customPriority) {
            this.key = k;
            this.value = v;
            this.priority = customPriority;//r.nextInt(1000);

            this.left = null;
            this.right = null;
        }

        int getSize() {
            int r = 0;
            if (this.left != null) r++;
            if (this.right != null) r++;
            return r;
        }

        public String toString()
        {
            return "[k:" + this.key + " v:" + this.value + " p:" + this.priority + " s:" + this.getSize() + "]";
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
        return size;
    }
    ////
    public boolean containsKey(Key k) {
        return containsH(this.root, k);
    }
    private boolean containsH(Node n, Key k) { //contains helper function
        if (n == null) return false;

        int cmp = k.compareTo(n.key);
        if (cmp < 0) return containsH(n.left,k);
        else if(cmp > 0) return containsH(n.right,k);
        else return false;
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

        return newR;
    }



    private Node rotateRight(Node root) {
        Node newR = root.left;
        Node swap = newR.right;
        newR.right = root;
        root.left = swap;

        return newR;
    }


    public void put(Key k, Value v) {
        this.root = putH(this.root, k, v, r.nextInt(1000));
    }

    public void put(Key k, Value v, int priority) {
        this.root = putH(this.root, k, v, priority);
    }

    //precisa ter o return node para conseguir atualizar o Node recursivamente
    public Node putH(Node n, Key k, Value v, int priority) {

        /*printTreap(this.root, 0);
        System.out.println();
        System.out.println();*/

        if (n == null) { this.size++; return new Node(k, v, priority); }

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
        return n;
    }


    public Key min() {
        if (size > 0) {
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


    Treap shallowCopy() {
        public UnrolledLinkedList<T> shallowCopy() {
            UnrolledLinkedList<T> newList = new UnrolledLinkedList<T>(this.blockSize);

            newList.first = this.first.shallowCopy();
            newList.nNodes = this.nNodes;
            newList.blockSize = this.blockSize;


            return newList;
        }

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

    public void deleteMin() {
        deleteMinH(this.root, null);
    }

    public void deleteMinH(Node n, Node father) {
        if (n.left != null)
            deleteMinH(n.left, n);
        else {
            n = null;
            if (father != null) father.left = null;
            else root = root.right;
        }
    }

    public void delete(Key k) {

//        this.root = deleteH(this.root, k, null, -1);

        this.root = deleteRec(k, this.root);

      //  this.root = removeH(k, this.root);
        //tem que atualizar a root sempre.


     //   while ()
    }


    private Node deleteRec(Key key, Node current) {


        if (current == null) return null;

        printTreap(this.root, 0, current);
        System.out.println();
        System.out.println();
        System.out.println();

        int cmpr = key.compareTo(current.key);

        if (cmpr < 0) {
            current.left = deleteRec(key, current.left);
           // return current;
        } else if (cmpr > 0) {
            current.right = deleteRec(key, current.right);
           // return current;
        } else { // If the current node is the node to be deleted
            current.priority = Integer.MIN_VALUE;

            if (current.hasNoNulls()) { // If current has two children, perform rotations to fix heap priorities
                Node newRoot;
                if (current.left.priority < current.right.priority) {
                    newRoot = current.right;
                    rotateLeft(current);
                    newRoot.left = deleteRec(key, current.left);
                } else {
                    newRoot = current.left;
                    rotateRight(current);
                    newRoot.right = deleteRec(key, current.right);
                }
                return newRoot;
            } else if (current.left != null) { // If current has a left child
                return current.left;
            } else if (current.right != null) { // If current has a right child
                return current.right;
            } else { // If current is a leaf
                return null;
            }
        }
        System.out.println("will return");
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

        // Base case
        if (root == null) {
            return;
        }

        // increase distance between levels
        space += height;

        // print the right child first
        printTreap(root.right, space, where);
        System.lineSeparator();

        for (int i = height; i < space; i++) {
            System.out.print(' ');
        }

        if (root.key != where.key)
            System.out.println(root.key + "(" + root.priority + ")");
        else
            System.out.println("\u001B[41m"+ root.key + "(" + root.priority + ")" + "\u001B[0m");

        System.lineSeparator();
        printTreap(root.left, space, where);
    }

    public static void main(String[] args) {
        Treap<Integer, Integer> treap = new Treap<>();

        treap.put(0, 20);
        treap.put(30, 10);
        treap.put(-1, 0);
        treap.put(-30, 0);
        treap.put(15, 0);
        treap.put(15, 0);
        treap.put(25, 0);
       /* treap.put(35, 0);
        treap.put(70, 0);
        treap.put(12, 0);

        treap.put(29, 0);
        treap.put( -1, 0);
        treap.put(-5, 10);*/



      /*  treap.printTreap(treap.root, 0, treap.root);
        System.out.println();
        System.out.println();
        System.out.println();*/

        treap.delete(15);

      //  treap.delete(35);


        treap.printTreap(treap.root, 0, treap.root);

        treap.printTreapBeginning();



        /*System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        treap.root.right = treap.rotateLeft(treap.root.right);

        treap.printTreap(treap.root, 0);*/


      /*  treap.put(-30, 9);
        treap.put(29, 0);
        treap.put(-99, 9);
        treap.put(-100, 0);*/


/*        treap.printTreap(treap.root, 0);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        treap.printTreap(treap.root, 0);*/
    }

}