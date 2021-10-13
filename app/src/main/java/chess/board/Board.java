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
}
