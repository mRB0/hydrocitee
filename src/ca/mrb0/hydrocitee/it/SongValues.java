package ca.mrb0.hydrocitee.it;

import ca.mrb0.hydrocitee.util.Prop;


public class SongValues {
	public final Prop<String> songname = new Prop<String>("");
	
	public final Prop<Integer> cwt_major = new Prop<Integer>(2), cwt_minor = new Prop<Integer>(14);
	public final Prop<Integer> cmwt_major = new Prop<Integer>(2), cmwt_minor = new Prop<Integer>(11);
	
	// flags (not all represented)
	public final Prop<Boolean> stereo = new Prop<Boolean>(true);
	public final Prop<Boolean> instruments = new Prop<Boolean>(true);
	public final Prop<Boolean> linearSlides = new Prop<Boolean>(true);
	public final Prop<Boolean> oldEffects = new Prop<Boolean>(false);
	public final Prop<Boolean> linkedGxx = new Prop<Boolean>(false);
	
	public final Prop<Integer> globalVol = new Prop<Integer>(128);
	public final Prop<Integer> mixingVol = new Prop<Integer>(48);
	public final Prop<Integer> speed = new Prop<Integer>(6);
	public final Prop<Integer> tempo = new Prop<Integer>(125);
	public final Prop<Integer> sep = new Prop<Integer>(128);
	
	public SongValues() {
		super();
	}
	
	public SongValues(String songname, int cwt_major, int cwt_minor,
			int cmwt_major, int cmwt_minor, boolean stereo,
			boolean instruments,
			boolean linearSlides, boolean oldEffects, boolean linkedGxx,
			int globalVol, int mixingVol, int speed, int tempo, int sep) {
		super();
		this.songname.set(songname);
		this.cwt_major.set(cwt_major);
		this.cwt_minor.set(cwt_minor);
		this.cmwt_major.set(cmwt_major);
		this.cmwt_minor.set(cmwt_minor);
		this.stereo.set(stereo);
		this.instruments.set(instruments);
		this.linearSlides.set(linearSlides);
		this.oldEffects.set(oldEffects);
		this.linkedGxx.set(linkedGxx);
		this.globalVol.set(globalVol);
		this.mixingVol.set(mixingVol);
		this.speed.set(speed);
		this.tempo.set(tempo);
		this.sep.set(sep);
		
		freeze();
	}


	public SongValues(SongValues copy) {
		super();
		this.songname.set(copy.songname.get());
		this.cwt_major.set(copy.cwt_major.get());
		this.cwt_minor.set(copy.cwt_minor.get());
		this.cmwt_major.set(copy.cmwt_major.get());
		this.cmwt_minor.set(copy.cmwt_minor.get());
		this.stereo.set(copy.stereo.get());
		this.instruments.set(copy.instruments.get());
		this.linearSlides.set(copy.linearSlides.get());
		this.oldEffects.set(copy.oldEffects.get());
		this.linkedGxx.set(copy.linkedGxx.get());
		this.globalVol.set(copy.globalVol.get());
		this.mixingVol.set(copy.mixingVol.get());
		this.speed.set(copy.speed.get());
		this.tempo.set(copy.tempo.get());
		this.sep.set(copy.sep.get());
	}


	private void freeze() {
		this.songname.freeze();
		this.cwt_major.freeze();
		this.cwt_minor.freeze();
		this.cmwt_major.freeze();
		this.cmwt_minor.freeze();
		this.stereo.freeze();
		this.instruments.freeze();
		this.linearSlides.freeze();
		this.oldEffects.freeze();
		this.linkedGxx.freeze();
		this.globalVol.freeze();
		this.mixingVol.freeze();
		this.speed.freeze();
		this.tempo.freeze();
		this.sep.freeze();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cmwt_major == null) ? 0 : cmwt_major.hashCode());
		result = prime * result
				+ ((cmwt_minor == null) ? 0 : cmwt_minor.hashCode());
		result = prime * result
				+ ((cwt_major == null) ? 0 : cwt_major.hashCode());
		result = prime * result
				+ ((cwt_minor == null) ? 0 : cwt_minor.hashCode());
		result = prime * result
				+ ((globalVol == null) ? 0 : globalVol.hashCode());
		result = prime * result
				+ ((instruments == null) ? 0 : instruments.hashCode());
		result = prime * result
				+ ((linearSlides == null) ? 0 : linearSlides.hashCode());
		result = prime * result
				+ ((linkedGxx == null) ? 0 : linkedGxx.hashCode());
		result = prime * result
				+ ((mixingVol == null) ? 0 : mixingVol.hashCode());
		result = prime * result
				+ ((oldEffects == null) ? 0 : oldEffects.hashCode());
		result = prime * result + ((sep == null) ? 0 : sep.hashCode());
		result = prime * result
				+ ((songname == null) ? 0 : songname.hashCode());
		result = prime * result + ((speed == null) ? 0 : speed.hashCode());
		result = prime * result + ((stereo == null) ? 0 : stereo.hashCode());
		result = prime * result + ((tempo == null) ? 0 : tempo.hashCode());
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
		if (cmwt_major == null) {
			if (other.cmwt_major != null) {
				return false;
			}
		} else if (!cmwt_major.equals(other.cmwt_major)) {
			return false;
		}
		if (cmwt_minor == null) {
			if (other.cmwt_minor != null) {
				return false;
			}
		} else if (!cmwt_minor.equals(other.cmwt_minor)) {
			return false;
		}
		if (cwt_major == null) {
			if (other.cwt_major != null) {
				return false;
			}
		} else if (!cwt_major.equals(other.cwt_major)) {
			return false;
		}
		if (cwt_minor == null) {
			if (other.cwt_minor != null) {
				return false;
			}
		} else if (!cwt_minor.equals(other.cwt_minor)) {
			return false;
		}
		if (globalVol == null) {
			if (other.globalVol != null) {
				return false;
			}
		} else if (!globalVol.equals(other.globalVol)) {
			return false;
		}
		if (instruments == null) {
			if (other.instruments != null) {
				return false;
			}
		} else if (!instruments.equals(other.instruments)) {
			return false;
		}
		if (linearSlides == null) {
			if (other.linearSlides != null) {
				return false;
			}
		} else if (!linearSlides.equals(other.linearSlides)) {
			return false;
		}
		if (linkedGxx == null) {
			if (other.linkedGxx != null) {
				return false;
			}
		} else if (!linkedGxx.equals(other.linkedGxx)) {
			return false;
		}
		if (mixingVol == null) {
			if (other.mixingVol != null) {
				return false;
			}
		} else if (!mixingVol.equals(other.mixingVol)) {
			return false;
		}
		if (oldEffects == null) {
			if (other.oldEffects != null) {
				return false;
			}
		} else if (!oldEffects.equals(other.oldEffects)) {
			return false;
		}
		if (sep == null) {
			if (other.sep != null) {
				return false;
			}
		} else if (!sep.equals(other.sep)) {
			return false;
		}
		if (songname == null) {
			if (other.songname != null) {
				return false;
			}
		} else if (!songname.equals(other.songname)) {
			return false;
		}
		if (speed == null) {
			if (other.speed != null) {
				return false;
			}
		} else if (!speed.equals(other.speed)) {
			return false;
		}
		if (stereo == null) {
			if (other.stereo != null) {
				return false;
			}
		} else if (!stereo.equals(other.stereo)) {
			return false;
		}
		if (tempo == null) {
			if (other.tempo != null) {
				return false;
			}
		} else if (!tempo.equals(other.tempo)) {
			return false;
		}
		return true;
	}
	
	
}
