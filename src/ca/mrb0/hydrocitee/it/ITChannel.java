package ca.mrb0.hydrocitee.it;

public class ITChannel {
	public final int pan, vol;

	public ITChannel(int pan, int vol) {
		super();
		this.pan = pan;
		this.vol = vol;
	}
	
	public ITChannel(ITChannel copy) {
		super();
		this.pan = copy.pan;
		this.vol = copy.vol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pan;
		result = prime * result + vol;
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
		ITChannel other = (ITChannel) obj;
		if (pan != other.pan) {
			return false;
		}
		if (vol != other.vol) {
			return false;
		}
		return true;
	}
	
	
}
