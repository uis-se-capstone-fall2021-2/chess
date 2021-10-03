package chess.board;
import java.util.ArrayList;
import chess.*;
public class Board implements IBoard {
    int[] board;

    public Board() {
        board = new int[] {
            2,3,4,6,5,4,3,2,
            1,1,1,1,1,1,1,1,
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
            -1,-1,-1,-1,-1,-1,-1,-1,
            -2,-3,-4,-6,-5,-4,-3,-2
        };

        // Do moves from here
    }
    public int getPiece(Position position){
        return board[position.rank.value * 8 + position.file.value];
    }
    public int[] updateBoard(MoveIntent intent){
        return null;
    }

}