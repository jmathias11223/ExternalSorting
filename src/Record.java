import java.nio.ByteBuffer;

public class Record implements Comparable<Record> {
    
    // Entire 8 byte array
    private byte[] bin;
    // The run that this Record was sorted with
    private int runID;

    public Record(byte[] bin) {
        this.bin = bin;
    }
    
    
    public Record(byte[] bin, int runID) {
        this.bin = bin;
        this.runID = runID;
    }


    public float getKey() {
        ByteBuffer buff = ByteBuffer.wrap(bin);
        return buff.getFloat(4);
    }


    public int getData() {
        ByteBuffer buff = ByteBuffer.wrap(bin);
        return buff.getInt();
    }
    
    
    public byte[] getBytes() {
        return bin;
    }
    
    
    public int getRun() {
        return runID;
    }
    
    
    public void setBytes(byte[] newBin) {
        bin = newBin;
    }


    @Override
    public int compareTo(Record rec) {
        return (int)(getKey() - rec.getKey());
    }
    
    @Override
    public String toString() {
        return getData()+" "+getKey();
    }

}
