package player;

import java.util.List;

import card.Card;

public class PlayerResultDto {
    private final Name name;
    private final List<Card> cards;
    private final int score;

    private PlayerResultDto(Name name, List<Card> cards, int score) {
        this.name = name;
        this.cards = cards;
        this.score = score;
    }

    public static PlayerResultDto from(Player player) {
        return new PlayerResultDto(player.getName(), player.showCards(), player.calculateScore());
    }

    public Name getName() {
        return name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getScore() {
        return score;
    }
}