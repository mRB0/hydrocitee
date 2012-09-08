package ca.mrb0.hydrocitee.it;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.collect.ImmutableList;

public abstract class ITEnvelope {
    public final boolean enabled;
    public final boolean loop;
    public final boolean susloop;
    
    public final int loopBegin;
    public final int loopEnd;
    public final int susloopBegin;
    public final int susloopEnd;
    
    public final List<NodePoint> nodes;

    protected ITEnvelope(boolean enabled, boolean loop, boolean susloop,
            int loopBegin, int loopEnd, int susloopBegin, int susloopEnd,
            List<NodePoint> nodes) {
        super();
        this.enabled = enabled;
        this.loop = loop;
        this.susloop = susloop;
        this.loopBegin = loopBegin;
        this.loopEnd = loopEnd;
        this.susloopBegin = susloopBegin;
        this.susloopEnd = susloopEnd;
        this.nodes = ImmutableList.copyOf(nodes);
    }

    protected static ITEnvelope newEnvelopeFromData(byte[] data, int offs, EnvelopeConstructor ctor) {
        boolean enabled;
        boolean loop;
        boolean susloop;
        
        int loopBegin;
        int loopEnd;
        int susloopBegin;
        int susloopEnd;
        
        List<NodePoint> nodes;
        
        
        int flags = 0xff & data[offs++];
        enabled = (flags & 0x1) != 0;
        loop = (flags & 0x2) != 0;
        susloop = (flags & 0x3) != 0;
        
        int numPoints = 0xff & data[offs++];
        
        nodes = ctor.loadNodes(numPoints);
        
        loopBegin = 0xff & data[offs++];
        loopEnd = 0xff & data[offs++];
        susloopBegin = 0xff & data[offs++];
        susloopEnd = 0xff & data[offs++];
        
        return ctor.construct(enabled, loop, susloop, loopBegin, loopEnd, susloopBegin, susloopEnd, nodes);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + (loop ? 1231 : 1237);
        result = prime * result + loopBegin;
        result = prime * result + loopEnd;
        result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
        result = prime * result + (susloop ? 1231 : 1237);
        result = prime * result + susloopBegin;
        result = prime * result + susloopEnd;
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
        if (enabled != other.enabled) {
            return false;
        }
        if (loop != other.loop) {
            return false;
        }
        if (loopBegin != other.loopBegin) {
            return false;
        }
        if (loopEnd != other.loopEnd) {
            return false;
        }
        if (nodes == null) {
            if (other.nodes != null) {
                return false;
            }
        } else if (!nodes.equals(other.nodes)) {
            return false;
        }
        if (susloop != other.susloop) {
            return false;
        }
        if (susloopBegin != other.susloopBegin) {
            return false;
        }
        if (susloopEnd != other.susloopEnd) {
            return false;
        }
        return true;
    }
    
    
    protected static interface EnvelopeConstructor {
        public List<NodePoint> loadNodes(int count);
        public ITEnvelope construct(boolean enabled, boolean loop, boolean susloop,
                int loopBegin, int loopEnd, int susloopBegin, int susloopEnd,
                List<NodePoint> nodes);
    }
    
}
