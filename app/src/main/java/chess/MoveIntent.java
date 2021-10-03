package chess;

public class MoveIntent {
    public final ChessPiece chessPiece;
    public final Position from;
    public final Position to;
    public MoveIntent(ChessPiece chessPiece, Position from, Position to) {
        this.chessPiece = chessPiece;
        this.from = from;
        this.to = to;
    }
    
}
