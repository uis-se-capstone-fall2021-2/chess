package chess.game.db;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import chess.MoveIntent;
import chess.MoveValidator;
import chess.PlayerColor;
import chess.game.GameCompletionState;
import chess.game.GameInfo;
import chess.game.GameState;

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
  private long player1; // playerId, player1 is white
  @Column
  private long player2; // playerId, player2 is black
  @Column
  private GameCompletionState completionState;
  @Column
  @ElementCollection(targetClass=Move.class)
  private List<Move> moves = new ArrayList<Move>();

  private final int[] board;

  public Game() {
    this.board = initializeBoard(moves);
  }

  public Game(
    long player1,
    long player2,
    long owner
  ) {
    this.player1 = player1;
    this.player2 = player2;
    this.board = initializeBoard(moves);
    this.owner = owner;
    this.completionState = GameCompletionState.ACTIVE;
  }

  private int[] initializeBoard(List<Move> moves) {
    int[] board = new int[64];
    // replay the moves to bring the board to current state
    for(Move move : moves){
      // TODO: update board with move
    }

    return board;
  }

  public long getGameId() {
    return id;
  }
  public long getOwner() {
    return owner;
  }

  public long getWinner() {
    return winner;
  }
  public void setWinnner(long playerId) {
    winner = playerId;
  }

  public long[] getPlayers() {
    return new long[] {player1, player2};
  }
  public boolean hasPlayer(long playerId) {
    return (
      player1 == playerId ||
      player2 == playerId
    );
  }

  public GameCompletionState getCompletionState() {
    return completionState;
  }
  public void setCompletionState(GameCompletionState state) {
    completionState = state;
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
      moves.size(),
      getCompletionState()
    );
  }

  public GameState getGameState() {
    return new GameState(
      getGameId(),
      getOwner(),
      getWinner(),
      getPlayers(),
      moves.size(),
      playerInCheck(),
      getCompletionState()
    );
  }

  public long currentPlayer() {
    return getPlayers()[(int)moves.size() % 2];
  }

  public long playerInCheck() {
    // TODO: determine if one of the players is in check
    return -1;
  }


  public boolean move(long playerId, MoveIntent intent){
    MoveValidator validator = new MoveValidator();

    int moveCount = moves.size();
    
    PlayerColor playerColor = moveCount % 2 == 0 ? PlayerColor.WHITE: PlayerColor.BLACK;
    
    // validate with MoveValidator
    if(validator.validateMove(intent, this.board, getMoveHistory(), playerColor)){
        moves.add(new Move(intent));

        // TODO: update board
        return true;
    } else {
      return false;
    }
  }
}
