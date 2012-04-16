package ca.mrb0.hydrocitee.it;

public abstract class AbstractVolumeColumnEntry implements VolumeColumnEntry {
    public final int value;
    
    protected AbstractVolumeColumnEntry(int value) {
        super();
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
    
    
    public static class Volume extends AbstractVolumeColumnEntry {
        public Volume(int value) {
            super(value);
        }
        
        @Override
        public String toString() {
            return String.format("v%02d", getValue());
        }
    }
    
    public static class Panning extends AbstractVolumeColumnEntry {
        public Panning(int value) {
            super(value);
        }
        
        @Override
        public String toString() {
            return String.format("p%02d", getValue());
        }
    }
    
    public static class Effect extends AbstractVolumeColumnEntry {
        public final int effect;

        public Effect(int effect, int value) {
            super(value);
            this.effect = effect;
        }
        
        public int getEffect() {
            return effect;
        }
        
        @Override
        public String toString() {
            char ltr = (char)((int)'A' + effect);
            return String.format(" %c%d", ltr, getValue());
        }
    }
    
}