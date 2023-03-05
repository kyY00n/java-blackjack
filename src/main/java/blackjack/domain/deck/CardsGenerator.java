package blackjack.domain.deck;

import java.util.Stack;

import blackjack.domain.card.Card;

public interface CardsGenerator {
    Stack<Card> generate();
}