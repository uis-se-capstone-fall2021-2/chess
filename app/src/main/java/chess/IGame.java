package chess;

public interface IGame {
  public int gameId();
  public boolean move(Player player, MoveIntent intent);
  public GameState getGameState();
}
