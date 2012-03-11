package ca.mrb0.hydrocitee.it;

public class ITEnvelope {
	protected boolean enabled;
	protected boolean loop;
	protected boolean susloop;
	
	protected int loopBegin;
	protected int loopEnd;
	protected int susloopBegin;
	protected int susloopEnd;
	
	protected NodePoint nodes[];
	
	ITEnvelope() {
		
	}

	ITEnvelope(ITEnvelope copy) {
		enabled = copy.enabled;
		loop = copy.loop;
		susloop = copy.susloop;

		loopBegin = copy.loopBegin;
		loopEnd = copy.loopEnd;
		susloopBegin = copy.susloopBegin;
		susloopEnd = copy.susloopEnd;

		nodes = copy.nodes;
	}
	
	static ITEnvelope newFromData(byte data[], int offs) {
		ITEnvelope env = new ITEnvelope();
		
		int flags = 0xff & data[offs++];
		env.enabled = (flags & 0x1) != 0;
		env.loop = (flags & 0x2) != 0;
		env.susloop = (flags & 0x3) != 0;
		
		env.nodes = new NodePoint[0xff & data[offs++]]; // numPoints
		
		env.loopBegin = 0xff & data[offs++];
		env.loopEnd = 0xff & data[offs++];
		env.susloopBegin = 0xff & data[offs++];
		env.susloopEnd = 0xff & data[offs++];
		
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
}
