package chess.game;

import java.util.Date;


/**
 * A lightweight summary of a Game for display in a list view
 */
public class GameInfo extends GameInfoBase {

    public final Date createdAt;
    public final Date updatedAt;
    public final Date completedAt;
    
    public GameInfo(
        long gameId,
        long owner, // playerId
        long winner, // playerId, array of length 2, first player is white
        long[] players,
        long moveCount,
        GameStatus status,
        Date createdAt,
        Date updatedAt,
        Date completedAt
    ) {
        super(gameId, owner, winner, players, moveCount, status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }
}
