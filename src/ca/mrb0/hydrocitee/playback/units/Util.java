package ca.mrb0.hydrocitee.playback.units;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.List;

import ca.mrb0.hydrocitee.playback.units.SampleFormat.Size;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

public class Util {
    
    public static int byteIndexForFrame(SampleFormat sf, int frame, int channel) {
        int dSize;
        if (sf.size == Size.SDouble) {
            dSize = 8;
        } else {
            dSize = 4;
        }
        
        return dSize * (sf.channels * frame + channel);
    }
    
    
    public static class BestSampleFormatSizeComparator implements Comparator<SampleFormat.Size> {
        public static final List<SampleFormat.Size> formatPreference = ImmutableList.of(
                Size.SFloat, Size.SDouble, Size.Sint32, Size.Sint16, Size.Uint16, Size.Sint8, Size.Uint8
                ); 
        
        @Override
        public int compare(Size o1, Size o2) {
            return Integer.compare(formatPreference.indexOf(o1), formatPreference.indexOf(o2));
        }
    }
    
    public static interface SampleBufferWriter {
        public void writeSample(ByteBuffer dest, int index, Number sample);
    }

    public static SampleFormat getBestMatchingFormat(SupportedSampleFormats inFormats, SupportedSampleFormats outFormats) {
        Ordering<Integer> ordering = Ordering.natural();
        
        List<Integer> inChannels = null;
        List<Integer> outChannels = null;
        
        if (inFormats.channels.isPresent()) {
            inChannels = ordering.sortedCopy(inFormats.channels.get());
        }
        if (outFormats.channels.isPresent()) {
            outChannels = ordering.sortedCopy(outFormats.channels.get());
        }
        
        int channels;
        if (inChannels == null) {
            if (outChannels == null) {
                channels = 2;
            } else {
                channels = ordering.max(outChannels);
            }
        } else {
            channels = ordering.max(outChannels);
        }
        
        Ordering<Size> sizeOrdering = Ordering.from(new BestSampleFormatSizeComparator());
        List<SampleFormat.Size> inBestSizes = sizeOrdering.sortedCopy(inFormats.size);
        Size bestSize = null;
        
        for(Size s : inBestSizes) {
            if (outFormats.size.contains(s)) {
                bestSize = s;
                break;
            }
        }
        
        if (bestSize == null) {
            throw new IllegalArgumentException("Couldn't find any matching sample formats");
        }
        return new SampleFormat(channels, bestSize);
    }

    public static SampleBufferWriter writerForFormat(SampleFormat fmt) {
        if (fmt.size == Size.SFloat) {
            return new SampleBufferWriter() {
                @Override
                public void writeSample(ByteBuffer dest, int index, Number sample) {
                    dest.putFloat(index, sample.floatValue());
                }
            };
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
