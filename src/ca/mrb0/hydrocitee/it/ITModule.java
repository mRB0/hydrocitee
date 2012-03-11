package ca.mrb0.hydrocitee.it;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

public class ITModule {

	private static Logger l = Logger.getLogger(ITModule.class);

	private SongValues songSettings;
	private int orders[];
	
	
	public static int unpack16(byte arr[], int offs) {
		return ((int)arr[offs+1] << 8) | (int)arr[offs];
	}
	
	public static long unpack32(byte arr[], int offs) {
		return ((long)arr[offs+3] << 32) | ((long)arr[offs+2] << 16) | ((long)arr[offs+1] << 8) | (long)arr[offs];
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
		int nul = Arrays.asList(songNameBytes).indexOf('\0');
		if (nul == -1) {
			nul = 26;
		}
		// TODO: rtrim songname?
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

		int gv = params[18];
		int mv = params[19];
		int speed = params[20];
		int tempo = params[21];
		int sep = params[22];
		
		int msglen = unpack16(params, 24);
		long msgoffs = unpack32(params, 26);
		
		mod.songSettings = new SongValues(songName, cwt_major, cwt_minor, cmwt_major, cmwt_minor, stereo, instruments, linear, oldfx, linked, gv, mv, speed, tempo, sep);
		
		mod.orders = new int[ordnum];
		l.debug(String.format("read %d orders", ordnum));
		for(int i = 0; i < ordnum; i++) {
			l.debug(String.format("    %d", i));
			
			int next = is.read();
			if (next == -1) {
				throw new IllegalArgumentException("couldn't read all the orders");
			}
			mod.orders[0] = next;
		}
		
		
		long insOffsets[] = loadLongBlock(is, insnum);
		long smpOffsets[] = loadLongBlock(is, smpnum);
		long patOffsets[] = loadLongBlock(is, patnum);
		
		long startOffset = 0x00c0 + ordnum + (insnum * 4) + (smpnum * 4) + (patnum * 4);
		byte fileContents[] = new byte[0];
		
		byte buf[] = new byte[2048];
		int read;
		while(0 != (read = is.read(buf))) {
			int oldLen = fileContents.length;
			
			fileContents = Arrays.copyOf(fileContents, oldLen + read);
			for(int i = 0; i < read; i++) {
				fileContents[oldLen + i] = buf[i];
			}
		}
		
		
		
		return mod;
	}
	
	private static long[] loadLongBlock(InputStream is, int count) throws IOException {
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
	
	public SongValues getValues() {
		return new SongValues(songSettings);
	}
	
	public void setValues(SongValues newValues) {
		// TODO: Validate newValues before using it.
		this.songSettings = new SongValues(newValues);
	}
	
}
