package blackjack.domain.participant;

public class Name {
    private final String value;

    public Name(String value) {
        String valueWithoutBlank = removeBlank(value);
        validateNameLength(valueWithoutBlank);
        this.value = valueWithoutBlank;
    }

    private static String removeBlank(String value) {
        return value.replace(" ", "");
    }

    private static void validateNameLength(String value) {
        if (value.length() < 1 || value.length() > 5) {
            throw new IllegalArgumentException("이름은 1글자 이상 5글자 이하만 가능합니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
