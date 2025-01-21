// Special AVL Tree that handles duplicates
import java.util.*;

class Vertex<U extends Comparable<U>> {
    public Vertex<U> parent, left, right;
    public U key;
    public int height;
    public int size;
    public int count; // we add a new attribute count for storing frequency

    // Constructor
    public Vertex(U v) { 
        key = v;
        parent = left = right = null;
        height = 0;
        size = 1;
        count = 1; // itself
    }
}

class AVL<U extends Comparable<U>> {
    public Vertex<U> root;
    public U defaultValue;

    public AVL(U defaultValue) {
        this.root = null;
        this.defaultValue = defaultValue;
    }

    public U search(U v) {
        Vertex<U> res = search(root, v);
        return res == null ? this.defaultValue : res.key;
    }

    // Helper method for search
    public Vertex<U> search(Vertex<U> T, U v) {
        if (T == null)
            return null;                // not found
        else if (T.key.compareTo(v) == 0)
            return T;                   // found
        else if (T.key.compareTo(v) < 0)
            return search(T.right, v);  // search to the right
        else
            return search(T.left, v);   // search to the left
    }
    
    public U findMin() {
        Vertex<U> v = findMin(root);
        return v == null ? this.defaultValue : v.key;
    }

    // Helper method for findMin
    public Vertex<U> findMin(Vertex<U> T) {
        // Empty tree
        if (T == null) {
            return null;
        }

        // Non-empty tree
        if (T.left == null)
            return T;                   // this is the min
        else
            return findMin(T.left);     // go to the left
    }

    public U findMax() {
        Vertex<U> v = findMax(root);
        return v == null ? this.defaultValue : v.key;
    }

    // Helper method for findMax
    public Vertex<U> findMax(Vertex<U> T) {
        // Empty tree
        if (T == null) {
            return null;
        }

        // Non-empty tree
        if (T.right == null)
            return T;                   // this is the max
        else
            return findMax(T.right);    // go to the right
    }

    public U successor(U v) {   // assumes v exists in the tree (otherwise, can dummy-insert and redelete later)
        Vertex<U> vPos = search(root, v);
        if (vPos == null)
            return this.defaultValue;
        Vertex<U> succV = successor(vPos);
        return succV == null ? this.defaultValue : succV.key;
    }

    // Helper method for successor
    public Vertex<U> successor(Vertex<U> T) {
        if (T.right != null)                    // this subtree has a right subtree
            return findMin(T.right);            // the successor is the minimum of right subtree
        else {
            Vertex<U> par = T.parent;
            Vertex<U> cur = T;
            // if par(ent) is not root and cur(rent) is its right children
            while ((par != null) && (cur == par.right)) {
                cur = par;                      // continue moving up
                par = cur.parent;
            }
            return par;  // this is the successor of T
        }
    }

    public U predecessor(U v) { // assumes v exists in the tree (otherwise, can dummy-insert and redelete later)
        Vertex<U> vPos = search(root, v);
        if (vPos == null)
            return this.defaultValue;
        Vertex<U> predV = predecessor(vPos);
        return predV == null ? this.defaultValue : predV.key;
    }

    // Helper method for predecessor
    public Vertex<U> predecessor(Vertex<U> T) {
        if (T.left != null)                     // this subtree has a left subtree
            return findMax(T.left);             // the successor is the maximum of left subtree
        else {
            Vertex<U> par = T.parent;
            Vertex<U> cur = T;
            // if par(ent) is not root and cur(rent) is its left children
            while ((par != null) && (cur == par.left)) {
                cur = par;                      // continue moving up
                par = cur.parent;
            }
            return par;  // this is the predecessor of T
        }
    }

    // Inorder traversal
    public void inorder() { 
        inorder(root);
        System.out.println();
    }

    // Helper method for inorder
    public void inorder(Vertex<U> T) {
        if (T == null)
            return;
        inorder(T.left);                    // recursively go to the left
        for (int i = 0; i < T.count; i++)
            System.out.print(" " + T.key.toString());    // visit this node for T.count times
        inorder(T.right);                   // recursively go to the right
    }

    public int height(Vertex<U> T) {
        if (T == null)
            return -1;
        else
            return T.height;
    }

    public int size(Vertex<U> T) {
        if (T == null)
            return 0;
        else
            return T.size;
    }

    public void updateHeight(Vertex<U> T) {
        T.height = 1 + Math.max(height(T.left), height(T.right));
    }

    // Since the AVL can have duplicate values, we store the frequency in the count attribute for each vertex
    public void updateSize(Vertex<U> T) {
        T.size = size(T.left) + size(T.right) + T.count;
    }

    // Balance factor of a Vertex<U> T
    public int bf(Vertex<U> T) {
        if (T == null)
            return 0;
        else
            return height(T.left) - height(T.right);
    }

    public void insert(U v) {
        root = insert(root, v, 1);
    }

    // Helper method for insert
    public Vertex<U> insert(Vertex<U> T, U v, int count) {
        if (T == null) {
            Vertex<U> newV = new Vertex<U>(v);
            newV.count = count;
            return newV;           // insertion point is found
        }

        if (T.key.compareTo(v) < 0) {                    // search to the right
            T.right = insert(T.right, v, count);
            T.right.parent = T;
        }
        else if (T.key.compareTo(v) > 0) {               // search to the left
            T.left = insert(T.left, v, count);
            T.left.parent = T;
        }
        else // T.key.compareTo(v) == 0, v exists!
            T.count += count; // increase the frequency

        updateHeight(T);
        updateSize(T);
        T = rebalance(T);

        return T;                           // return the updated tree
    }  

    public void delete(U v) {
        root = delete(root, v, 1);
    }

    // Helper method for delete
    public Vertex<U> delete(Vertex<U> T, U v, int count) {
        if (T == null)
            return T;                                       // cannot find the item to be deleted

        if (T.key.compareTo(v) < 0)                                      // search to the right
            T.right = delete(T.right, v, count);
        else if (T.key.compareTo(v) > 0)                                 // search to the left
            T.left = delete(T.left, v, count);
        else {                                              // this is the node to be deleted
            if (T.count <= count) {
                if (T.left == null && T.right == null)          // this is a leaf
                    T = null;                                   // simply erase this node for good
                else if (T.left == null && T.right != null) {   // only one child at right
                    T.right.parent = T.parent;
                    T = T.right;                                // bypass T
                }
                else if (T.left != null && T.right == null) {   // only one child at left
                    T.left.parent = T.parent;
                    T = T.left;                                 // bypass T
                }
                else {                                          // has two children, find successor
                    Vertex<U> successorV = successor(search(root, v));
                    T.key = successorV.key;                                         // replace this key with the successor's key
                    T.count = successorV.count;                                     // replace the count too!
                    T.right = delete(T.right, successorV.key, successorV.count);    // delete the old successorV
                }
            } else  // no need to delete key from the tree, decrement frequency
                T.count--;
        }

        // For every node, if it is not deleted for good, do the rebalancing
        if (T != null) {
            updateHeight(T);
            updateSize(T);
            T = rebalance(T);
        }

        return T; // return the updated tree
    }

    public Vertex<U> leftRotate(Vertex<U> T) { // given T.right is not null
        Vertex<U> w = T.right;
        w.parent = T.parent;
        T.parent = w;
        T.right = w.left;
        if (w.left != null)
            w.left.parent = T;
        w.left = T;

        updateHeight(T);
        updateSize(T);
        updateHeight(w);
        updateSize(w);

        return w;
    }

    public Vertex<U> rightRotate(Vertex<U> T) { // given T.left is not null
        Vertex<U> w = T.left;
        w.parent = T.parent;
        T.parent = w;
        T.left = w.right;
        if (w.right != null)
            w.right.parent = T;
        w.right = T;

        updateHeight(T);
        updateSize(T);
        updateHeight(w);
        updateSize(w);

        return w;
    }

    public Vertex<U> rebalance(Vertex<U> T) {
        if (T != null) {
            if (bf(T) == 2) { // T has a left child
                if (bf(T.left) == -1) { // LR case
                    T.left = leftRotate(T.left);
                }

                // Either LL or LR case, do this
                T = rightRotate(T);
            }
            else if (bf(T) == -2) { // T has a right child
                if (bf(T.right) == 1) { // RL case
                    T.right = rightRotate(T.right);
                }

                // Either RL or RR case, do this
                T = leftRotate(T);
            }
        }

        return T;
    }

    public int rank(U k) {
        return rank(root, k);
    }

    // Helper method for rank
    // Rank of k in a subtree of root T
    public int rank(Vertex<U> T, U k) { // O(log N)
        if (T == null)
            return 0;
        else if (T.key.compareTo(k) == 0)        // it's the root
            return size(T.left) + 1;
        else if (T.key.compareTo(k) > 0)         // k is somewhere in the left of the subtree
            return rank(T.left, k);
        else                                     // k is somewhere in the right of the subtree
            return rank(T.right, k) + size(T.left) + T.count;
    }

    // Selects the node with rank k
    public Vertex<U> select(int k) {
        return select(root, k);
    }

    public int countRanged(U a, U b) {
        return countRanged(root, a, b);
    }

    public int countRanged(Vertex<U> T, U a, U b) {
        if (T == null)
            return 0;
        if (T.key.compareTo(a) == 0 && T.key.compareTo(b) == 0)
            return 1;
        if (T.key.compareTo(a) >= 0 && T.key.compareTo(b) <= 0)
            return 1 + countRanged(T.left, a, b) + countRanged(T.right, a, b);
        if (T.key.compareTo(a) < 0)
            return countRanged(T.right, a, b);
        return countRanged(T.left, a, b);
    }

    // Helper method for select
    public Vertex<U> select(Vertex<U> T, int k) { // O(log N)
        if ((T.left == null && 1 <= k && k <= T.count) || (T.left != null && T.left.size + 1 <= k && k <= T.left.size + T.count))
            return T;
        else if ((T.left == null && k > T.count) || (T.left != null && k > T.left.size + T.count)) // T is somewhere in the right of the subtree
            return T.left == null ? select(T.right, k-T.count) : select(T.right, k-T.left.size-T.count);
        else // T is somewhere in the left of the subtree
            return select(T.left, k);
    }

    // Lowest common ancestor. Assuming a and b is contained in the tree
    public U LCA(U a, U b) {
        return LCA(root, a, b);
    }

    // Helper method for LCA, no parent pointer used
    public U LCA(Vertex<U> T, U a, U b) {    // O(log N)
        U maxVal = a.compareTo(b) > 0 ? a : b, minVal = a.compareTo(b) < 0 ? a : b;
        if (T.key.compareTo(maxVal) > 0)        // current node is larger, go left
            return LCA(T.left, a, b);
        else if (T.key.compareTo(minVal) < 0)   // current node is too low, go right
            return LCA(T.right, a, b);
        else                                    // this means minVal <= T.key <= maxVal
            return T.key;
    }
}

// https://visualgo.net/en/bst?mode=AVL&create=41,20,65,11,32,50,91,29,37,72,99
public class AVLDemo2 {
    public static void main (String[] args) {
        AVL<Integer> avl = new AVL<Integer>(-1);

        /*
        We want the tree to be initially
                           41
                20                  65          ----> TREE 1
            11      29          50      91
                      32              72   99
        
        Then we insert 37
                           41
                20                  65
            11      29          50      91
                      32              72   99
                        37

        Rebalancing is needed, the tree is now like this
                          41
                20                  65          ----> TREE 2
            11      32          50      91
                  29   37             72  99
        */

        // Create tree 1 by inserting these vertices
        int[] vertices = {41,20,65,11,32,50,91,29,72,99};
        for (int v : vertices) {
            avl.insert(v);
        }

        // Check tree 1
        System.out.println("TEST TREE 1");
        System.out.println(avl.root.key);           // 41
        avl.inorder(); //  11 20 29 32 41 50 65 72 91 99
        System.out.println(avl.findMin());          // 11
        System.out.println(avl.findMax());          // 99
        System.out.println(avl.successor(11));      // 20
        System.out.println(avl.successor(32));      // 41
        System.out.println(avl.successor(99));      // -1
        System.out.println(avl.predecessor(11));    // -1
        System.out.println(avl.predecessor(37));    // -1
        System.out.println(avl.predecessor(41));    // 32
        System.out.println(avl.predecessor(99));    // 91
        System.out.println(avl.LCA(11,37));         // 20
        System.out.println(avl.LCA(29,37));         // 32
        System.out.println(avl.LCA(99,11));         // 41
        System.out.println(avl.LCA(99,50));         // 65
        System.out.println(avl.root.size);          // 10
        System.out.println(avl.root.height);        // 3
        System.out.println(avl.bf(avl.root));       // 0
        System.out.println(avl.rank(32));           // 4
        System.out.println(avl.rank(91));           // 9
        System.out.println(avl.select(6).key);      // 50
        System.out.println(avl.select(4).key);      // 32
        System.out.println(avl.select(10).key);     // 99

        System.out.println();
        avl.insert(37);

        // Check tree 2
        System.out.println("TEST TREE 2");
        System.out.println(avl.root.key);                       // 41
        avl.inorder(); //  11 20 29 32 37 41 50 65 72 91 99
        System.out.println(avl.findMin());                      // 11
        System.out.println(avl.findMax());                      // 99
        System.out.println(avl.successor(11));                  // 20
        System.out.println(avl.successor(32));                  // 37
        System.out.println(avl.successor(99));                  // -1
        System.out.println(avl.predecessor(11));                // -1
        System.out.println(avl.predecessor(32));                // 29
        System.out.println(avl.predecessor(41));                // 37
        System.out.println(avl.predecessor(99));                // 91
        System.out.println(avl.LCA(11,32));                     // 20
        System.out.println(avl.LCA(29,37));                     // 32
        System.out.println(avl.LCA(99,11));                     // 41
        System.out.println(avl.LCA(99,50));                     // 65
        System.out.println(avl.root.size);                      // 11
        System.out.println(avl.root.height);                    // 3
        System.out.println(avl.bf(avl.root));                   // 0
        System.out.println(avl.rank(32));                       // 4
        System.out.println(avl.rank(91));                       // 10
        System.out.println(avl.select(6).key);                  // 41
        System.out.println(avl.select(5).key);                  // 37
        System.out.println(avl.select(10).key);                 // 91
        System.out.println(avl.root.left.right.left.key);       // 29
        System.out.println(avl.root.left.right.right.key);      // 37

        System.out.println();
        avl.delete(91);
        /*
        TREE 3
                          41
                20                  65
            11      32          50      99
                  29   37             72
        */
        System.out.println("TEST TREE 3");
        System.out.println(avl.root.key);                       // 41
        avl.inorder();  //  11 20 29 32 37 41 50 65 72 99
        System.out.println(avl.root.right.right.left.key);      // 72
        System.out.println(avl.rank(72));                       // 9
        System.out.println(avl.select(8).right.key);            // 99
        System.out.println(avl.root.size);                      // 10
        System.out.println(avl.LCA(72,99));                     // 99
        System.out.println(avl.LCA(29,37));                     // 32
        System.out.println(avl.LCA(50,72));                     // 65
        System.out.println(avl.successor(50));                  // 65
        System.out.println(avl.predecessor(72));                // 65
        System.out.println(avl.select(9).parent.key);           // 99

        System.out.println();
        avl.delete(99);
        /*
        TREE 4
                          41
                20                  65
            11      32          50      72
                  29   37
        */
        System.out.println("TEST TREE 4");
        System.out.println(avl.root.key);                                       // 41
        avl.inorder();  //  11 20 29 32 37 41 50 65 72
        System.out.println(avl.root.right.right.key);                           // 72
        System.out.println(avl.rank(72));                                       // 9
        System.out.println(avl.select(8).right.key);                            // 72
        System.out.println(avl.select(7).parent.parent.left.right.right.key);   // 37
        System.out.println(avl.root.size);                                      // 9
        System.out.println(avl.LCA(72,41));                                     // 41
        System.out.println(avl.LCA(29,37));                                     // 32
        System.out.println(avl.LCA(50,72));                                     // 65
        System.out.println(avl.successor(50));                                  // 65
        System.out.println(avl.predecessor(41));                                // 37
        System.out.println(avl.predecessor(72) == avl.root.right.key);          // true
        System.out.println(avl.bf(avl.root));                                   // 1
        System.out.println(avl.bf(avl.root.left));                              // -1
        System.out.println(avl.bf(avl.root.right));                             // 0
        System.out.println(avl.bf(avl.root.right.left));                        // 0

        System.out.println();
        avl.delete(50);
        /*
        TREE 5
                          41
                20                  65
            11      32                  72
                  29   37
        */
        System.out.println("TEST TREE 5");
        System.out.println(avl.root.key);                                   // 41
        avl.inorder();  //  11 20 29 32 37 41 65 72
        System.out.println(avl.root.right.right.key);                       // 72
        System.out.println(avl.rank(65));                                   // 7
        System.out.println(avl.rank(72));                                   // 8
        System.out.println(avl.select(8).parent.key);                       // 65
        System.out.println(avl.select(7).parent.left.right.right.key);      // 37
        System.out.println(avl.root.size);                                  // 8
        System.out.println(avl.LCA(72,41));                                 // 41
        System.out.println(avl.LCA(29,37));                                 // 32
        System.out.println(avl.successor(50));                              // -1
        System.out.println(avl.predecessor(41));                            // 37
        System.out.println(avl.predecessor(72) == avl.root.right.key);      // true
        System.out.println(avl.bf(avl.root.right));                         // -1
        System.out.println(avl.bf(avl.root.right.right));                   // 0

        System.out.println();
        avl.delete(41);
        /*
        TREE 6
                          32
                20                  65
            11      29          37      72
        */
        System.out.println("TEST TREE 6");
        System.out.println(avl.root.key);                                                       // 32
        avl.inorder(); //  11 20 29 32 37 65 72
        System.out.println(avl.root.left.key);                                                  // 20
        System.out.println(avl.root.right.key);                                                 // 65
        System.out.println(avl.root.left.left.key);                                             // 11
        System.out.println(avl.root.left.right.key);                                            // 29
        System.out.println(avl.root.right.left.key);                                            // 37
        System.out.println(avl.root.right.right.key);                                           // 72
        System.out.println(avl.bf(avl.root));                                                   // 0
        System.out.println(avl.rank(37));                                                       // 5
        System.out.println(avl.select(6).key);                                                  // 65
        System.out.println(avl.select(6).parent.left.right.key);                                // 29
        System.out.println(avl.root.size);                                                      // 7
        System.out.println(avl.root.left.size*avl.root.right.size);                             // 9
        System.out.println(avl.LCA(72,32));                                                     // 32
        System.out.println(avl.LCA(29,37));                                                     // 32
        System.out.println(avl.LCA(11,29));                                                     // 20
        System.out.println(avl.successor(50));                                                  // -1
        System.out.println(avl.predecessor(41));                                                // -1
        System.out.println(avl.successor(32));                                                  // 37
        System.out.println(avl.predecessor(72));                                                // 65
        System.out.println(avl.predecessor(avl.predecessor(72)) == avl.root.right.left.key);    // true

        // Final tree, deleting 7 from https://visualgo.net/en/bst?mode=AVL&create=8,6,16,3,7,13,19,2,11,15,18,10
        // Checking for multiple rebalancings
        /*
        From this (TREE 7)
                                                8
                        6                                           16
                3               (7)                          13              19
            2                                           11      15      18
                                                    10

        to this (TREE 8)
                                    13
                        8                       16
                3               11       15             19
            2       6       10                      18
        */
        System.out.println();
        AVL<Integer> avl2 = new AVL<Integer>(-1);
        int[] vertices2 = {8,6,16,3,7,13,19,2,11,15,18,10};
        for (int v : vertices2) {
            avl2.insert(v);
        }

        System.out.println("TEST TREE 7");
        avl2.inorder(); //  2 3 6 7 8 10 11 13 15 16 18 19
        System.out.println(avl2.root.key);                                                                  // 8
        System.out.println(avl2.root.left.key);                                                             // 6
        System.out.println(avl2.root.right.key);                                                            // 16
        System.out.println(avl2.root.left.left.key);                                                        // 3
        System.out.println(avl2.root.left.right.key);                                                       // 7
        System.out.println(avl2.root.right.left.key);                                                       // 13
        System.out.println(avl2.root.right.right.key);                                                      // 19
        System.out.println(avl2.root.left.left.left.key);                                                   // 2
        System.out.println(avl2.root.right.left.left.key);                                                  // 11
        System.out.println(avl2.root.right.left.right.key);                                                 // 15
        System.out.println(avl2.root.right.right.left.key);                                                 // 18
        System.out.println(avl2.root.right.left.left.left.key == avl2.predecessor(avl2.select(7).key));     // true

        System.out.println();
        avl2.delete(7);

        System.out.println("TEST TREE 8");
        avl2.inorder(); //  2 3 6 8 10 11 13 15 16 18 19
        System.out.println(avl2.root.key);                                                                  // 13
        System.out.println(avl2.root.left.key);                                                             // 8
        System.out.println(avl2.root.right.key);                                                            // 16
        System.out.println(avl2.root.left.left.key);                                                        // 3
        System.out.println(avl2.root.left.right.key);                                                       // 11
        System.out.println(avl2.root.right.left.key);                                                       // 15
        System.out.println(avl2.root.right.right.key);                                                      // 19
        System.out.println(avl2.root.left.left.left.key);                                                   // 2
        System.out.println(avl2.root.left.left.right.key);                                                  // 6
        System.out.println(avl2.root.left.right.left.key);                                                  // 10
        System.out.println(avl2.root.right.right.left.key);                                                 // 18
        System.out.println(avl2.predecessor(avl2.root.right.left.key)*avl2.successor(avl2.root.key));       // 195
        avl2.insert(18);
        avl2.insert(18);
        avl2.insert(18);
        avl2.delete(16);
        avl2.inorder(); //  2 3 6 8 10 11 13 15 18 18 18 18 19
        System.out.println(avl2.predecessor(18));                                                           // 15
        System.out.println(avl2.successor(18));                                                             // 19

        System.out.println();
        AVL<Integer> avl3 = new AVL<Integer>(-1);
        int[] vertices3 = {9,8,7,8,7,6,6,6,5,4,1,1,2,3,3,3,3};
        for (int v : vertices3) {
            avl3.insert(v);
        }

        System.out.println("TEST TREE 9");
        avl3.inorder(); //  1 1 2 3 3 3 3 4 5 6 6 6 7 7 8 8 9
        System.out.println(avl3.select(9).key+avl3.select(10).key+avl3.select(12).key+avl3.select(14).key==24); // true
    }
}