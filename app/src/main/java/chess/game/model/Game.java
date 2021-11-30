package chess.game.model;

import java.util.Date;
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
import chess.board.InCheck;
import chess.game.GameStatus;
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
  private Date createdAt;

  @Column
  @Getter
  private Date updatedAt;

  @Column
  @Getter
  private Date completedAt;

  @Column
  @Getter
  private long owner; // playerId

  @Column
  @Getter
  @Setter
  private long winner; // playerId | -1 if stalemate?

  @Column
  @Getter
  private long player1; // playerId, player1 is white

  @Column
  @Getter
  private long player2; // playerId, player2 is black

  @Column
  @Getter
  @Setter
  private GameStatus status;

  @Column
  @Getter
  @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
  @OrderBy("timestamp ASC")
  @ElementCollection(targetClass=Move.class)
  private List<Move> moves = new ArrayList<Move>();

  private static final long STALEMATE = -1;

  @Transient
  private Board _board;

  public Game() {}

  public Game(
    long player1,
    long player2,
    long owner
  ) {
    this.player1 = player1;
    this.player2 = player2;
    this.owner = owner;
    this.status = GameStatus.PENDING;
  }

  private Board initializeBoard() {
    ArrayList<MoveIntent> moveRecord = new ArrayList<>();
    for(Move move : getMoves()){
      moveRecord.add(move.asIntent());
    }

    return new Board(moveRecord);
  }

  public Board getBoard() {
    if(_board == null) {
      _board = initializeBoard();
    }
    return _board;
  }

  @PreUpdate
  @PrePersist
  public void updateTimeStamps() {
    updatedAt = new Date();
    if(createdAt == null) {
      createdAt = new Date();
    }
    if(completedAt == null && status.compareTo(GameStatus.ACTIVE) > 0) {
      completedAt = new Date();
    }
      
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

  public List<MoveIntent> getMoveHistory() {
    List<MoveIntent> history = new ArrayList<MoveIntent>();
    int i = 0;
    for(Move move: getMoves()) {
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
      getMoves().size(),
      getStatus(),
      getCreatedAt(),
      getUpdatedAt(),
      getCompletedAt()
    );
  }

  public GameState getGameState() {
    return new GameState(
      getGameId(),
      getOwner(),
      getWinner(),
      getPlayers(),
      getMoves().size(),
      playerInCheck(),
      getBoard(),
      getStatus()
    );
  }

  public long currentPlayer() {
    return getPlayers()[(int)getMoves().size() % 2];
  }
  public PlayerColor currentPlayerColor() {
    if(currentPlayer() == player1)
      return PlayerColor.WHITE;
    else
      return PlayerColor.BLACK;
  }
  // determine if one of the players is in check
  public long playerInCheck() {
    Board board = getBoard();
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
    Board board = getBoard();
    if(MoveValidator.validateMove(intent, board, getMoveHistory(), currentPlayerColor())){
        moves.add(new Move(intent));

        board.updateBoard(intent);

        //if opponent no longer has any valid moves, && their king is in check, the game in won.
        if(MoveValidator.getAllValidMoves(getGameState(), getMoveHistory(), currentPlayerColor()).isEmpty()) {
          if(board.inCheck() != InCheck.NONE ) {
            //Player who last moved has won
            winner = (currentPlayerColor() == PlayerColor.BLACK) ? getPlayer1() : getPlayer2();
            status = GameStatus.COMPLETE;
          } else {
            // game has ended in a stalemate, no moves yet player is not in check.
            status = GameStatus.COMPLETE;
            winner = STALEMATE;
          }
        }
        updateTimeStamps();
        return true;
        
    } else {
      return false;
    }
  }
}
