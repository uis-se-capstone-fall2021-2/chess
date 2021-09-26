package chess;
import java.util.Objects;


public class MoveIntent {
    ChessPiece chessPiece;
    Position from;
    Position to;
    public MoveIntent(ChessPiece chessPiece, Position from, Position to) {
        this.chessPiece = chessPiece;
        this.from = from;
        this.to = to;
    }
    public boolean equals(MoveIntent intent){
        return ( chessPiece == intent.chessPiece && from.equals(intent.from) && to.equals(intent.to) );
    }
    public int hashCode() {
        return Objects.hash(chessPiece, from.hashCode(), to.hashCode());
    }
}
