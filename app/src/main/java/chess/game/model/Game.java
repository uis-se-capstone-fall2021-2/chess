package chess.game.model;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


import lombok.Getter;
import lombok.Setter;
import chess.ChessPiece;
import chess.MoveIntent;
import chess.MoveValidator;
import chess.PlayerColor;
import chess.Rank;
import chess.board.Board;
import chess.board.InCheck;
import chess.game.GameStatus;
import chess.game.PGNUtility;
import chess.game.GameInfo;
import chess.game.GameState;

/**
 * A full representation of a chess game.
 */
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

  
  /** Creates and returns a GameInfo object
   * @return GameInfo new GameInfo object
   */
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

  
  /** Creates and returns a GameState object.
   * @return GameState
   */
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

  
  /** Gets the player who is up next to make a move.
   * @return long
   */
  public long currentPlayer() {
    return getPlayers()[(int)getMoves().size() % 2];
  }
  
  /** Gets the <code>PlayerColor</code> of the current player.
   * @return PlayerColor
   */
  public PlayerColor currentPlayerColor() {
    if(currentPlayer() == player1)
      return PlayerColor.WHITE;
    else
      return PlayerColor.BLACK;
  }
  
  /** Gets the player ID of the player that is in check
   * @return long Player in check player ID, or -1 if nobody is in check.
   */
  // determine if one of the players is in check
  public long playerInCheck() {
    InCheck inCheckStatus = getBoard().inCheck();
    switch(inCheckStatus){
      case WHITE:
        return player1;
      case BLACK:
        return player2;
      case NONE:
        return -1; // game is stalemate when there are no legal moves and incheck == NONE
      default:
        return -1;
    }
  }


  
  /** Executes a move on the game, after verifying that the move is legal.
   *  Also makes a check to determine if the game is finished after the move is executed.
   *  If the game is determined to be over, sets {@link #winner} to the player ID of the winner, or to {@link #STALEMATE} 
   * @param playerId ID of the moving player
   * @param intent MoveIntent of the desired move
   * @return boolean true when the move was successful, false when it was not
   */
  public boolean move(long playerId, MoveIntent intent){
    Board board = getBoard();

    //Pawn auto promote to queen when not specified
    if(intent.chessPiece.equals(ChessPiece.PAWN)){
      Rank pawnRank = intent.to.rank;
      // pawn is on first or last rank and promotion is unset
      if((pawnRank.equals(Rank._1) || pawnRank.equals(Rank._8)) && (intent.promotion.equals(ChessPiece.NONE))){
        //replace intent with a new one with promotion set to queen
        intent = new MoveIntent(intent.chessPiece, intent.from, intent.to, ChessPiece.QUEEN);
      }
    }


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
  /**
   * Converts the games move history to standard chess notation so that it can be imported into other chess software.
   * 
   * <p> Expected sample output: </p>
   * <code>
   *  1. e4 e5
   *  2. Nf3 Nc6
   *  3. d4 Nxd6
   *  4. Nxe5 Nxc2+
   * </code>
   * @return a string containing the game's moves in algebraic notation, includes newline characters.
   */
  public String export() {
    StringBuilder pgnString = new StringBuilder();
    Board newBoard = new Board();
    List<MoveIntent> newHistory = new ArrayList<>();
    List<MoveIntent> history = getMoveHistory();
    int turnNumber = 0;
    for(int i = 0; i < history.size(); i++){
      if(i%2 == 0){
        if(i > 0)
          pgnString.append("\n");
        turnNumber++;
        pgnString.append(turnNumber + ". ");
      }
      MoveIntent currentMove = history.get(i);
      pgnString.append(PGNUtility.convertMove(currentMove, newBoard, newHistory) + " ");
      newBoard.updateBoard(currentMove);
      newHistory.add(currentMove);

    }

    return pgnString.toString();
  }
}
