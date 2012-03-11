package ca.mrb0.hydrocitee.it;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class ITInstrument {
	private static Logger l = Logger.getLogger(ITInstrument.class);
	
	public enum NNA {
		Cut,
		Continue,
		NoteOff,
		NoteFade
	};
	
	public enum DCT {
		Off,
		Note,
		Sample,
		Instrument
	};
	
	public enum DCA {
		Cut,
		NoteOff,
		NoteFade
	};
	
	private String filename;
	private String instrumentName;
	
	private NNA nna;
	private DCT dct;
	private DCA dca;
	
	private int fadeOut;
	private int pitchPanSep;
	private int pitchPanCtr;
	private int globalVol;
	private int defaultPan;
	private int randVol;
	private int randPan;
	
	private int filterCutoff;
	private int filterRes;
	
	private int midiChan;
	private int midiPgm;
	private int midiBnk;
	
	private NoteSamplePair noteSampleMap[];
	
	private ITVolumeEnvelope volEnv;
	private ITPanEnvelope panEnv;
	private ITPitchEnvelope pitchEnv;
	
	public static class NoteSamplePair {
		public final int targetNote;
		public final int sample;
		
		public NoteSamplePair(int note, int sample) {
			super();
			this.targetNote = note;
			this.sample = sample;
		}
	}
	
	public static ITInstrument newFromArray(byte data[], int offs) {
		ITInstrument inst = new ITInstrument();
		
		if (!Arrays.equals(Arrays.copyOfRange(data, offs, offs + 4), new byte[] { 'I', 'M', 'P', 'I' })) {
			throw new IllegalArgumentException(String.format("instrument at 0x%x had broken impi", offs));
		}
		
		offs += 4;
		byte filename[] = Arrays.copyOfRange(data, offs, offs + 12);
		int nul = ITModule.arrayIndexOf(filename, (byte)0);
		if (nul == -1) {
			nul = filename.length;
		}
		filename = Arrays.copyOfRange(filename, 0, nul);
		try {
			inst.filename = new String(filename, "windows-1252");
		} catch(UnsupportedEncodingException e) {
			// hopefully shouldn't happen as we are using a hardcoded encoding
			l.error("Unsupported encoding: " + e.toString());
			throw new RuntimeException(e);
		}
		
		offs += 12;
		offs++;
		inst.nna = NNA.values()[0xff & data[offs++]];
		inst.dct = DCT.values()[0xff & data[offs++]];
		inst.dca = DCA.values()[0xff & data[offs++]];
		inst.fadeOut = ITModule.unpack16(data, offs);
		offs += 2;
		inst.pitchPanSep = data[offs++];
		inst.pitchPanCtr = 0xff & data[offs++];
		inst.globalVol = 0xff & data[offs++];
		inst.defaultPan = 0xff & data[offs++];
		inst.randVol = 0xff & data[offs++];
		inst.randPan = 0xff & data[offs++];
		offs += 4;
		
		byte instname[] = Arrays.copyOfRange(data, offs, offs + 26);
		nul = ITModule.arrayIndexOf(instname, (byte)0);
		if (nul == -1) {
			nul = instname.length;
		}
		instname = Arrays.copyOf(instname, nul);
		try {
			inst.instrumentName = new String(instname, "windows-1252");
		} catch(UnsupportedEncodingException e) {
			// hopefully shouldn't happen as we are using a hardcoded encoding
			l.error("Unsupported encoding: " + e.toString());
			throw new RuntimeException(e);
		}
		
		offs += 26;
		
		inst.filterCutoff = 0xff & data[offs++];
		inst.filterRes = 0xff & data[offs++];
		inst.midiChan = 0xff & data[offs++];
		inst.midiPgm = 0xff & data[offs++];
		inst.midiBnk = ITModule.unpack16(data, offs);
		offs += 2;
		
		inst.noteSampleMap = new NoteSamplePair[120];
		for(int i = 0; i < inst.noteSampleMap.length; i++) {
			inst.noteSampleMap[i] = new NoteSamplePair(0xff & data[offs], 0xff & data[offs+1]);
			offs += 2;
		}
		
		inst.volEnv = ITVolumeEnvelope.newFromData(data, offs);
		offs += 0x52;
		inst.panEnv = ITPanEnvelope.newFromData(data, offs);
		offs += 0x52;
		inst.pitchEnv = ITPitchEnvelope.newFromData(data, offs);
		
		return inst;
	}
}
