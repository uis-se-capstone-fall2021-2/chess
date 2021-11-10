package chess.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import chess.ChessPiece;
import chess.MoveIntent;
import chess.MoveValidator;
import chess.PlayerColor;
import chess.Position;
import chess.board.Board;
import chess.game.GameState;
import chess.player.model.Player;

public abstract class ChessAI extends Player {
    public abstract MoveIntent chooseMove(GameState state, List<MoveIntent> moveHistory);

    @Override
    public String getDisplayName() {
        return this.getClass().getSimpleName() + " AI";
    }

    @Override
    public void notify(GameState gameState) {
        //TODO: does notify need to do anything for AI?
    }

    // moveValidator has a function to get all moves for a specific piece, this uses that function
    // to get all possible moves for all positions.
    protected List<MoveIntent> getAllValidMoves(GameState state, List<MoveIntent> moveHistory) {
        List<MoveIntent> validMoves = new ArrayList<>();
        Board board = state.board;
        PlayerColor team = (state.players[0] == this.playerId) ? PlayerColor.WHITE : PlayerColor.BLACK;
        for(int i = 0; i < board.board.length; i++) {
            int piece = board.board[i];
            // if piece is mine
            if(piece / Math.abs(piece) == team.value) {
                validMoves.addAll(MoveValidator.getValidMoves(ChessPiece.FromInteger(piece), new Position(i), board, moveHistory, team));
            }
        }
        return validMoves;
    }

    // returns simple piece values
    protected double getPieceValue(int piece) {
        int team = piece / Math.abs(piece);
        switch(Math.abs(piece)) {
            case 0:
            return 0;
            case 1:
            return (1.0) * team;
            case 2:
            return (3.2) * team;
            case 3:
            case 4:
            return (3.33) * team;
            case 5:
            return (5.1) * team;
            case 6:
            return (99999.0) * team;
            default:
            return 0;

        }
    }
    protected double getBoardScore(Board board) {
        double boardScore = 0.0;
        for(int i = 0; i < board.board.length; i++){
            int piece = board.board[i];
            boardScore+= getPieceValue(piece);
        }
        return boardScore;
    }
}