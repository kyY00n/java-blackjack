package blackjack.view;

import blackjack.domain.card.Card;
import blackjack.domain.game.PlayerWinningDto;
import blackjack.domain.participant.ParticipantCardsDto;
import blackjack.domain.participant.ParticipantResultDto;
import blackjack.domain.participant.dealer.DealerFirstCardDto;
import blackjack.domain.participant.dealer.DealerWinningDto;
import java.util.List;
import java.util.stream.Collectors;

public class OutputView {

    public void printFirstDrawMessage(List<String> names) {
        String joinedNames = String.join(", ", names);
        System.out.printf("%n딜러와 %s에게 2장을 나누었습니다.%n", joinedNames);
    }

    public void printFirstOpenCards(DealerFirstCardDto dealerFirstOpen, List<ParticipantCardsDto> playersCards) {
        System.out.println(dealerFirstOpen.getName().getValue() + ": " + dealerFirstOpen.getCard());
        playersCards.forEach(this::printPlayerCard);
        System.out.println();
    }

    public void printPlayerCard(ParticipantCardsDto participantCardsDto) {
        System.out.println(
                participantCardsDto.getName().getValue() + "카드: " + parseCards(participantCardsDto));
    }

    private String parseCards(ParticipantCardsDto participantCardsDto) {
        return participantCardsDto.getCards().stream()
                .map(Card::toString)
                .collect(Collectors.joining(", "));
    }

    public void printDealerHitMessage() {
        System.out.println("\n딜러는 16이하라 한장의 카드를 더 받았습니다.");
    }

    public void printFinalResults(ParticipantResultDto dealerResult, List<ParticipantResultDto> playerResults) {
        System.out.println();
        printFinalResult(dealerResult);
        for (ParticipantResultDto result : playerResults) {
            printFinalResult(result);
        }
    }

    private void printFinalResult(ParticipantResultDto result) {
        System.out.println(
                result.getName().getValue() + " 카드: " + result.getCards().stream().map(Card::toString).collect(
                        Collectors.joining(", ")) + " - 결과: " + result.getScore());
    }

    public void printWinningResults(DealerWinningDto dealerWinningResult, List<PlayerWinningDto> playerWinningResults) {
        System.out.println();
        System.out.println("## 최종 승패");
        String parsedDealerWinningResult = parseDealerWinningResult(dealerWinningResult);
        System.out.println(dealerWinningResult.getName().getValue() + ": " + parsedDealerWinningResult);

        playerWinningResults.forEach(playerWinningDto -> System.out.println(
                playerWinningDto.getName().getValue() + ": " + playerWinningDto.getResult().getLabel()));
    }

    private String parseDealerWinningResult(DealerWinningDto dealerWinningResult) {
        return dealerWinningResult.getResultToCount().keySet().stream()
                .map(result -> dealerWinningResult.getResultToCount().get(result) + result.getLabel()).collect(
                        Collectors.joining(" "));
    }
}
