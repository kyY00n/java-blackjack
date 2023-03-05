package blackjack.player;

import static blackjack.domain.game.Result.LOSE;
import static blackjack.domain.game.Result.TIE;
import static blackjack.domain.game.Result.WIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import blackjack.domain.deck.Deck;
import blackjack.domain.deck.ShuffledCardsGenerator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardNumber;
import blackjack.domain.card.Pattern;
import blackjack.domain.participant.dealer.Dealer;
import blackjack.domain.participant.Name;
import blackjack.domain.participant.player.Player;
import blackjack.domain.participant.player.PlayerWinningDto;
import blackjack.domain.participant.player.Players;

class PlayersTest {
    @DisplayName("생성할 수 있다")
    @Test
    void create() {
        assertThatCode(() -> new Players()).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("플레이어 한 명을 추가할 수 있다.")
    void addPlayer() {
        Players players = new Players();
        Player player = new Player(new Name("로지"));
        players.add(player);
    }

    @DisplayName("플레이어들이 알아서 덱에서 카드를 받는다.")
    @Test
    void takeCardOnce() {
        //given
        Deck deck = new Deck(new ShuffledCardsGenerator());
        Players players = new Players();
        Player player = new Player(new Name("로지"));
        players.add(player);
        //when
        players.takeCard(deck, 2);
        //then
        assertThat(player.showCards()).hasSize(2);
    }

    @DisplayName("현재 플레이어의 인원수를 반환할 수 있다.")
    @Test
    void count() {
        Players players = new Players();
        Player player1 = new Player(new Name("폴로"));
        Player player2 = new Player(new Name("로지"));
        Player player3 = new Player(new Name("연어"));
        players.add(player1);
        players.add(player2);
        players.add(player3);

        assertThat(players.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("인덱스에 해당하는 플레이어의 버스트 여부를 알 수 있다")
    void isBust() {
        Players players = new Players();
        Player player1 = new Player(new Name("폴로"));
        players.add(player1);
        player1.hit(new Card(CardNumber.KING, Pattern.HEART));
        player1.hit(new Card(CardNumber.KING, Pattern.DIAMOND));
        player1.hit(new Card(CardNumber.KING, Pattern.SPADE));

        assertThat(players.isBust(0)).isTrue();
    }

    @Test
    @DisplayName("플레이어의 이름과 승패결과를 가져올 수 있다.")
    void getWinningResult() {
        Players players = new Players();
        Player player = new Player(new Name("폴로"));
        players.add(player);

        player.win();
        List<PlayerWinningDto> playerWinningResults = players.getWinningResults();
        PlayerWinningDto playerWinningDto = playerWinningResults.get(0);

        assertThat(playerWinningDto.getName().getValue()).isEqualTo("폴로");
        assertThat(playerWinningDto.getResult()).isEqualTo(WIN);
    }

    @Nested
    @DisplayName("승패를 계산하는 기능")
    class CalculateWinning {

        @Test
        @DisplayName("플레이어의 점수가 딜러의 점수보다 높고 플레이어가 버스트가 아니면 WIN을 반환한다.")
        void winWhenScoreIsHigher() {
            Players players = new Players();
            Player player = new Player(new Name("폴로"));
            players.add(player);

            Dealer dealer = new Dealer();
            player.hit(new Card(CardNumber.KING, Pattern.HEART));
            dealer.hit(new Card(CardNumber.FIVE, Pattern.CLOVER));

            players.calculateWinning(dealer);
            assertThat(player.getResult()).isEqualTo(WIN);
        }

        @Test
        @DisplayName("딜러가 버스트이고 플레이어가 버스트가 아니면 WIN을 반환한다.")
        void winWhenDealerBust() {
            Players players = new Players();
            Player player = new Player(new Name("폴로"));
            players.add(player);
            Dealer dealer = new Dealer();

            player.hit(new Card(CardNumber.KING, Pattern.HEART));
            dealer.hit(new Card(CardNumber.FIVE, Pattern.CLOVER));
            dealer.hit(new Card(CardNumber.KING, Pattern.SPADE));
            dealer.hit(new Card(CardNumber.KING, Pattern.CLOVER));
            players.calculateWinning(dealer);

            assertThat(player.getResult()).isEqualTo(WIN);
        }

        @Test
        @DisplayName("딜러보다 점수가 낮고 딜러가 버스트가 아니면 LOSE를 반환한다")
        void loseWhenLowerScore() {
            Players players = new Players();
            Player player = new Player(new Name("폴로"));
            players.add(player);
            Dealer dealer = new Dealer();

            player.hit(new Card(CardNumber.KING, Pattern.HEART));
            dealer.hit(new Card(CardNumber.FIVE, Pattern.CLOVER));
            dealer.hit(new Card(CardNumber.KING, Pattern.SPADE));
            players.calculateWinning(dealer);

            assertThat(player.getResult()).isEqualTo(LOSE);
        }

        @Test
        @DisplayName("플레이어가 버스트이면 LOSE를 반환한다")
        void loseWhenPlayerBust() {
            Players players = new Players();
            Player player = new Player(new Name("폴로"));
            players.add(player);
            Dealer dealer = new Dealer();

            player.hit(new Card(CardNumber.KING, Pattern.HEART));
            player.hit(new Card(CardNumber.JACK, Pattern.CLOVER));
            player.hit(new Card(CardNumber.JACK, Pattern.DIAMOND));
            dealer.hit(new Card(CardNumber.FIVE, Pattern.CLOVER));
            dealer.hit(new Card(CardNumber.KING, Pattern.SPADE));
            players.calculateWinning(dealer);

            assertThat(player.getResult()).isEqualTo(LOSE);
        }

        @Test
        @DisplayName("플레이어와 딜러의 점수가 같고 버스트가 아닌 경우 TIE를 반환한다.")
        void tieWhenSameScore() {
            Players players = new Players();
            Player player = new Player(new Name("폴로"));
            players.add(player);
            Dealer dealer = new Dealer();

            player.hit(new Card(CardNumber.KING, Pattern.HEART));
            dealer.hit(new Card(CardNumber.KING, Pattern.SPADE));
            players.calculateWinning(dealer);

            assertThat(player.getResult()).isEqualTo(TIE);
        }
    }
}
