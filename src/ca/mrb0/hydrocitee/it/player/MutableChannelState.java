package ca.mrb0.hydrocitee.it.player;

import java.util.List;

import ca.mrb0.hydrocitee.it.player.ITPlaybackState.VirtualChannelState;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

class MutableChannelState implements ITPlaybackState.ChannelState {
    List<MutableVirtualChannelState> vchans = Lists.newLinkedList();
    Optional<Integer> lastSample;
    int lastVol;
    
    ITPlaybackState state;
    
    MutableChannelState(ITPlaybackState state, ITPlaybackState.ChannelState copy) {
        super();
        
        this.state = state;
        
        lastSample = copy.lastSample();
        lastVol = copy.lastVolume();
        
        vchans = Lists.newLinkedList();
        for(VirtualChannelState vcs : copy.virtualChannels()) {
            vchans.add(new MutableVirtualChannelState(this, vcs));
        }
        
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
        // TODO: Return a list of VirtualChannelStateSnapshots?
        return ImmutableList.<VirtualChannelState>copyOf(vchans);
    }
    
    @Override
    public ITPlaybackState playback() {
        return state;
    }
    
}