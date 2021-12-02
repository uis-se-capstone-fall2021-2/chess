package chess;

import com.fasterxml.jackson.annotation.JsonCreator;
/**
 * Representation of a chess piece
 */
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

    @JsonCreator
    public static ChessPiece fromJson(String key) {
        for(ChessPiece p: values()) {
            if(p.name().equals(key)) {
                return p;
            }
        }
        return null;
    }
}
