package chess;

import java.util.ArrayList;
import java.util.List;

import chess.board.Board;

public class MoveValidator {
    public static boolean validateMove(MoveIntent intent, Board board, List<MoveIntent> moveRecord) {
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
        List<MoveIntent> validMoves = getValidMoves(intent.chessPiece, intent.from, board, moveRecord, playerColor);
        for(MoveIntent move : validMoves){
            if(move.equals(intent)) {
                return true;
            }
        }
        return false;
    }

    public static List<MoveIntent> getValidMoves(ChessPiece piece, Position startPos, Board board, List<MoveIntent> moveRecord, PlayerColor playerColor){

        List<MoveIntent> validMoves = new ArrayList<MoveIntent>();
        List<Position> locationsToCheck = new ArrayList<Position>();
        int x,y;

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
                        if(moveRecord.get(moveRecord.size() - 1).equals(intentToCheck)) {
                            // if the previous move was the opposite pawn moving adjectent to this pawn.
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value + 1))));
                        }
                    }
                        // RIGHT EN PASSANT
                    if(startPos.file.value < 7 && startPos.rank.value == 4) {
                        Position right = new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value));
                        Position rightPrevious = new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value + 2));
                        MoveIntent intentToCheck = new MoveIntent(ChessPiece.PAWN, rightPrevious, right);
                        if(moveRecord.get(moveRecord.size() - 1).equals(intentToCheck)) {
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
                        if(moveRecord.get(moveRecord.size() - 1).equals(intentToCheck)) {
                            // if the previous move was the opposite pawn moving adjectent to this pawn.
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, new Position(File.FromInteger(startPos.file.value - 1), Rank.FromInteger(startPos.rank.value - 1))));
                        }
                    }
                        // RIGHT EN PASSANT
                    if(startPos.file.value < 7 && startPos.rank.value == 3) {
                        Position right = new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value));
                        Position rightPrevious = new Position(File.FromInteger(startPos.file.value + 1), Rank.FromInteger(startPos.rank.value - 2));
                        MoveIntent intentToCheck = new MoveIntent(ChessPiece.PAWN, rightPrevious, right);
                        if(moveRecord.get(moveRecord.size() - 1).equals(intentToCheck)) {
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
                x = startPos.file.value;
                y = startPos.rank.value;
                // Direction : Up & right
                while(x < 7 && y < 7){
                    x++;
                    y++;
                    Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }
                x = startPos.file.value;
                y = startPos.rank.value;
                // Direction : Up & left
                while(x > 0 && y < 7){
                    x--;
                    y++;
                    Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }
                x = startPos.file.value;
                y = startPos.rank.value;
                // Direction : down & right
                while(x < 7 && y > 0){
                    x++;
                    y--;
                    Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }
                x = startPos.file.value;
                y = startPos.rank.value;
                // Direction : down & left
                while(x > 0 && y > 0){
                    x--;
                    y--;
                    Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }

                for(Position endPos : locationsToCheck) {
                    if(playerColor == PlayerColor.WHITE) {
                        if(board.getPiece(endPos) <= 0) {
                            // make a new moveIntent for it
                            validMoves.add(new MoveIntent(piece, startPos, endPos));
                        }
                    } else {
                        if(board.getPiece(endPos) >= 0) {
                            // make a new moveIntent for it
                            validMoves.add(new MoveIntent(piece, startPos, endPos));
                        }
                    }
                }
                break;
            case ROOK:
                x = startPos.file.value;
                y = startPos.rank.value;
                // right
                while(x < 7) {
                    x++;
                    Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }
                // left
                while(x > 0) {
                    x--;
                    Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }
                // up
                while(y < 7) {
                    y++;
                    Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }
                // down
                while(y > 0) {
                    y--;
                    Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }
                for(Position endPos : locationsToCheck) {
                    if(playerColor == PlayerColor.WHITE) {
                        if(board.getPiece(endPos) <= 0) {
                            // make a new moveIntent for it
                            validMoves.add(new MoveIntent(piece, startPos, endPos));
                        }
                    } else {
                        if(board.getPiece(endPos) >= 0) {
                            // make a new moveIntent for it
                            validMoves.add(new MoveIntent(piece, startPos, endPos));
                        }
                    }
                }
                break;
            case QUEEN:
                // reuse bishop and rook switches and combine the results of those calls.
                List<MoveIntent> horizontalMoves = getValidMoves(ChessPiece.ROOK, startPos, board, moveRecord, playerColor);
                validMoves = getValidMoves(ChessPiece.BISHOP, startPos, board, moveRecord, playerColor);
                validMoves.addAll(horizontalMoves);
                break;
            case KING:
                x = startPos.file.value;
                y = startPos.rank.value;
                if(x < 7) {
                    locationsToCheck.add(new Position(File.FromInteger(x + 1), Rank.FromInteger(y)));
                    if(y < 7)
                        locationsToCheck.add(new Position(File.FromInteger(x + 1), Rank.FromInteger(y + 1)));
                    if (y > 0)
                        locationsToCheck.add(new Position(File.FromInteger(x + 1), Rank.FromInteger(y - 1)));
                }
                if(x > 0){
                    locationsToCheck.add(new Position(File.FromInteger(x - 1), Rank.FromInteger(y)));
                    if(y < 7)
                        locationsToCheck.add(new Position(File.FromInteger(x - 1), Rank.FromInteger(y + 1)));
                    if (y > 0)
                        locationsToCheck.add(new Position(File.FromInteger(x - 1), Rank.FromInteger(y - 1)));
                }
                if(y > 0)
                    locationsToCheck.add(new Position(File.FromInteger(x), Rank.FromInteger(y - 1)));
                if(y < 7)
                    locationsToCheck.add(new Position(File.FromInteger(x), Rank.FromInteger(y + 1)));

                // TODO: Find out if castling is legal.


                for(Position endPos : locationsToCheck) {
                    if(playerColor == PlayerColor.WHITE) {
                        if(board.getPiece(endPos) <= 0) {
                            // make a new moveIntent for it
                            validMoves.add(new MoveIntent(piece, startPos, endPos));
                        }
                    } else {
                        if(board.getPiece(endPos) >= 0) {
                            // make a new moveIntent for it
                            validMoves.add(new MoveIntent(piece, startPos, endPos));
                        }
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("MoveValidator: Invalid piece type input");
        
        }
        // TODO: Remove moves that result in check from validMoves
        return validMoves;

    }
}
