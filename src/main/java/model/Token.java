package model;

public enum Token {
    TOKEN_8_POINTS(8),
    TOKEN_6_POINTS(6),
    TOKEN_4_POINTS(4),
    TOKEN_2_POINTS(2),
    TOKEN_GAME_END(1);

    private int value;
    Token(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
