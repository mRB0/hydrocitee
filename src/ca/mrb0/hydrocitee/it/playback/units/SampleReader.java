package ca.mrb0.hydrocitee.it.playback.units;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.log4j.Logger;

import ca.mrb0.hydrocitee.it.ITSample;
import ca.mrb0.hydrocitee.it.ITSampleData;
import ca.mrb0.hydrocitee.playback.units.PortType;
import ca.mrb0.hydrocitee.playback.units.SampleFormat;
import ca.mrb0.hydrocitee.playback.units.SampleFormat.Size;
import ca.mrb0.hydrocitee.playback.units.SupportedSampleFormats;
import ca.mrb0.hydrocitee.playback.units.Unit;
import ca.mrb0.hydrocitee.playback.units.UnitState;
import ca.mrb0.hydrocitee.playback.units.Util;
import ca.mrb0.hydrocitee.playback.units.Util.SampleBufferWriter;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class SampleReader implements Unit {

    private Logger l = Logger.getLogger(SampleReader.class);

    private ITSample sample;
    private SampleBufferWriter writer;
    private int sampleOffset = 0;
    private SampleFormat outputFormat;
    
    public void setSample(ITSample sample) {
        this.sample = sample;
        sampleOffset = 0;
    }
    
    @Override
    public UnitState getState() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void useState(UnitState state) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void connectToUnit(Unit source, int sourceOutputPort, int inputPort) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void setOutputFormat(int outputPort, SampleFormat format) {
        if (outputPort != 0 || format.channels != 2 || format.size != Size.SFloat) {
            throw new UnsupportedOperationException("Can only output 2-channel floats for now (pending implementation of others; eg. should definitely be 1-channel)");
        }
        
        writer = Util.writerForFormat(format);
        outputFormat = format;
    }
    
    @Override
    public Map<PortType, Optional<SupportedSampleFormats>> getSupportedFormats() {
        // XXX: should output 1 channel, not 2
        SupportedSampleFormats supportedInFormats = new SupportedSampleFormats(ImmutableList.<SampleFormat.Size>of(), Optional.of(ImmutableList.<Integer>of()));
        SupportedSampleFormats supportedOutFormats = new SupportedSampleFormats(ImmutableList.of(SampleFormat.Size.SFloat),
                Optional.of(ImmutableList.of(2)));
        
        return ImmutableMap.of(PortType.Input, Optional.of(supportedInFormats), 
                PortType.Output, Optional.of(supportedOutFormats));
    }

    @Override
    public int readOutput(int port, int len, int index, ByteBuffer dest) {
        ITSampleData sd = sample.sampleData.get();
        
        for(int i = 0; i < len; i++) {
            sampleOffset %= sd.sampleData.size();
            
            int c = sd.sampleData.get(sampleOffset);
            float out;
            
            if (sample._16bit) {
                out = (float)(c / 32768.0);
            } else {
                out = (float)(c / 128.0);
            }
            
            out = (float)(((out + 1.0) % 2.0) - 1.0);
            
            for(int ch = 0; ch < 2; ch++) {
                writer.writeSample(dest, Util.byteIndexForFrame(outputFormat, i, ch), out);
            }
            
            sampleOffset++;
        }
        return len;
    }
    
}
