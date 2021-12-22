package aed.tables;
import java.util.*;

public class Treap<Key extends Comparable<Key>,Value> {

    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    private int size;
    private Node root;
    protected Random r;

    private int sizeR;

    private int selectR;
    private Key selectKey;


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

           // this.size = 1;
            this.size = size;
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
            Node newNode = new Node(this.key, this.value, this.size, this.priority);
            //newNode.size = this.size;// this.size();

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

        public boolean hasNoNulls() { return this.left != null && this.right != null; }
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

    public int getPr(Key k) {
        return getPrH(this.root, k);
    }
    private Integer getPrH(Node n, Key k) {
        if(n == null) return null;
        int cmp = k.compareTo(n.key);
        if (cmp < 0) return getPrH(n.left,k);
        else if(cmp > 0) return getPrH(n.right,k);
        else return n.priority;
    }

    public Node getNode(Key k) {
        return getNodeH(this.root, k);
    }
    private Node getNodeH(Node n, Key k) {
        if(n == null) return null;
        int cmp = k.compareTo(n.key);
        if (cmp < 0) return getNodeH(n.left,k);
        else if(cmp > 0) return getNodeH(n.right,k);
        else return n;
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

        if (n == null) { /*pqK.insert(k);*/ return new Node(k, v, 1, priority);}

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
            n.priority = priority;
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
        //TODO: better approach
        //deleteMinH(this.root, null);
      //  delete(min());
        if (this.root != null)
            this.root = deleteMinH(this.root, null);
    }

    public Node deleteMinH(Node n, Node father) {
        /*if (n.left != null)
            deleteMinH(n.left, n);
        else {
            n = null;
            if (father != null)  {father.left = null; father.update();}
            else { root = root.right; root.update();}
        }*/

        if (n.left != null)
            n.left = deleteMinH(n.left, n);
        else {
            if (n.right != null) {
               n = n.right;
               n.update();
               return n;
            }
            else {
                //father.left = null
                n = null;
             //   father.update();
                return null;
            }
        }
        n.update();
        return n;
    }


    public Treap<Key, Value> shallowCopy() {
        Treap<Key, Value> newTreap = new Treap<Key, Value>(this.r);

        if (this.root != null)
            newTreap.root = this.root.shallowCopy();

        return newTreap;

    }

    public void delete(Key k) {

//        this.root = deleteH(this.root, k, null, -1);
        this.root = deleteH(k, this.root);

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
                if (current.left.priority > current.right.priority) {
                    current = rotateRight(current);
                    current.right = deleteH(key, current.right);
                }
                else {
                    current = rotateLeft(current);
                    current.left = deleteH(key, current.left);
                }
            }
            else {
                if (current.right == null) {current = current.left;}
                else if (current.left == null) {current = current.right;}
            }
            //else if (current.left != null) { current.left.update(); return current.left; }
            //else if (current.right != null) { current.right.update(); return current.right; }
           // else {  return null; }//is a leaf
        }
        //current.update();
       // pq.removeMin();
        //assert current != null;
        if (current != null) current.update();
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


    @SuppressWarnings("unchecked")
    public Treap[] split(Key k) {
        Treap<Key, Value>[] treaps = (Treap<Key, Value>[]) new Treap[2];

        put(k, null, Integer.MAX_VALUE);

        treaps[0] = new Treap<Key, Value>();

        treaps[1] = new Treap<Key, Value>();

        treaps[0].root = this.root.left;//.shallowCopy();

        treaps[1].root = this.root.right;//.shallowCopy();

        return treaps;
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
       // delete(max());
        if (this.root != null)
            this.root = deleteMaxH(this.root, null);
    }


    public Node deleteMaxH(Node n, Node father) {

        if (n.right != null)
            n.right = deleteMaxH(n.right, n);
        else {
            if (n.left != null) {
                n = n.left;
                n.update();
                return n;
            }
            else {
                //father.left = null
                n = null;
                //   father.update();
                return null;
            }
        }
        n.update();
        return n;
    }

    public int rankH(Node n, Key k) {
        if (n != null) {
            if (k.compareTo(n.key) < 0) {
                return rankH(n.left, k);
            }
            else if (k.compareTo(n.key) == 0) {
                return getSize(n.left);
            }
            else {
                return getSize(n.left)+1+rankH(n.right, k);
            }
        }
        else return 0;
    }

    public int rank(Key k ){

        return rankH(this.root, k);

    }

    public int size(Key min, Key max) {
        sizeR = 0;
        sizeH(this.root, min, max);
        return sizeR;
    }

    private void sizeH(Node n, Key min, Key max) {

        if (n !=null) {
            if (n.key.compareTo(min) >= 0 && n.key.compareTo(max) <= 0) {
                sizeR++;

                sizeH(n.left, min, max);
                sizeH(n.right, min, max);
            }
            else if (n.key.compareTo(max) > 0)
                sizeH(n.left, min, max);
            else if (n.key.compareTo(min) < 0)
                sizeH(n.right, min, max);
        }
    }

    public Key select(int n) {
      /* Treap<Key, Value> copy = new Treap<>();

        copy = shallowCopy();

        for (int i = 0; i < n; i++) {
            copy.deleteMin();
        }
        return copy.min();*/
       /* selectR = 0;
        selectKey = null;

        selectH(this.root, n);
        return selectKey;*/

        if (n >= 0)
            return selectH(this.root, n);
        else return null;
    }

    private Key selectH(Node current, int n/*, int skipped*/) {
        if (current == null)
            return null;

        int sizeL = getSize(current.left);
       // int sizeR = getSize(current.right);

        if (n < sizeL) {
           // skipped = getSize(current.right);
            return selectH(current.left, n);
        }
        else if (n > sizeL) {
            return selectH(current.right, n-1-sizeL);
        }
        else {
            return current.key;
        }
    }


   /* private void selectH(Node current, int n) {

        if (current == null || n < 0 || selectR > n) return;

        selectH(current.left, n);

//        System.out.println(current.key);

        if (selectR == n) {
            selectKey = current.key;
            selectR++;
            return;
        }
        selectR++;



        selectH(current.right, n);
    }*/

    public void KeysH(Node n, Stack<Key> s) {
        if (n == null) return;

        KeysH(n.left, s);
        s.push(n.key);
        KeysH(n.right, s);

    }

    public void KeysH(Node n, Stack<Key> s, Key min, Key max) {
       // if (n == null || !(n.key.compareTo(min) >= 0 && n.key.compareTo(max) <= 0)) return;

        if (n == null) return;

        KeysH(n.left, s, min, max);
        if (n.key.compareTo(min) >= 0 && n.key.compareTo(max) <= 0)
            s.push(n.key);
        KeysH(n.right, s, min, max);

    }


    public Iterable<Key> keys() {
        Stack<Key> s =  new Stack<Key>();
        KeysH(this.root, s);

        return s;
    }

    public Iterable<Key> keys(Key min, Key max) {
         Stack<Key> s =  new Stack<Key>();
        KeysH(this.root, s, min, max);

        return s;

    }

    public void prioritiesH(Node n, Stack<Integer> s) {
        if (n == null) return;

        prioritiesH(n.left, s);
        s.push(n.priority);
        prioritiesH(n.right, s);

    }

    public void valuesH(Node n, Stack<Value> s) {
        if (n == null) return;

        valuesH(n.left, s);
        s.push(n.value);
        valuesH(n.right, s);

    }


    public Iterable<Integer> priorities() {
        Stack<Integer> s =  new Stack<Integer>();
        prioritiesH(this.root, s);
        return s;
    }

    public Iterable<Value> values() {
        Stack<Value> s =  new Stack<Value>();
        valuesH(this.root, s);
        return s;
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
        treap.put(1, 20);

        treap.put(30, 10);
        treap.put(-1, 0);
        treap.put(-30, 0);
        treap.put(25, 0);

        treap.put(15, 0);

        treap.put(70, 1);
        treap.put(-2, 0);
        treap.put(-10, 0);



        //treap.printTreap(treap.root, 0, treap.root);
        treap.printTreapBeginning();

        //treap.deleteMin();
     //   treap.deleteMax();

        System.out.println();
        System.out.println();
        System.out.println();


        System.out.println(treap.select(100));
    }

}