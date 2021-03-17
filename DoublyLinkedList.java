class DoublyLinkedList {
    public Node head;
    public Node tail;
    public int size;
    
    public DoublyLinkedList () {
        this.head = new Node(-1); // dummy
        this.tail = this.head;
        this.size = 0;
    }

    public void insert (int v) { // O(1), insert to the back
        Node newNode = new Node(v);
        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        size++;
    }

    public void insertBefore (Node curr, Node newNode) { // O(1)
        newNode.next = curr.next;
        curr.next = newNode;
        newNode.prev = curr;
        if (newNode.next != null)
            newNode.next.prev = newNode;
        size++;
    }

    public void delete (Node curr) { // O(1)
        if (curr.prev != null) {
            if (curr.next != null) {
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
            } else {
                curr.prev.next = null;
            }
        } else {
            if (curr.next != null) {
                curr.next.prev = null;
            }
            // no handling for empty LL
        }
        size--;
    }

    public int get (int k) {
        Node curr = head;
        for (int i = 0; i <= k; i++) {
            curr = curr.next;
        }
        return curr.val;
    }
}

class Node {
    public int val;
    public Node next;
    public Node prev;

    public Node (int v) {
        this.val = v;
        this.next = null;
        this.prev = null;
    }
}