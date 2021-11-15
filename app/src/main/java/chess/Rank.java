package chess;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public enum Rank {
    @JsonProperty("_1") _1(0),
    @JsonProperty("_2") _2(1),
    @JsonProperty("_3") _3(2),
    @JsonProperty("_4") _4(3),
    @JsonProperty("_5") _5(4),
    @JsonProperty("_6") _6(5),
    @JsonProperty("_7") _7(6),
    @JsonProperty("_8") _8(7);
    public int value;
    private Rank(int value){
        this.value = value;
    }
    public static Rank FromInteger(int i){
        for(Rank type : values()) {
            if(type.value == i){
                return type;
            }
        }
        return null;
    }

    @JsonCreator
    public static Rank fromKey(String key) {
        for(Rank rank: values()) {
            if(rank.name().equals(key)) {
                return rank;
            }
        }
        return null;
    }
}
