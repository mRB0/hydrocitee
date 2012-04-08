package ca.mrb0.hydrocitee.it;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;

import ca.mrb0.hydrocitee.util.Streams;

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
	
	public final String filename;
	public final String instrumentName;
	
	public final NNA nna;
	public final DCT dct;
	public final DCA dca;
	
	public final int fadeOut;
	public final int pitchPanSep;
	public final int pitchPanCtr;
	public final int globalVol;
	public final int defaultPan;
	public final boolean defaultPanEnabled;
	public final int randVol;
	public final int randPan;
	
	public final int filterCutoff;
	public final int filterRes;
	
	public final int midiChan;
	public final int midiPgm;
	public final int midiBnk;
	
	public final List<NoteSamplePair> noteSampleMap;
	
	public final ITVolumeEnvelope volEnv; 
	public final ITPanEnvelope panEnv; 
	public final ITPitchEnvelope pitchEnv; 
	
	public static class NoteSamplePair {
		public final int targetNote;
		public final int sample;
		
		public NoteSamplePair(int note, int sample) {
			super();
			this.targetNote = note;
			this.sample = sample;
		}
	}
	
	
	public ITInstrument(String filename, String instrumentName, NNA nna,
            DCT dct, DCA dca, int fadeOut, int pitchPanSep, int pitchPanCtr,
            int globalVol, int defaultPan, boolean defaultPanEnabled, int randVol, int randPan,
            int filterCutoff, int filterRes, int midiChan, int midiPgm,
            int midiBnk, List<NoteSamplePair> noteSampleMap,
            ITVolumeEnvelope volEnv, ITPanEnvelope panEnv,
            ITPitchEnvelope pitchEnv) {
        super();
        // TODO: Add tests to these so we can prevent invalid values
        this.filename = filename;
        this.instrumentName = instrumentName;
        this.nna = nna;
        this.dct = dct;
        this.dca = dca;
        this.fadeOut = fadeOut;
        this.pitchPanSep = pitchPanSep;
        this.pitchPanCtr = pitchPanCtr;
        this.globalVol = globalVol;
        this.defaultPan = defaultPan;
        this.defaultPanEnabled = defaultPanEnabled;
        this.randVol = randVol;
        this.randPan = randPan;
        this.filterCutoff = filterCutoff;
        this.filterRes = filterRes;
        this.midiChan = midiChan;
        this.midiPgm = midiPgm;
        this.midiBnk = midiBnk;
        this.noteSampleMap = ImmutableList.copyOf(noteSampleMap);
        this.volEnv = volEnv;
        this.panEnv = panEnv;
        this.pitchEnv = pitchEnv;
    }

    private static List<NoteSamplePair> emptyNoteSampleMap() {
        List<NoteSamplePair> nsmap = new ArrayList<NoteSamplePair>(120);
        for (int i = 0; i < 120; i++) {
            nsmap.add(new NoteSamplePair(i, 0));
        }
        return ImmutableList.copyOf(nsmap);
    }
	
    public ITInstrument() {
        // TODO: Figure out correct values for pitch-pan ctr, and initial filter settings
		this("", "", NNA.Cut, DCT.Off, DCA.Cut, 0, 0, 0, 128, 0, false, 0, 0, 255, 0, 0, 0, 0, ImmutableList.copyOf(emptyNoteSampleMap()), null, null, null);
	}
	
	public static ITInstrument newFromData(byte data[], int offs) {
	    String filename;
	    String instrumentName;
	    
	    NNA nna;
	    DCT dct;
	    DCA dca;
	    
	    int fadeOut;
	    int pitchPanSep;
	    int pitchPanCtr;
	    int globalVol;
	    int defaultPan;
	    boolean defaultPanEnabled;
	    int randVol;
	    int randPan;
	    
	    int filterCutoff;
	    int filterRes;
	    
	    int midiChan;
	    int midiPgm;
	    int midiBnk;
	    
	    List<NoteSamplePair> noteSampleMap;
	    
	    ITVolumeEnvelope volEnv; 
	    ITPanEnvelope panEnv; 
	    ITPitchEnvelope pitchEnv; 
		
		if (!Arrays.equals(Arrays.copyOfRange(data, offs, offs + 4), new byte[] { 'I', 'M', 'P', 'I' })) {
			throw new IllegalArgumentException(String.format("instrument at 0x%x had broken impi", offs));
		}
		
		offs += 4;
		byte rawFilename[] = Arrays.copyOfRange(data, offs, offs + 12);
		int nul = Streams.arrayIndexOf(rawFilename, (byte)0);
		if (nul == -1) {
			nul = rawFilename.length;
		}
		rawFilename = Arrays.copyOfRange(rawFilename, 0, nul);
		try {
			filename = new String(rawFilename, "windows-1252");
		} catch(UnsupportedEncodingException e) {
			// hopefully shouldn't happen as we are using a hardcoded encoding
			l.error("Unsupported encoding: " + e.toString());
			throw new RuntimeException(e);
		}
		
		offs += 12;
		offs++;
		nna = NNA.values()[0xff & data[offs++]];
		dct = DCT.values()[0xff & data[offs++]];
		dca = DCA.values()[0xff & data[offs++]];
		fadeOut = Streams.unpack16(data, offs);
		offs += 2;
		pitchPanSep = (int)data[offs++];
		pitchPanCtr = 0xff & data[offs++];
		globalVol = 0xff & data[offs++];
		defaultPan = 0x7f & data[offs];
		defaultPanEnabled = (0x80 & data[offs]) == 0;
		offs++;
		randVol = 0xff & data[offs++];
		randPan = 0xff & data[offs++];
		offs += 4;
		
		byte instname[] = Arrays.copyOfRange(data, offs, offs + 26);
		nul = Streams.arrayIndexOf(instname, (byte)0);
		if (nul == -1) {
			nul = instname.length;
		}
		instname = Arrays.copyOf(instname, nul);
		try {
			instrumentName = new String(instname, "windows-1252");
		} catch(UnsupportedEncodingException e) {
			// hopefully shouldn't happen as we are using a hardcoded encoding
			l.error("Unsupported encoding: " + e.toString());
			throw new RuntimeException(e);
		}
		
		offs += 26;
		
		filterCutoff = 0xff & data[offs++];
		filterRes = 0xff & data[offs++];
		midiChan = 0xff & data[offs++];
		midiPgm = 0xff & data[offs++];
		midiBnk = Streams.unpack16(data, offs);
		offs += 2;
		
		noteSampleMap = new ArrayList<NoteSamplePair>(120);
		for(int i = 0; i < 120; i++) {
			noteSampleMap.add(new NoteSamplePair(0xff & data[offs], 0xff & data[offs+1]));
			offs += 2;
		}
		
		volEnv = ITVolumeEnvelope.newFromData(data, offs);
		offs += 0x52;
		panEnv = ITPanEnvelope.newFromData(data, offs);
		offs += 0x52;
		pitchEnv = ITPitchEnvelope.newFromData(data, offs);
		
        return new ITInstrument(filename, instrumentName, nna, dct, dca, fadeOut, pitchPanSep, pitchPanCtr, globalVol, defaultPan, defaultPanEnabled, randVol, randPan,
                filterCutoff, filterRes, midiChan, midiPgm, midiBnk, noteSampleMap, volEnv, panEnv, pitchEnv);
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dca == null) ? 0 : dca.hashCode());
        result = prime * result + ((dct == null) ? 0 : dct.hashCode());
        result = prime * result + defaultPan;
        result = prime * result + fadeOut;
        result = prime * result
                + ((filename == null) ? 0 : filename.hashCode());
        result = prime * result + filterCutoff;
        result = prime * result + filterRes;
        result = prime * result + globalVol;
        result = prime * result
                + ((instrumentName == null) ? 0 : instrumentName.hashCode());
        result = prime * result + midiBnk;
        result = prime * result + midiChan;
        result = prime * result + midiPgm;
        result = prime * result + ((nna == null) ? 0 : nna.hashCode());
        result = prime * result
                + ((noteSampleMap == null) ? 0 : noteSampleMap.hashCode());
        result = prime * result + ((panEnv == null) ? 0 : panEnv.hashCode());
        result = prime * result
                + ((pitchEnv == null) ? 0 : pitchEnv.hashCode());
        result = prime * result + pitchPanCtr;
        result = prime * result + pitchPanSep;
        result = prime * result + randPan;
        result = prime * result + randVol;
        result = prime * result + ((volEnv == null) ? 0 : volEnv.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ITInstrument other = (ITInstrument) obj;
        if (dca != other.dca) {
            return false;
        }
        if (dct != other.dct) {
            return false;
        }
        if (defaultPan != other.defaultPan) {
            return false;
        }
        if (fadeOut != other.fadeOut) {
            return false;
        }
        if (filename == null) {
            if (other.filename != null) {
                return false;
            }
        } else if (!filename.equals(other.filename)) {
            return false;
        }
        if (filterCutoff != other.filterCutoff) {
            return false;
        }
        if (filterRes != other.filterRes) {
            return false;
        }
        if (globalVol != other.globalVol) {
            return false;
        }
        if (instrumentName == null) {
            if (other.instrumentName != null) {
                return false;
            }
        } else if (!instrumentName.equals(other.instrumentName)) {
            return false;
        }
        if (midiBnk != other.midiBnk) {
            return false;
        }
        if (midiChan != other.midiChan) {
            return false;
        }
        if (midiPgm != other.midiPgm) {
            return false;
        }
        if (nna != other.nna) {
            return false;
        }
        if (noteSampleMap == null) {
            if (other.noteSampleMap != null) {
                return false;
            }
        } else if (!noteSampleMap.equals(other.noteSampleMap)) {
            return false;
        }
        if (panEnv == null) {
            if (other.panEnv != null) {
                return false;
            }
        } else if (!panEnv.equals(other.panEnv)) {
            return false;
        }
        if (pitchEnv == null) {
            if (other.pitchEnv != null) {
                return false;
            }
        } else if (!pitchEnv.equals(other.pitchEnv)) {
            return false;
        }
        if (pitchPanCtr != other.pitchPanCtr) {
            return false;
        }
        if (pitchPanSep != other.pitchPanSep) {
            return false;
        }
        if (randPan != other.randPan) {
            return false;
        }
        if (randVol != other.randVol) {
            return false;
        }
        if (volEnv == null) {
            if (other.volEnv != null) {
                return false;
            }
        } else if (!volEnv.equals(other.volEnv)) {
            return false;
        }
        return true;
    }
	
	
}
