package ca.mrb0.hydrocitee.it.player;

import com.google.common.base.Optional;

import ca.mrb0.hydrocitee.it.player.ITPlaybackState.ChannelState;
import ca.mrb0.hydrocitee.it.player.ITPlaybackState.VirtualChannelState;

class MutableVirtualChannelState implements VirtualChannelState {
    Optional<Integer> sample;
    double sampleOffset;
    double volume;
    ChannelState channel;
    
    MutableVirtualChannelState(ChannelState channel, VirtualChannelState copy) {
        this.channel = channel;
        
        this.sample = copy.sample();
        this.sampleOffset = copy.sampleOffset();
        this.volume = copy.volume();
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

    @Override
    public ChannelState channel() {
        return channel;
    }
}
