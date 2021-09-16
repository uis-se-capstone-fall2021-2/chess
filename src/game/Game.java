package game;

// TODO: remove abstract and implement methods
public abstract class Game implements IGame {
    private final int gameId;
    private final Player[] players; // [Player1, Player2], turn order is determined by index. index 0 is white, index 1 is black
    private final int[] board;
    private long moveCount; // determine current player by players[moveCount % 2]
    private boolean complete;
    private Player winner;
    private Player inCheck;
    /**
     * Represents a game between 2 players
     * @param players
     */
    public Game(
        int gameId,
        Player[] players,
        MoveIntent[] moves
    ) {
        this.gameId = gameId;
        this.players = players;
        this.board = new int[64];
        this.moveCount = moves.length;
        this.complete = false;
        this.winner = null;
        // TODO: create the default chess board. Also, Should we leave the board as an int array, or should we change ChessPiece to include information on which team a piece is on
    }

    private void initializeFromSavedState(MoveIntent[] moves) {
        // TODO:
        // replay the moves to bring the board to current state
        // notify players
    }

    public int gameId() {
        return this.gameId; // TODO: I feel like my IDE should be complaining about this but isn't
    }
    public abstract boolean move(Player player, MoveIntent intent);
    public abstract GameState getGameState();

}
