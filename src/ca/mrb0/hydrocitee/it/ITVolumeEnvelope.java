package ca.mrb0.hydrocitee.it;


public class ITVolumeEnvelope extends ITEnvelope {
	public ITVolumeEnvelope() {
		super();
		nodes = new NodePoint[] { new NodePoint(64, 0), new NodePoint(64, 100) };
	}

	public ITVolumeEnvelope(ITEnvelope copy) {
		super(copy);
	}

	public static ITVolumeEnvelope newFromData(byte data[], int offs) {
		ITVolumeEnvelope env = new ITVolumeEnvelope(ITEnvelope.newFromData(
				data, offs));

		for (int i = 0; i < env.nodes.length; i++) {
			int val = 0xff & data[offs + 6 + i*3];
			int tick = ITModule.unpack16(data, offs + 6 + i*3 + 1);
			
			env.nodes[i] = new NodePoint(val, tick);
		}

		return env;
	}
}
