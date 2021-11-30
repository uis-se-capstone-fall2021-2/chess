package chess;

import chess.board.Board;

public class ChessTestUtilities {
    public static MoveIntent stringToMoveIntent(String input, Board board){
        String from = input.split(" ")[0];
        String to = input.split(" ")[1];
        ChessPiece promotion = ChessPiece.NONE;
        if(input.split(" ").length == 3){
            promotion = ChessPiece.QUEEN;
        }
        Position posTo = stringToPosition(to);
        Position posFrom = stringToPosition(from);
        ChessPiece type = ChessPiece.FromInteger(board.getPiece(posFrom));
        return new MoveIntent(type, posFrom, posTo, promotion);

    }

    public static Position stringToPosition(String input) {
        int x=0, y=0;
        switch(input.charAt(0)) {
            case 'a':
            x = 0;
            break;
            case 'b':
            x = 1;
            break;
            case 'c':
            x = 2;
            break;
            case 'd':
            x = 3;
            break;
            case 'e':
            x = 4;
            break;
            case 'f':
            x = 5;
            break;
            case 'g':
            x = 6;
            break;
            case 'h':
            x = 7;
            break;
        }
        switch(input.charAt(1)) {
            case '1':
            y = 0;
            break;
            case '2':
            y = 1;
            break;
            case '3':
            y = 2;
            break;
            case '4':
            y = 3;
            break;
            case '5':
            y = 4;
            break;
            case '6':
            y = 5;
            break;
            case '7':
            y = 6;
            break;
            case '8':
            y = 7;
            break;
        }

        return new Position(x,y);
    }
}

