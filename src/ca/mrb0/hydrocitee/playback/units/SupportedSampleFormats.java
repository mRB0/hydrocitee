package ca.mrb0.hydrocitee.playback.units;

import java.util.List;

import ca.mrb0.hydrocitee.playback.units.SampleFormat.Size;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public class SupportedSampleFormats {
    public final List<SampleFormat.Size> size;
    public final Optional<ImmutableList<Integer>> channels;
    
    public SupportedSampleFormats(List<Size> size,
            Optional<? extends List<Integer>> channels) {
        super();
        
        this.size = ImmutableList.copyOf(size);
        if (channels.isPresent()) {
            this.channels = Optional.of(ImmutableList.copyOf(channels.get()));
        } else {
            this.channels = Optional.absent();
        }
    }
    
}