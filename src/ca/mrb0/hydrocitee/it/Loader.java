package ca.mrb0.hydrocitee.it;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import ca.mrb0.hydrocitee.util.Streams;

import com.google.common.collect.ImmutableList;

public class Loader {
    private static Logger l = Logger.getLogger(Loader.class);
    
    public static ITModule newModuleFromInputStream(InputStream is) throws IOException {
    	byte[] impm = new byte[4];
    	
    	if (is.read(impm) != impm.length || !Arrays.equals(impm, new byte[] { 'I', 'M', 'P', 'M' })) {
    		l.error(String.format("failed to read impm; read %c%c%c%c instead", impm[0], impm[1], impm[2], impm[3]));
    		throw new IllegalArgumentException("missing impm");
    	}
    	
    	byte[] songNameBytes = new byte[26];
    	
    	if (is.read(songNameBytes) != songNameBytes.length) {
    		throw new IllegalArgumentException("truncated reading song name");
    	}
    	
    	int nul = Streams.arrayIndexOf(songNameBytes, (byte)0);
    	if (nul == -1) {
    		nul = 26;
    	}
    
    	String songName = new String(Arrays.copyOfRange(songNameBytes, 0, nul), "windows-1252");
    	
    	byte[] params = new byte[34];
    	if (is.read(params) != params.length) {
    		throw new IllegalArgumentException("truncated reading song params");
    	}
    	
    	int ordnum = Streams.unpack16(params, 2);
    	int insnum = Streams.unpack16(params, 4);
    	int smpnum = Streams.unpack16(params, 6);
    	int patnum = Streams.unpack16(params, 8);
    	int cwt_raw = Streams.unpack16(params, 10);
    	int cmwt_raw = Streams.unpack16(params, 12);
    	
    	int cwt_major = (cwt_raw >> 8) & 0xf;
    	int cwt_minor = 10 * ((cwt_raw >> 4) & 0xf) + (cwt_raw & 0xf);
    	int cmwt_major = (cmwt_raw >> 8) & 0xf;
    	int cmwt_minor = 10 * ((cmwt_raw >> 4) & 0xf) + (cmwt_raw & 0xf);
    	
    	int flags = Streams.unpack16(params, 14);
    	boolean stereo = (flags & 0x1) != 0;
    	boolean instruments = (flags & 0x4) != 0;
    	boolean linear = (flags & 0x8) != 0;
    	boolean oldfx = (flags & 0x10) != 0;
    	boolean linked = (flags & 0x20) != 0;
    
    	int special = Streams.unpack16(params, 16);
    	boolean hasMessage = (special & 0x1) != 0;
    	
    	
    	int gv = 0xff & params[18];
    	int mv = 0xff & params[19];
    	int speed = 0xff & params[20];
    	int tempo = 0xff & params[21];
    	int sep = 0xff & params[22];
    	
    	int msglen = Streams.unpack16(params, 24);
    	long msgoffs = Streams.unpack32(params, 26);
    	
    	SongValues songValues = new SongValues(songName, cwt_major, cwt_minor, cmwt_major, cmwt_minor, stereo, instruments, linear, oldfx, linked, gv, mv, speed, tempo, sep);
    	
    	int[] channelPans = Streams.readByteBlock(is, 64);
    	int[] channelVols = Streams.readByteBlock(is, 64);
    	
    	List<ITChannel> channels = new ArrayList<ITChannel>(64);
    	for(int i = 0; i < 64; i++) {
    		channels.add(new ITChannel(channelPans[i], channelVols[i]));
    	}
    	
    	
    	int[] orders = Streams.readByteBlock(is, ordnum);
    	List<Integer> orderList = new ArrayList<Integer>(orders.length);
    	for(int i = 0; i < orders.length; i++) {
    		orderList.add(orders[i]);
    	}
    	
    	long[] insOffsets = Streams.readLongBlock(is, insnum);
    	long[] smpOffsets = Streams.readLongBlock(is, smpnum);
    	long[] patOffsets = Streams.readLongBlock(is, patnum);
    	
    	// get lazy and read the rest of the inputstream immediately so we can randomly address the remaining data
    	long startOffset = 0x00c0 + ordnum + (insnum * 4) + (smpnum * 4) + (patnum * 4);
    	byte[] contents = new byte[0];
    	
    	byte[] buf = new byte[2048];
    	int read;
    	while(-1 != (read = is.read(buf))) {
    		int oldLen = contents.length;
    		
    		contents = Arrays.copyOf(contents, oldLen + read);
    		for(int i = 0; i < read; i++) {
    			contents[oldLen + i] = buf[i];
    		}
    	}
    	
    	String message = null;
    	if (hasMessage) {
    		int offs = (int)(msgoffs - startOffset);
    		
    		byte[] msgdata = Arrays.copyOfRange(contents, offs, offs + msglen);
    		nul = Streams.arrayIndexOf(msgdata, (byte)0);
    		if (nul == -1) {
    			nul = msgdata.length;
    		}
    		for(int i = 0; i < msgdata.length; i++) {
    			if (msgdata[i] == 0x0d) {
    				msgdata[i] = "\n".getBytes("windows-1252")[0];
    			}
    		}
    		message = new String(Arrays.copyOf(msgdata, nul), "windows-1252");
    	}
    	
    	// load instruments
    	
    	ImmutableList.Builder<ITInstrument> instrumentList = ImmutableList.builder();
    	for(int i = 0; i < insOffsets.length; i++) {
    		instrumentList.add(ITInstrument.newFromData(contents, (int)(insOffsets[i] - startOffset)));
    	}
    	
    	// load samples
    	
    	ImmutableList.Builder<ITSample> sampleList = ImmutableList.builder();
        for(int i = 0; i < smpOffsets.length; i++) {
            sampleList.add(ITSample.newFromData(contents, (int)(smpOffsets[i] - startOffset), (int)startOffset));
        }
    	
    	// load patterns
        
        ImmutableList.Builder<ITPattern> patterns = ImmutableList.builder();
        for(int i = 0; i < patOffsets.length; i++) {
            if (patOffsets[i] == 0) {
                patterns.add(new ITPattern(64));
            } else {
                patterns.add(ITPattern.newFromData(contents, (int)(patOffsets[i] - startOffset)));
            }
        }
    	
    	return new ITModule(songValues, orderList, message, channels, instrumentList.build(), sampleList.build(), patterns.build());
    	
    }

}
