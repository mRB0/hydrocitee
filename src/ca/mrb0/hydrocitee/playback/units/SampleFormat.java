package ca.mrb0.hydrocitee.playback.units;

public class SampleFormat {
    public enum Size {
        Sint8, Uint8, Sint16, Uint16, Sint32, SFloat, SDouble
    }
    
    public final int channels;
    public final Size size;
    
    public SampleFormat(int channels, Size size) {
        super();
        this.channels = channels;
        this.size = size;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + channels;
        result = prime * result + ((size == null) ? 0 : size.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SampleFormat other = (SampleFormat) obj;
        if (channels != other.channels)
            return false;
        if (size != other.size)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SampleFormat [channels=" + channels + ", size=" + size + "]";
    }
}