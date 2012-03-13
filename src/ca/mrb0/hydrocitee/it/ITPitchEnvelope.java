package ca.mrb0.hydrocitee.it;

import ca.mrb0.hydrocitee.util.Streams;
import fj.data.List;



public class ITPitchEnvelope extends ITEnvelope {
	
	public final boolean isFilterEnv;

    private static List<ITEnvelope.NodePoint> emptyNodeList() {
        return List.<ITEnvelope.NodePoint>list(new NodePoint(0, 0), new NodePoint(0, 100));
    }
	
	public ITPitchEnvelope() {
	    this(false, false, false, 0, 1, 0, 0, emptyNodeList(), false);
	}

	public ITPitchEnvelope(boolean enabled, boolean loop, boolean susloop,
            int loopBegin, int loopEnd, int susloopBegin, int susloopEnd,
            List<NodePoint> nodes, boolean isFilterEnv) {
        super(enabled, loop, susloop, loopBegin, loopEnd, susloopBegin, susloopEnd,
                nodes);
        
        this.isFilterEnv = isFilterEnv;
    }

	public static ITPitchEnvelope newFromData(byte data[], int offs) {
        ITEnvelope env = ITEnvelope.newFromData(data, offs);
        
        int flags = 0xff & data[offs];
        boolean isFilterEnv = (flags & 0x80) != 0;
        
        int numPoints = env.nodes.length();
        
        List<NodePoint> nodes = List.<NodePoint>list();
        for (int i = 0; i < numPoints; i++) {
            int val = data[offs + 6 + i*3];
            int tick = Streams.unpack16(data, offs + 6 + i*3 + 1);
            
            nodes = nodes.snoc(new NodePoint(val, tick));
        }

        return new ITPitchEnvelope(env.enabled, env.loop, env.susloop, env.loopBegin, env.loopEnd, env.susloopBegin, env.susloopEnd, nodes, isFilterEnv);
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (isFilterEnv ? 1231 : 1237);
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
        if (isFilterEnv != other.isFilterEnv) {
            return false;
        }
        return true;
    }
	
}
