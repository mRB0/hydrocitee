package ca.mrb0.hydrocitee.it;



public class ITPanEnvelope extends ITEnvelope {
	
	public ITPanEnvelope() {
		super();
		nodes = new NodePoint[] { new NodePoint(0, 0), new NodePoint(0, 100) };
	}

	public ITPanEnvelope(ITEnvelope copy) {
		super(copy);
	}
	
	public static ITPanEnvelope newFromData(byte data[], int offs) {
		ITPanEnvelope env = new ITPanEnvelope(ITEnvelope.newFromData(
				data, offs));

		for (int i = 0; i < env.nodes.length; i++) {
			int val = data[offs + 6 + i*3];
			int tick = ITModule.unpack16(data, offs + 6 + i*3 + 1);
			
			env.nodes[i] = new NodePoint(val, tick);
		}

		return env;
	}
}
