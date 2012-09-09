package ca.mrb0.hydrocitee.playback.units.impl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.Map;

import ca.mrb0.hydrocitee.playback.units.PortType;
import ca.mrb0.hydrocitee.playback.units.SampleFormat;
import ca.mrb0.hydrocitee.playback.units.SampleFormat.Size;
import ca.mrb0.hydrocitee.playback.units.SupportedSampleFormats;
import ca.mrb0.hydrocitee.playback.units.Unit;
import ca.mrb0.hydrocitee.playback.units.UnitState;
import ca.mrb0.hydrocitee.playback.units.Util;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class SampleConversion implements Unit {

    public final SampleFormat inFormat, outFormat;
    private Unit source;
    
    public SampleConversion(SampleFormat inFormat, SampleFormat outFormat) {
        super();
        
        if (outFormat.channels < inFormat.channels) {
            throw new IllegalArgumentException(String.format("Sorry, but SampleConversion can't mix channels.  Expected: inFormat.channels=%d <= outFormat.channels=%d", inFormat.channels, outFormat.channels));
        } else if ((outFormat.channels % inFormat.channels) != 0) {
            throw new IllegalArgumentException(String.format("Sorry, but SampleConversion can't replicate channels unevenly.  Expected: outFormat.channels=%d is an even multiple of inFormat.channels=%d", outFormat.channels, inFormat.channels));
        } else if (outFormat.channels != inFormat.channels) {
            // xxx remove this restriction someday
            throw new IllegalArgumentException("Need same number of input as output channels");
        }
        
        this.inFormat = inFormat;
        this.outFormat = outFormat;
    }
    
    @Override
    public UnitState getState() {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void useState(UnitState state) {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void connectToUnit(Unit source, int sourceOutputPort, int inputPort) {
        Optional<SupportedSampleFormats> outFormats = source.getSupportedFormats().get(PortType.Output);
        if (!outFormats.isPresent()) {
            throw new IllegalArgumentException("Unit " + source.toString() + " doesn't support any output formats");
        }
        
        Optional<SupportedSampleFormats> inFormats = getSupportedFormats().get(PortType.Input);
        
        SampleFormat bestMatch = Util.getBestMatchingFormat(inFormats.get(), outFormats.get());
        
        source.setOutputFormat(sourceOutputPort, bestMatch);
        
        if (inFormat.size == Size.SDouble) {
            sourceBuffer = ByteBuffer.allocate(8 * inFormat.channels);
        } else if (inFormat.size == Size.SFloat) {
            sourceBuffer = ByteBuffer.allocate(4 * inFormat.channels);
        } else if (inFormat.size == Size.Sint32 || inFormat.size == Size.Sint16 || inFormat.size == Size.Uint16 || inFormat.size == Size.Sint8 || inFormat.size == Size.Uint8) {
            sourceBuffer = ByteBuffer.allocate(4 * inFormat.channels);
        }
        writer = writerForFormat(outFormat);
        
        this.sourceOutputPort = sourceOutputPort;
        this.source = source;
    }
    
    private int sourceOutputPort;
    private ByteBuffer sourceBuffer;
    private SampleBufferWriter writer;
    
    @Override
    public void setOutputFormat(int outputPort, SampleFormat format) {
        if (!outFormat.equals(format)) {
            throw new IllegalArgumentException("Expected SampleFormat " + outFormat.toString() + ", but got " + format.toString() + " instead");
        }
        if (outputPort != 0) {
            throw new IllegalArgumentException("Expected outputPort 0, but got " + outputPort + " instead");
        }
    }
    
    @Override
    public Map<PortType, Optional<SupportedSampleFormats>> getSupportedFormats() {
        SupportedSampleFormats supportedInFormats = new SupportedSampleFormats(ImmutableList.of(inFormat.size), Optional.of(ImmutableList.of(inFormat.channels)));
        SupportedSampleFormats supportedOutFormats = new SupportedSampleFormats(ImmutableList.of(outFormat.size), Optional.of(ImmutableList.of(outFormat.channels)));
        
        return ImmutableMap.of(PortType.Input, Optional.of(supportedInFormats), 
                PortType.Output, Optional.of(supportedOutFormats));
    }

    @Override
    public int readOutput(int port, int len, int index, ByteBuffer dest) {
        if (port != 0) {
            throw new IllegalArgumentException("Expected port 0, got port " + port);
        }
        
        for(int i = 0; i < len; i++) {
            source.readOutput(sourceOutputPort, 1, 0, sourceBuffer);
            
            for(int c = 0; c < inFormat.channels; c++) {
                // hobo conversion by turning everything to a double first
                
                int outBufferOffs = outFormat.channels * (index + i) + c;
                
                double convertedValue;
                if (inFormat.size == Size.SDouble) {
                    convertedValue = sourceBuffer.getDouble(c * 8);
                } else if (inFormat.size == Size.SFloat) {
                    convertedValue = sourceBuffer.getFloat(c * 4);
                } else if (inFormat.size == Size.Sint32) {
                    convertedValue = sourceBuffer.getInt(c * 4) / -((double)Integer.MIN_VALUE);
                } else if (inFormat.size == Size.Sint16) {
                    convertedValue = sourceBuffer.getInt(c * 4) / -((double)Short.MIN_VALUE);
                } else if (inFormat.size == Size.Uint16) {
                    convertedValue = (sourceBuffer.getInt(c * 4) + Short.MIN_VALUE) / -((double)Short.MIN_VALUE);
                } else if (inFormat.size == Size.Sint8) {
                    convertedValue = sourceBuffer.getInt(c * 4) / -((double)256);
                } else if (inFormat.size == Size.Uint8) {
                    convertedValue = (sourceBuffer.getInt(c * 4) - 128) / -((double)256);
                } else {
                    throw new IllegalArgumentException("Got unexpected inFormat size for inFormat " + inFormat.toString());
                }
                
                writer.writeSample(dest, outBufferOffs, convertedValue);
            }
        }
        
        return len;
    }
    
    static interface SampleBufferWriter {
        public void writeSample(ByteBuffer dest, int index, Number sample);
    }
    
    static SampleBufferWriter writerForFormat(SampleFormat fmt) {
        if (fmt.size == Size.SFloat) {
            return new SampleBufferWriter() {
                @Override
                public void writeSample(ByteBuffer dest, int index, Number sample) {
                    int actualIndex = index * 4;
                    dest.putFloat(index, sample.floatValue());
                }
            };
        } else {
            throw new UnsupportedOperationException("not implemented");
        }
    }
    
}
