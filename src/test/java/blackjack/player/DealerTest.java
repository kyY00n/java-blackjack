package blackjack.player;

import static blackjack.domain.game.WinningResult.LOSE;
import static blackjack.domain.game.WinningResult.TIE;
import static blackjack.domain.game.WinningResult.WIN;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import blackjack.domain.game.WinningResult;
import blackjack.domain.card.Card;
import blackjack.domain.card.CardNumber;
import blackjack.domain.card.Pattern;
import blackjack.domain.participant.dealer.Dealer;

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

    @Test
    @DisplayName("딜러의 점수합계가 16점 이하인지 확인할 수 있다.")
    void isUnderScore() {
        Dealer dealer = new Dealer();
        Card card1 = new Card(CardNumber.ACE, Pattern.HEART);
        Card card2 = new Card(CardNumber.EIGHT, Pattern.HEART);
        Card card3 = new Card(CardNumber.SIX, Pattern.HEART);
        dealer.hit(card1);
        dealer.hit(card2);
        dealer.hit(card3);

        Assertions.assertThat(dealer.isUnderScore()).isTrue();
    }

    @Test
    @DisplayName("딜러가 버스트인지 확인할 수 있다.")
    void isBust() {
        Dealer dealer = new Dealer();
        Card card1 = new Card(CardNumber.KING, Pattern.SPADE);
        Card card2 = new Card(CardNumber.EIGHT, Pattern.HEART);
        Card card3 = new Card(CardNumber.KING, Pattern.HEART);
        dealer.hit(card1);
        dealer.hit(card2);
        dealer.hit(card3);

        Assertions.assertThat(dealer.isBust()).isTrue();
    }

    @Test
    @DisplayName("딜러의 승패 결과를 확인할 수 있다.")
    void getWinningResult() {
        Dealer dealer = new Dealer();
        dealer.win();
        dealer.win();
        dealer.lose();
        dealer.tie();

        Map<WinningResult, Integer> dealerResult = dealer.getDealerResult();

        Assertions.assertThat(dealerResult.get(WIN)).isEqualTo(2);
        Assertions.assertThat(dealerResult.get(LOSE)).isEqualTo(1);
        Assertions.assertThat(dealerResult.get(TIE)).isEqualTo(1);
    }
}
