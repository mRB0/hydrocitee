package ca.mrb0.hydrocitee.it;

import ca.mrb0.hydrocitee.util.Prop;
import fj.data.List;

public class ITEnvelope {
	public final Prop<Boolean> enabled = new Prop<Boolean>();
	public final Prop<Boolean> loop = new Prop<Boolean>();
	public final Prop<Boolean> susloop = new Prop<Boolean>();
	
	public final Prop<Integer> loopBegin = new Prop<Integer>();
	public final Prop<Integer> loopEnd = new Prop<Integer>();
	public final Prop<Integer> susloopBegin = new Prop<Integer>();
	public final Prop<Integer> susloopEnd = new Prop<Integer>();
	
	public final Prop<List<NodePoint>> nodes = new Prop<List<NodePoint>>(List.<NodePoint>list());
	
	protected ITEnvelope() {
		
	}

	protected ITEnvelope(ITEnvelope copy) {
		enabled.set(copy.enabled.get());
		loop.set(copy.loop.get());
		susloop.set(copy.susloop.get());

		loopBegin.set(copy.loopBegin.get());
		loopEnd.set(copy.loopEnd.get());
		susloopBegin.set(copy.susloopBegin.get());
		susloopEnd.set(copy.susloopEnd.get());

		nodes.set(copy.nodes.get());
	}
	
	protected static ITEnvelope newFromData(byte data[], int offs) {
		ITEnvelope env = new ITEnvelope();
		
		int flags = 0xff & data[offs++];
		env.enabled.set((flags & 0x1) != 0);
		env.loop.set((flags & 0x2) != 0);
		env.susloop.set((flags & 0x3) != 0);
		
		int numPoints = 0xff & data[offs++];
		env.nodes.set(List.<NodePoint>replicate(numPoints, null));
		
		env.loopBegin.set(0xff & data[offs++]);
		env.loopEnd.set(0xff & data[offs++]);
		env.susloopBegin.set(0xff & data[offs++]);
		env.susloopEnd.set(0xff & data[offs++]);
		
		return env;
	}

	public static class NodePoint {
		public final int val;
		public final int tick;

		public NodePoint(int val, int tick) {
			super();
			this.val = val;
			this.tick = tick;
		}
	}
	
	protected void freeze() {
		enabled.freeze();
		loop.freeze();
		susloop.freeze();

		loopBegin.freeze();
		loopEnd.freeze();
		susloopBegin.freeze();
		susloopEnd.freeze();

		nodes.freeze();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((loop == null) ? 0 : loop.hashCode());
		result = prime * result
				+ ((loopBegin == null) ? 0 : loopBegin.hashCode());
		result = prime * result + ((loopEnd == null) ? 0 : loopEnd.hashCode());
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		result = prime * result + ((susloop == null) ? 0 : susloop.hashCode());
		result = prime * result
				+ ((susloopBegin == null) ? 0 : susloopBegin.hashCode());
		result = prime * result
				+ ((susloopEnd == null) ? 0 : susloopEnd.hashCode());
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
		ITEnvelope other = (ITEnvelope) obj;
		if (enabled == null) {
			if (other.enabled != null) {
				return false;
			}
		} else if (!enabled.equals(other.enabled)) {
			return false;
		}
		if (loop == null) {
			if (other.loop != null) {
				return false;
			}
		} else if (!loop.equals(other.loop)) {
			return false;
		}
		if (loopBegin == null) {
			if (other.loopBegin != null) {
				return false;
			}
		} else if (!loopBegin.equals(other.loopBegin)) {
			return false;
		}
		if (loopEnd == null) {
			if (other.loopEnd != null) {
				return false;
			}
		} else if (!loopEnd.equals(other.loopEnd)) {
			return false;
		}
		if (nodes == null) {
			if (other.nodes != null) {
				return false;
			}
		} else if (!nodes.equals(other.nodes)) {
			return false;
		}
		if (susloop == null) {
			if (other.susloop != null) {
				return false;
			}
		} else if (!susloop.equals(other.susloop)) {
			return false;
		}
		if (susloopBegin == null) {
			if (other.susloopBegin != null) {
				return false;
			}
		} else if (!susloopBegin.equals(other.susloopBegin)) {
			return false;
		}
		if (susloopEnd == null) {
			if (other.susloopEnd != null) {
				return false;
			}
		} else if (!susloopEnd.equals(other.susloopEnd)) {
			return false;
		}
		return true;
	}
	
}
