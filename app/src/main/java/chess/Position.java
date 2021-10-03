package chess;

public class Position {
    public Rank rank;
    public File file;

    public Position(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }
}
