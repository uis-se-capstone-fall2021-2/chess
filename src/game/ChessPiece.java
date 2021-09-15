package game;
public enum ChessPiece {
    NONE(0),
    PAWN(1),
    ROOK(2),
    KNIGHT(3),
    BISHOP(4),
    QUEEN(5),
    KING(6);
    public int value;
    ChessPiece(int value){
        this.value = value;
    }
}
