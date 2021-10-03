package chess;
public enum Rank {
    _1(0),
    _2(1),
    _3(2),
    _4(3),
    _5(4),
    _6(5),
    _7(6),
    _8(7);
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
}
