package ca.mrb0.hydrocitee.it.player;

public class ITPlayer {

    private MutablePlaybackState state;
    
    public ITPlayer(ITPlaybackState initialState) {
        super();
        
        this.state = new MutablePlaybackState(initialState);
    }
    
    public int getSamples(int[] outBuf, int offs, int len) {
        return 0;
    }

    // public ITPlaybackState getState() { return new ITPlaybackStateSnapshot... }

}
