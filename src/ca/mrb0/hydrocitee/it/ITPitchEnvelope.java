package ca.mrb0.hydrocitee.it;

import java.util.ArrayList;
import java.util.List;

import ca.mrb0.hydrocitee.util.Streams;

import com.google.common.collect.ImmutableList;

public class ITPitchEnvelope extends ITEnvelope {

    public final boolean isFilterEnv;

    private static List<ITEnvelope.NodePoint> emptyNodeList() {
        return ImmutableList.of(new NodePoint(0, 0), new NodePoint(0, 100));
    }

    public ITPitchEnvelope() {
        this(false, false, false, 0, 1, 0, 0, ImmutableList
                .copyOf(emptyNodeList()), false);
    }

    public ITPitchEnvelope(boolean enabled, boolean loop, boolean susloop,
            int loopBegin, int loopEnd, int susloopBegin, int susloopEnd,
            List<NodePoint> nodes, boolean isFilterEnv) {
        super(enabled, loop, susloop, loopBegin, loopEnd, susloopBegin,
                susloopEnd, nodes);

        this.isFilterEnv = isFilterEnv;
    }

    public static ITPitchEnvelope newFromData(byte data[], int offs) {
        return (ITPitchEnvelope) ITEnvelope.newFromData(data, offs,
                new Constructor(data, offs));
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

    private static final class Constructor implements EnvelopeConstructor {
        private final byte data[];
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
                int val = data[offs + 6 + i * 3];
                int tick = Streams.unpack16(data, offs + 6 + i * 3 + 1);

                nodes.add(new NodePoint(val, tick));
            }
            return nodes;
        }

        @Override
        public ITPitchEnvelope construct(boolean enabled, boolean loop,
                boolean susloop, int loopBegin, int loopEnd, int susloopBegin,
                int susloopEnd, List<NodePoint> nodes) {

            int flags = 0xff & data[offs];
            boolean isFilterEnv = (flags & 0x80) != 0;

            return new ITPitchEnvelope(enabled, loop, susloop, loopBegin,
                    loopEnd, susloopBegin, susloopEnd, nodes, isFilterEnv);
        }
    };
}
