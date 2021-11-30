package chess;
/**
 * Enum to represent a player's team
 */
public enum PlayerColor {
    WHITE(1),
    BLACK(-1);
    public int value;
    PlayerColor(int value){
        this.value = value;
    }
}
