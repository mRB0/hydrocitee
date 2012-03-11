package ca.mrb0.hydrocitee.it;

import java.util.Arrays;

public class SongValues {
	public String songname;
	
	public int cwt_major, cwt_minor;
	public int cmwt_major, cmwt_minor;
	
	// flags (not all represented)
	public boolean stereo;
	public boolean linearSlides;
	public boolean oldEffects;
	public boolean linkedGxx;
	
	public int globalVol;
	public int mixingVol;
	public int speed;
	public int tempo;
	public int sep;
	public int msglen;
	public long msgoffs;
	
	public int channelPan[] = new int[64];
	public int channelVol[] = new int[64];
	
	public int orders[];

	public SongValues() {
		super();
	}
	
	public SongValues(String songname, int cwt_major, int cwt_minor,
			int cmwt_major, int cmwt_minor, boolean stereo,
			boolean linearSlides, boolean oldEffects, boolean linkedGxx,
			int globalVol, int mixingVol, int speed, int tempo, int sep,
			int msglen, long msgoffs, int[] channelPan, int[] channelVol,
			int[] orders) {
		super();
		this.songname = songname;
		this.cwt_major = cwt_major;
		this.cwt_minor = cwt_minor;
		this.cmwt_major = cmwt_major;
		this.cmwt_minor = cmwt_minor;
		this.stereo = stereo;
		this.linearSlides = linearSlides;
		this.oldEffects = oldEffects;
		this.linkedGxx = linkedGxx;
		this.globalVol = globalVol;
		this.mixingVol = mixingVol;
		this.speed = speed;
		this.tempo = tempo;
		this.sep = sep;
		this.msglen = msglen;
		this.msgoffs = msgoffs;
		this.channelPan = channelPan;
		this.channelVol = channelVol;
		this.orders = orders;
	}

	public SongValues(SongValues copy) {
		super();
		this.songname = copy.songname;
		this.cwt_major = copy.cwt_major;
		this.cwt_minor = copy.cwt_minor;
		this.cmwt_major = copy.cmwt_major;
		this.cmwt_minor = copy.cmwt_minor;
		this.stereo = copy.stereo;
		this.linearSlides = copy.linearSlides;
		this.oldEffects = copy.oldEffects;
		this.linkedGxx = copy.linkedGxx;
		this.globalVol = copy.globalVol;
		this.mixingVol = copy.mixingVol;
		this.speed = copy.speed;
		this.tempo = copy.tempo;
		this.sep = copy.sep;
		this.msglen = copy.msglen;
		this.msgoffs = copy.msgoffs;
		this.channelPan = Arrays.copyOf(copy.channelPan, copy.channelPan.length);
		this.channelVol = Arrays.copyOf(copy.channelVol, copy.channelVol.length);
		this.orders = Arrays.copyOf(copy.orders, copy.orders.length);
	}
}
