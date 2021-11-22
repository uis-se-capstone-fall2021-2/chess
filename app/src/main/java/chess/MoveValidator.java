package chess;

import java.util.ArrayList;
import java.util.List;

import chess.board.Board;
import chess.game.GameState;

public class MoveValidator {
    public static boolean validateMove(MoveIntent intent, Board board, List<MoveIntent> moveRecord, PlayerColor moveColor) {
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

        if(playerColor != moveColor){
            // Player is trying to move opponents piece.
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

        List<MoveIntent> validMoves = new ArrayList<>();
        List<Position> locationsToCheck = new ArrayList<>();
        int x,y;
        x = startPos.file.value;
        y = startPos.rank.value;
        switch(piece){
            case PAWN:
                if(playerColor == PlayerColor.WHITE){
                    // white pawn
                    if(y < 7) {
                        Position front = new Position(x, y + 1);
                        if(board.getPiece(front) == 0) {
                            if(y == 6){
                                //pawn can promote
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front, ChessPiece.BISHOP));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front, ChessPiece.KNIGHT));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front, ChessPiece.QUEEN));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front, ChessPiece.ROOK));
                            } else {
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front));
                            }
                        }
                        // if pawn is on its starting position
                        if(y == 1){
                            Position doubleFront = new Position(x, y + 2);
                            if(board.getPiece(front) == 0 && board.getPiece(doubleFront) == 0){
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, doubleFront));
                            }
                        }
                    }

                    // CAPTURES
                    if(x > 0) {
                        Position leftCapture = new Position(x - 1, y + 1);
                        if(board.getPiece(leftCapture) < 0){
                            if(y == 6){
                                //pawn can capture and promote
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture, ChessPiece.BISHOP));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture, ChessPiece.KNIGHT));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture, ChessPiece.QUEEN));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture, ChessPiece.ROOK));
                            } else {
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture));
                            }
                        }
                    }
                    if(x < 7) {
                        Position rightCapture = new Position(x + 1, y + 1);
                        if(board.getPiece(rightCapture) < 0){
                            if(y == 6){
                                //pawn can capture and promote
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture, ChessPiece.BISHOP));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture, ChessPiece.KNIGHT));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture, ChessPiece.QUEEN));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture, ChessPiece.ROOK));
                            } else {
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture));
                            }
                        }
                    }

                    // EN PASSANT
                        // LEFT EN PASSANT
                    if(x > 0 && y == 4) {
                        Position left = new Position(x - 1, y);
                        Position leftPrevious = new Position(x - 1, y + 2);
                        MoveIntent intentToCheck = new MoveIntent(ChessPiece.PAWN, leftPrevious, left);
                        if(moveRecord.get(moveRecord.size() - 1).equals(intentToCheck)) {
                            // if the previous move was the opposite pawn moving adjectent to this pawn.
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, new Position(x - 1, y + 1)));
                        }
                    }
                        // RIGHT EN PASSANT
                    if(x < 7 && y == 4) {
                        Position right = new Position(x + 1, y);
                        Position rightPrevious = new Position(x + 1, y + 2);
                        MoveIntent intentToCheck = new MoveIntent(ChessPiece.PAWN, rightPrevious, right);
                        if(moveRecord.get(moveRecord.size() - 1).equals(intentToCheck)) {
                            // if the previous move was the opposite pawn moving adjectent to this pawn.
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, new Position(x + 1, y + 1)));
                        }
                    }
                        


                } else {
                    // black pawn
                    if(y > 0) {
                        Position front = new Position(x, y - 1);
                        if(board.getPiece(front) == 0) {
                            if(y == 1) {
                                // pawn can promote
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front, ChessPiece.BISHOP));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front, ChessPiece.KNIGHT));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front, ChessPiece.QUEEN));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front, ChessPiece.ROOK));
                            } else {
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, front));
                            }
                        }
                        // if pawn is on its starting position
                        if(y == 6){
                            Position doubleFront = new Position(x, y - 2);
                            if(board.getPiece(front) == 0 && board.getPiece(doubleFront) == 0){
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, doubleFront));
                            }
                        }
                    }
                    // CAPTURES
                    if(x > 0) {
                        Position leftCapture = new Position(x - 1, y - 1);
                        if(board.getPiece(leftCapture) > 0){
                            if(y == 1) {
                                // capture and promote
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture, ChessPiece.BISHOP));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture, ChessPiece.KNIGHT));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture, ChessPiece.QUEEN));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture, ChessPiece.ROOK));
                            } else {
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, leftCapture));
                            }
                        }
                    }
                    if(x < 7) {
                        Position rightCapture = new Position(x + 1, y - 1);
                        if(board.getPiece(rightCapture) > 0){
                            if(y == 1) {
                                // capture and promote
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture, ChessPiece.BISHOP));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture, ChessPiece.KNIGHT));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture, ChessPiece.QUEEN));
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture, ChessPiece.ROOK));
                            } else {
                                validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, rightCapture));
                            }
                        }
                    }


                    // EN PASSANT
                        // LEFT EN PASSANT
                    if(x > 0 && y == 3) {
                        Position left = new Position(x - 1, y);
                        Position leftPrevious = new Position(x - 1, y - 2);
                        MoveIntent intentToCheck = new MoveIntent(ChessPiece.PAWN, leftPrevious, left);
                        if(moveRecord.get(moveRecord.size() - 1).equals(intentToCheck)) {
                            // if the previous move was the opposite pawn moving adjectent to this pawn.
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, new Position(x - 1, y - 1)));
                        }
                    }
                        // RIGHT EN PASSANT
                    if(x < 7 && y == 3) {
                        Position right = new Position(x + 1, y);
                        Position rightPrevious = new Position(x + 1, y - 2);
                        MoveIntent intentToCheck = new MoveIntent(ChessPiece.PAWN, rightPrevious, right);
                        if(moveRecord.get(moveRecord.size() - 1).equals(intentToCheck)) {
                            // if the previous move was the opposite pawn moving adjectent to this pawn.
                            validMoves.add(new MoveIntent(ChessPiece.PAWN, startPos, new Position(x + 1, y - 1)));
                        }
                    }

                }
                break;
            case KNIGHT:
                //a knight has 8 potential moves that it can make. Here we add these 8 potential moves to an array, if they exist on the game board
                if(x < 6) {
                    // right 2
                    if(y < 7)
                        locationsToCheck.add(new Position(x + 2, y + 1)); // right 2 up 1
                    if(y > 0)
                        locationsToCheck.add(new Position(x + 2, y - 1)); // right 2 down 1
                }
                if(x > 1) {
                    // left 2
                    if(y < 7)
                        locationsToCheck.add(new Position(x - 2, y + 1)); // left 2 up 1
                    if(y > 0)
                        locationsToCheck.add(new Position(x - 2, y - 1)); // left 2 down 1
                }
                if(y < 6) {
                    // up 2
                    if(x < 7)
                        locationsToCheck.add(new Position(x + 1, y + 2)); // up 2 right 1
                    if(x > 0)
                        locationsToCheck.add(new Position(x - 1, y + 2)); // up 2 left 1
                }
                if(y > 1) {
                    // down 2
                    if(x < 7)
                        locationsToCheck.add(new Position(x + 1, y - 2)); // down 2 right 1
                    if(x > 0)
                        locationsToCheck.add(new Position(x - 1, y - 2)); // down 2 left 1
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
                    Position next = new Position(x, y);
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
                    Position next = new Position(x, y);
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
                    Position next = new Position(x, y);
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
                    Position next = new Position(x, y);
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
                    Position next = new Position(x, y);
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }
                x = startPos.file.value;
                y = startPos.rank.value;
                // left
                while(x > 0) {
                    x--;
                    Position next = new Position(x, y);
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }
                x = startPos.file.value;
                y = startPos.rank.value;
                // up
                while(y < 7) {
                    y++;
                    Position next = new Position(x, y);
                    locationsToCheck.add(next);
                    if(board.getPiece(next) != 0) {
                        break;
                    }
                }
                x = startPos.file.value;
                y = startPos.rank.value;
                // down
                while(y > 0) {
                    y--;
                    Position next = new Position(x, y);
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
            x = startPos.file.value;
            y = startPos.rank.value;
            // right
            while(x < 7) {
                x++;
                Position next = new Position(x, y);
                locationsToCheck.add(next);
                if(board.getPiece(next) != 0) {
                    break;
                }
            }
            x = startPos.file.value;
            y = startPos.rank.value;
            // left
            while(x > 0) {
                x--;
                Position next = new Position(x, y);
                locationsToCheck.add(next);
                if(board.getPiece(next) != 0) {
                    break;
                }
            }
            x = startPos.file.value;
            y = startPos.rank.value;
            // up
            while(y < 7) {
                y++;
                Position next = new Position(x, y);
                locationsToCheck.add(next);
                if(board.getPiece(next) != 0) {
                    break;
                }
            }
            x = startPos.file.value;
            y = startPos.rank.value;
            // down
            while(y > 0) {
                y--;
                Position next = new Position(x, y);
                locationsToCheck.add(next);
                if(board.getPiece(next) != 0) {
                    break;
                }
            }
            x = startPos.file.value;
            y = startPos.rank.value;
            // Direction : Up & right
            while(x < 7 && y < 7){
                x++;
                y++;
                Position next = new Position(x, y);
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
                Position next = new Position(x, y);
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
                Position next = new Position(x, y);
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
                Position next = new Position(x, y);
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
            
            
            
            // // reuse bishop and rook switches and combine the results of those calls.
                // List<MoveIntent> horizontalMoves = getValidMoves(ChessPiece.ROOK, startPos, board, moveRecord, playerColor);
                // validMoves = getValidMoves(ChessPiece.BISHOP, startPos, board, moveRecord, playerColor);
                // validMoves.addAll(horizontalMoves);
                break;
            case KING:
                x = startPos.file.value;
                y = startPos.rank.value;
                if(x < 7) {
                    locationsToCheck.add(new Position(x + 1, y));
                    if(y < 7)
                        locationsToCheck.add(new Position(x + 1, y + 1));
                    if (y > 0)
                        locationsToCheck.add(new Position(x + 1, y - 1));
                }
                if(x > 0){
                    locationsToCheck.add(new Position(x - 1, y));
                    if(y < 7)
                        locationsToCheck.add(new Position(x - 1, y + 1));
                    if (y > 0)
                        locationsToCheck.add(new Position(x - 1, y - 1));
                }
                if(y > 0)
                    locationsToCheck.add(new Position(x,y - 1));
                if(y < 7)
                    locationsToCheck.add(new Position(x, y + 1));

                // King wants to castle:
                // white king king side castle
                if(x == 3 && y == 0 && board.getPiece(new Position(1, 0)) == 0 && board.getPiece(new Position(2, 0)) == 0){
                    boolean legalMoveFlag = true;
                    for(MoveIntent move : moveRecord){
                        // if the king or the rook has ever moved, cannot castle
                        if(move.from.equals(new Position(0,0)) || move.from.equals(new Position(3,0))) {
                            legalMoveFlag = false;
                        }
                    }
                    // cannot castle when a king has to pass thru threatened square, or is in check.
                    if(positionUnderThreat(new Position(3, 0), 1, board) || positionUnderThreat(new Position(2, 0), 1, board)) {
                        legalMoveFlag = false;
                    }
                    if(legalMoveFlag)
                        locationsToCheck.add(new Position(x - 2, y));
                }
                // white king queen side castle
                if(x == 3 && y == 0 && board.getPiece(new Position(4, 0)) == 0 && board.getPiece(new Position(5, 0)) == 0 && board.getPiece(new Position(6, 0)) == 0){
                    boolean legalMoveFlag = true;
                    for(MoveIntent move : moveRecord){
                        if(move.from.equals(new Position(7,0)) || move.from.equals(new Position(3,0))) {
                            // if the king or the rook has ever moved, cannot castle
                            legalMoveFlag = false;
                        }
                    }
                    // cannot castle when a king has to pass thru threatened square, or is in check.
                    if(positionUnderThreat(new Position(3, 0), 1, board) || positionUnderThreat(new Position(4, 0), 1, board)) {
                        legalMoveFlag = false;
                    }
                    if(legalMoveFlag)
                        locationsToCheck.add(new Position(x + 2, y));
                }
                // black king side castle
                if(x == 3 && y == 6 && board.getPiece(new Position(1,6)) == 0 && board.getPiece(new Position(2,6)) == 0){
                    boolean legalMoveFlag = true;
                    for(MoveIntent move : moveRecord){
                        // if the king or the rook has ever moved, cannot castle
                        if(move.from.equals(new Position(0, 6))  || move.from.equals(new Position(3, 6))) {
                            legalMoveFlag = false;
                        }
                    }
                    // cannot castle when a king has to pass thru threatened square, or is in check.
                    if(positionUnderThreat(new Position(3, 6), -1, board) || positionUnderThreat(new Position(2, 6), -1, board)) {
                        legalMoveFlag = false;
                    }
                    if(legalMoveFlag)
                        locationsToCheck.add(new Position(x - 2, y));
                }
                if(x == 3 && y == 6 && board.getPiece(new Position(4,6)) == 0 && board.getPiece(new Position(5,6)) == 0){
                    boolean legalMoveFlag = true;
                    for(MoveIntent move : moveRecord){
                        // if the king or the rook has ever moved, cannot castle
                        if(move.from.equals(new Position(6, 6))  || move.from.equals(new Position(3, 6))) {
                            legalMoveFlag = false;
                        }
                    }
                    // cannot castle when a king has to pass thru threatened square, or is in check.
                    if(positionUnderThreat(new Position(3, 6), -1, board) || positionUnderThreat(new Position(4, 6), -1, board)) {
                        legalMoveFlag = false;
                    }
                    if(legalMoveFlag)
                        locationsToCheck.add(new Position(x + 2, y));
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
            default:
                throw new IllegalArgumentException("MoveValidator: Invalid piece type input");
        
        }

        ArrayList<MoveIntent> validMovesNotInCheck = new ArrayList<>();
        for(MoveIntent move : validMoves) {
            Board tempBoard = board.copy();
            tempBoard.updateBoard(move);
            if(tempBoard.inCheck() != playerColor.value) {
                validMovesNotInCheck.add(move);
            }
        }
        return validMovesNotInCheck;

    }

    public static List<MoveIntent> getAllValidMoves(GameState state, List<MoveIntent> moveHistory, PlayerColor team) {
        List<MoveIntent> validMoves = new ArrayList<>();
        Board board = state.board;
        for(int i = 0; i < board.board.length; i++) {
            int piece = board.board[i];
            // if piece is mine
            if(piece != 0 && piece / Math.abs(piece) == team.value) {
                validMoves.addAll(MoveValidator.getValidMoves(ChessPiece.FromInteger(piece), new Position(i), board, moveHistory, team));
            }
        }
        return validMoves;
    }




    public static boolean positionUnderThreat(Position loc, int team, Board board) {
        int x = loc.file.value;
        int y = loc.rank.value;
        // check for pawns:

        if ( team == 1 ) {
            if(x > 0 && y < 7) {
                Position leftCapture = new Position(x - 1, y + 1);
                if(board.getPiece(leftCapture) == (-team)){
                    return true;
                }
            }
            if(x < 7 && y < 7) {
                Position rightCapture = new Position(x + 1, y + 1);
                if(board.getPiece(rightCapture) == (-team)){
                    return true;
                }
            }
        } else {
            if(x > 0 && y > 0) {
                Position leftCapture = new Position(x - 1, y - 1);
                if(board.getPiece(leftCapture) == (-team)){
                    return true;
                }
            }
            if(x < 7 && y > 0) {
                Position rightCapture = new Position(x + 1, y - 1);
                if(board.getPiece(rightCapture) == (-team)){
                    return true;
                }
            }
        }



        // check for knights:
        if(x < 6) {
            if(y < 7 && board.getPiece(new Position(x + 2, y + 1)) == (-team * 3)){
                return true;
            }
            if(y > 0 && board.getPiece(new Position(x + 2, y - 1)) == (-team * 3)){
                return true;
            }
        }
        if(x > 1) {
            // left 2
            if(y < 7 && board.getPiece(new Position(x - 2, y + 1))  == (-team * 3)) {
                return true;
            } // left 2 up 1
            if(y > 0 && board.getPiece(new Position(x - 2, y - 1))  == (-team * 3)) {
                return true;
            } // left 2 down 1
        }
        if(y < 6) {
            // up 2
            if(x < 7 && board.getPiece(new Position(x + 1, y + 2))  == (-team * 3)) {
                return true;
            } // up 2 right 1
            if(x > 0 && board.getPiece(new Position(x - 1, y + 2))  == (-team * 3)) {
                return true;
            } // up 2 left 1
        }
        if(y > 1) {
            // down 2
            if(x < 7 && board.getPiece(new Position(x + 1, y - 2)) == (-team * 3)) {
                return true;
             } // down 2 right 1
            if(x > 0 && board.getPiece(new Position(x - 1, y - 2)) == (-team * 3)) {
                return true;
             } // down 2 left 1
        }

        // checking threat from rook/queen:
        while(x < 7) {
            x++;
            Position next = new Position(x, y);
            int piece = board.getPiece(next);
            if(piece == (-team * 2) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }
        x = loc.file.value;
        y = loc.rank.value;
        while(y < 7) {
            y++;
            Position next = new Position(x, y);
            int piece = board.getPiece(next);
            if(piece == (-team * 2) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }
        x = loc.file.value;
        y = loc.rank.value;
        while(x > 0) {
            x--;
            Position next = new Position(x, y);
            int piece = board.getPiece(next);
            if(piece == (-team * 2) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }
        x = loc.file.value;
        y = loc.rank.value;
        while(y < 7) {
            y++;
            Position next = new Position(x, y);
            int piece = board.getPiece(next);
            if(piece == (-team * 2) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }

        //checking threat from bishop/queen:
        x = loc.file.value;
        y = loc.rank.value;
        while(x < 7 && y < 7){
            x++;
            y++;
            Position next = new Position(x, y);
            int piece = board.getPiece(next);
            if(piece == (-team * 4) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }
        x = loc.file.value;
        y = loc.rank.value;
        while(x > 0 && y < 7){
            x--;
            y++;
            Position next = new Position(x, y);
            int piece = board.getPiece(next);
            if(piece == (-team * 4) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }
        x = loc.file.value;
        y = loc.rank.value;
        while(x < 7 && y > 0){
            x++;
            y--;
            Position next = new Position(x, y);
            int piece = board.getPiece(next);
            if(piece == (-team * 4) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }
        x = loc.file.value;
        y = loc.rank.value;
        while(x > 0 && y > 0){
            x--;
            y--;
            Position next = new Position(x, y);
            int piece = board.getPiece(next);
            if(piece == (-team * 4) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }
        return false;
    }
}
