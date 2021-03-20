import java.util.*;

class AdjacencyMatrix {
    public int[][] matrix;
    public boolean directed;
    public int numVertices;
    public boolean weighted;

    public AdjacencyMatrix (int V, boolean dir) {
        directed = dir; // to store whether it's a directed or undirected graph
        numVertices = V;
        matrix = new int[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                matrix[i][j] = 0;
            }
        }
        weighted = true;
    }

    public AdjacencyMatrix (AdjacencyList al) { // create AM from AL
        directed = al.directed;
        numVertices = al.numVertices;
        matrix = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                matrix[i][j] = 0;
            }
            for (int j = 0; j < al.list.get(i).size(); j++) {
                int k = al.list.get(i).get(j).first;
                int w = al.list.get(i).get(j).second;
                matrix[i][k] = w;
            }
        }
        weighted = true; // If the AL is not weighted, please manually change weighted to false
    }

    public void connect (int a, int b) { // strictly for unweighted graph
        weighted = false;
        connect(a,b,1);
    }

    public void connect (int a, int b, int w) { // strictly for weighted graph
        matrix[a][b] = w;
        if (!directed)
            matrix[b][a] = w;
    }

    public void printMatrix () {
        System.out.println("ADJACENCY MATRIX");
        for (int i = 0; i < numVertices; i++)
            System.out.println(Arrays.toString(matrix[i]));
        System.out.println();
    }

    // There is an O(1) way for this but since it's less frequently queried, I'll make the O(V^2) version
    public void transpose () { // for D/W or D/U graph
        if (directed) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = i+1; j < numVertices; j++) {
                    int temp = matrix[i][j];
                    matrix[i][j] = matrix[j][i];
                    matrix[j][i] = temp;
                }
            }
        }
    }

    // There is an O(1) way for this but since it's less frequently queried, I'll make the O(V^2) version
    public void complement () { // only for unweighted graph (D/U or U/U)
        if (!weighted) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (i != j) {
                        matrix[i][j] = 1-matrix[i][j]; // 0 to 1, 1 to 0
                    }
                }
            }
        }
    }

    public void addVertex () {
        numVertices++;
        int[][] newMatrix = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i != numVertices-1 && j != numVertices-1)
                    newMatrix[i][j] = matrix[i][j];
                else
                    newMatrix[i][j] = 0;
            }
        }
        matrix = newMatrix;
    }

    public void removeEdge (int i, int j) {
        matrix[i][j] = 0;
        if (!directed)
            matrix[j][i] = 0;
    }

    public void clearVertex (int V) {
        for (int i = 0; i < numVertices; i++) {
            matrix[V][i] = 0;
            matrix[i][V] = 0;
        }
    }

    public void deleteVertex (int V) {
        numVertices--;
        int[][] newMatrix = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices+1; i++) {
            for (int j = 0; j < numVertices+1; j++) {
                if (i < V) {
                    if (j < V)
                        newMatrix[i][j] = matrix[i][j];
                    else if (j > V)
                        newMatrix[i][j-1] = matrix[i][j];
                } else if (i > V) {
                    if (j < V)
                        newMatrix[i-1][j] = matrix[i][j];
                    else if (j > V)
                        newMatrix[i-1][j-1] = matrix[i][j];
                }
            }
        }
        matrix = newMatrix;
    }
}

class AdjacencyList {
    public List<List<Pair>> list;
    public int numVertices;
    public boolean directed;
    public int[] visited; // for BFS/DFS
    public int[] parent; // for BFS/DFS
    public int[] indeg; // for toposort

    public AdjacencyList (int V, boolean dir) {
        directed = dir;
        numVertices = V;
        list = new ArrayList<List<Pair>>();
        for (int i = 0; i < V; i++) {
            list.add(new ArrayList<Pair>());
        }
    }

    public AdjacencyList (AdjacencyMatrix am) { // Convert Adjacency Matrix to Adjacency List
        numVertices = am.numVertices;
        directed = am.directed;
        list = new ArrayList<List<Pair>>();
        for (int i = 0; i < numVertices; i++) {
            list.add(new ArrayList<Pair>());
            for (int j = 0; j < numVertices; j++) {
                if (am.matrix[i][j] != 0)
                    list.get(i).add(new Pair(j,am.matrix[i][j]));
            }
        }
    }

    public int outDegree (int V) { // the best way the query outDegree is using Adjacency List!
        return list.get(V).size();
    }

    public void connect (int a, int b) { // unweighted graph
        connect(a,b,1);
    }

    public void connect (int a, int b, int w) { // weighted graph
        list.get(a).add(new Pair(b,w));
        Collections.sort(list.get(a)); // O(n) just like insertion, not O(n log n)
        if (!directed) {
            list.get(b).add(new Pair(a,w));
            Collections.sort(list.get(b)); // O(n) just like insertion, not O(n log n)
        }
    }

    public void printList () {
        System.out.println("ADJACENCY LIST");
        for (int i = 0; i < numVertices; i++) {
            System.out.println(i+": "+list.get(i));
        }
        System.out.println();
    }

    public void addVertex () {
        numVertices++;
        list.add(new ArrayList<Pair>());
    }

    public List<Integer> BFS (int s, boolean showPath) { // BFS from a single source
        visited = new int[numVertices];
        parent = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = 0; // Initialize to 0
            parent[i] = -1; // Initialize to -1
        }

        List<Integer> bfs = new ArrayList<Integer>();
        Queue<Integer> q = new LinkedList<Integer>();
        q.offer(s);
        visited[s] = 1;

        while (!q.isEmpty()) {
            Integer u = q.poll();
            for (int i = 0; i < list.get(u).size(); i++) {
                if (visited[list.get(u).get(i).first] == 0) {
                    visited[list.get(u).get(i).first] = 1;
                    parent[list.get(u).get(i).first] = u;
                    q.offer(list.get(u).get(i).first);
                }
            }
            bfs.add(u);
        }

        // Output path reconstruction, iterative version
        // Recursive version is similar, check while t != -1
        if (showPath) {
            int t = bfs.get(bfs.size()-1); // end of BFS
            System.out.print("Path: ");
            Stack<Integer> st = new Stack<Integer>(); // to reverse path
            while (t != s) {
                st.push(t);
                t = parent[t];
            }
            System.out.print(s+"-");
            while (!st.isEmpty()) {
                System.out.print(st.pop()+"-");
            }
            System.out.println("end");
        }

        return bfs;
    }

    public List<Integer> DFS (int s, boolean showPath) { // DFS from a single source
        visited = new int[numVertices];
        parent = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = 0; // Initialize to 0
            parent[i] = -1; // Initialize to -1
        }

        List<Integer> dfs = DFSRecursive(s);

        // Output path reconstruction, iterative version
        // Recursive version is similar, check while t != -1
        if (showPath) {
            int t = dfs.get(dfs.size()-1); // end of DFS
            System.out.print("Path: ");
            Stack<Integer> st = new Stack<Integer>(); // to reverse path
            while (t != s) {
                st.push(t);
                t = parent[t];
            }
            System.out.print(s+"-");
            while (!st.isEmpty()) {
                System.out.print(st.pop()+"-");
            }
            System.out.println("end");
        }

        return dfs;
    }

    public List<Integer> DFSRecursive (int u) { // helper method
        visited[u] = 1;
        List<Integer> dfs = new ArrayList<Integer>();
        dfs.add(u);
        for (int i = 0; i < list.get(u).size(); i++) {
            if (visited[list.get(u).get(i).first] == 0) {
                parent[list.get(u).get(i).first] = u;
                List<Integer> dfsrec = DFSRecursive(list.get(u).get(i).first);
                for (int j : dfsrec) {
                    dfs.add(j);
                }
            }
        }

        return dfs;
    }

    public boolean reachable (int u, int v) { // used BFS, can change to DFS
        List<Integer> bfs = BFS(u,false);
        return visited[v] == 1;
    }

    public int shortestPathLength (int u, int v) {
        boolean r = reachable(u,v);
        if (r) {
            int ans = 0, t = v;
            while (t != u) {
                ans++;
                t = parent[t];
            }
            return ans;
        } else
            return 0;
    }

    public int countCC () { // connected components
        int ans = 0;

        visited = new int[numVertices];
        parent = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            visited[i] = 0; // Initialize to 0
        }

        for (int i = 0; i < numVertices; i++) {
            if (visited[i] == 0) {
                ans++;
                DFSRecursive(i);
            }
        }

        return ans;
    }

    public List<Integer> toposortBFS () { // Kahn's Algorithm
        // Initialization
        indeg = new int[numVertices];
        parent = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            indeg[i] = 0;
            parent[i] = -1;
        }
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                indeg[list.get(i).get(j).first]++;
            }
        }
        Queue<Integer> q = new LinkedList<Integer>();
        for (int i = 0; i < numVertices; i++) {
            if (indeg[i] == 0) {
                q.offer(i);
            }
        }
        List<Integer> toposort = new ArrayList<Integer>();

        while (!q.isEmpty()) {
            int u = q.poll();
            toposort.add(u);
            for (int i = 0; i < list.get(u).size(); i++) {
                indeg[list.get(u).get(i).first]--;
                if (indeg[list.get(u).get(i).first] == 0) {
                    parent[list.get(u).get(i).first] = u;
                    q.offer(list.get(u).get(i).first);
                }
            }
        }

        return toposort;
    }

    public List<Integer> toposortDFS () {
        // Initialization
        visited = new int[numVertices];
        parent = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = 0; // Initialize to 0
            parent[i] = -1; // Initialize to -1
        }

        Stack<Integer> semisort = new Stack<Integer>();
        List<Integer> toposort = new ArrayList<Integer>();

        for (int i = 0; i < numVertices; i++) {
            if (visited[i] == 0)
                semisort = DFSRecursive(semisort, i);
        }

        while (!semisort.isEmpty())
            toposort.add(semisort.pop());

        return toposort;
    }

    public Stack<Integer> DFSRecursive (Stack<Integer> toposort, int u) { // helper method, overloading DFSRecursive
        visited[u] = 1;
        for (int i = 0; i < list.get(u).size(); i++) {
            if (visited[list.get(u).get(i).first] == 0) {
                parent[list.get(u).get(i).first] = u;
                DFSRecursive(toposort, list.get(u).get(i).first);
            }
        }
        toposort.push(u);
        return toposort;
    }

    public int countSCC () { // Kosaraju's Algorithm
        // DFS topological sort of G
        List<Integer> toposort = toposortDFS();

        // Create G'
        AdjacencyMatrix transposedAM = new AdjacencyMatrix(this);
        transposedAM.transpose(); // this is now G'
        AdjacencyList transposedAL = new AdjacencyList(transposedAM); // adjacency list of G'

        int SCC = 0;
        transposedAL.visited = new int[numVertices];
        transposedAL.parent = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            transposedAL.visited[i] = 0;
            transposedAL.parent[i] = -1;
        }

        // In lecture, process K from last to first, which the same as toposort from first to last!
        for (int i = 0; i < toposort.size(); i++) {
            if (transposedAL.visited[i] == 0) {
                SCC++;
                transposedAL.DFSRecursive(i);
            }
        }

        return SCC;
    }
}

class EdgeList {
    public List<Triple> list;
    public boolean directed;

    public EdgeList (boolean dir) {
        directed = dir;
        list = new ArrayList<Triple>();
    }

    public EdgeList (AdjacencyMatrix am) { // O(V^2), converting AM to EL
        directed = am.directed;
        int V = am.numVertices;
        list = new ArrayList<Triple>(); // manual connect to avoid sorting since it will be already sorted
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (am.matrix[i][j] != 0) {
                    if (directed)
                        list.add(new Triple(i,j,am.matrix[i][j]));
                    else
                        list.add(new Triple(Math.min(i,j),Math.max(i,j),am.matrix[i][j]));
                }
            }
        }
    }

    public void connect (int a, int b) { // unweighted graph
        connect(a,b,1);
    }

    public void connect (int a, int b, int w) { // weighted graph
        if (directed)
            list.add(new Triple(a,b,w));
        else
            list.add(new Triple(Math.min(a,b),Math.max(a,b),w));
        Collections.sort(list);
    }

    public void printList () {
        System.out.println("EDGE LIST");
        System.out.println(list);
        System.out.println();
    }
}

class Pair implements Comparable<Pair> {
    // This is how we access the elements of the pair, just like in C++
    public int first;
    public int second;

    public Pair (int v, int w) {
        first = v;
        second = w;
    }

    public String toString () {
        return "<"+first+","+second+">";
    }

    @Override
    public int compareTo (Pair other) {
        if (this.first != other.first)
            return this.first - other.first;
        else
            return this.second - other.second;
    }
}

class Triple implements Comparable<Triple> {
    // Similarly for Triple
    public int first;
    public int second;
    public int third;

    public Triple (int a, int b, int w) {
        first = a;
        second = b;
        third = w;
    }

    public String toString () {
        return "<"+first+","+second+","+third+">";
    }

    @Override
    public int compareTo (Triple other) {
        if (this.first != other.first)
            return this.first - other.first;
        else
            return this.second - other.second;
        // there won't be cases for equal first and second
    }
}

class Graph {
    public AdjacencyMatrix am;
    public AdjacencyList al;
    public EdgeList el;
    public boolean directed;
    public int numVertices;

    public Graph (int V, boolean dir) { // O(V^2) for AM, O(V) for AL, O(1) for EL
        directed = dir;
        numVertices = V;
        am = new AdjacencyMatrix(numVertices,directed);
        al = new AdjacencyList(numVertices,directed);
        el = new EdgeList(directed);
    }

    public Graph (AdjacencyMatrix mat) { // given an AM, create the AL and EL, will take O(V^2) for both DS
        am = mat;
        directed = mat.directed;
        numVertices = mat.numVertices;
        al = new AdjacencyList(mat);
        el = new EdgeList(mat);
    }

    public void connect (int a, int b) { // for D/U or U/U graph, O(1) for AM, O(k) for AL, O(E) for EL, k neighbors
        am.connect(a,b);
        al.connect(a,b);
        el.connect(a,b);
    }

    public void connect (int a, int b, int w) { // for D/W or U/W graph, O(1) for AM, O(k) for AL, O(E) for EL, k neighbors
        am.connect(a,b,w);
        al.connect(a,b,w);
        el.connect(a,b,w);
    }

    public void addVertex () { // resizing AM takes O(V^2), can be optimized to O(V); also O(V) for AL
        am.addVertex();
        al.addVertex(); // no need to deal with edge list
    }

    public void enumerateNeighbors (int V) { System.out.println(al.list.get(V)); } // O(k), k neighbors, best with AL
    public int outDegree (int V) { return al.outDegree(V); } // O(1) with AL
    public boolean existsEdge (int u, int v) { return am.matrix[u][v] != 0; } // O(1) with AM
    public int numEdges () { return el.list.size(); } // O(1) with EL

    public void BFS (int v) { System.out.println(al.BFS(v,true)); }
    public void DFS (int v) { System.out.println(al.DFS(v,true)); }

    public boolean reachable (int u, int v) { return al.reachable(u,v); }
    public int shortestPathLength (int u, int v) { return al.shortestPathLength(u,v); }
    public int countCC () { return al.countCC(); }
    public int countSCC () { return al.countSCC(); }

    public void showAM () { am.printMatrix(); }
    public void showAL () { al.printList(); }
    public void showEL () { el.printList(); }

    public void toposort (boolean useBFS) { System.out.println(useBFS ? al.toposortBFS() : al.toposortDFS()); }
}

public class GraphDemo {
    public static void testG1 () { // U/U graph
        // U/U CP3 Fig 2.4
        System.out.println("Test U/U CP3 Fig 2.4");
        Graph g1 = new Graph(7,false);
        g1.connect(3,4);
        g1.connect(4,5);
        g1.connect(5,6);
        g1.connect(3,1);
        g1.connect(1,2);
        g1.connect(1,0);
        g1.connect(0,2);
        g1.connect(2,4);
        g1.showAM();
        g1.showAL();
        g1.showEL();
        System.out.println();
    }

    public static void testG2 () { // U/W graph
        // U/W K5 (Complete)
        System.out.println("Test U/W K5");
        Graph g2 = new Graph(5,false);
        g2.connect(0,1,1);
        g2.connect(1,2,2);
        g2.connect(2,3,3);
        g2.connect(3,4,4);
        g2.connect(4,0,5);
        g2.connect(0,3,6);
        g2.connect(3,1,7);
        g2.connect(1,4,8);
        g2.connect(4,2,9);
        g2.connect(2,0,10);
        g2.showAM();
        g2.showAL();
        g2.showEL();
        System.out.println();
    }

    public static void testG3 () { // D/U graph
        // D/U CP3 Fig 4.4
        System.out.println("Test D/U CP3 Fig 4.4");
        Graph g3 = new Graph(8,true);
        g3.connect(0,1);
        g3.connect(1,3);
        g3.connect(3,4);
        g3.connect(0,2);
        g3.connect(1,2);
        g3.connect(2,3);
        g3.connect(2,5);
        g3.connect(7,6);
        g3.showAM();
        g3.showAL();
        g3.showEL();
        System.out.println();
    }

    public static void testG4 () { // D/W graph
        // D/W CP3 Fig 4.26B*
        System.out.println("Test D/W CP3 Fig 4.26B*");
        Graph g4 = new Graph(5,true);
        g4.connect(0,1,99);
        g4.connect(0,2,50);
        g4.connect(1,2,50);
        g4.connect(2,3,99);
        g4.connect(1,3,50);
        g4.connect(1,4,50);
        g4.connect(3,4,75);
        g4.showAM();
        g4.showAL();
        g4.showEL();
        System.out.println();
    }

    public static void testG5 () {
        // D/U CP3 4.9
        System.out.println("Test CP3 4.9");
        Graph g5 = new Graph(8,true);
        g5.connect(0,1);
        g5.connect(2,1);
        g5.connect(1,3);
        g5.connect(3,2);
        g5.connect(3,4);
        g5.connect(4,5);
        g5.connect(5,7);
        g5.connect(7,6);
        g5.connect(6,4);
        g5.enumerateNeighbors(3);                           // [<2,1>, <4,1>]
        System.out.println(g5.outDegree(1));                // 1
        System.out.println(g5.outDegree(3));                // 2
        System.out.println(g5.existsEdge(1,3));             // true
        System.out.println(g5.existsEdge(4,0));             // false
        System.out.println(g5.numEdges());                  // 9

        g5.BFS(3);                                          // Path: 3-4-5-7-6-end -> a possible path from 3
                                                            // [3, 2, 4, 1, 5, 7, 6] -> the sequence of traversal

        g5.DFS(0);                                          // Path: 0-1-3-4-5-7-6-end -> a possible path from 0
                                                            // [0, 1, 3, 2, 4, 5, 7, 6]

        System.out.println(g5.reachable(0,4));              // true, check 0-1-3-4
        System.out.println(g5.reachable(1,6));              // true, check 1-3-4-5-7-6
        System.out.println(g5.reachable(2,0));              // false
        System.out.println(g5.shortestPathLength(0,6));     // 6
        System.out.println(g5.shortestPathLength(3,0));     // 0, since it's not reachable
        System.out.println();
    }

    public static void testG6 () { // counting CCs
        // CP3 4.1
        System.out.println("Test CP3 4.1");
        Graph g6 = new Graph(9,false);
        g6.connect(0,1);
        g6.connect(2,1);
        g6.connect(3,1);
        g6.connect(3,2);
        g6.connect(3,4);
        g6.connect(7,6);
        g6.connect(6,8);
        g6.showAL();
        System.out.println("Number of CCs: "+g6.countCC());  // 3, lecture example
        System.out.println();
    }

    public static void testG7 () { // topological sort
        // CP3 4.4
        System.out.println("Test CP3 4.4");
        Graph g7 = new Graph(8,true);
        g7.connect(0,1);
        g7.connect(0,2);
        g7.connect(1,2);
        g7.connect(1,3);
        g7.connect(2,3);
        g7.connect(3,4);
        g7.connect(2,5);
        g7.connect(7,6);
        g7.showEL();
        System.out.print("BFS topological order: ");
        g7.toposort(true);                                  // [0, 7, 1, 6, 2, 3, 5, 4]
        System.out.print("DFS topological order: ");
        g7.toposort(false);                                 // [7, 6, 0, 1, 2, 5, 3, 4]
        System.out.println();
    }

    public static void testG8 () { // counting SCCs
        // Lecture 13 Slide 38
        System.out.println("Test SCC (L13, S38)");
        Graph g8 = new Graph(10,true);
        g8.connect(0,1);
        g8.connect(1,2);
        g8.connect(2,0);
        g8.connect(1,4);
        g8.connect(1,3);
        g8.connect(4,5);
        g8.connect(5,6);
        g8.connect(5,3);
        g8.connect(4,3);
        g8.connect(3,6);
        g8.connect(6,4);
        g8.connect(3,7);
        g8.connect(6,8);
        g8.connect(7,8);
        g8.connect(7,9);
        g8.connect(9,8);
        g8.connect(9,6);
        g8.showAL();
        System.out.println("Number of SCCs: "+g8.countSCC());   // 3, lecture example
        System.out.println();
    }

    public static void testG9 () { // everything I can test ;)
        // CP3 4.3
        System.out.println("Test CP3 4.3");
        Graph g9 = new Graph(13,false); // actually it's directed but for every edge (u,v) that exists, edge (v,u) also exists
        g9.connect(0,4);
        g9.connect(4,8);
        g9.connect(8,9);
        g9.connect(9,10);
        g9.connect(10,11);
        g9.connect(11,12);
        g9.connect(12,7);
        g9.connect(7,3);
        g9.connect(3,2);
        g9.connect(2,1);
        g9.connect(1,0);
        g9.connect(1,5);
        g9.connect(2,6);
        g9.connect(5,6);
        g9.connect(5,10);
        g9.connect(6,11);
        
        g9.showAM();
        g9.showAL();
        g9.showEL();

        System.out.println("Transposing this graph...");
        AdjacencyMatrix transposed = new AdjacencyMatrix(g9.al);
        transposed.transpose();
        transposed.printMatrix();       // will be the same as the AM since it's symmetric :)
        transposed.clearVertex(0);      // let's clear some vertexes and remove some edges shall we?
        transposed.clearVertex(1);
        transposed.directed = true;     // not undirected anymore, must manually changed directed due to the connect method
        transposed.removeEdge(2,3);
        System.out.println("Clear vertex 0, vertex 1, and remove edge(2,3)...");
        transposed.printMatrix();
        transposed.transpose();
        System.out.println("Transposing again...");
        transposed.printMatrix();
        transposed.transpose();
        System.out.println("Retransposing back...");
        transposed.printMatrix();       // must be the same as the second previous matrix
        System.out.println("Complementing the original graph now...");
        AdjacencyMatrix complemented = new AdjacencyMatrix(g9.al);
        complemented.weighted = false;
        complemented.complement();
        complemented.printMatrix();
        for (int i = 0; i < 8; i++)
            complemented.deleteVertex(0);
        System.out.println("Deleting 8 first vertices...");
        complemented.printMatrix();     // should be the 5x5 bottom corner of the previous matrix

        g9.enumerateNeighbors(6);                           // [<2,1>, <5,1>, <11,1>]
        System.out.println(g9.outDegree(5));                // 3
        System.out.println(g9.existsEdge(1,12));            // false
        System.out.println(g9.reachable(1,12));             // true
        System.out.println(g9.existsEdge(1,5));             // true
        System.out.println(g9.reachable(1,5));              // true, obviously since the edge exists
        System.out.println(g9.numEdges());                  // 16
        System.out.println(g9.shortestPathLength(0,12));    // 5, one of them is 0-1-2-3-7-12
        System.out.println(g9.shortestPathLength(4,11));    // 4, one of them is 4-8-9-10-11
        System.out.println(g9.countCC());                   // 1, since they are all connected

        g9.BFS(0);                                          // it will traverse [0, 1, 4, 2, 5, 8, 3, 6, 10, 9, 7, 11, 12] in order
                                                            // the path resulted is the shortest path from 0 to 12
        g9.BFS(5);                                          // it will traverse [5, 1, 6, 10, 0, 2, 11, 9, 4, 3, 12, 8, 7] in order
                                                            // similarly, the path resulted is the shortest path from 5 to 7
        g9.DFS(0);                                          // it will traverse [0, 1, 2, 3, 7, 12, 11, 6, 5, 10, 9, 8, 4] in order
                                                            // different to DFS, the path resulted seems to be a copy of the ArrayList itself
        g9.DFS(6);                                          // it will traverse [6, 2, 1, 0, 4, 8, 9, 10, 5, 11, 12, 7, 3] in order

        g9.toposort(true);                                  // we can't do toposort since it is not a DAG, will return an empty list
        g9.toposort(false);                                 // similar to above but will just do regular DFS from 0
                                                            // which is [0, 1, 2, 3, 7, 12, 11, 6, 5, 10, 9, 8, 4]
        System.out.println(g9.countSCC());                  // 1
        System.out.println();
    }

    public static void main (String[] args) {
        testG1();
        testG2();
        testG3();
        testG4();
        testG5();
        testG6();
        testG7();
        testG8();
        testG9();

        /*
        Note :
            To check whether a graph is DAG or not, simply compare toposortBFS with []. If equal, then it isn't a DAG
            To check whether a graph is a tree, we can simulate BFS and see if the length of the shortest path is equal to the traversal
            Finally, to check whether a graph is bipartite, we can again run BFS and allocate the vertexes travesed alternatively based on depth
            The final statement is a work in progress :)
        */
    }
}