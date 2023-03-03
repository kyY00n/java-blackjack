package blackjack.player;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import card.Card;
import card.CardNumber;
import card.Pattern;
import player.Dealer;

class DealerTest {
    @Test
    @DisplayName("딜러를 생성한다.")
    void create() {
        assertThatCode(() -> new Dealer())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("딜러는 카드를 받을 수 있다")
    void hit() {
        Dealer dealer = new Dealer();
        Card card = new Card(CardNumber.ACE, Pattern.HEART);

        assertThatCode(() -> dealer.hit(card))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("딜러는 받은 카드의 점수 합계를 구할 수 있다")
    void calculateScore() {
        Dealer dealer = new Dealer();
        Card card1 = new Card(CardNumber.ACE, Pattern.HEART);
        Card card2 = new Card(CardNumber.EIGHT, Pattern.HEART);
        Card card3 = new Card(CardNumber.SIX, Pattern.HEART);
        dealer.hit(card1);
        dealer.hit(card2);
        dealer.hit(card3);

        assertThat(dealer.calculateScore()).isEqualTo(15);
    }

    @Test
    @DisplayName("딜러는 현재 가지고 있는 카드를 반환할수 있다.")
    void showCards() {
        Dealer dealer = new Dealer();
        Card card1 = new Card(CardNumber.ACE, Pattern.HEART);
        Card card2 = new Card(CardNumber.EIGHT, Pattern.HEART);
        Card card3 = new Card(CardNumber.SIX, Pattern.HEART);
        dealer.hit(card1);
        dealer.hit(card2);
        dealer.hit(card3);

        assertThat(dealer.showCards()).contains(card1, card2, card3);
    }

    @Test
    @DisplayName("딜러는 현재 가지고 있는 카드중 한장만 반환할 수 있다.")
    void showOneCard() {
        Dealer dealer = new Dealer();
        Card card1 = new Card(CardNumber.ACE, Pattern.HEART);
        Card card2 = new Card(CardNumber.EIGHT, Pattern.HEART);
        Card card3 = new Card(CardNumber.SIX, Pattern.HEART);
        dealer.hit(card1);
        dealer.hit(card2);
        dealer.hit(card3);

        assertThat(dealer.showOneCard()).isEqualTo(card1);
    }
}