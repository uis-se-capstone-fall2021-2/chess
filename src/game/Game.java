package game;

public abstract class Game {
    int gameId;
    Player[] players; // [Player1, Player2], turn order is determined by index. index 0 is white, index 1 is black
    long moveCount; // determine current player by players[moveCount % 2]
    int[] board;
    boolean complete;
    Player winner;
    /**
     * Create a new game between 2 players
     * @param players
     */
    public Game(Player[] players) {
        this.players = players;
        this.moveCount = 0;
        this.complete = false;
        this.winner = null;
        //TODO: create the default chess board. Also, Should we leave the board as an int array, or should we change ChessPiece to include information on which team a piece is on
        //TODO: determine a new game's ID.
    }

    public abstract boolean move(Player player, MoveIntent intent);
    public abstract GameState getGameState();

}
