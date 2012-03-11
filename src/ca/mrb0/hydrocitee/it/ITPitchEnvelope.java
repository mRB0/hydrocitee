package ca.mrb0.hydrocitee.it;



public class ITPitchEnvelope extends ITEnvelope {
	
	protected boolean isFilterEnv;
	
	public ITPitchEnvelope() {
		super();
		nodes = new NodePoint[] { new NodePoint(0, 0), new NodePoint(0, 100) };
	}

	public ITPitchEnvelope(ITEnvelope copy) {
		super(copy);
	}

	public static ITPitchEnvelope newFromData(byte data[], int offs) {
		ITPitchEnvelope env = new ITPitchEnvelope(ITEnvelope.newFromData(
				data, offs));

		int flags = 0xff & data[offs];
		env.isFilterEnv = (flags & 0x80) != 0;

		for (int i = 0; i < env.nodes.length; i++) {
			int val = data[offs + 6 + i*3];
			int tick = ITModule.unpack16(data, offs + 6 + i*3 + 1);
			
			env.nodes[i] = new NodePoint(val, tick);
		}

		return env;
	}
}
