package chess.game.db;

import java.util.ArrayList;
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
  private long id;
  @Column
  private long owner; // playerId
  @Column
  private long winner; // playerId
  @Column
  private long[] players; // playerId, array of length 2, first player is white
  @Column
  private GameCompletionState completionState;
  @Column
  @ElementCollection(targetClass=Move.class)
  private List<Move> moves = new ArrayList<Move>();

  public long getGameId() {
    return id;
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
    List<MoveIntent> history = new ArrayList<MoveIntent>();
    int i = 0;
    for(Move move: moves) {
      history.add(i++, move.asIntent());
    }
    return history;
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
