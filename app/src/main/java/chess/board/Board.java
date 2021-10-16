package chess.board;
import java.util.ArrayList;
import chess.*;
public class Board implements IBoard {
    public final int[] board;

    public Board() {
        board = new int[] {
            2,3,4,6,5,4,3,2,
            1,1,1,1,1,1,1,1,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            -1,-1,-1,-1,-1,-1,-1,-1,
            -2,-3,-4,-6,-5,-4,-3,-2
        };
    }

    public Board(ArrayList<MoveIntent> moveRecord) {
        board = new int[] {
            2,3,4,6,5,4,3,2,
            1,1,1,1,1,1,1,1,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            -1,-1,-1,-1,-1,-1,-1,-1,
            -2,-3,-4,-6,-5,-4,-3,-2
        };

        // replay the moves to bring the board to current state
        for(MoveIntent move : moveRecord){
            // update board with move
            updateBoard(move, moveRecord.size() % 2);
        }
    }

    public Board( int[] board ) {
        this.board = board;
    }

    public Board copy() {
        int[] newBoard = new int[board.length];
        for(int i = 0; i < board.length; i++) {
            newBoard[i] = board[i];
        }
        return new Board(newBoard);
    }

    public int getPiece(Position position){
        return board[position.rank.value * 8 + position.file.value];
    }

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

    public int[] updateBoard(MoveIntent intent, int playerNumber){
        int fromPosition = intent.from.rank.value * 8 + intent.from.file.value;
        int toPosition = intent.to.rank.value * 8 + intent.to.file.value;

        board[fromPosition] = 0;

        if(playerNumber == 1)
            board[toPosition] = intent.chessPiece.value;
        else
            board[toPosition] = -intent.chessPiece.value;

        return board;
    }
    /*** inCheck return which player is in check, or a 0 if nobody is
     * 
     * @return int: -1, 0, or 1
     */
    public int inCheck() {
        Position bKing = getPositionOf(-6);
        Position wKing = getPositionOf(6);
        if(positionUnderThreat(bKing, -1)) return -1;
        if(positionUnderThreat(wKing, 1)) return 1;
        return 0;
        // CHECK bKing in check:
    }

    private boolean positionUnderThreat(Position loc, int team) {
        int x = loc.file.value;
        int y = loc.rank.value;
        // check for pawns:

        if ( team == 1 ) {
            if(x > 0 && y < 7) {
                Position leftCapture = new Position(File.FromInteger(loc.file.value - 1), Rank.FromInteger(loc.rank.value + 1));
                if(board.getPiece(leftCapture) == (-team)){
                    return true;
                }
            }
            if(x < 7 && y < 7) {
                Position rightCapture = new Position(File.FromInteger(loc.file.value + 1), Rank.FromInteger(loc.rank.value + 1));
                if(board.getPiece(rightCapture) == (-team)){
                    vreturn true;
                }
            }
        } else {
            if(x > 0 && y > 0) {
                Position leftCapture = new Position(File.FromInteger(loc.file.value - 1), Rank.FromInteger(loc.rank.value - 1));
                if(board.getPiece(leftCapture) == (-team)){
                    return true;
                }
            }
            if(x < 7 && y > 0) {
                Position rightCapture = new Position(File.FromInteger(loc.file.value + 1), Rank.FromInteger(loc.rank.value - 1));
                if(board.getPiece(rightCapture) == (-team)){
                    vreturn true;
                }
            }
        }



        // check for knights:
        if(loc.file.value < 6) {
            if(loc.rank.value < 7){
                if(getPiece(new Position(File.FromInteger(loc.file.value + 2), Rank.FromInteger(startPos.rank.value + 1))) == (-team * 3)))
                    return true;
            }
            if(loc.rank.value > 0){
                if(getPiece(new Position(File.FromInteger(loc.file.value + 2), Rank.FromInteger(startPos.rank.value - 1))) == (-team * 3)))
                    return true;
            }
        }
        if(loc.file.value > 1) {
            // left 2
            if(loc.rank.value < 7) {
                if(getPiece(new Position(File.FromInteger(loc.file.value - 2), Rank.FromInteger(loc.rank.value + 1)))  == (-team * 3))
                    return true;
            } // left 2 up 1
            if(loc.rank.value > 0) {
                if(getPiece(new Position(File.FromInteger(loc.file.value - 2), Rank.FromInteger(loc.rank.value - 1)))  == (-team * 3))
                    return true;
            } // left 2 down 1
        }
        if(loc.rank.value < 6) {
            // up 2
            if(loc.file.value < 7) {
                if(getPiece(new Position(File.FromInteger(loc.file.value + 1), Rank.FromInteger(loc.rank.value + 2)))  == (-team * 3))
                    return true;
            } // up 2 right 1
            if(loc.file.value > 0) {
                if(getPiece(new Position(File.FromInteger(loc.file.value - 1), Rank.FromInteger(loc.rank.value + 2)))  == (-team * 3))
                    return true;
            } // up 2 left 1
        }
        if(loc.rank.value > 1) {
            // down 2
            if(loc.file.value < 7) {
                if(getPiece(new Position(File.FromInteger(loc.file.value + 1), Rank.FromInteger(loc.rank.value - 2))) == (-team * 3))
                    return true
             } // down 2 right 1
            if(loc.file.value > 0) {
                if(getPiece(new Position(File.FromInteger(loc.file.value - 1), Rank.FromInteger(loc.rank.value - 2))) == (-team * 3))
                    return true
             } // down 2 left 1
        }

        // checking threat from rook/queen:
        while(x < 7) {
            x++;
            Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
            int piece = getPiece(next);
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
            Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
            int piece = getPiece(next);
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
            Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
            int piece = getPiece(next);
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
            Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
            int piece = getPiece(next);
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
            Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
            int piece = getPiece(next);
            if(piece == (-team * 2) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }
        while(x > 0 && y < 7){
            x--;
            y++;
            Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
            int piece = getPiece(next);
            if(piece == (-team * 2) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }
        while(x < 7 && y > 0){
            x++;
            y--;
            Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
            int piece = getPiece(next);
            if(piece == (-team * 2) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }
        while(x > 0 && y > 0){
            x--;
            y--;
            Position next = new Position(File.FromInteger(x), Rank.FromInteger(y));
            int piece = getPiece(next);
            if(piece == (-team * 2) || piece == (-team * 5)) {
                return true;
            }
            if(piece != 0) {
                break;
            }
        }

    }


}
