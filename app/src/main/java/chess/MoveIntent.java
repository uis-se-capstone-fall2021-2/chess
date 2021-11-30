package chess;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;


public class MoveIntent {
    public final ChessPiece chessPiece;
    @JsonProperty("from")
    public final Position from;
    @JsonProperty("to")
    public final Position to;
    public final ChessPiece promotion;
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
        return from.toString() + " -> " + to.toString();
    }
}
