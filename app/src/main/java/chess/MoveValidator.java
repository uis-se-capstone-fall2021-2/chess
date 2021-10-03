package chess;
import chess.board.*;
import java.util.ArrayList;

public class MoveValidator {
    public boolean validateMove(MoveIntent intent, Board board,ArrayList<MoveIntent> moveRecord) {
        PlayerColor playerColor;
        Position startingPoint = intent.from;
        int piece = board.getPiece(startingPoint);
        if(piece > 0) {
            playerColor = PlayerColor.WHITE;
        } else if (piece < 0) {
            playerColor = PlayerColor.BLACK;
        } else {
            // Attempted to move a piece that does not exist.
            return false;
        }
        ArrayList<MoveIntent> validMoves = getValidMoves(intent.chessPiece, intent.from, board, moveRecord, playerColor);
        for(MoveIntent move : validMoves){
            if(move.equals(intent)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<MoveIntent> getValidMoves(ChessPiece piece, Position startPos, Board board ,ArrayList<MoveIntent> moveRecord, PlayerColor playerColor){

        ArrayList<MoveIntent> validMoves = new ArrayList<MoveIntent>();
        ArrayList<Position> locationsToCheck = new ArrayList<Position>();

        // TODO: after populating validMoves, should make checks for if king is in check and remove moves that end with king in check. Alternatively, king checks could made before moves are put into validmoves in the first place.

        switch(piece){
            case PAWN:
                if(playerColor == PlayerColor.WHITE){
                    // white pawn
                    if(startPos.rank.value < 7) {
                        Position front = new Position(File.FromInteger(startPos.file.value), Rank.FromInteger(startPos.rank.value + 1));
                        if(board.getPiece(front) == 0) {
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front));
                        }
                        // if pawn is on its starting position
                        if(startPos.rank.value == 1){
                            Position doubleFront = new Position(File.FromInteger(startPos.file.value), Rank.FromInteger(startPos.rank.value + 2));
                            if(board.getPiece(front) == 0 && board.getPiece(doubleFront) == 0){
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, doubleFront));
                            }
                        }
                    }


                    // CAPTURES
                    if(startPos.file.value > 0) {
                        Position leftCapture = new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value + 1));
                        if(board.getPiece(leftCapture) < 0){
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture));
                        }
                    }
                    if(startPos.file.value < 7) {
                        Position rightCapture = new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value + 1));
                        if(board.getPiece(rightCapture) < 0){
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture));
                        }
                    }

                    // EN PASSANT
                        // LEFT EN PASSANT
                    if(startPos.file.value > 0 && startPos.rank.value == 4) {
                        Position left = new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value));
                        Position leftPrevious = new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value + 2));
                        MoveIntent intentToCheck = new MoveIntent(ChessPiece.PAWN, leftPrevious, left);
                        if(moveRecord.get(moveRecord.size()).equals(intentToCheck)) {
                            // if the previous move was the opposite pawn moving adjectent to this pawn.
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value + 1))));
                        }
                    }
                        // RIGHT EN PASSANT
                    if(startPos.file.value < 7 && startPos.rank.value == 4) {
                        Position right = new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value));
                        Position rightPrevious = new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value + 2));
                        MoveIntent intentToCheck = new MoveIntent(ChessPiece.PAWN, rightPrevious, right);
                        if(moveRecord.get(moveRecord.size()).equals(intentToCheck)) {
                            // if the previous move was the opposite pawn moving adjectent to this pawn.
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value + 1))));
                        }
                    }
                        


                } else {
                    // black pawn
                    if(startPos.rank.value > 0) {
                        Position front = new Position(File.FromInteger(startPos.file.value), Rank.FromInteger(startPos.rank.value - 1));
                        if(board.getPiece(front) == 0) {
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front));
                        }
                        // if pawn is on its starting position
                        if(startPos.rank.value == 6){
                            Position doubleFront = new Position(File.FromInteger(startPos.file.value), Rank.FromInteger(startPos.rank.value - 2));
                            if(board.getPiece(front) == 0 && board.getPiece(doubleFront) == 0){
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, doubleFront));
                            }
                        }
                    }
                    // CAPTURES
                    if(startPos.file.value > 0) {
                        Position leftCapture = new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value - 1));
                        if(board.getPiece(leftCapture) > 0){
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture));
                        }
                    }
                    if(startPos.file.value < 7) {
                        Position rightCapture = new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value - 1));
                        if(board.getPiece(rightCapture) > 0){
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture));
                        }
                    }


                    // EN PASSANT
                        // LEFT EN PASSANT
                    if(startPos.file.value > 0 && startPos.rank.value == 3) {
                        Position left = new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value));
                        Position leftPrevious = new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value - 2));
                        MoveIntent intentToCheck = new MoveIntent(ChessPiece.PAWN, leftPrevious, left);
                        if(moveRecord.get(moveRecord.size()).equals(intentToCheck)) {
                            // if the previous move was the opposite pawn moving adjectent to this pawn.
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value - 1))));
                        }
                    }
                        // RIGHT EN PASSANT
                    if(startPos.file.value < 7 && startPos.rank.value == 3) {
                        Position right = new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value));
                        Position rightPrevious = new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value - 2));
                        MoveIntent intentToCheck = new MoveIntent(ChessPiece.PAWN, rightPrevious, right);
                        if(moveRecord.get(moveRecord.size()).equals(intentToCheck)) {
                            // if the previous move was the opposite pawn moving adjectent to this pawn.
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value - 1))));
                        }
                    }

                }
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
                        if(board.getPiece(endPos) <= 0) {
                            // make a new moveIntent for it
                            validMoves.add(new MoveIntent(piece, startPos, endPos));
                        }
                    }
                } else {
                    for(Position endPos : locationsToCheck) {
                        // same as above, but for other team
                        if(board.getPiece(endPos) >= 0) {
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
}
