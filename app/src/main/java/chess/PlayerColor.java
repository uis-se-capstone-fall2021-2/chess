package chess;
public enum PlayerColor {
    WHITE(1),
    BLACK(-1);
    public int value;
    PlayerColor(int value){
        this.value = value;
    }
}
