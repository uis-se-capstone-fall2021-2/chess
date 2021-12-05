package chess;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A move in a chess game
 * MoveIntents are not necessarily valid moves.
 */
public class MoveIntent {
    @JsonProperty("chessPiece")
    public final ChessPiece chessPiece;
    @JsonProperty("from")
    public final Position from;
    @JsonProperty("to")
    public final Position to;
    @JsonProperty("promotion")
    public final ChessPiece promotion;

    public MoveIntent() {
        this.chessPiece = ChessPiece.NONE;
        this.from = null;
        this.to = null;
        this.promotion = ChessPiece.NONE;
    }

    public MoveIntent(ChessPiece chessPiece, Position from, Position to) {
        this.chessPiece = chessPiece;
        this.from = from;
        this.to = to;
        this.promotion = ChessPiece.NONE;
    }
    //promotion optional addition
    public MoveIntent(ChessPiece chessPiece, Position from, Position to, ChessPiece promotion) {
        this.chessPiece = chessPiece;
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }
    public boolean equals(MoveIntent intent){
        return ( chessPiece == intent.chessPiece && from.equals(intent.from) && to.equals(intent.to) && this.promotion == intent.promotion);
    }
    public int hashCode() {
        return Objects.hash(chessPiece, from.hashCode(), to.hashCode(), promotion);
    }
    public String toString() {
        if(promotion != ChessPiece.NONE){
            return from.toString() + " -> " + to.toString() + " Promote: " + promotion.name();
        }
        return from.toString() + " -> " + to.toString();
    }

    // @JsonCreator
    // public static MoveIntent fromJson(String chessPiece, String from, String to) {
    //     return new MoveIntent(ChessPiece.fromJson(chessPiece), 
    // }

}
