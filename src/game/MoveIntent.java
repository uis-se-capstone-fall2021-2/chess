package game;

public class MoveIntent {
    ChessPiece chessPiece;
    Position from;
    Position to;
    public MoveIntent(ChessPiece chessPiece, Position from, Position to) {
        this.chessPiece = chessPiece;
        this.from = from;
        this.to = to;
    }
    
}
