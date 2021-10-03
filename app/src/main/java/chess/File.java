package chess;

public enum File {
    A(0),
    B(1),
    C(2),
    D(3),
    E(4),
    F(5),
    G(6),
    H(7);
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
}
