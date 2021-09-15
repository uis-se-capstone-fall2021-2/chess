package game;


public enum GameCompletionState {
    ACTIVE(1),
    COMPLETE(2),
    TERMINATED(3);
    public int value;
    GameCompletionState(int value){
        this.value = value;
    }
}
