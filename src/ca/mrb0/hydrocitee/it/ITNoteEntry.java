package ca.mrb0.hydrocitee.it;

import com.google.common.base.Optional;

public class ITNoteEntry {
    public final Optional<Integer> note;
    public final Optional<Integer> instrument;
    public final Optional<Integer> volume;
    public final Optional<Integer> effect;
    public final Optional<Integer> effectArg;

    public static final ITNoteEntry emptyNote = new ITNoteEntry(
            Optional.<Integer> absent(), Optional.<Integer> absent(),
            Optional.<Integer> absent(), Optional.<Integer> absent(),
            Optional.<Integer> absent());

    public ITNoteEntry(Optional<Integer> note, Optional<Integer> instrument,
            Optional<Integer> volume, Optional<Integer> effect,
            Optional<Integer> effectArg) {
        super();
        this.note = note;
        this.instrument = instrument;
        this.volume = volume;
        this.effect = effect;
        this.effectArg = effectArg;
    }
    
    public static String stringForNoteNum(Optional<Integer> note) {
        if (!note.isPresent()) {
            return "...";
        }
        
        int n = note.get();
        
        if (n == 254) {
            return "^^^";
        }
        if (n == 255) {
            return "===";
        }
        
        String[] noteList = { "C-", "C#", "D-", "D#", "E-", "F-", "F#", "G-", "G#", "A-", "A#", "B-" };
        String pitch = noteList[n % 12];
        int octave = n / 12;
        
        return String.format("%s%d", pitch, octave);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(stringForNoteNum(note));
        builder.append(" ");
        
        builder.append(instrument.isPresent() ? String.format("%02d", instrument.get()) : "..");
        builder.append(" ");
        
        if (volume.isPresent()) {
            int vol = volume.get();
            if (vol <= 64) {
                builder.append(String.format("v%02d", vol));
            } else if (vol <= 124 || vol >= 193) {
                int ltrP, val;
                if (vol <= 124) {
                    ltrP = (vol - 65) / 10;
                    val = (vol - 65) % 10;
                } else {
                    ltrP = ((vol - 193) / 10) + 6;
                    val = (vol - 193) % 10;
                }
                char ltr = (char)((int)'A' + ltrP);
                builder.append(String.format(" %c%d", ltr, val));
            } else if (vol >= 128 && vol <= 192) {
                builder.append(String.format("p%02d", vol - 192));
            } else {
                builder.append(String.format("%02X?", vol));
            }
        } else {
            builder.append("...");
        }
        builder.append(" ");

        builder.append(effect.isPresent() ? (char)(effect.get() - 1 + (int)'A') : ".");
        builder.append(effectArg.isPresent() ? String.format("%02X", effectArg.get()) : "..");
        
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((effect == null) ? 0 : effect.hashCode());
        result = prime * result
                + ((effectArg == null) ? 0 : effectArg.hashCode());
        result = prime * result
                + ((instrument == null) ? 0 : instrument.hashCode());
        result = prime * result + ((note == null) ? 0 : note.hashCode());
        result = prime * result + ((volume == null) ? 0 : volume.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ITNoteEntry other = (ITNoteEntry) obj;
        if (effect == null) {
            if (other.effect != null) {
                return false;
            }
        } else if (!effect.equals(other.effect)) {
            return false;
        }
        if (effectArg == null) {
            if (other.effectArg != null) {
                return false;
            }
        } else if (!effectArg.equals(other.effectArg)) {
            return false;
        }
        if (instrument == null) {
            if (other.instrument != null) {
                return false;
            }
        } else if (!instrument.equals(other.instrument)) {
            return false;
        }
        if (note == null) {
            if (other.note != null) {
                return false;
            }
        } else if (!note.equals(other.note)) {
            return false;
        }
        if (volume == null) {
            if (other.volume != null) {
                return false;
            }
        } else if (!volume.equals(other.volume)) {
            return false;
        }
        return true;
    }

}
