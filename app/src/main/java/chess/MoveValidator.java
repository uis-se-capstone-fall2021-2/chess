package chess;

public class MoveValidator {
    public boolean validateMove(MoveIntent intent, GameRecord record, PlayerColor playerColor) throws IllegalArgumentException {
        boolean moveIsLegal = false;
        switch(intent.chessPiece){
            case PAWN:
                //TODO: handle pawn move validation
                break;
            case KNIGHT:
                //TODO: handle knight move validation
                break;
            case BISHOP:
                //TODO: handle bishop move validation
                break;
            case ROOK:
                //TODO: handle rook move validation
                break;
            case QUEEN:
                //TODO: handle queen move validation
                break;
            case KING:
                //TODO: handle king move validation
                break;
            default:
                throw new IllegalArgumentException("MoveValidator: Invalid piece type input");
        }
        return moveIsLegal;
    }
    private int getPiece(File file, Rank rank, int[] board){
        return board[rank.value * 8 + file.value];
    }
    private int getPiece(int x, int y, int[] board) {
        return board[y * 8 + x];
    }
}
