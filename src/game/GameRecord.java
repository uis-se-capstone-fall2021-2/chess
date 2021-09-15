package game;
// TODO: does GameInfo and GameRecord need to be seperate?
public class GameRecord {
    int[] board;

    // TODO: add record of algebraic moves here

    public GameRecord(int[] board) {
        this.board = board;
    }
    public void setBoard(int[] board){
        this.board = board;
    }
}
