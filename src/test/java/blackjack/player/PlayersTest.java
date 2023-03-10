package blackjack.player;

import static blackjack.domain.game.WinningResult.LOSE;
import static blackjack.domain.game.WinningResult.TIE;
import static blackjack.domain.game.WinningResult.WIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.domain.card.Card;
import blackjack.domain.card.CardNumber;
import blackjack.domain.card.Pattern;
import blackjack.domain.deck.Deck;
import blackjack.domain.deck.ShuffledCardsGenerator;
import blackjack.domain.game.WinningResult;
import blackjack.domain.participant.Name;
import blackjack.domain.participant.dealer.Dealer;
import blackjack.domain.participant.player.Player;
import blackjack.domain.participant.player.Players;
import java.util.Map;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PlayersTest {
    @DisplayName("생성할 수 있다")
    @Test
    void create() {
        assertThatCode(() -> new Players()).doesNotThrowAnyException();
    }

    @DisplayName("플레이어들의 이름은 중복될 수 없다.")
    @Test
    void cannotHaveSameName() {
        Players players = new Players();
        Player rosie = new Player(new Name("로지"));
        Player rosy = new Player(new Name("로지"));
        Players newPlayers = players.add(rosie);

        assertThatThrownBy(()-> newPlayers.add(rosy))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("플레이어 한 명을 추가할 수 있다.")
    void addPlayer() {
        //given
        Players players = new Players();
        Player player = new Player(new Name("로지"));
        //when
        Players newPlayers = players.add(player);
        //then
        assertThat(newPlayers).extracting("players", InstanceOfAssertFactories.list(Player.class))
                .contains(player);
    }

    @DisplayName("플레이어들이 알아서 덱에서 카드를 받는다.")
    @Test
    void takeCardOnce() {
        //given
        Deck deck = new Deck(new ShuffledCardsGenerator());
        Players players = new Players();
        Player player = new Player(new Name("로지"));
        Players newPlayers = players.add(player);
        //when
        newPlayers.hitFirstCards(deck);
        //then
        assertThat(player.showCards()).hasSize(2);
    }

    @Test
    @DisplayName("플레이어의 이름과 승패결과를 가져올 수 있다.")
    void getWinningResult() {
        Dealer dealer = new Dealer();
        Player player = new Player(new Name("폴로"));
        player.hit(new Card(CardNumber.KING, Pattern.DIAMOND));
        Players players = new Players().add(player);

        Map<Player, WinningResult> playerToResult = players.calculateWinning(dealer);

        assertThat(playerToResult.get(player)).isEqualTo(WIN);
    }

    @Nested
    @DisplayName("승패를 계산하는 기능")
    class CalculateWinning {

        @Test
        @DisplayName("플레이어의 점수가 딜러의 점수보다 높고 플레이어가 버스트가 아니면 WIN을 반환한다.")
        void winWhenScoreIsHigher() {
            //given
            Players players = new Players();
            Player player = new Player(new Name("폴로"));
            Players newPlayers = players.add(player);

            Dealer dealer = new Dealer();
            player.hit(new Card(CardNumber.KING, Pattern.HEART));
            dealer.hit(new Card(CardNumber.FIVE, Pattern.CLOVER));
            //when
            Map<Player, WinningResult> playerToResult = newPlayers.calculateWinning(dealer);
            //then
            assertThat(playerToResult.get(player)).isEqualTo(WIN);
        }

        @Test
        @DisplayName("딜러가 버스트이고 플레이어가 버스트가 아니면 WIN을 반환한다.")
        void winWhenDealerBust() {
            //given
            Players players = new Players();
            Player player = new Player(new Name("폴로"));
            Players newPlayers = players.add(player);
            Dealer dealer = new Dealer();

            player.hit(new Card(CardNumber.KING, Pattern.HEART));
            dealer.hit(new Card(CardNumber.FIVE, Pattern.CLOVER));
            dealer.hit(new Card(CardNumber.KING, Pattern.SPADE));
            dealer.hit(new Card(CardNumber.KING, Pattern.CLOVER));
            //when
            Map<Player, WinningResult> playerToResult = newPlayers.calculateWinning(dealer);
            //then
            assertThat(playerToResult.get(player)).isEqualTo(WIN);
        }

        @Test
        @DisplayName("딜러보다 점수가 낮고 딜러가 버스트가 아니면 LOSE를 반환한다")
        void loseWhenLowerScore() {
            //given
            Players players = new Players();
            Player player = new Player(new Name("폴로"));
            Players newPlayers = players.add(player);
            Dealer dealer = new Dealer();
            player.hit(new Card(CardNumber.KING, Pattern.HEART));
            dealer.hit(new Card(CardNumber.FIVE, Pattern.CLOVER));
            dealer.hit(new Card(CardNumber.KING, Pattern.SPADE));
            //when
            Map<Player, WinningResult> playerToResult = newPlayers.calculateWinning(dealer);
            //then
            assertThat(playerToResult.get(player)).isEqualTo(LOSE);
        }

        @Test
        @DisplayName("플레이어가 버스트이면 LOSE를 반환한다")
        void loseWhenPlayerBust() {
            //given
            Players players = new Players();
            Player player = new Player(new Name("폴로"));
            Players newPlayers = players.add(player);
            Dealer dealer = new Dealer();
            player.hit(new Card(CardNumber.KING, Pattern.HEART));
            player.hit(new Card(CardNumber.JACK, Pattern.CLOVER));
            player.hit(new Card(CardNumber.JACK, Pattern.DIAMOND));
            dealer.hit(new Card(CardNumber.FIVE, Pattern.CLOVER));
            dealer.hit(new Card(CardNumber.KING, Pattern.SPADE));
            //when
            Map<Player, WinningResult> playerToResult = newPlayers.calculateWinning(dealer);
            //then
            assertThat(playerToResult.get(player)).isEqualTo(LOSE);
        }

        @Test
        @DisplayName("플레이어와 딜러의 점수가 같고 버스트가 아닌 경우 TIE를 반환한다.")
        void tieWhenSameScore() {
            //given
            Players players = new Players();
            Player player = new Player(new Name("폴로"));
            Players newPlayers = players.add(player);
            Dealer dealer = new Dealer();

            player.hit(new Card(CardNumber.KING, Pattern.HEART));
            dealer.hit(new Card(CardNumber.KING, Pattern.SPADE));
            //when
            Map<Player, WinningResult> playerToResult = newPlayers.calculateWinning(dealer);
            //then
            assertThat(playerToResult.get(player)).isEqualTo(TIE);
        }
    }
}
