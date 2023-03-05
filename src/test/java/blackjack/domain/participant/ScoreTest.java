package blackjack.domain.participant;

import static org.assertj.core.api.Assertions.*;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardNumber;
import blackjack.domain.card.Pattern;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ScoreTest {
    @DisplayName("생성테스트")
    @Test
    void create() {
        assertThatCode(() -> new Score(0))
                .doesNotThrowAnyException();
    }

    @DisplayName("점수를 반환할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 10})
    void getValue(int value) {
        //given
        Score score = new Score(value);
        //when
        int actual = score.getValue();
        //then
        assertThat(actual).isEqualTo(value);
    }

    @DisplayName("같은 값을 가진 객체는 같다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 21})
    void equals() {
        //given
        Score score1 = new Score(1);
        Score score2 = new Score(1);
        //when
        //then
        assertThat(score2).isEqualTo(score1);
    }

    @DisplayName("값을 더할 수 있다.")
    @Test
    void plus() {
        //given
        Score score = new Score(0);
        //when
        Score afterPlus = score.plus(10);
        //then
        assertThat(afterPlus).isEqualTo(new Score(10));
    }

    @DisplayName("카드 리스트로 점수 객체를 생성할 수 있다.")
    @Test
    void createFrom() {
        //given
        List<Card> cards = List.of(new Card(CardNumber.EIGHT, Pattern.CLOVER), new Card(CardNumber.EIGHT, Pattern.DIAMOND));
        //when
        Score score = Score.from(cards);
        //then
        assertThat(score).isEqualTo(new Score(16));
    }

    @DisplayName("점수 합이 10이하일 때, 에이스는 11로 계산된다.")
    @Test
    void calculateAceAsEleven() {
        //given
        List<Card> cards = List.of(new Card(CardNumber.ACE, Pattern.CLOVER), new Card(CardNumber.EIGHT, Pattern.DIAMOND));
        //when
        Score score = Score.from(cards);
        //then
        assertThat(score).isEqualTo(new Score(19));
    }
}