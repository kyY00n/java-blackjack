package blackjack.domain.participant.player;

import blackjack.domain.deck.Deck;
import blackjack.domain.game.WinningResult;
import blackjack.domain.participant.ParticipantCardsDto;
import blackjack.domain.participant.ParticipantResultDto;
import blackjack.domain.participant.dealer.Dealer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Players {
    private final List<Player> players;

    public Players() {
        players = new ArrayList<>();
    }

    public Players(List<Player> players) {
        this.players = players;
    }


    public Players add(Player newPlayer) {
        if (nameAlreadyExists(newPlayer)) {
            throw new IllegalArgumentException("플레이어의 이름은 중복일 수 없습니다.");
        }
        ArrayList<Player> newPlayers = new ArrayList<>(players);
        newPlayers.add(newPlayer);
        return new Players(newPlayers);
    }

    private boolean nameAlreadyExists(Player newPlayer) {
        return players.stream().anyMatch(player -> player.getName().equals(newPlayer.getName()));
    }

    public void hitAdditionalCard(Deck deck, CardDecisionStrategy cardDecisionStrategy,
                                  CardDisplayStrategy cardDisplayStrategy) {
        players.forEach(player -> player.hitAdditionalCardFrom(deck, cardDecisionStrategy, cardDisplayStrategy));
    }

    public void hitFirstCards(Deck deck) {
        players.forEach(player -> {
            player.hit(deck.drawCard());
            player.hit(deck.drawCard());
        });
    }

    public List<ParticipantResultDto> getPlayerResults() {
        return players.stream()
                .map(ParticipantResultDto::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public Map<Player, WinningResult> calculateWinning(Dealer dealer) {
        Map<Player, WinningResult> playerToResult = new HashMap<>();
        for (Player player : players) {
            playerToResult.put(player, player.combat(dealer));
        }
        return Collections.unmodifiableMap(playerToResult);
    }

    public List<ParticipantCardsDto> getPlayerCards() {
        return players.stream()
                .map(ParticipantCardsDto::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
