// Min-heap implementation
public class MinHeap<T> {
    private Comparable[] Heap; // Pointer to the heap array
    private int size; // Maximum size of the heap
    private int n; // Number of things now in heap
    
    
    public MinHeap(int maxSize) {
        Heap = new Comparable[maxSize];
        size = maxSize;
        n = 0;
    }

    // Constructor supporting preloading of heap contents
    public MinHeap(Comparable[] h, int num, int max) {
        Heap = h;
        n = num;
        size = max;
        buildheap();
    }
    
    
    public boolean isFull() {
        return n == size;
    }


    // Return current size of the heap
    public int heapsize() {
        return n;
    }


    // Return true if pos a leaf position, false otherwise
    public boolean isLeaf(int pos) {
        return (pos >= n / 2) && (pos < n);
    }


    // Return position for left child of pos
    public int leftchild(int pos) {
        if (pos >= n / 2)
            return -1;
        return 2 * pos + 1;
    }


    // Return position for right child of pos
    public int rightchild(int pos) {
        if (pos >= (n - 1) / 2)
            return -1;
        return 2 * pos + 2;
    }


    // Return position for parent
    public int parent(int pos) {
        if (pos <= 0)
            return -1;
        return (pos - 1) / 2;
    }


    // Insert val into heap
    public void insert(Comparable key) {
        if (n >= size) {
            System.out.println("Heap is full");
            return;
        }
        int curr = n++;
        Heap[curr] = key; // Start at end of heap
        // Now sift up until curr's parent's key > curr's key
        while ((curr != 0) && (Heap[curr].compareTo(Heap[parent(curr)]) < 0)) {
            swap(curr, parent(curr));
            curr = parent(curr);
        }
    }


    public void swap(int pos1, int pos2) {
        Comparable temp = Heap[pos1];
        Heap[pos1] = Heap[pos2];
        Heap[pos2] = temp;
    }


    // Heapify contents of Heap
    public void buildheap() {
        for (int i = n / 2 - 1; i >= 0; i--)
            siftdown(i);
    }


    // Put element in its correct place
    public void siftdown(int pos) {
        if ((pos < 0) || (pos >= n))
            return; // Illegal position
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            if ((j < (n - 1)) && (Heap[j].compareTo(Heap[j + 1]) > 0))
                j++; // j is now index of child with greater value
            if (Heap[pos].compareTo(Heap[j]) <= 0)
                return;
            swap(pos, j);
            pos = j; // Move down
        }
    }


    // Remove and return min value
    public Comparable removeMin() {
        if (n == 0)
            return -1; // Removing from empty heap
        swap(0, --n); // Swap minimum with last value
        siftdown(0); // Put new heap root val in correct place
        return Heap[n];
    }


    // Remove and return element at specified position
    public Comparable remove(int pos) {
        if ((pos < 0) || (pos >= n))
            return -1; // Illegal heap position
        if (pos == (n - 1))
            n--; // Last element, no work to be done
        else {
            swap(pos, --n); // Swap with last value
            update(pos);
        }
        return Heap[n];
    }


    // Modify the value at the given position
    public void modify(int pos, Comparable newVal) {
        if ((pos < 0) || (pos >= n))
            return; // Illegal heap position
        Heap[pos] = newVal;
        update(pos);
    }


    // The value at pos has been changed, restore the heap property
    public void update(int pos) {
        // If it is a big value, push it up
        while ((pos > 0) && (Heap[pos].compareTo(Heap[parent(pos)]) < 0)) {
            swap(pos, parent(pos));
            pos = parent(pos);
        }
        siftdown(pos); // If it is little, push down
    }
    
    // Remove and return min value WITHOUT siftdown
    public Comparable pop() {
        if(n == 0) {
            return -1;
        }
        Comparable temp = Heap[0];
        Heap[0] = null;
        n--;
        return temp;
    }
    
    /**
     * Used to add a value at position 0
     * 
     * @param val
     */
    public void add(Comparable val) {
        Heap[0] = val;
        n++;
        update(0);
    }
    
    
    public void addAtEnd(Comparable val) {
        Heap[n] = val;
    }
    
    
    public boolean isEmpty() {
        return n == 0;
    }
    
    
    public void reheapify() {
        n = size;
        buildheap();
    }
    
    
    public void clear() {
        n = 0;
    }
    
    
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(Comparable comp: Heap) {
            str.append(comp.toString() + " ");
        }
        return str.toString();
    }
}
