package ca.mrb0.hydrocitee.it;

import ca.mrb0.hydrocitee.util.Streams;
import fj.data.List;



public class ITPanEnvelope extends ITEnvelope {
	
    private static List<ITEnvelope.NodePoint> emptyNodeList() {
        return List.<ITEnvelope.NodePoint>list(new NodePoint(64, 0), new NodePoint(64, 100));
    }

    public ITPanEnvelope() {
        this(false, false, false, 0, 1, 0, 0, emptyNodeList());
    }
    
    public ITPanEnvelope(boolean enabled, boolean loop, boolean susloop,
            int loopBegin, int loopEnd, int susloopBegin, int susloopEnd,
            List<NodePoint> nodes) {
        super(enabled, loop, susloop, loopBegin, loopEnd, susloopBegin, susloopEnd,
                nodes);
    }

    public static ITPanEnvelope newFromData(byte data[], int offs) {
        ITEnvelope env = ITEnvelope.newFromData(data, offs);
        
        int numPoints = env.nodes.length();
        
        List<NodePoint> nodes = List.<NodePoint>list();
        for (int i = 0; i < numPoints; i++) {
            int val = data[offs + 6 + i*3];
            int tick = Streams.unpack16(data, offs + 6 + i*3 + 1);
            
            nodes = nodes.snoc(new NodePoint(val, tick));
        }

        return new ITPanEnvelope(env.enabled, env.loop, env.susloop, env.loopBegin, env.loopEnd, env.susloopBegin, env.susloopEnd, nodes);
    }
}
