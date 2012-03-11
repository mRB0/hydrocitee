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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cmwt_major;
		result = prime * result + cmwt_minor;
		result = prime * result + cwt_major;
		result = prime * result + cwt_minor;
		result = prime * result + globalVol;
		result = prime * result + (instruments ? 1231 : 1237);
		result = prime * result + (linearSlides ? 1231 : 1237);
		result = prime * result + (linkedGxx ? 1231 : 1237);
		result = prime * result + mixingVol;
		result = prime * result + (oldEffects ? 1231 : 1237);
		result = prime * result + sep;
		result = prime * result
				+ ((songname == null) ? 0 : songname.hashCode());
		result = prime * result + speed;
		result = prime * result + (stereo ? 1231 : 1237);
		result = prime * result + tempo;
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
		SongValues other = (SongValues) obj;
		if (cmwt_major != other.cmwt_major) {
			return false;
		}
		if (cmwt_minor != other.cmwt_minor) {
			return false;
		}
		if (cwt_major != other.cwt_major) {
			return false;
		}
		if (cwt_minor != other.cwt_minor) {
			return false;
		}
		if (globalVol != other.globalVol) {
			return false;
		}
		if (instruments != other.instruments) {
			return false;
		}
		if (linearSlides != other.linearSlides) {
			return false;
		}
		if (linkedGxx != other.linkedGxx) {
			return false;
		}
		if (mixingVol != other.mixingVol) {
			return false;
		}
		if (oldEffects != other.oldEffects) {
			return false;
		}
		if (sep != other.sep) {
			return false;
		}
		if (songname == null) {
			if (other.songname != null) {
				return false;
			}
		} else if (!songname.equals(other.songname)) {
			return false;
		}
		if (speed != other.speed) {
			return false;
		}
		if (stereo != other.stereo) {
			return false;
		}
		if (tempo != other.tempo) {
			return false;
		}
		return true;
	}
}
