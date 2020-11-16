import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReplacementSortCheck {
    
    public static final int FILE_SIZE = 0xffff0;
    
    public static void check() throws IOException {
        byte[] input = new byte[FILE_SIZE];
        ReplacementSort.runFile.seek(0);
        ReplacementSort.runFile.read(input);
        
        //byte[] input = Files.readAllBytes(Paths.get("files/runFile.bin"));
        //System.out.println("Length of input array: " + input.length);
        System.out.println("Run starts list: ");
        System.out.println("Size: "+ReplacementSort.runStarts.size());
        for(int i = 0; i < ReplacementSort.runStarts.size(); i++) {
            System.out.println(ReplacementSort.runStarts.get(i) / 8);
        }
        System.out.println();
        System.out.println("ReplacementSort Test(In terms of Records):");
        int count = 1;
        for(int i = 8; i < input.length; i += 8) {
            byte[] temp = { input[i], input[i + 1], input[i + 2], input[i + 3],
                input[i + 4], input[i + 5], input[i + 6], input[i + 7] };
            Record rec1 = new Record(temp, 1);
            byte[] temp2 = { input[i - 8], input[i - 7], input[i - 6], input[i - 5],
                input[i - 4], input[i - 3], input[i - 2], input[i - 1] };
            Record rec2 = new Record(temp2, 1);
            if(rec1.compareTo(rec2) >= 0) {
                count++;
            }
            else {
                System.out.println(count);
                count = 1;
            }
        }
        System.out.println(count);
    }
    
    
    public static void checkOutput() throws IOException {
        System.out.println();
        System.out.println("Multiway Merge Test:");
        byte[] input = new byte[FILE_SIZE];
        Externalsorting.output.seek(0);
        Externalsorting.output.read(input);
        
        //byte[] input = Files.readAllBytes(Paths.get("files/runFile.bin"));
        //System.out.println("Length of input array: " + input.length);
        
        int count = 1;
        for(int i = 8; i < input.length; i += 8) {
            byte[] temp = { input[i], input[i + 1], input[i + 2], input[i + 3],
                input[i + 4], input[i + 5], input[i + 6], input[i + 7] };
            Record rec1 = new Record(temp, 1);
            byte[] temp2 = { input[i - 8], input[i - 7], input[i - 6], input[i - 5],
                input[i - 4], input[i - 3], input[i - 2], input[i - 1] };
            Record rec2 = new Record(temp2, 1);
            if(rec1.compareTo(rec2) >= 0) {
                count++;
            }
            else {
                System.out.println(count);
                count = 1;
            }
        }
        System.out.println(count);
    }
}
