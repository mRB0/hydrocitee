package ca.mrb0.hydrocitee.it;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class ITModule {
	
	private SongValues songSettings;
	
	private static Logger l = Logger.getLogger(ITModule.class);
	
	public static int unpack16(byte arr[], int offs) {
		return ((int)arr[offs+1] << 8) | (int)arr[offs];
	}
	
	public static int unpack32(byte arr[], int offs) {
		return ((int)arr[offs+3] << 32) | ((int)arr[offs+2] << 16) | ((int)arr[offs+1] << 8) | (int)arr[offs];
	}
	
	public static ITModule newFromInputStream(InputStream is) throws IOException {
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
		
		
		
		return null;
	}
	
	public SongValues getValues() {
		return new SongValues(songSettings);
	}
	
	public void setValues(SongValues newValues) {
		// TODO: Validate newValues before using it.
		this.songSettings = new SongValues(newValues);
	}
	
}
