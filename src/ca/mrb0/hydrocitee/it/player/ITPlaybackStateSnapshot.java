package ca.mrb0.hydrocitee.it.player;

import java.util.List;

import ca.mrb0.hydrocitee.it.ITModule;

import com.google.common.collect.ImmutableList;

public class ITPlaybackStateSnapshot implements ITPlaybackState {
    private final ITModule module;
    private final int speed, tempo;
    private final int pattern, row;
    private final int tick;
    private final double sample;
    private final int sampleRate;
    
    private List<ChannelState> channels;

    public ITPlaybackStateSnapshot(ITModule module, int speed, int tempo,
            int pattern, int row, int tick, int sample, int sampleRate,
            List<ChannelState> channels) {
        super();
        this.module = module;
        this.speed = speed;
        this.tempo = tempo;
        this.pattern = pattern;
        this.row = row;
        this.tick = tick;
        this.sample = sample;
        this.sampleRate = sampleRate;
        
        ImmutableList.Builder<ChannelState> cb = ImmutableList.builder();
        for(int i = 0; i < 64; i++) {
            if (channels == null) {
                cb.add(ITChannelStateSnapshot.empty(this));

            } else {
                cb.add(ITChannelStateSnapshot.copyOf(this, channels.get(i)));
            }
        }
        this.channels = cb.build();
    }

    public static ITPlaybackStateSnapshot newState(ITModule module, int sampleRate) {
        return new ITPlaybackStateSnapshot(module, 
                module.songSettings.speed, 
                module.songSettings.tempo,
                0, 
                0, 
                0, 
                0, 
                sampleRate,
                null);
    }
    
    @Override
    public ITModule module() {
        return module;
    }

    @Override
    public int speed() {
        return speed;
    }

    @Override
    public int tempo() {
        return tempo;
    }

    @Override
    public int pattern() {
        return pattern;
    }

    @Override
    public int row() {
        return row;
    }

    @Override
    public int tick() {
        return tick;
    }

    @Override
    public double sample() {
        return sample;
    }

    @Override
    public int sampleRate() {
        return sampleRate;
    }

    @Override
    public ChannelState channelState(int channel) {
        return channels.get(channel);
    }

}
