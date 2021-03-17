class Vertex {
    public Vertex parent, left, right;
    public int key;
    public int height;
    public int size;

    // Constructor
    public Vertex(int v) { 
        key = v;
        parent = left = right = null;
        height = 0;
        size = 1;
    }
}

// We assume that the value of the vertices in the tree is nonnegative
class AVL {
    public Vertex root;

    public AVL() {
        root = null;
    }

    public int search(int v) {
        Vertex res = search(root, v);
        return res == null ? -1 : res.key;
    }

    // Helper method for search
    public Vertex search(Vertex T, int v) {
        if (T == null)
            return null;                // not found
        else if (T.key == v)
            return T;                   // found
        else if (T.key < v)
            return search(T.right, v);  // search to the right
        else
            return search(T.left, v);   // search to the left
    }
    
    public int findMin() {
        return findMin(root);
    }

    // Helper method for findMin
    public int findMin(Vertex T) {
        // Empty tree
        if (T == null) {
            return -1;
        }

        // Non-empty tree
        if (T.left == null)
            return T.key;               // this is the min
        else
            return findMin(T.left);     // go to the left
    }

    public int findMax() {
        return findMax(root);
    }

    // Helper method for findMax
    public int findMax(Vertex T) {
        // Empty tree
        if (T == null) {
            return -1;
        }

        // Non-empty tree
        if (T.right == null)
            return T.key;               // this is the max
        else
            return findMax(T.right);    // go to the right
    }

    public int successor(int v) {
        Vertex vPos = search(root, v);
        return vPos == null ? -1 : successor(vPos);
    }

    // Helper method for successor
    public int successor(Vertex T) {
        if (T.right != null)                    // this subtree has a right subtree
            return findMin(T.right);            // the successor is the minimum of right subtree
        else {
            Vertex par = T.parent;
            Vertex cur = T;
            // if par(ent) is not root and cur(rent) is its right children
            while ((par != null) && (cur == par.right)) {
                cur = par;                      // continue moving up
                par = cur.parent;
            }
            return par == null ? -1 : par.key;  // this is the successor of T
        }
    }

    public int predecessor(int v) {
        Vertex vPos = search(root, v);
        return vPos == null ? -1 : predecessor(vPos);
    }

    // Helper method for predecessor
    public int predecessor(Vertex T) {
        if (T.left != null)                     // this subtree has a left subtree
            return findMax(T.left);             // the successor is the maximum of left subtree
        else {
            Vertex par = T.parent;
            Vertex cur = T;
            // if par(ent) is not root and cur(rent) is its left children
            while ((par != null) && (cur == par.left)) {
                cur = par;                      // continue moving up
                par = cur.parent;
            }
            return par == null ? -1 : par.key;  // this is the predecessor of T
        }
    }

    public void inorder() { 
        inorder(root);
        System.out.println();
    }

    // Helper method for inorder
    public void inorder(Vertex T) {
        if (T == null)
            return;
        inorder(T.left);                    // recursively go to the left
        System.out.printf(" %d", T.key);    // visit this node
        inorder(T.right);                   // recursively go to the right
    }

    // Added from Lecture 11: AVL Tree
    public void updateHeight(Vertex T) {
        if (T.left != null && T.right != null)
            T.height = Math.max(T.left.height,T.right.height) + 1;
        else if (T.left != null)
            T.height = T.left.height + 1;
        else if (T.right != null)
            T.height = T.right.height + 1;
        else
            T.height = 0;
    }

    // Added from Lecture 11: AVL Tree
    public void updateSize(Vertex T) {
        if (T.left != null && T.right != null)
            T.size = T.left.size + T.right.size + 1;
        else if (T.left != null)
            T.size = T.left.size + 1;
        else if (T.right != null)
            T.size = T.right.size + 1;
        else
            T.size = 1;
    }

    // Added from Lecture 11: AVL Tree
    // Balance factor of a vertex T
    public int bf(Vertex T) {
        if (T.left != null && T.right != null)
            return T.left.height - T.right.height;
        else if (T.left != null)
            return T.left.height + 1;
        else if (T.right != null)
            return -1 - T.right.height;
        else
            return 0;
    }

    public void insert(int v) {
        root = insert(root, v);
    }

    // Helper method for insert
    public Vertex insert(Vertex T, int v) {
        if (T == null)
            return new Vertex(v);           // insertion point is found

        if (T.key < v) {                    // search to the right
            T.right = insert(T.right, v);
            T.right.parent = T;
        }
        else {                              // search to the left
            T.left = insert(T.left, v);
            T.left.parent = T;
        }

        updateHeight(T);
        updateSize(T);
        T = rebalance(T);

        return T;                           // return the updated tree
    }  

    public void delete(int v) {
        root = delete(root, v);
    }

    // Helper method for delete
    public Vertex delete(Vertex T, int v) {
        if (T == null)
            return T;                                       // cannot find the item to be deleted

        if (T.key < v)                                      // search to the right
            T.right = delete(T.right, v);
        else if (T.key > v)                                 // search to the left
            T.left = delete(T.left, v);
        else {                                              // this is the node to be deleted
            if (T.left == null && T.right == null)          // this is a leaf
                T = null;                                   // simply erase this node
            else if (T.left == null && T.right != null) {   // only one child at right
                T.right.parent = T.parent;
                T = T.right;                                // bypass T
            }
            else if (T.left != null && T.right == null) {   // only one child at left
                T.left.parent = T.parent;
                T = T.left;                                 // bypass T
            }
            else {                                          // has two children, find successor
                int successorV = successor(v);
                T.key = successorV;                         // replace this key with the successor's key
                T.right = delete(T.right, successorV);      // delete the old successorV
            }
        }

        if (T != null) {
            updateHeight(T);
            updateSize(T);
            T = rebalance(T);
        }

        return T;                                           // return the updated tree
    }

    // Adapted from Lecture 11: AVL Tree
    public Vertex leftRotate(Vertex T) { // given T.right is not null
        Vertex w = T.right;
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

    // Adapted from Lecture 11: AVL Tree
    public Vertex rightRotate(Vertex T) { // given T.left is not null
        Vertex w = T.left;
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

    // Adapted from Lecture 11: AVL Tree
    public Vertex rebalance(Vertex T) {
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

    public int rank(int k) {
        return rank(root, k);
    }

    // Helper method for rank
    // Rank of k in a subtree of root T
    public int rank(Vertex T, int k) { // O(log N)
        if (T.key == k)             // it's the root
            if (T.left != null)
                return T.left.size + 1;
            else
                return 1;
        else if (T.key > k)         // k is somewhere in the left of the subtree
            return rank(T.left, k);
        else                        // k is somewhere in the right of the subtree
            if (T.left != null)
                return rank(T.right, k) + T.left.size + 1;
            else
                return rank(T.right, k) + 1;
    }

    // Selects the node with rank k
    public Vertex select(int k) {
        return select(root, k);
    }

    // Helper method for select
    public Vertex select(Vertex T, int k) { // O(log N)
        if ((T.left == null && k == 1) || (T.left != null && k == T.left.size + 1))
            return T;
        else if ((T.left == null && k > 1) || (T.left != null && k > T.left.size + 1)) // T is somewhere in the right of the subtree
            return T.left == null ? select(T.right, k-1) : select(T.right, k-T.left.size-1);
        else // T is somewhere in the left of the subtree
            return select(T.left, k);
    }

    // Lowest common ancestor. Assuming a and b is contained in the tree
    public int LCA(int a, int b) {
        return LCA(root, a, b);
    }

    // Helper method for LCA, no parent pointer used
    public int LCA(Vertex T, int a, int b) {    // O(log N)
        int maxVal = Math.max(a,b), minVal = Math.min(a,b);
        if (T.key > maxVal)                     // current node is larger, go left
            return LCA(T.left, a, b);
        else if (T.key < minVal)                // current node is too low, go right
            return LCA(T.right, a, b);
        else                                    // this means minVal <= T.key <= maxVal
            return T.key;
    }
}

// https://visualgo.net/en/bst?mode=AVL&create=41,20,65,11,32,50,91,29,37,72,99
public class AVLDemo {
    public static void main (String[] args) {
        AVL avl = new AVL();

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
        System.out.println(avl.LCA(29,37));         // 29
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
        AVL avl2 = new AVL();
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
    }
}