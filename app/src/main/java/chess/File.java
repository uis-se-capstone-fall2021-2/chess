package chess;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * A file, or x coordinate, on a chess board.
 */
public enum File {
    @JsonProperty("A") A(0),
    @JsonProperty("B") B(1),
    @JsonProperty("C") C(2),
    @JsonProperty("D") D(3),
    @JsonProperty("E") E(4),
    @JsonProperty("F") F(5),
    @JsonProperty("G") G(6),
    @JsonProperty("H") H(7);
    public int value;
    private File(int value){
        this.value = value;
    }
    public static File FromInteger(int i) {
        for(File type : values()) {
            if(type.value == i){
                return type;
            }
        }
        return null;
    }

    @JsonCreator
    public static File fromKey(String key) {
        for(File file: values()) {
            if(file.name().equals(key)) {
                return file;
            }
        }
        return null;
    }

}
