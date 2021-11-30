package chess.ai.model;
import java.util.List;

import javax.persistence.Entity;

import chess.MoveIntent;
import chess.board.Board;
import chess.game.GameState;
import chess.game.GameStatus;
import chess.game.service.GameService;
import chess.player.model.Player;

@Entity
public abstract class ChessAI extends Player {



    public abstract MoveIntent chooseMove(GameState state, List<MoveIntent> moveHistory);

    protected ChessAI() {
        super();
    }

    @Override
    public void notify(GameState gameState, List<MoveIntent> moveHistory) {
        GameService gameService = springApplicationContext.getBean(GameService.class);

        if(gameState.status == GameStatus.ACTIVE) {
            MoveIntent move = chooseMove(gameState, moveHistory);
        
            gameService.move(
                gameState.gameId,
                getPlayerId(),
                move
            );
        } else if (gameState.status == GameStatus.PENDING) {
            gameService.acceptGameInvite(gameState.gameId, getPlayerId());
        }

    }
    


}