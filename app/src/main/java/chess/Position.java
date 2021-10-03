package chess;
import java.util.Objects;
public class Position {
    public final Rank rank;
    public final File file;

    public Position(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }
    public boolean equals(Position position){
        return ( rank == position.rank && file == position.file );
    }
    public int hashCode() {
        return Objects.hash(rank, file);
    }
}   
