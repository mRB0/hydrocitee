package ca.mrb0.hydrocitee.it;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class ITModule {

	private static Logger l = Logger.getLogger(ITModule.class);

	private SongValues songSettings;
	private int orders[];
	private String songMessage = "";
	private ITChannel channels[] = new ITChannel[64];
	private ITInstrument instruments[];
			
	public static int unpack16(byte arr[], int offs) {
		return ((int)(0xff & arr[offs+1]) << 8) | (int)(0xff & arr[offs]);
	}
	
	public static long unpack32(byte arr[], int offs) {
		long out = 0;
		out |= (long)(0xff & arr[offs]);
		out |= (long)(0xff & arr[offs+1]) << 8;
		out |= (long)(0xff & arr[offs+2]) << 16;
		out |= (long)(0xff & arr[offs+3]) << 24;
		return out;
	}
	
	
	private static long[] readLongBlock(InputStream is, int count) throws IOException {
		long offsets[] = new long[count];
		for(int i = 0; i < count; i++) {
			byte offs[] = new byte[4];
			if (is.read(offs) != offs.length) {
				throw new IllegalArgumentException("couldn't read all the offsets");
			}
			offsets[i] = unpack32(offs, 0);
		}
		return offsets;
	}
	
	private static int[] readByteBlock(InputStream is, int count) throws IOException {
		int bytes[] = new int[count];
		for(int i = 0; i < count; i++) {
			int b = is.read();
			if (b == -1) {
				throw new IllegalArgumentException("couldn't read all the bytes");
			}
			bytes[i] = b;
		}
		return bytes;
	}
	
	public static int arrayIndexOf(byte[] haystack, byte needle) {
		for(int i = 0; i < haystack.length; i++) {
			if (haystack[i] == needle) {
				return i;
			}
		}
		return -1;
	}
	
	public static ITModule newFromInputStream(InputStream is) throws IOException {
		ITModule mod = new ITModule();
		
		byte impm[] = new byte[4];
		
		if (is.read(impm) != impm.length || !Arrays.equals(impm, new byte[] { 'I', 'M', 'P', 'M' })) {
			l.error(String.format("failed to read impm; read %c%c%c%c instead", impm[0], impm[1], impm[2], impm[3]));
			throw new IllegalArgumentException("missing impm");
		}
		
		byte songNameBytes[] = new byte[26];
		
		if (is.read(songNameBytes) != songNameBytes.length) {
			throw new IllegalArgumentException("truncated reading song name");
		}
		
		int nul = arrayIndexOf(songNameBytes, (byte)0);
		if (nul == -1) {
			nul = 26;
		}

		String songName = new String(Arrays.copyOfRange(songNameBytes, 0, nul), "windows-1252");
		
		byte params[] = new byte[34];
		if (is.read(params) != params.length) {
			throw new IllegalArgumentException("truncated reading song params");
		}
		
		int ordnum = unpack16(params, 2);
		int insnum = unpack16(params, 4);
		int smpnum = unpack16(params, 6);
		int patnum = unpack16(params, 8);
		int cwt_raw = unpack16(params, 10);
		int cmwt_raw = unpack16(params, 12);
		
		int cwt_major = (cwt_raw >> 8) & 0xf;
		int cwt_minor = 10 * ((cwt_raw >> 4) & 0xf) + (cwt_raw & 0xf);
		int cmwt_major = (cmwt_raw >> 8) & 0xf;
		int cmwt_minor = 10 * ((cmwt_raw >> 4) & 0xf) + (cmwt_raw & 0xf);
		
		int flags = unpack16(params, 14);
		boolean stereo = (flags & 0x1) != 0;
		boolean instruments = (flags & 0x4) != 0;
		boolean linear = (flags & 0x8) != 0;
		boolean oldfx = (flags & 0x10) != 0;
		boolean linked = (flags & 0x20) != 0;

		int special = unpack16(params, 16);
		boolean hasMessage = (special & 0x1) != 0;
		
		
		int gv = 0xff & params[18];
		int mv = 0xff & params[19];
		int speed = 0xff & params[20];
		int tempo = 0xff & params[21];
		int sep = 0xff & params[22];
		
		int msglen = unpack16(params, 24);
		long msgoffs = unpack32(params, 26);
		
		mod.songSettings = new SongValues(songName, cwt_major, cwt_minor, cmwt_major, cmwt_minor, stereo, instruments, linear, oldfx, linked, gv, mv, speed, tempo, sep);
		
		int channelPans[] = readByteBlock(is, 64);
		int channelVols[] = readByteBlock(is, 64);
		
		for(int i = 0; i < 64; i++) {
			mod.channels[i] = new ITChannel(channelPans[i], channelVols[i]);
		}
		
		mod.orders = readByteBlock(is, ordnum);
		
		long insOffsets[] = readLongBlock(is, insnum);
		long smpOffsets[] = readLongBlock(is, smpnum);
		long patOffsets[] = readLongBlock(is, patnum);
		
		// get lazy and read the rest of the inputstream immediately so we can randomly address the remaining data
		long startOffset = 0x00c0 + ordnum + (insnum * 4) + (smpnum * 4) + (patnum * 4);
		byte contents[] = new byte[0];
		
		byte buf[] = new byte[2048];
		int read;
		while(-1 != (read = is.read(buf))) {
			int oldLen = contents.length;
			
			contents = Arrays.copyOf(contents, oldLen + read);
			for(int i = 0; i < read; i++) {
				contents[oldLen + i] = buf[i];
			}
		}
		
		if (hasMessage) {
			int offs = (int)(msgoffs - startOffset);
			
			byte msgdata[] = Arrays.copyOfRange(contents, offs, offs + msglen);
			nul = arrayIndexOf(msgdata, (byte)0);
			if (nul == -1) {
				nul = msgdata.length;
			}
			for(int i = 0; i < msgdata.length; i++) {
				if (msgdata[i] == 0x0d) {
					msgdata[i] = "\n".getBytes("windows-1252")[0];
				}
			}
			mod.songMessage = new String(Arrays.copyOf(msgdata, nul), "windows-1252");
		}
		
		// load instruments
		
		mod.instruments = new ITInstrument[insOffsets.length];
		for(int i = 0; i < insOffsets.length; i++) {
			mod.instruments[i] = ITInstrument.newFromArray(contents, (int)(insOffsets[i] - startOffset));
		}
		
		return mod;
	}
	
	public SongValues getValues() {
		return new SongValues(songSettings);
	}
	
	public void setValues(SongValues newValues) {
		// TODO: Validate newValues before using it.
		this.songSettings = new SongValues(newValues);
	}
	
}
