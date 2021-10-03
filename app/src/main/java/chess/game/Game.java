package chess.game;

import chess.MoveIntent;
import chess.Player;
import chess.PlayerColor;
import chess.MoveValidator;
import java.util.ArrayList;

public class Game implements IGame {
    private final int gameId;
    private final Player[] players; // [Player1, Player2], turn order is determined by index. index 0 is white, index 1 is black
    private final int[] board;
    private long moveCount; // determine current player by players[moveCount % 2]
    private boolean complete;
    private Player winner;
    private Player inCheck;
    private ArrayList<MoveIntent> moves;
    private long owner;
    /**
     * Represents a game between 2 players
     * @param players
     */
    public Game(
        int gameId,
        Player[] players,
        ArrayList<MoveIntent> moves
    ) {
        this.gameId = gameId;
        this.players = players;
        this.board = new int[64];
        this.moveCount = moves.size();
        this.complete = false;
        this.winner = null;
        // TODO: create the default chess board. Also, Should we leave the board as an int array, or should we change ChessPiece to include information on which team a piece is on
    }

    private void initializeFromSavedState(ArrayList<MoveIntent> moves) {
        // replay the moves to bring the board to current state
        for(MoveIntent move : moves){
            move(players[(int)moveCount % 2], move);
        }

        // notify players
        players[0].notify(this.getGameState());
        players[1].notify(this.getGameState());
    }

    public int gameId() {
        return this.gameId; // TODO: I feel like my IDE should be complaining about this but isn't
    }

    public boolean move(Player player, MoveIntent intent){
        MoveValidator validator = new MoveValidator();

        // verify it is player's turn
        if(players[(int)moveCount % 2] == player){
            PlayerColor playerColor = moveCount % 2 == 0 ? PlayerColor.WHITE: PlayerColor.BLACK;
            
            // validate with MoveValidator
            if(validator.validateMove(intent, this.board, moves, playerColor)){
                moves.add(intent);
                //notify players
                players[0].notify(this.getGameState());
                players[1].notify(this.getGameState());

                moveCount++;
                return true;
            }
        }
        return false;
    }

    // determine game state
    // return new GameState(this.gameId, ...)
    public GameState getGameState(){
        
        return new GameState(this.gameId, this.owner, this.winner.playerId, thePlayers, this.moveCount, this.inCheck.playerId, gameCompletionState);
    }

}
