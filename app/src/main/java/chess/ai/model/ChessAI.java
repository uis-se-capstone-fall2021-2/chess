package chess.ai.model;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import chess.MoveIntent;
import chess.game.GameState;
import chess.game.GameStatus;
import chess.game.service.GameService;
import chess.player.model.Player;


/**
 * Generic AI template to be extended by specific AI implementations.
 */
@Entity
@DiscriminatorValue(value=Player.PlayerType.AI)
@DiscriminatorColumn(name="AI_TYPE")
public abstract class ChessAI extends Player {

    public static class AIType {
        public static final String Advanced = "Advanced";
        public static final String Beginner = "Beginner";
    }

    protected ChessAI() {
        super();
    }

    public abstract MoveIntent chooseMove(GameState state, List<MoveIntent> moveHistory);

    @Override
    public String getPlayerType() {
        return Player.PlayerType.AI;
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