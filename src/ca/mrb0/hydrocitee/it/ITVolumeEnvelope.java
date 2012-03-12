package ca.mrb0.hydrocitee.it;

import ca.mrb0.hydrocitee.util.Streams;
import fj.data.List;


public class ITVolumeEnvelope extends ITEnvelope {
	public ITVolumeEnvelope() {
		super();
		nodes.set(List.<ITEnvelope.NodePoint>list(new NodePoint(64, 0), new NodePoint(64, 100)));
	}

	private ITVolumeEnvelope(ITEnvelope copy) {
		super(copy);
		freeze();
	}

	public ITVolumeEnvelope(ITVolumeEnvelope copy) {
		super(copy);
		freeze();
	}

	public static ITVolumeEnvelope newFromData(byte data[], int offs) {
		ITVolumeEnvelope env = new ITVolumeEnvelope(ITEnvelope.newFromData(
				data, offs));

		int numPoints = env.nodes.get().length();
		env.nodes.set(List.<NodePoint>list());
		for (int i = 0; i < numPoints; i++) {
			int val = 0xff & data[offs + 6 + i*3];
			int tick = Streams.unpack16(data, offs + 6 + i*3 + 1);
			
			env.nodes.set(env.nodes.get().snoc(new NodePoint(val, tick)));
		}

		env.freeze();
		return env;
	}
}
