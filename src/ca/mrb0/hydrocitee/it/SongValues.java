package ca.mrb0.hydrocitee.it;


public class SongValues {
	public String songname;
	
	public int cwt_major, cwt_minor;
	public int cmwt_major, cmwt_minor;
	
	// flags (not all represented)
	public boolean stereo;
	public boolean instruments;
	public boolean linearSlides;
	public boolean oldEffects;
	public boolean linkedGxx;
	
	public int globalVol;
	public int mixingVol;
	public int speed;
	public int tempo;
	public int sep;
	
	public SongValues() {
		super();
	}
	
	
	public SongValues(String songname, int cwt_major, int cwt_minor,
			int cmwt_major, int cmwt_minor, boolean stereo,
			boolean instruments,
			boolean linearSlides, boolean oldEffects, boolean linkedGxx,
			int globalVol, int mixingVol, int speed, int tempo, int sep) {
		super();
		this.songname = songname;
		this.cwt_major = cwt_major;
		this.cwt_minor = cwt_minor;
		this.cmwt_major = cmwt_major;
		this.cmwt_minor = cmwt_minor;
		this.stereo = stereo;
		this.instruments = instruments;
		this.linearSlides = linearSlides;
		this.oldEffects = oldEffects;
		this.linkedGxx = linkedGxx;
		this.globalVol = globalVol;
		this.mixingVol = mixingVol;
		this.speed = speed;
		this.tempo = tempo;
		this.sep = sep;
	}


	public SongValues(SongValues copy) {
		super();
		this.songname = copy.songname;
		this.cwt_major = copy.cwt_major;
		this.cwt_minor = copy.cwt_minor;
		this.cmwt_major = copy.cmwt_major;
		this.cmwt_minor = copy.cmwt_minor;
		this.stereo = copy.stereo;
		this.instruments = copy.instruments;
		this.linearSlides = copy.linearSlides;
		this.oldEffects = copy.oldEffects;
		this.linkedGxx = copy.linkedGxx;
		this.globalVol = copy.globalVol;
		this.mixingVol = copy.mixingVol;
		this.speed = copy.speed;
		this.tempo = copy.tempo;
		this.sep = copy.sep;
	}
}
