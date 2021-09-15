
# Chess

## Entities

```java
// high level for summary views
interface PlayerInfo {
  int playerId;
  String alias;
}

// how a Player is represented in persistence 
interface PlayerRecord extends PlayerInfo {} 

// runtime object
interface Player extends PlayerInfo {
  void notify(GameState gameState)
}

enum GameCompletionState {
  Active 1,
  Complete 2,
  Terminated 3
}

enum ChessPiece {
  NONE 0,
  PAWN 1,
  ROOK 2,
  KNIGHT 3,
  BISHOP 4,
  QUEEN 5,
  KING 6
}

// x coord
enum File {
  A 0,
  B 1,
  C 2,
  D 3,
  E 4,
  F 5,
  G 6,
  H 7
}

// y coord (stride)
enum Rank {
  _1 0,
  _2 1,
  _3 2,
  _4 3,
  _5 4,
  _6 5,
  _7 6,
  _8 7
}

enum PlayerColor {
  WHITE 1
  BLACK -1
}

interface Position {
  File file;
  Rank rank;
}

interface MoveIntent {
  ChessPiece chessPiece;
  Position from;
  Position to;
}

// high level for summary views
interface GameInfo {
  int gameId;
  int owner; // playerId
  int[2] players; // [playerId, playerId], turn order is determined by index
  long moveCount; // current player can be determined by players[moveCount % 2]
  GameCompletionState completed;
  int winner; // playerId
}

// how a Game is represented in persistence 
interface GameRecord extends GameInfo {
  ChessPiece[64] board;
  // TODO: add record of algebraic moves here
}

interface GameState extends GameInfo {
  int inCheck; // playerId, null if no one in check
}

// runtime object
interface Game {
  int gameId;
  Player[2] players; // [Player1, Player2], turn order is determined by index. index 0 is white, index 1 is black
  long moveCount; // determine current player by players[moveCount % 2]
  ChessPiece[64] board;
  boolean complete;
  Player winner;

  // validate move,
  // update game state, persist
  // increment moveCount
  // notify players of new game state
  boolean move(Player player, MoveIntent intent);
  GameState getGameState();
}

interface MoveValidator {
  boolean validateMove(
    MoveIntent intent,
    ChessPiece[64] board,
    PlayerColor playerColor
  );
}
```

## Controllers

```java

interface PlayerController {
  int createPlayer(TBD playerInfo); // returns playerId
  PlayerInfo[] listOpponents();
  PlayerRecord getPlayerDetails(int playerId);
}

interface GameController {
  int[] listAvailableGames(int playerId); // returns gameId[] where GameRecord.completionState === GameCompletionState.Active
  int createGame(int playerId, int opponentId, PlayerColor player1Color); // returns gameId
  boolean quitGame(int playerId, int gameId); // game must be active. Sets GameRecord.completionState = GameCompletionState.Terminated
  boolean deleteGame(int playerId, int gameId); // game must be complete or terminated, playerId must be owner
  GameState getGameState(int playerId, int gameId); // playerId must be either player1 or player2 of Game
  boolean move(int gameId, int playerId, MoveIntent intent);
}

```


## Players

```java

interface ChessAIPlayer extends Player {}

interface UserPlayer extends Player {}

```

  