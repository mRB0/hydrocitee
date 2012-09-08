package ca.mrb0.hydrocitee.it;

import java.util.ArrayList;
import java.util.List;

import ca.mrb0.hydrocitee.util.Streams;

import com.google.common.collect.ImmutableList;



public class ITPanEnvelope extends ITEnvelope {
    
    private static List<ITEnvelope.NodePoint> emptyNodeList() {
        return ImmutableList.of(new NodePoint(0, 0), new NodePoint(0, 100));
    }

    public ITPanEnvelope() {
        this(false, false, false, 0, 1, 0, 0, ImmutableList.copyOf(emptyNodeList()));
    }
    
    public ITPanEnvelope(boolean enabled, boolean loop, boolean susloop,
            int loopBegin, int loopEnd, int susloopBegin, int susloopEnd,
            List<NodePoint> nodes) {
        super(enabled, loop, susloop, loopBegin, loopEnd, susloopBegin, susloopEnd,
                nodes);
    }

    public static ITPanEnvelope newFromData(byte[] data, int offs) {
        return (ITPanEnvelope) ITEnvelope.newEnvelopeFromData(data, offs, new Constructor(data, offs));
    }

    private static final class Constructor implements EnvelopeConstructor {
        private final byte[] data;
        private final int offs;
        
        protected Constructor(byte[] data, int offs) {
            super();
            this.data = data;
            this.offs = offs;
        }

        @Override
        public List<NodePoint> loadNodes(int count) {
            List<NodePoint> nodes = new ArrayList<NodePoint>(count);
            for (int i = 0; i < count; i++) {
                int val = data[offs + 6 + i*3];
                int tick = Streams.unpack16(data, offs + 6 + i*3 + 1);
                
                nodes.add(new NodePoint(val, tick));
            }
            return nodes;
        }
        
        @Override
        public ITPanEnvelope construct(boolean enabled, boolean loop,
                boolean susloop, int loopBegin, int loopEnd, int susloopBegin,
                int susloopEnd, List<NodePoint> nodes) {
            
            return new ITPanEnvelope(enabled, loop, susloop, loopBegin, loopEnd, susloopBegin, susloopEnd, nodes);
        }
    };
}
