import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MultiwayMerge {

    public static MinHeap<Record> heap = ReplacementSort.getHeap();
    // Stores number of Records in each run
    private static ArrayList<Integer> runCounters = new ArrayList<Integer>();
    private static ArrayList<Long> currRunPointers = new ArrayList<Long>();
    private static ArrayList<Long> nextRunPointers = new ArrayList<Long>();

    /**
     * Main method for multiway merge
     * 
     * @throws IOException
     */
    public static void merge(RandomAccessFile output, RandomAccessFile runFile)
        throws IOException {
        runCounters.clear();
        currRunPointers.clear();
        currRunPointers.addAll(ReplacementSort.runStarts);
        int runCounter = 1;
        int outputCount = 0;

        // Dumps first <16 blocks into the heap
        runFile.seek(0);
        runFile.read(ReplacementSort.inputBuffer);
        dumpToHeap(ReplacementSort.inputBuffer, runCounter++);
        for (int i = 1; i < ReplacementSort.runStarts.size() && i < 16; i++) {
            runFile.seek(ReplacementSort.runStarts.get(i));
            runFile.read(ReplacementSort.inputBuffer);
            dumpToHeap(ReplacementSort.inputBuffer, runCounter++);
        }

        // Main functionality of merge
        while (!heap.isEmpty()) {
            Record popped = (Record)heap.removeMin();
            // Add to output buffer
            for (int j = 0; j < 8; j++) {
                ReplacementSort.outputBuffer[outputCount + j] = popped
                    .getBytes()[j];
            }
            outputCount += 8;
            // Update run counter list
            runCounters.set(popped.getRun() - 1, runCounters.get(popped.getRun()
                - 1) - 1);
            // Check if the current run has any elements in the heap
            // If not, load the next block from the run
            if (runCounters.get(popped.getRun() - 1) <= 0) {
                loadNextBlock(popped.getRun());
            }
            // Check if output buffer is full
            if (outputCount >= ReplacementSort.outputBuffer.length) {
                outputCount = 0;
                output.write(ReplacementSort.outputBuffer);
            }
        }
        

        // Recursive condition
        int count = 0;
        while(ReplacementSort.runStarts.size() > 0 && count < 16) {
            ReplacementSort.runStarts.remove(0);
            count++;
        }
        if (ReplacementSort.runStarts.size() != 0) {
            merge(output, runFile);
        }
    }

    /**
     * Dumps input array to the heap
     * 
     * @param input
     * @param runID
     * @throws IOException
     */
    public static void dumpToHeap(byte[] input, int runID) throws IOException {
        for (int i = 0; i < input.length; i += 8) {
            byte[] temp = { input[i], input[i + 1], input[i + 2], input[i + 3],
                input[i + 4], input[i + 5], input[i + 6], input[i + 7] };
            Record rec = new Record(temp, runID);
            heap.insert(rec);
        }
        currRunPointers.set(runID - 1, ReplacementSort.runFile
            .getFilePointer());
        runCounters.add(input.length / 8);
    }

    /**
     * implement counter for non 1-block length runs
     * 
     * @param runID
     * @throws IOException
     */
    public static void loadNextBlock(int runID) throws IOException {
        // Checks if the current run has been completely loaded
        if (runID < ReplacementSort.runStarts.size()
            && currRunPointers.get(runID
                - 1) >= ReplacementSort.runStarts.get(runID)) {
            return;
        }
        int numRecs = numberOfRecordsToLoad(runID);
        ReplacementSort.runFile.seek(currRunPointers.get(runID - 1));
        int numRead = ReplacementSort.runFile.read(ReplacementSort.inputBuffer);
        if (numRead == -1) {
            return;
        }
        byte[] input = ReplacementSort.inputBuffer;
        if(numRecs == -1) {
            for (int i = 0; i < numRead; i += 8) {
                byte[] temp = { input[i], input[i + 1], input[i + 2], input[i + 3],
                    input[i + 4], input[i + 5], input[i + 6], input[i + 7] };
                Record rec = new Record(temp, runID);
                heap.insert(rec);
            }
            runCounters.set(runID - 1, numRead / 8);
        }
        else {
            for (int i = 0; i < input.length && i < numRecs; i += 8) {
                byte[] temp = { input[i], input[i + 1], input[i + 2], input[i + 3],
                    input[i + 4], input[i + 5], input[i + 6], input[i + 7] };
                Record rec = new Record(temp, runID);
                heap.insert(rec);
            }
            runCounters.set(runID - 1, numRecs / 8);
        }
        currRunPointers.set(runID - 1, ReplacementSort.runFile
            .getFilePointer());
    }
    
    
    public static int numberOfRecordsToLoad(int runID) {
        // Check to see if this is the last run
        // No action needs to be taken for this; easiest run
        if(runID >= ReplacementSort.runStarts.size()) {
            return -1;
        }
        // Check if there is more than 1 block left
        if(ReplacementSort.runStarts.get(runID) - currRunPointers.get(runID - 1) >= ReplacementSort.BLOCK_SIZE * 8) {
            return ReplacementSort.BLOCK_SIZE * 8;
        }
        else {
            return (int)(ReplacementSort.runStarts.get(runID) - currRunPointers.get(runID - 1));
        }
    }


    public static void updateRunStarts() {
        ReplacementSort.runStarts.clear();
    }
}
