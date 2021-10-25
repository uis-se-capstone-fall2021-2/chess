package chess.game.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import chess.ChessPiece;
import chess.File;
import chess.MoveIntent;
import chess.MoveValidator;
import chess.Position;
import chess.Rank;
import chess.PlayerColor;
import chess.board.Board;
import chess.game.GameCompletionState;
import chess.game.GameInfo;
import chess.game.GameState;


@Entity
@Table(name="Games")
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  @Getter
  private long gameId;
  @Column
  @Getter
  private long owner; // playerId
  @Column
  @Getter
  @Setter
  private long winner; // playerId
  @Column
  @Getter
  private long player1; // playerId, player1 is white
  @Column
  @Getter
  private long player2; // playerId, player2 is black
  @Column
  private GameCompletionState completionState;
  @Column
  @ElementCollection(targetClass=Move.class)
  private List<Move> moves = new ArrayList<Move>();

  @Transient
  private Board board = new Board();

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

  private Board initializeBoard(List<Move> moves) {
    ArrayList<MoveIntent> moveRecord = new ArrayList<>();
    for(Move move : moves){
      moveRecord.add(move.asIntent());
    }

    return board = new Board(moveRecord);
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
      -1,
      this.board,
      getCompletionState()
    );
  }

  public long currentPlayer() {
    return getPlayers()[(int)moves.size() % 2];
  }
  public PlayerColor currentPlayerColor() {
    if(currentPlayer() == player1)
      return PlayerColor.WHITE;
    else
      return PlayerColor.BLACK;
  }
  // determine if one of the players is in check
  public long playerInCheck() {
    // check if white king is in check
    // first, get position of white king
    Position whiteKingLocation = board.getPositionOf(ChessPiece.KING.value);
    // check every black piece to see if white king's position is a possible move
    // if it is, return white player's id
    for(int row = 0; row < 8; row++){
      for(int column = 0; column < 8; column++){
        Position position = new Position(File.FromInteger(column), Rank.FromInteger(row));
        int chessPiece = board.getPiece(position);
        // get black piece
        if(board.getPiece(position) < 0){
          // if white king's location is possible move, then white king is in check
          MoveIntent intent = new MoveIntent(ChessPiece.FromInteger(chessPiece), position, whiteKingLocation);
          if(MoveValidator.validateMove(intent, board, getMoveHistory(), currentPlayerColor())) {
            return player1;
          }
        }
      }
    }

    // check if black king is in check
    // first, get position of black king
    Position blackKingLocation = board.getPositionOf(-ChessPiece.KING.value);
    // check every white piece to see if black king's position is a possible move
    // if it is, return black player's id
    for(int row = 0; row < 8; row++){
      for(int column = 0; column < 8; column++){
        Position position = new Position(File.FromInteger(column), Rank.FromInteger(row));
        int chessPiece = board.getPiece(position);
        // get white piece
        if(board.getPiece(position) > 0){
          // if black king's location is a possible move, then black king is in check
          MoveIntent intent = new MoveIntent(ChessPiece.FromInteger(chessPiece), position, blackKingLocation);
          if(MoveValidator.validateMove(intent, board, getMoveHistory(), currentPlayerColor())){
            return player2;
          }
        }
      }
    }
    return -1;
  }


  public boolean move(long playerId, MoveIntent intent){
    if(MoveValidator.validateMove(intent, this.board, getMoveHistory(), currentPlayerColor())){
        moves.add(new Move(intent));

        board.updateBoard(intent);
        return true;
        
    } else {
      return false;
    }
  }
}
