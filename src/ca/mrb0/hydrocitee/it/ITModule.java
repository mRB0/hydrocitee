package ca.mrb0.hydrocitee.it;

import java.util.List;

import org.apache.log4j.Logger;


import com.google.common.collect.ImmutableList;

public class ITModule {

    private static Logger l = Logger.getLogger(ITModule.class);

    public final SongValues songSettings; // 
    public final List<Integer> orders; // 
    
    public final String songMessage; // ""
    public final List<ITChannel> channels; // List.<ITChannel>list()
    
    public final List<ITInstrument> instruments; // 
    public final List<ITSample> samples; // 
    public final List<ITPattern> patterns; // 
    
    
    public ITModule(SongValues songSettings, List<Integer> orders,
            String songMessage, List<ITChannel> channels,
            List<ITInstrument> instruments, List<ITSample> samples,
            List<ITPattern> patterns) {
        super();
        this.songSettings = songSettings;
        this.orders = ImmutableList.copyOf(orders);
        this.songMessage = songMessage;
        this.channels = ImmutableList.copyOf(channels);
        this.instruments = ImmutableList.copyOf(instruments);
        this.samples = ImmutableList.copyOf(samples);
        this.patterns = ImmutableList.copyOf(patterns);
    }
    
}
