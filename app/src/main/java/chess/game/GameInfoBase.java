package chess.game;

import java.util.Date;

import lombok.AllArgsConstructor;

/**
 * A lightweight summary of a Game for display in a list view
 */
@AllArgsConstructor
public class GameInfoBase {
    public final long gameId;
    public final long owner; // playerId
    public final long winner; // playerId
    public final long[] players; // playerId, array of length 2, first player is white
    public final long moveCount;
    public final GameCompletionState state;
}
