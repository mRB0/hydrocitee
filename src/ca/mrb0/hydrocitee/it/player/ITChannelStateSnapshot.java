package ca.mrb0.hydrocitee.it.player;

import java.util.List;

import ca.mrb0.hydrocitee.it.player.ITPlaybackState.ChannelState;
import ca.mrb0.hydrocitee.it.player.ITPlaybackState.VirtualChannelState;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public class ITChannelStateSnapshot implements ChannelState {
    private final List<VirtualChannelState> vchans;
    private final Optional<Integer> lastSample;
    private final int lastVol;
    
    private final ITPlaybackState state;

    public ITChannelStateSnapshot(List<VirtualChannelState> vchans,
            Optional<Integer> lastSample, int lastVol, ITPlaybackState state) {
        super();
        
        ImmutableList.Builder<VirtualChannelState> vcsb = ImmutableList.builder();
        for(VirtualChannelState vcs : vchans) {
            vcsb.add(ITVirtualChannelStateSnapshot.copyOf(this, vcs));
        }
        
        this.vchans = vcsb.build();
        this.lastSample = lastSample;
        this.lastVol = lastVol;
        this.state = state;
    }
    
    private ITChannelStateSnapshot(ITPlaybackStateSnapshot state, ChannelState copy) {
        this(copy.virtualChannels(), copy.lastSample(), copy.lastVolume(), state);
    }
    
    public static ITChannelStateSnapshot empty(ITPlaybackStateSnapshot state) {
        return new ITChannelStateSnapshot(ImmutableList.<VirtualChannelState>of(), Optional.<Integer>absent(), 64, state);
    }
    
    public static ITChannelStateSnapshot copyOf(ITPlaybackStateSnapshot state, ChannelState copy) {
        if (copy instanceof ITChannelStateSnapshot && copy.playback() == state) {
            return (ITChannelStateSnapshot)copy;
        } else {
            return new ITChannelStateSnapshot(state, copy);
        }
    }
    
    @Override
    public ITPlaybackState playback() {
        return state;
    }

    @Override
    public Optional<Integer> lastSample() {
        return lastSample;
    }

    @Override
    public int lastVolume() {
        return lastVol;
    }

    @Override
    public List<VirtualChannelState> virtualChannels() {
        return vchans;
    }

}
