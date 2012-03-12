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
}
