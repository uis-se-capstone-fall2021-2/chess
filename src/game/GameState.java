package game;

//TODO: do GameState and GameInfo need to be seperate? If not, GameRecord will need to be adjusted too
public class GameState extends GameInfo {

    int inCheck; // playerId, null if no one in check
    public GameState(int gameId, int owner, int winner, int[] players, long moveCount, GameCompletionState completed) {
        super(gameId, owner, winner, players, moveCount, completed);
        //TODO: determine how inCheck is set, do we need a method to find out based on game state, or will that be passed in from Game?
    }


}
