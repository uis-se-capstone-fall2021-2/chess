
package game;
/**
 * Abstract player class, should be exteneded by {@link game.ChessAIPlayer} and {@link game.userPlayer}
 */
public abstract class Player extends PlayerInfo{
    /**
     * Inherited constructor from {@link game.PlayerInfo}
     * @param playerId
     * @param alias
     */
    public Player(int playerId, String alias) {
        super(playerId, alias);
    }
    /**
     *  Used to notify player of changeins in gamestate.
     *  Should notify when player is in check, when the game is over,
     *  and when the player's turn has begun
     *  @param gameState
     */
    abstract void notify(GameState gameState);

}