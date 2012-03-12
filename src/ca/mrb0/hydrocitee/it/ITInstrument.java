package ca.mrb0.hydrocitee.it;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import ca.mrb0.hydrocitee.util.Prop;
import fj.data.List;

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
	
	// TODO: Add tests to these so we can prevent invalid values
	public final Prop<String> filename = new Prop<String>("");
	public final Prop<String> instrumentName = new Prop<String>("");
	
	public final Prop<NNA> nna = new Prop<NNA>(NNA.Cut);
	public final Prop<DCT> dct = new Prop<DCT>(DCT.Off);
	public final Prop<DCA> dca = new Prop<DCA>(DCA.Cut);
	
	public final Prop<Integer> fadeOut = new Prop<Integer>(0);
	public final Prop<Integer> pitchPanSep = new Prop<Integer>(0);
	public final Prop<Integer> pitchPanCtr = new Prop<Integer>(0); // TODO: figure out the note for C-5 or whatever
	public final Prop<Integer> globalVol = new Prop<Integer>(128);
	public final Prop<Integer> defaultPan = new Prop<Integer>(0x80); // 0x80 = off
	public final Prop<Integer> randVol = new Prop<Integer>(0);
	public final Prop<Integer> randPan = new Prop<Integer>(0); // not implemented in IT?
	
	public final Prop<Integer> filterCutoff = new Prop<Integer>(255); // TODO: find correct values
	public final Prop<Integer> filterRes = new Prop<Integer>(0);
	
	public final Prop<Integer> midiChan = new Prop<Integer>(0);
	public final Prop<Integer> midiPgm = new Prop<Integer>(0);
	public final Prop<Integer> midiBnk = new Prop<Integer>(0);
	
	public final Prop<List<NoteSamplePair>> noteSampleMap = new Prop<List<NoteSamplePair>>(List.<NoteSamplePair>list());
	
	public final Prop<ITVolumeEnvelope> volEnv = new Prop<ITVolumeEnvelope>();
	public final Prop<ITPanEnvelope> panEnv = new Prop<ITPanEnvelope>();
	public final Prop<ITPitchEnvelope> pitchEnv = new Prop<ITPitchEnvelope>();
	
	public static class NoteSamplePair {
		public final int targetNote;
		public final int sample;
		
		public NoteSamplePair(int note, int sample) {
			super();
			this.targetNote = note;
			this.sample = sample;
		}
	}
	
	public ITInstrument() {
		super();
		for(int i = 0; i < 120; i++) {
			noteSampleMap.set(noteSampleMap.get().snoc(new NoteSamplePair(i, 0)));
		}
	}
	
	public static ITInstrument newFromData(byte data[], int offs) {
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
			inst.filename.set(new String(filename, "windows-1252"));
		} catch(UnsupportedEncodingException e) {
			// hopefully shouldn't happen as we are using a hardcoded encoding
			l.error("Unsupported encoding: " + e.toString());
			throw new RuntimeException(e);
		}
		
		offs += 12;
		offs++;
		inst.nna.set(NNA.values()[0xff & data[offs++]]);
		inst.dct.set(DCT.values()[0xff & data[offs++]]);
		inst.dca.set(DCA.values()[0xff & data[offs++]]);
		inst.fadeOut.set(ITModule.unpack16(data, offs));
		offs += 2;
		inst.pitchPanSep.set((int)data[offs++]);
		inst.pitchPanCtr.set(0xff & data[offs++]);
		inst.globalVol.set(0xff & data[offs++]);
		inst.defaultPan.set(0xff & data[offs++]);
		inst.randVol.set(0xff & data[offs++]);
		inst.randPan.set(0xff & data[offs++]);
		offs += 4;
		
		byte instname[] = Arrays.copyOfRange(data, offs, offs + 26);
		nul = ITModule.arrayIndexOf(instname, (byte)0);
		if (nul == -1) {
			nul = instname.length;
		}
		instname = Arrays.copyOf(instname, nul);
		try {
			inst.instrumentName.set(new String(instname, "windows-1252"));
		} catch(UnsupportedEncodingException e) {
			// hopefully shouldn't happen as we are using a hardcoded encoding
			l.error("Unsupported encoding: " + e.toString());
			throw new RuntimeException(e);
		}
		
		offs += 26;
		
		inst.filterCutoff.set(0xff & data[offs++]);
		inst.filterRes.set(0xff & data[offs++]);
		inst.midiChan.set(0xff & data[offs++]);
		inst.midiPgm.set(0xff & data[offs++]);
		inst.midiBnk.set(ITModule.unpack16(data, offs));
		offs += 2;
		
		inst.noteSampleMap.set(List.<NoteSamplePair>list());
		for(int i = 0; i < 120; i++) {
			inst.noteSampleMap.set(inst.noteSampleMap.get().snoc(new NoteSamplePair(0xff & data[offs], 0xff & data[offs+1])));
			offs += 2;
		}
		
		inst.volEnv.set(ITVolumeEnvelope.newFromData(data, offs));
		offs += 0x52;
		inst.panEnv.set(ITPanEnvelope.newFromData(data, offs));
		offs += 0x52;
		inst.pitchEnv.set(ITPitchEnvelope.newFromData(data, offs));
		
		inst.freeze();
		
		return inst;
	}
	
	private void freeze() {
		filename.freeze();
		instrumentName.freeze();
		
		nna.freeze();
		dct.freeze();
		dca.freeze();
		
		fadeOut.freeze();
		pitchPanSep.freeze();
		pitchPanCtr.freeze();
		globalVol.freeze();
		defaultPan.freeze();
		randVol.freeze();
		randPan.freeze();
		
		filterCutoff.freeze();
		filterRes.freeze();
		
		midiChan.freeze();
		midiPgm.freeze();
		midiBnk.freeze();
		
		noteSampleMap.freeze();
		
		volEnv.freeze();
		panEnv.freeze();
		pitchEnv.freeze();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dca == null) ? 0 : dca.hashCode());
		result = prime * result + ((dct == null) ? 0 : dct.hashCode());
		result = prime * result
				+ ((defaultPan == null) ? 0 : defaultPan.hashCode());
		result = prime * result + ((fadeOut == null) ? 0 : fadeOut.hashCode());
		result = prime * result
				+ ((filename == null) ? 0 : filename.hashCode());
		result = prime * result
				+ ((filterCutoff == null) ? 0 : filterCutoff.hashCode());
		result = prime * result
				+ ((filterRes == null) ? 0 : filterRes.hashCode());
		result = prime * result
				+ ((globalVol == null) ? 0 : globalVol.hashCode());
		result = prime * result
				+ ((instrumentName == null) ? 0 : instrumentName.hashCode());
		result = prime * result + ((midiBnk == null) ? 0 : midiBnk.hashCode());
		result = prime * result
				+ ((midiChan == null) ? 0 : midiChan.hashCode());
		result = prime * result + ((midiPgm == null) ? 0 : midiPgm.hashCode());
		result = prime * result + ((nna == null) ? 0 : nna.hashCode());
		result = prime * result
				+ ((noteSampleMap == null) ? 0 : noteSampleMap.hashCode());
		result = prime * result + ((panEnv == null) ? 0 : panEnv.hashCode());
		result = prime * result
				+ ((pitchEnv == null) ? 0 : pitchEnv.hashCode());
		result = prime * result
				+ ((pitchPanCtr == null) ? 0 : pitchPanCtr.hashCode());
		result = prime * result
				+ ((pitchPanSep == null) ? 0 : pitchPanSep.hashCode());
		result = prime * result + ((randPan == null) ? 0 : randPan.hashCode());
		result = prime * result + ((randVol == null) ? 0 : randVol.hashCode());
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
		if (dca == null) {
			if (other.dca != null) {
				return false;
			}
		} else if (!dca.equals(other.dca)) {
			return false;
		}
		if (dct == null) {
			if (other.dct != null) {
				return false;
			}
		} else if (!dct.equals(other.dct)) {
			return false;
		}
		if (defaultPan == null) {
			if (other.defaultPan != null) {
				return false;
			}
		} else if (!defaultPan.equals(other.defaultPan)) {
			return false;
		}
		if (fadeOut == null) {
			if (other.fadeOut != null) {
				return false;
			}
		} else if (!fadeOut.equals(other.fadeOut)) {
			return false;
		}
		if (filename == null) {
			if (other.filename != null) {
				return false;
			}
		} else if (!filename.equals(other.filename)) {
			return false;
		}
		if (filterCutoff == null) {
			if (other.filterCutoff != null) {
				return false;
			}
		} else if (!filterCutoff.equals(other.filterCutoff)) {
			return false;
		}
		if (filterRes == null) {
			if (other.filterRes != null) {
				return false;
			}
		} else if (!filterRes.equals(other.filterRes)) {
			return false;
		}
		if (globalVol == null) {
			if (other.globalVol != null) {
				return false;
			}
		} else if (!globalVol.equals(other.globalVol)) {
			return false;
		}
		if (instrumentName == null) {
			if (other.instrumentName != null) {
				return false;
			}
		} else if (!instrumentName.equals(other.instrumentName)) {
			return false;
		}
		if (midiBnk == null) {
			if (other.midiBnk != null) {
				return false;
			}
		} else if (!midiBnk.equals(other.midiBnk)) {
			return false;
		}
		if (midiChan == null) {
			if (other.midiChan != null) {
				return false;
			}
		} else if (!midiChan.equals(other.midiChan)) {
			return false;
		}
		if (midiPgm == null) {
			if (other.midiPgm != null) {
				return false;
			}
		} else if (!midiPgm.equals(other.midiPgm)) {
			return false;
		}
		if (nna == null) {
			if (other.nna != null) {
				return false;
			}
		} else if (!nna.equals(other.nna)) {
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
		if (pitchPanCtr == null) {
			if (other.pitchPanCtr != null) {
				return false;
			}
		} else if (!pitchPanCtr.equals(other.pitchPanCtr)) {
			return false;
		}
		if (pitchPanSep == null) {
			if (other.pitchPanSep != null) {
				return false;
			}
		} else if (!pitchPanSep.equals(other.pitchPanSep)) {
			return false;
		}
		if (randPan == null) {
			if (other.randPan != null) {
				return false;
			}
		} else if (!randPan.equals(other.randPan)) {
			return false;
		}
		if (randVol == null) {
			if (other.randVol != null) {
				return false;
			}
		} else if (!randVol.equals(other.randVol)) {
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
