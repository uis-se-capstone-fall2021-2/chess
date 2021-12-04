package chess.board;
import java.util.ArrayList;
import java.util.Arrays;

import chess.*;
/**
 * Representation of a chess board with an internal 1d integer array.
 * Board can be updated with new moves through {@link #updateBoard(MoveIntent)}
 * 
 */
public class Board implements IBoard {
    public final int[] board;

    public Board() {
        board = new int[] {
            2,3,4,5,6,4,3,2,
            1,1,1,1,1,1,1,1,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            -1,-1,-1,-1,-1,-1,-1,-1,
            -2,-3,-4,-5,-6,-4,-3,-2
        };
    }

    public Board(ArrayList<MoveIntent> moveRecord) {
        board = new int[] {
            2,3,4,5,6,4,3,2,
            1,1,1,1,1,1,1,1,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            -1,-1,-1,-1,-1,-1,-1,-1,
            -2,-3,-4,-5,-6,-4,-3,-2
        };

        // replay the moves to bring the board to current state
        for(MoveIntent move : moveRecord){
            updateBoard(move);
        }
    }

    public Board( int[] board ) {
        this.board = board;
    }

    public Board copy() {
        int[] newBoard = Arrays.copyOf(board, board.length);
        return new Board(newBoard);
    }
    public int getPiece(Position position){
        return board[position.rank.value * 8 + position.file.value];
    }
    
    /** Gets the position of a requested integer representation of a piece (allowing negative input for a black piece).
     *  In the case of duplicate pieces, returns the first piece of correct type.
     * @param piece integer representation of requested piece
     * @return Position position of requested piece on board.
     */
    public Position getPositionOf(int piece){
        Position position = null;
        for(int row = 0; row < 8; row++){
            for(int column = 0; column < 8; column++){
                position = new Position(File.FromInteger(column), Rank.FromInteger(row));
                if(getPiece(position) == piece) {
                    return position;
                }
            }
        }
        return position;
    }
    public int[] updateBoard(MoveIntent intent){
        int fromIndex = intent.from.rank.value * 8 + intent.from.file.value;
        int toIndex = intent.to.rank.value * 8 + intent.to.file.value;
        int piece = getPiece(intent.from);

        if(intent.promotion != ChessPiece.NONE){
            // maintain piece's team:
            piece = (piece > 0) ? intent.promotion.value : -intent.promotion.value;
        }
        // En Passant
        if(intent.chessPiece == ChessPiece.PAWN && (intent.from.file != intent.to.file) && getPiece(intent.to) == 0){
            int opposingPawnRank = (piece > 0) ? intent.to.rank.value - 1 : intent.to.rank.value + 1;
            board[opposingPawnRank * 8 + intent.to.file.value] = 0;
        }
        if(intent.chessPiece == ChessPiece.KING && (Math.abs(intent.to.file.value - intent.from.file.value) == 2)) {
            if(intent.to.file.value - intent.from.file.value == 2){
                // kingside castle
                board[intent.to.rank.value * 8 + 7] = 0;
                board[toIndex - 1] = (piece > 0) ? 2 : -2;
            } else {
                // queenside castle
                board[intent.to.rank.value * 8] = 0;
                board[toIndex + 1] = (piece > 0) ? 2 : -2;
            }
        }

        board[fromIndex] = 0;
        board[toIndex] = piece;
        return board;
    }
    /** figures out who is in check, if anybody
     * 
     * @return InCheck enum representing which team is in check
     */
    public InCheck inCheck() {
        Position bKing = getPositionOf(-6);
        Position wKing = getPositionOf(6);
        boolean blackInCheck = MoveValidator.positionUnderThreat(bKing, -1, this);
        boolean whiteInCheck = MoveValidator.positionUnderThreat(wKing, 1, this);

        if(whiteInCheck && blackInCheck) {
            // avoid a situation where white's move is seen as valid because it 
            // puts black in check, despite white actually being in check.
            return InCheck.BOTH;
        } else {
            if(whiteInCheck){
                return InCheck.WHITE;
            }
            if(blackInCheck){
                return InCheck.BLACK;
            }
        }

        return InCheck.NONE;

    }


}
