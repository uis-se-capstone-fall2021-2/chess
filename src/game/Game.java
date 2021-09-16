package game;

public abstract class Game {
    int gameId;
    Player[] players; // [Player1, Player2], turn order is determined by index. index 0 is white, index 1 is black
    long moveCount; // determine current player by players[moveCount % 2]
    int[] board;
    boolean complete;
    Player winner;

    public abstract boolean move(Player player, MoveIntent intent);
    public abstract GameState getGameState();

}
