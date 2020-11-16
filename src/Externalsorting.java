import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class Externalsorting {
    public static RandomAccessFile input;
    public static RandomAccessFile output;
    private static boolean isTesting = true;
    
    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            System.out.println("2 file names required.");
        }
        File outputFile = new File(args[1]);
        outputFile.delete();
        File runFile = new File("files/runfile.bin");
        runFile.delete();
        try {
            input = new RandomAccessFile(args[0], "r");
            output = new RandomAccessFile(args[1], "rw");
        }
        catch(Exception e) {
            return;
        }
        
        
        // Sorts the data in the input file and stores sorted run data in ReplacementSort.runFile
        int flag = ReplacementSort.sort();
        if(isTesting) {
            ReplacementSortCheck.check();
        }
        if(flag == 1) {
            MultiwayMerge.merge(output, ReplacementSort.runFile);
            if(!isTesting) {
                output.seek(0);
                byte[] arr = new byte[8];
                for(int i = 1; i <= output.length() / 8192; i++) {
                    output.read(arr);
                    Record rec = new Record(arr);
                    System.out.print(rec.getData() + " " + rec.getKey());
                    if(i % 4 == 0) {
                        System.out.println();
                    }
                    else {
                        System.out.print(", ");
                    }
                    output.seek(output.getFilePointer() + 8192 - 8);
                }
            }
        }
        if(isTesting) {
            ReplacementSortCheck.checkOutput();
        }
        output.close();
    }
    
}
