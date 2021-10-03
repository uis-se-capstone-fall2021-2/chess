package chess;

import java.util.ArrayList;
import java.util.List;

public class MoveValidator {
    public boolean validateMove(MoveIntent intent, int[] board, List<MoveIntent> moveRecord, PlayerColor playerColor) {

        ArrayList<MoveIntent> validMoves = getValidMoves(intent.chessPiece, intent.from, board, moveRecord, playerColor);
        for(MoveIntent move : validMoves){
            if(move.equals(intent)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<MoveIntent> getValidMoves(ChessPiece piece, Position startPos, int[] board, List<MoveIntent> moveRecord, PlayerColor playerColor){

        ArrayList<MoveIntent> validMoves = new ArrayList<MoveIntent>();
        ArrayList<Position> locationsToCheck = new ArrayList<Position>();

        switch(piece){
            case PAWN:
 
                break;
            case KNIGHT:
                //a knight has 8 potential moves that it can make. Here we add these 8 potential moves to an array, if they exist on the game board
                if(startPos.file.value < 6) {
                    // right 2
                    if(startPos.rank.value < 7)
                        locationsToCheck.add(new Position(File.FromInteger(startPos.file.value + 2), Rank.FromInteger(startPos.rank.value + 1))); // right 2 up 1
                    if(startPos.rank.value > 0)
                        locationsToCheck.add(new Position(File.FromInteger(startPos.file.value + 2), Rank.FromInteger(startPos.rank.value - 1))); // right 2 down 1
                }
                if(startPos.file.value > 1) {
                    // left 2
                    if(startPos.rank.value < 7)
                        locationsToCheck.add(new Position(File.FromInteger(startPos.file.value - 2), Rank.FromInteger(startPos.rank.value + 1))); // left 2 up 1
                    if(startPos.rank.value > 0)
                        locationsToCheck.add(new Position(File.FromInteger(startPos.file.value - 2), Rank.FromInteger(startPos.rank.value - 1))); // left 2 down 1
                }
                if(startPos.rank.value < 6) {
                    // up 2
                    if(startPos.file.value < 7)
                        locationsToCheck.add(new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value + 2))); // up 2 right 1
                    if(startPos.file.value > 0)
                        locationsToCheck.add(new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value + 2))); // up 2 left 1
                }
                if(startPos.rank.value > 1) {
                    // down 2
                    if(startPos.file.value < 7)
                        locationsToCheck.add(new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value - 2))); // down 2 right 1
                    if(startPos.file.value > 0)
                        locationsToCheck.add(new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value - 2))); // down 2 left 1
                }
                if(playerColor == PlayerColor.WHITE) {
                    for(Position endPos : locationsToCheck){
                        // if the piece that exists on the desired location is empty, or is a black piece:
                        if(getPiece(endPos, board) <= 0) {
                            // make a new moveIntent for it
                            validMoves.add(new MoveIntent(piece, startPos, endPos));
                        }
                    }
                } else {
                    for(Position endPos : locationsToCheck) {
                        // same as above, but for other team
                        if(getPiece(endPos,board) >= 0) {
                            validMoves.add(new MoveIntent(piece, startPos, endPos));
                        }
                    }
                }

                break;
            case BISHOP:

                break;
            case ROOK:

                break;
            case QUEEN:

                break;
            case KING:

                break;
            default:
                throw new IllegalArgumentException("MoveValidator: Invalid piece type input");
        
        }

        return validMoves;

    }
    //FIXME: maybe this doesn't belong here?
    public int getPiece(Position position, int[] board){
        return board[position.rank.value * 8 + position.file.value];
    }
}
