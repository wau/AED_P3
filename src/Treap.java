import java.util.Random;

public class Treap<Key extends Comparable<Key>,Value> {

    private int size;
    private Node root;
    protected Random r;

    private class Node {
        private Key key;
        private Value value;
        private Node left;
        private Node right;
        private int size;

        public Node(Key k, Value v, int size)
        {
            this.key = k;
            this.value = v;
            this.size = size;
        }
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

    public void put(Key k, , Value v) {

    }
    ///
    public static void main(String[] args) {
        Treap<Integer, Integer> treap = new Treap<>();

        //treap.containsKey(20);
    }

}