package chess;

public class Position {
    public final Rank rank;
    public final File file;

    public Position(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }
}
