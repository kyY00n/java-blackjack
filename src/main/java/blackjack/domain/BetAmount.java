package blackjack.domain;

public class BetAmount {

    public static final int MIN = 10000;
    public static final int MAX = 100000;

    private final int value;

    public BetAmount(int value) {
        if (MIN > value || value > MAX) {
            throw new IllegalArgumentException("범위가 올바르지 않습니다.");
        }
        this.value = value;
    }


    public int getValue() {
        return value;
    }
}
