package ca.mrb0.hydrocitee.playback.units;

import java.nio.ByteBuffer;
import java.util.Map;

import com.google.common.base.Optional;

public interface Unit {
    public UnitState getState();
    public void useState(UnitState state);
    
    public void connectToUnit(Unit source, int sourceOutputPort, int inputPort);
    public void setOutputFormat(int outputPort, SampleFormat format);
    
    public Map<PortType, Optional<SupportedSampleFormats>> getSupportedFormats();
    
    public int readOutput(int port, int len, int index, ByteBuffer dest);
}
