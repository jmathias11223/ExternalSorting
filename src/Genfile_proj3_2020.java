/***************************************************************************************
    WARNING: This program uses the Assertion class. When it is run with wrong arguments,
    assertions must be turned on. For example, under Linux, use:
         java -ea Genfile_proj3_2020

    This file generate a data file for project 3. The size is a multiple of 8192 bytes.
    Each record is one non-negative int and one float. Please be aware that this 
    small piece of code is not designed to generate test cases. Please modify the 
    code accordingly if you want to use it for other purposes.
 
    Usage Example:
          java Genfile_proj3_2020 Sampledata.bin 2
    This will generate a binary data file Sampledata.bin with 2 blocks (16,384 bytes).
****************************************************************************************/

import java.io.*;
import java.util.*;
import java.math.*;

public class Genfile_proj3_2020 {

static final int NumRecs = 1024; // Each record holds 8 bytes. Each block has 8192 bytes
    
/** Initialize the random variable */
static private Random value = new Random(); // Hold the Random class object

    
static int randInt() {
    return value.nextInt(Integer.MAX_VALUE );
}

static float randFloat() {
    return value.nextFloat()*Float.MAX_VALUE;
}
    
public static void main(String args[]) throws IOException {
    int val;
    float val2;
    assert (args.length == 2) :
         "\nUsage: Genfile_proj3_2020 <filename> <size>" +
	 "\nOptions \nSize is measured in blocks of 8192 bytes";

  int filesize = Integer.parseInt(args[1]); // Size of file in blocks
  DataOutputStream file = new DataOutputStream(
      new BufferedOutputStream(new FileOutputStream(args[0])));

  for (int i=0; i<filesize; i++)
      for (int j=0; j<NumRecs; j++) {
	     val = (int)(randInt());
         file.writeInt(val);
	     val2 = (float)(randFloat());
	     file.writeFloat(val2);
      }
  
  file.flush();
  file.close();
}

}
