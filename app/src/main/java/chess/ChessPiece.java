package chess;

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
    public static ChessPiece FromInteger(int i) {
        // allow negative integers as input
        int input = Math.abs(i);
        
        for(ChessPiece type : values()) {
            if(type.value == input){
                return type;
            }
        }
        return null;
    }
}
