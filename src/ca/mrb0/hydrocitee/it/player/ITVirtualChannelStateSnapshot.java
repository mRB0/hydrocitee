package ca.mrb0.hydrocitee.it.player;

import com.google.common.base.Optional;

import ca.mrb0.hydrocitee.it.player.ITPlaybackState.ChannelState;
import ca.mrb0.hydrocitee.it.player.ITPlaybackState.VirtualChannelState;

public class ITVirtualChannelStateSnapshot implements VirtualChannelState {
    
    private final ChannelState channel;
    private final Optional<Integer> sample;
    private final double sampleOffset;
    private final double volume;
    
    public ITVirtualChannelStateSnapshot(ChannelState channel,
            Optional<Integer> sample, double sampleOffset, double volume) {
        super();
        this.channel = channel;
        this.sample = sample;
        this.sampleOffset = sampleOffset;
        this.volume = volume;
    }

    public static ITVirtualChannelStateSnapshot copyOf(ITChannelStateSnapshot channelState, VirtualChannelState vc) {
        if (vc instanceof ITVirtualChannelStateSnapshot && channelState == vc.channel()) {
            return (ITVirtualChannelStateSnapshot)vc;
        } else {
            return new ITVirtualChannelStateSnapshot(channelState, vc.sample(), vc.sampleOffset(), vc.volume());
        }
    }
    
    @Override
    public ChannelState channel() {
        return channel;
    }

    @Override
    public Optional<Integer> sample() {
        return sample;
    }

    @Override
    public double sampleOffset() {
        return sampleOffset;
    }

    @Override
    public double volume() {
        return volume;
    }

}
