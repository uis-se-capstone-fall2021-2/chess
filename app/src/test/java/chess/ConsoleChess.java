package chess;

import chess.game.GameStatus;
import chess.game.model.Game;
import chess.ai.Advanced;
import chess.ai.ChessAI;
import chess.board.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class ConsoleChess {


    public static void main(String[] args) {
        Game game = new Game(1,0,1);
        Advanced ai = new Advanced(PlayerColor.WHITE);
        Scanner scan = new Scanner(System.in);


        while(true) {


            System.out.println(game.currentPlayerColor() + " to move ");
            MoveIntent aiMove = ai.chooseMove(game.getGameState(), game.getMoveHistory());
            game.move(1, aiMove);
            System.out.println("AI has moved");
            if(game.getStatus() == GameStatus.COMPLETE) {
                System.out.println("Game has ended: Winning PlayerID: " + game.getWinner());
                break;
            }
            drawChessBoard(game);
            System.out.println(game.currentPlayerColor() + " to move ");
            System.out.print("Enter a move (\"e2 e4\"): ");
            String moveChoice = scan.nextLine();
            MoveIntent intendedMove = stringToMoveIntent(moveChoice, game.getBoard());
            System.out.println("Move: " + intendedMove.from.toString() + " -> " + intendedMove.to.toString());
            if(!game.move(0, intendedMove)){
                System.out.println("INVALID MOVE");
                continue;
            }
            if(game.getStatus() == GameStatus.COMPLETE) {
                System.out.println("Game has ended: Winning PlayerID: " + game.getWinner());
                break;
            }
            drawChessBoard(game);

        }
        scan.close();
    }

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

    public static void drawChessBoard(Game g){
        System.out.println();
        Board board = g.getBoard();
        // for(int i = board.board.length - 1; i >= 0; i--) {
        //     System.out.print(getPieceLetter(board.getPiece(new Position(i))) + " ");
        //     if(i%8==0) System.out.print("\n");
        // }

        List<List<Integer>> rows = new ArrayList<>();
        List<Integer> thisRow = new ArrayList<>();
        for(int i = 0; i < board.board.length; i++){
            if(i%8==0 && i>0) {
                rows.add(thisRow);
                thisRow = new ArrayList<>();
            }
            thisRow.add(board.board[i]);
        }
        rows.add(thisRow);
        Collections.reverse(rows);
        for(List<Integer> row : rows){
            for(Integer item : row) {
                System.out.print(getPieceLetter(item) + " ");
            }
            System.out.println();
        }

        System.out.println(ChessAI.getBoardScore(g.getBoard()));
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
