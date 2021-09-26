package chess.game.db;

import java.util.List;
import javax.persistence.*;

import chess.MoveIntent;
import chess.game.GameCompletionState;
import chess.game.GameInfo;

@Entity
@Table(name="Games")
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private long gameId;
  @Column
  private long owner; // playerId
  @Column
  private long winner; // playerId
  @Column
  private long[] players; // playerId, array of length 2, first player is white
  @Column
  private GameCompletionState completionState;
  @Column
  private List<MoveIntent> moveHistory;

  public long getGameId() {
    return gameId;
  }
  public long getOwner() {
    return owner;
  }
  public long getWinner() {
    return winner;
  }
  public long[] getPlayers() {
    return players;
  }
  public GameCompletionState getCompletionState() {
    return completionState;
  }
  public List<MoveIntent> getMoveHistory() {
    return moveHistory;
  }

  public GameInfo info() {
    return new GameInfo(
      getGameId(),
      getOwner(),
      getWinner(),
      getPlayers(),
      getMoveHistory().size(),
      getCompletionState());
  }
}
