package chess;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {
    @JsonProperty("rank")
    public final Rank rank;
    @JsonProperty("file")
    public final File file;

    public Position(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }
    public Position(int x, int y) {
        this.file = File.FromInteger(x);
        this.rank = Rank.FromInteger(y);
    }
    public Position(int index) {
        this.file = File.FromInteger(index % 8);
        this.rank = Rank.FromInteger(index / 8);
    }
    public boolean equals(Position position){
        return ( rank == position.rank && file == position.file );
    }
    public int hashCode() {
        return Objects.hash(rank, file);
    }

    @JsonCreator
    public static Position fromJson(String file, String rank) {
        return new Position(File.fromKey(file), Rank.fromKey(rank));
    }
    public String toString() {
        return (file.name() + rank.name());
    }
}   
