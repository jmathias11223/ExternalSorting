import java.io.*;
import java.util.ArrayList;

public class ReplacementSort {
    public static final int BLOCK_SIZE = 1024;
    public static final int RECORD_SIZE = 8;
    public static RandomAccessFile runFile;
    private static FileWriter testFile;
    public static ArrayList<Long> runStarts;
    public static MinHeap<Record> heap;
    public static byte[] inputBuffer = new byte[BLOCK_SIZE * RECORD_SIZE];
    public static byte[] outputBuffer = new byte[BLOCK_SIZE * RECORD_SIZE];

    public static int sort() throws IOException {
        runFile = new RandomAccessFile("files/runfile.bin", "rw");
        heap = new MinHeap<Record>(BLOCK_SIZE * 16);
        runStarts = new ArrayList<Long>();
        runStarts.add(runFile.getFilePointer());
        int numData = 0;
        // Handles dumping the first 16 blocks into the heap
        while (numData != -1 && !heap.isFull()) {
            numData = Externalsorting.input.read(inputBuffer);
            if (numData != -1) {
                dumpToHeap(inputBuffer);
            }
        }
        if (Externalsorting.input.read(inputBuffer) == -1) {
            dumpToOutput();
            return 0;
        }

        // Handles blocks 17 through end of file
        int outputCount = 0;
        int readReturn = 0;
        while (Externalsorting.input.getFilePointer() < Externalsorting.input
            .length() || readReturn != -1) {
            for (int i = 0; i < inputBuffer.length; i += 8) {
                Record popped = (Record)heap.removeMin();
                // Add to output buffer
                for (int j = 0; j < 8; j++) {
                    outputBuffer[outputCount + j] = popped.getBytes()[j];
                }
                outputCount += 8;
                byte[] temp = { inputBuffer[i], inputBuffer[i + 1],
                    inputBuffer[i + 2], inputBuffer[i + 3], inputBuffer[i + 4],
                    inputBuffer[i + 5], inputBuffer[i + 6], inputBuffer[i
                        + 7] };
                Record insert = new Record(temp);
                // Check if popped record is greater than or less than the
                // insert record
                if (insert.compareTo(popped) > 0) {
                    heap.insert(insert);
                }
                else {
                    heap.addAtEnd(insert);
                }

                // Check heap, input, and output files
                if (heap.isEmpty()) {
                    dumpToOutput(outputBuffer, outputCount);
                    outputCount = 0;
                    runStarts.add(runFile.getFilePointer());
                    heap.reheapify();
                }
                if (outputCount >= BLOCK_SIZE * 8) {
                    dumpToOutput(outputBuffer, -1);
                    outputCount = 0;
                }
            }
            readReturn = Externalsorting.input.read(inputBuffer);
        }
        if(outputCount != 0) {
            dumpToOutput(outputBuffer, outputCount);
            outputCount = 0;
        }
        runStarts.add(runFile.getFilePointer());
        heap.reheapify();
        heapToRun();
        heap.clear();
        return 1;
    }
    
    
    public static MinHeap<Record> getHeap() {
        return heap;
    }
    
    
    public static void heapToRun() throws IOException {
        int outputSize = 0;
        while (heap.heapsize() != 0) {
            Record rec = (Record)heap.removeMin();
            for (int j = 0; j < 8; j++) {
                outputBuffer[outputSize + j] = rec.getBytes()[j];
            }
            outputSize += 8;
            if(outputSize >= outputBuffer.length) {
                dumpToOutput(outputBuffer, outputBuffer.length);
                outputSize = 0;
            }
        }
    }


    /**
     * Dumps the contents of the input array into the heap
     * 
     * @param input
     */
    public static void dumpToHeap(byte[] input) {
        for (int i = 0; i < input.length; i += 8) {
            byte[] temp = { input[i], input[i + 1], input[i + 2], input[i + 3],
                input[i + 4], input[i + 5], input[i + 6], input[i + 7] };
            Record rec = new Record(temp);
            heap.insert(rec);
        }
    }


    /**
     * Dumps the contents of the heap into the output file directly
     * Used for sorting <=16 blocks of data
     * 
     * @throws IOException
     */
    public static void dumpToOutput() throws IOException {
        testFile = new FileWriter("files/test.txt");
        int count = 0;
        int outputSize = 0;
        while (heap.heapsize() != 0) {
            Record rec = (Record)heap.removeMin();
            if (count % 1024 == 0) {
                if(count % 4096 == 0 && count != 0) {
                    System.out.println();
                }
                else if(count != 0) {
                    System.out.print(", ");
                }
                System.out.print(rec.toString());
            }
            for (int j = 0; j < 8; j++) {
                outputBuffer[outputSize + j] = rec.getBytes()[j];
            }
            outputSize += 8;
            if(outputSize >= outputBuffer.length) {
                Externalsorting.output.write(outputBuffer);
                outputSize = 0;
            }
            count++;
        }
    }


    /**
     * Dumps the contents of the output array to the run file
     * 
     * @param output
     * @throws IOException 
     */
    public static void dumpToOutput(byte[] output, int numBytes) throws IOException {
        if(numBytes == -1) {
            runFile.write(output);
        }
        else {
            runFile.write(output, 0, numBytes);
        }

    }
}
