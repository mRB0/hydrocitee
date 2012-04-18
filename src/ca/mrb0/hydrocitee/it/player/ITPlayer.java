package ca.mrb0.hydrocitee.it.player;

public class ITPlayer {

    private MutablePlaybackState state;
    
    public ITPlayer(ITPlaybackState initialState) {
        super();
        
        this.state = new MutablePlaybackState(initialState);
    }
    
    

    // public ITPlaybackState getState() { return new ITPlaybackStateSnapshot... }

}
