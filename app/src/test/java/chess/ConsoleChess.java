package chess;

import chess.game.model.Game;
import chess.ai.Beginner;
import chess.board.Board;

import java.util.Scanner;

import chess.Position;

public class ConsoleChess {


    public static void main(String[] args) {
        Game game = new Game(0,1,0);
        Beginner ai = new Beginner();
        Scanner scan = new Scanner(System.in);


        while(true) {

            if(game.getWinner() != 0){

            }

            drawChessBoard(game);
            System.out.println(game.currentPlayerColor() + " to move ");
            System.out.print("Enter a move (\"e2 e4\"): ");
            String moveChoice = scan.nextLine();
            MoveIntent intendedMove = stringToMoveIntent(moveChoice, game.getBoard());
            if(!game.move(0, intendedMove)){
                System.out.println("INVALID MOVE");
                continue;
            }
            System.out.println(game.currentPlayer());
            drawChessBoard(game);
            System.out.println(game.currentPlayerColor() + " to move ");
            MoveIntent aiMove = ai.chooseMove(game.getGameState(), game.getMoveHistory(), PlayerColor.BLACK);
            game.move(1, aiMove);
            System.out.println("AI has moved");
        }
        
    }

    public static MoveIntent stringToMoveIntent(String input, Board board){
        String from = input.split(" ")[0];
        String to = input.split(" ")[1];
        Position posTo = stringToPosition(to);
        Position posFrom = stringToPosition(from);
        ChessPiece type = ChessPiece.FromInteger(board.getPiece(posFrom));
        return new MoveIntent(type, posFrom, posTo);

    }

    public static Position stringToPosition(String input) {
        int x=0, y=0;
        switch(input.charAt(0)) {
            case 'a':
            x = 7;
            break;
            case 'b':
            x = 6;
            break;
            case 'c':
            x = 5;
            break;
            case 'd':
            x = 4;
            break;
            case 'e':
            x = 3;
            break;
            case 'f':
            x = 2;
            break;
            case 'g':
            x = 1;
            break;
            case 'h':
            x = 0;
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

    public static void drawChessBoard(Game g){
        System.out.println();
        Board board = g.getBoard();
        for(int i = board.board.length - 1; i >= 0; i--) {
            System.out.print(getPieceLetter(board.getPiece(new Position(i))) + " ");
            if(i%8==0) System.out.print("\n");
        }
        System.out.println();
    }

    public static String getPieceLetter(int piece){
        String out = "";
        if(piece == 0) {
            return "--";
        } 
        out = (piece > 0) ? "+" : "-";
        switch(Math.abs(piece)){
            case 1:
                out+= "P";
                break;
            case 2:
                out+= "R";
                break;
            case 3:
                out+= "N";
                break;
            case 4:
                out+= "B";
                break;
            case 5:
                out+= "Q";
                break;
            case 6:
                out+= "K";
                break;
        }
        return out;
    }
}
