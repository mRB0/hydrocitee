package ca.mrb0.hydrocitee.it;

import java.util.Collections;
import java.util.List;

import ca.mrb0.hydrocitee.util.Streams;

import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.primitives.Ints;

public class ITPattern {
    // row index ('y'): rows (how original)
    // column index ('x'): channels
    public final Table<Integer, Integer, ITNoteEntry> notes;
    public final int rows;

    public ITPattern(int rows) {
        super();

        this.rows = rows;
        this.notes = ImmutableTable.of();
    }

    public ITPattern(int rows, Table<Integer, Integer, ITNoteEntry> inNotes) {
        super();

        this.rows = rows;
        this.notes = ImmutableTable.copyOf(inNotes);
    }

    public static ITPattern newFromData(byte[] data, int offs) {
        int length = Streams.unpack16(data, offs);
        offs += 2;
        int rows = Streams.unpack16(data, offs);
        offs += 2;

        offs += 4;

        int startoffs = offs;
        int row = 0;

        int[] masks = new int[64];
        int[] lastNotes = new int[64];
        int[] lastInstruments = new int[64];
        int[] lastVolumes = new int[64];
        int[] lastEffects = new int[64];
        int[] lastEffArgs = new int[64];

        ImmutableTable.Builder<Integer, Integer, ITNoteEntry> builder = ImmutableTable
                .builder();

        while (offs - startoffs < length) {
            int channelvariable = data[offs++] & 0xff;

            if (channelvariable == 0) {
                row++;
                if (row >= rows) {
                    break;
                }
                continue;
            }

            int channel = (channelvariable - 1) & 63;
            if ((channelvariable & 128) != 0) {
                masks[channel] = data[offs++] & 0xff;
            }

            int mask = masks[channel];

            Optional<Integer> note = Optional.absent();
            Optional<Integer> instrument = Optional.absent();
            Optional<Integer> volume = Optional.absent();
            Optional<Integer> effect = Optional.absent();
            Optional<Integer> effectArg = Optional.absent();

            if ((mask & 1) != 0) {
                note = Optional.of(data[offs++] & 0xff);
                lastNotes[channel] = note.get();
            }

            if ((mask & 2) != 0) {
                instrument = Optional.of(data[offs++] & 0xff);
                lastInstruments[channel] = instrument.get();
            }

            if ((mask & 4) != 0) {
                volume = Optional.of(data[offs++] & 0xff);
                lastVolumes[channel] = volume.get();
            }

            if ((mask & 8) != 0) {
                int c = data[offs++] & 0xff;
                int v = data[offs++] & 0xff;

                lastEffects[channel] = c;
                lastEffArgs[channel] = v;

                if (c != 0) {
                    effect = Optional.of(c);
                }
                effectArg = Optional.of(v);
            }

            if ((mask & 16) != 0) {
                note = Optional.of(lastNotes[channel]);
            }

            if ((mask & 32) != 0) {
                instrument = Optional.of(lastInstruments[channel]);
            }

            if ((mask & 64) != 0) {
                volume = Optional.of(lastVolumes[channel]);
            }

            if ((mask & 128) != 0) {
                effect = Optional.of(lastEffects[channel]);
                effectArg = Optional.of(lastEffArgs[channel]);
            }

            builder.put(row, channel, new ITNoteEntry(note, instrument, volume,
                    effect, effectArg));
        }

        ITPattern ptn = new ITPattern(rows, builder.build());
        
        return ptn;
    }

    public ITNoteEntry get(int channel, int row) {
        ITNoteEntry note = notes.get(row, channel);
        if (note == null) {
            return ITNoteEntry.emptyNote;
        } else {
            return note;
        }
    }
    
    @Override
    public String toString() {
        // 15
        StringBuilder builder = new StringBuilder();
        
        for(int row = 0; row < rows; row++) {
            if (row != 0) {
                builder.append("\n");
            }
                
            for(int channel = 0; channel < 64; channel++) {
                if (channel != 0) {
                    builder.append(" | ");
                }
                
                ITNoteEntry entry = notes.get(row, channel);
                if (entry == null) {
                    builder.append("              ");
                } else {
                    builder.append(entry.toString());
                }
            }
        }
        
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((notes == null) ? 0 : notes.hashCode());
        result = prime * result + rows;
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
        ITPattern other = (ITPattern) obj;
        if (notes == null) {
            if (other.notes != null) {
                return false;
            }
        } else if (!notes.equals(other.notes)) {
            return false;
        }
        if (rows != other.rows) {
            return false;
        }
        return true;
    }
}
