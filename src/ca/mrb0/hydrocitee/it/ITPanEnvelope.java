package ca.mrb0.hydrocitee.it;

import fj.data.List;



public class ITPanEnvelope extends ITEnvelope {
	
	public ITPanEnvelope() {
		super();
		nodes.set(List.<ITEnvelope.NodePoint>list(new NodePoint(0, 0), new NodePoint(0, 100)));
	}

	public ITPanEnvelope(ITEnvelope copy) {
		super(copy);
	}
	
	public static ITPanEnvelope newFromData(byte data[], int offs) {
		ITPanEnvelope env = new ITPanEnvelope(ITEnvelope.newFromData(
				data, offs));

		int numPoints = env.nodes.get().length();
		env.nodes.set(List.<NodePoint>list());
		for (int i = 0; i < numPoints; i++) {
			int val = data[offs + 6 + i*3];
			int tick = ITModule.unpack16(data, offs + 6 + i*3 + 1);
			
			env.nodes.set(env.nodes.get().snoc(new NodePoint(val, tick)));
		}

		env.freeze();

		return env;
	}
}
