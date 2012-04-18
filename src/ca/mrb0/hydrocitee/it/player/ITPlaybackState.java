package ca.mrb0.hydrocitee.it.player;

import java.util.List;

import ca.mrb0.hydrocitee.it.ITModule;

import com.google.common.base.Optional;

public interface ITPlaybackState {
    public ITModule module();
    
    public int speed();
    public int tempo();
    
    public int pattern();
    public int row();
    public int tick();
    public double sample();
    
    public double sampleRate();
    
    public ChannelState channelState(int channel);
    
    public interface ChannelState {
        public ITPlaybackState playback();
        
        public Optional<Integer> lastSample();
        public int lastVolume();
        public List<VirtualChannelState> virtualChannels();
    }
    
    public interface VirtualChannelState {
        public ChannelState channel();
        
        public Optional<Integer> sample();
        public double sampleOffset();
        public double volume();
    }
}
