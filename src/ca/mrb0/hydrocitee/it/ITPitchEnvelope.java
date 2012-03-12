package ca.mrb0.hydrocitee.it;

import ca.mrb0.hydrocitee.util.Prop;
import ca.mrb0.hydrocitee.util.Streams;
import fj.data.List;



public class ITPitchEnvelope extends ITEnvelope {
	
	protected final Prop<Boolean> isFilterEnv = new Prop<Boolean>(false);
	
	public ITPitchEnvelope() {
		super();
		nodes.set(List.<ITEnvelope.NodePoint>list(new NodePoint(0, 0), new NodePoint(0, 100)));
	}

	private ITPitchEnvelope(ITEnvelope copy) {
		super(copy);
		freeze();
	}

	public ITPitchEnvelope(ITPitchEnvelope copy) {
		super(copy);
		isFilterEnv.set(copy.isFilterEnv.get());
		freeze();
	}

	public static ITPitchEnvelope newFromData(byte data[], int offs) {
		ITPitchEnvelope env = new ITPitchEnvelope(ITEnvelope.newFromData(
				data, offs));

		int flags = 0xff & data[offs];
		env.isFilterEnv.set((flags & 0x80) != 0);

		int numPoints = env.nodes.get().length();
		env.nodes.set(List.<NodePoint>list());
		for (int i = 0; i < numPoints; i++) {
			int val = data[offs + 6 + i*3];
			int tick = Streams.unpack16(data, offs + 6 + i*3 + 1);
			
			env.nodes.set(env.nodes.get().snoc(new NodePoint(val, tick)));
		}

		env.freeze();

		return env;
	}
	
	@Override
	protected void freeze() {
		super.freeze();
		isFilterEnv.freeze();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((isFilterEnv == null) ? 0 : isFilterEnv.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ITPitchEnvelope other = (ITPitchEnvelope) obj;
		if (isFilterEnv == null) {
			if (other.isFilterEnv != null) {
				return false;
			}
		} else if (!isFilterEnv.equals(other.isFilterEnv)) {
			return false;
		}
		return true;
	}
	
	
}
