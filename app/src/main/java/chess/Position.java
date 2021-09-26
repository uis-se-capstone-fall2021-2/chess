package chess;

public class Position {
    File file;
    Rank rank;
    public Position(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }
    @Override
    public boolean equals(Position position){
        return ( rank == position.rank && file == position.file );
    }
    @Override
    public int hashCode() {
        return Object.hash(rank, file);
    }
}   
