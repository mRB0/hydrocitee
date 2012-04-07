package ca.mrb0.hydrocitee.it;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * TODO: Find a much better (more efficient) way to store/access sample data
 * 
 * @author mrb
 *
 */
public class ITSampleData {
    private static Logger l = Logger.getLogger(ITSampleData.class);
    
    public enum CompressionType {
        Uncompressed,
        IT214,
        IT215
    };
    
    private int sampleData[];
    private List<Integer> sampleDataList = null;
    
    public ITSampleData(int sampleData[]) {
        this.sampleData = Arrays.copyOf(sampleData, sampleData.length);
    }
    
    public ITSampleData() {
        this(new int[0]);
    }
    
    public int[] getSampleData() {
        return Arrays.copyOf(sampleData, sampleData.length);
    }
    
    public synchronized List<Integer> getSampleDataList() {
        if (sampleDataList == null) {
            sampleDataList = new ArrayList<Integer>(sampleData.length);
            for (int i = 0; i < sampleData.length; i++) {
                sampleDataList.set(i, sampleData[i]);
            }
        }
        return sampleDataList;
    }
    
    public int get(int idx) {
        return sampleData[idx];
    }
    
    public int size() {
        return sampleData.length;
    }
    
    public static ITSampleData newFromData(byte data[], int offs, int dataLengthSamples, boolean is16Bit, CompressionType compressionType) {
        
        final int[] outData;
        
        if (compressionType != CompressionType.Uncompressed) {
            outData = ITSampleDataDecompressor.decompressSample(data, offs, dataLengthSamples, is16Bit, compressionType);
        } else {
            outData = new int[dataLengthSamples];
            for(int i = 0; i < dataLengthSamples; i++) {
                if (is16Bit) {
                    outData[i] = ((0xff & data[offs + 2 * i + 1]) << 8) | (0xff & data[offs + 2 * i]); 
                } else {
                    outData[i] = 0xff & data[offs + i];
                }
            }
        }
        
        return new ITSampleData(outData);
    }
}
