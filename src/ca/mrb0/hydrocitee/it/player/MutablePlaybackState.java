package ca.mrb0.hydrocitee.it.player;

import java.util.List;

import ca.mrb0.hydrocitee.it.ITModule;

import com.google.common.collect.Lists;

class MutablePlaybackState implements ITPlaybackState {
    ITModule module;
    int speed, tempo;
    int pattern, row;
    int tick;
    double sample;
    double sampleRate;
    
    List<MutableChannelState> channels;

    public MutablePlaybackState(ITPlaybackState initialState) {
        super();

        module = initialState.module();
        speed = initialState.speed();
        tempo = initialState.tempo();
        pattern = initialState.pattern();
        row = initialState.row();
        tick = initialState.tick();
        sample = initialState.sample();
        sampleRate = initialState.sampleRate();
        
        channels = Lists.newArrayListWithCapacity(64);
        for(int i = 0; i < 64; i++) {
            channels.add(i, new MutableChannelState(this, initialState.channelState(i)));
        }
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
    public double sampleRate() {
        return sampleRate;
    }

    @Override
    public ChannelState channelState(int channel) {
        // TODO: Return a ChannelStateSnapshot
        return channels.get(channel);
    }

}
