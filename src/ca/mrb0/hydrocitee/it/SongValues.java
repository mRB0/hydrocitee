package ca.mrb0.hydrocitee.it;

public class SongValues {
    public final String songname;

    public final int cwt_major, cwt_minor;
    public final int cmwt_major, cmwt_minor;

    // flags (not all represented)
    public final boolean stereo;
    public final boolean instruments;
    public final boolean linearSlides;
    public final boolean oldEffects;
    public final boolean linkedGxx;

    public final int globalVol;
    public final int mixingVol;
    public final int speed;
    public final int tempo;
    public final int sep;

    public SongValues() {
        super();

        songname = "";

        cwt_major = 2;
        cwt_minor = 14;
        cmwt_major = 2;
        cmwt_minor = 11;

        // flags (not all represented)
        stereo = true;
        instruments = true;
        linearSlides = true;
        oldEffects = false;
        linkedGxx = false;

        globalVol = 128;
        mixingVol = 48;
        speed = 6;
        tempo = 125;
        sep = 128;
    }

    public SongValues(String songname, int cwt_major, int cwt_minor,
            int cmwt_major, int cmwt_minor, boolean stereo,
            boolean instruments, boolean linearSlides, boolean oldEffects,
            boolean linkedGxx, int globalVol, int mixingVol, int speed,
            int tempo, int sep) {
        super();
        this.songname = songname;
        this.cwt_major = cwt_major;
        this.cwt_minor = cwt_minor;
        this.cmwt_major = cmwt_major;
        this.cmwt_minor = cmwt_minor;
        this.stereo = stereo;
        this.instruments = instruments;
        this.linearSlides = linearSlides;
        this.oldEffects = oldEffects;
        this.linkedGxx = linkedGxx;
        this.globalVol = globalVol;
        this.mixingVol = mixingVol;
        this.speed = speed;
        this.tempo = tempo;
        this.sep = sep;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cmwt_major;
        result = prime * result + cmwt_minor;
        result = prime * result + cwt_major;
        result = prime * result + cwt_minor;
        result = prime * result + globalVol;
        result = prime * result + (instruments ? 1231 : 1237);
        result = prime * result + (linearSlides ? 1231 : 1237);
        result = prime * result + (linkedGxx ? 1231 : 1237);
        result = prime * result + mixingVol;
        result = prime * result + (oldEffects ? 1231 : 1237);
        result = prime * result + sep;
        result = prime * result
                + ((songname == null) ? 0 : songname.hashCode());
        result = prime * result + speed;
        result = prime * result + (stereo ? 1231 : 1237);
        result = prime * result + tempo;
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
        SongValues other = (SongValues) obj;
        if (cmwt_major != other.cmwt_major) {
            return false;
        }
        if (cmwt_minor != other.cmwt_minor) {
            return false;
        }
        if (cwt_major != other.cwt_major) {
            return false;
        }
        if (cwt_minor != other.cwt_minor) {
            return false;
        }
        if (globalVol != other.globalVol) {
            return false;
        }
        if (instruments != other.instruments) {
            return false;
        }
        if (linearSlides != other.linearSlides) {
            return false;
        }
        if (linkedGxx != other.linkedGxx) {
            return false;
        }
        if (mixingVol != other.mixingVol) {
            return false;
        }
        if (oldEffects != other.oldEffects) {
            return false;
        }
        if (sep != other.sep) {
            return false;
        }
        if (songname == null) {
            if (other.songname != null) {
                return false;
            }
        } else if (!songname.equals(other.songname)) {
            return false;
        }
        if (speed != other.speed) {
            return false;
        }
        if (stereo != other.stereo) {
            return false;
        }
        if (tempo != other.tempo) {
            return false;
        }
        return true;
    }

}
