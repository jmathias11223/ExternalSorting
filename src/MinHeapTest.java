import student.TestCase;

public class MinHeapTest extends TestCase {
    private MinHeap<String> heap;
    private MinHeap<Integer> intHeap;
    private MinHeap<Integer> iHeap;
    
    
    public void setUp() {
        heap = new MinHeap<String>(7);
        intHeap = new MinHeap<Integer>(7);
        Integer[] arr = {1, 2, 3, 4, 5, 6, 7};
        iHeap = new MinHeap<Integer>(arr, 7, 7) ;
        heap.insert("Apple");
        heap.insert("Banana");
        heap.insert("Cherry");
        heap.insert("Grape");
        heap.insert("Apricot");
        heap.insert("Berries");
        heap.insert("Jelly");
    }
    
    
    public void testHeapSize() {
        assertEquals(0, intHeap.heapsize());
        assertEquals(7, heap.heapsize());
    }
    
    
    public void testPop() {
        heap.pop();
        heap.add("Tomatoes");
        assertEquals(7, heap.heapsize());
        iHeap.pop();
        iHeap.add(5);
        iHeap.pop();
        iHeap.addAtEnd(1);
        System.out.println(iHeap.toString());
        assertEquals(6, iHeap.heapsize());
    }
}
