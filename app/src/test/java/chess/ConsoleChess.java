package chess;

import chess.game.GameStatus;
import chess.game.model.Game;
import chess.ai.model.Advanced;
import chess.ai.model.Beginner;
import chess.ai.model.BoardEvaluation;
import chess.board.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class ConsoleChess {


    public static void main(String[] args) {
        Game game = new Game(1,0,1);
        Beginner ai = new Beginner();
        Scanner scan = new Scanner(System.in);


        while(true) {


            System.out.println(game.currentPlayerColor() + " to move ");
            MoveIntent aiMove = ai.chooseMove(game.getGameState(), game.getMoveHistory());
            game.move(1, aiMove);
            drawChessBoard(game);
            System.out.println("AI has moved");
            if(game.getStatus() == GameStatus.COMPLETE) {
                System.out.println("Game has ended: Winning PlayerID: " + game.getWinner());
                break;
            }

            System.out.println(game.currentPlayerColor() + " to move ");
            System.out.print("Enter a move (\"e2 e4\"): ");
            String moveChoice = scan.nextLine();
            MoveIntent intendedMove = ChessTestUtilities.stringToMoveIntent(moveChoice, game.getBoard());
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

    private static void drawChessBoard(Game g){
        System.out.println();
        Board board = g.getBoard();

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

        System.out.println(BoardEvaluation.getBoardScore(g.getBoard()));
        System.out.println();
    }

    private static String getPieceLetter(int piece){
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
