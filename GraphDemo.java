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
    }

    public void transpose () {
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

    public void complement () { // only for unweighted graph
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

    public AdjacencyList (int V, boolean dir) {
        directed = dir;
        numVertices = V;
        list = new ArrayList<List<Pair>>();
        for (int i = 0; i < V; i++) {
            list.add(new ArrayList<Pair>());
        }
    }

    public AdjacencyList (AdjacencyMatrix am) {
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

    // For Kattis : Ab Initio
    public int outDegree (int V) {
        return list.get(V).size();
    }

    // For Kattis : Ab Initio
    public long vertexHash (int V) {
        List<Pair> edges = list.get(V);
        long ans = 0;
        for (int i = edges.size()-1; i >= 0; i--) {
            ans *= 7;
            ans += outDegree(edges.get(i).first);
        }

        return ans % 1000000007;
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
    }

    public void addVertex () {
        numVertices++;
        list.add(new ArrayList<Pair>());
    }
}

class EdgeList {
    public List<Triple> list;
    public boolean directed;

    public EdgeList (boolean dir) {
        directed = dir;
        list = new ArrayList<Triple>();
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

class Graph { // Work in progress
    public AdjacencyMatrix am;
    public AdjacencyList al;
    public EdgeList el;
    public boolean directed;
    public int numVertices;

    public Graph (int V, boolean dir) {
        directed = dir;
        numVertices = V;
        am = new AdjacencyMatrix(numVertices,directed);
        al = new AdjacencyList(numVertices,directed);
        el = new EdgeList(directed);
    }

    public void connect (int a, int b) {
        am.connect(a,b);
        al.connect(a,b);
        el.connect(a,b);
    }

    public void connect (int a, int b, int w) {
        am.connect(a,b,w);
        al.connect(a,b,w);
        el.connect(a,b,w);
    }

    public void showAM() {
        am.printMatrix();
    }

    public void showAL() {
        al.printList();
    }

    public void showEL() {
        el.printList();
    }
}

public class GraphDemo {
    public static void main (String[] args) {
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

        // U/W K5 (Complete)
        System.out.println("\nTest U/W K5");
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

        // D/U CP3 Fig 4.4
        System.out.println("\nTest D/U CP3 Fig 4.4");
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

        // D/W CP3 Fig 4.26B*
        System.out.println("\nTest D/W CP3 Fig 4.26B*");
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
    }
}