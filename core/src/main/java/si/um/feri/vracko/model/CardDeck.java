package si.um.feri.vracko.model;

import java.util.ArrayList;
import java.util.Random;

import kotlin.Pair;
import si.um.feri.vracko.assets.RegionNames;
import si.um.feri.vracko.common.GameManager;

public class CardDeck {
    private static final ArrayList<Pair<Card, Integer>> cardDeck = new ArrayList<>();
    private static final String[] cardTextures = {
        RegionNames.CARD_CLUBS_02, RegionNames.CARD_CLUBS_03, RegionNames.CARD_CLUBS_04, RegionNames.CARD_CLUBS_05, RegionNames.CARD_CLUBS_06, RegionNames.CARD_CLUBS_07, RegionNames.CARD_CLUBS_08, RegionNames.CARD_CLUBS_09, RegionNames.CARD_CLUBS_10, RegionNames.CARD_CLUBS_JACK, RegionNames.CARD_CLUBS_QUEEN, RegionNames.CARD_CLUBS_KING, RegionNames.CARD_CLUBS_ACE,
        RegionNames.CARD_DIAMONDS_02, RegionNames.CARD_DIAMONDS_03, RegionNames.CARD_DIAMONDS_04, RegionNames.CARD_DIAMONDS_05, RegionNames.CARD_DIAMONDS_06, RegionNames.CARD_DIAMONDS_07, RegionNames.CARD_DIAMONDS_08, RegionNames.CARD_DIAMONDS_09, RegionNames.CARD_DIAMONDS_10, RegionNames.CARD_DIAMONDS_JACK, RegionNames.CARD_DIAMONDS_QUEEN, RegionNames.CARD_DIAMONDS_KING, RegionNames.CARD_DIAMONDS_ACE,
        RegionNames.CARD_HEARTS_02, RegionNames.CARD_HEARTS_03, RegionNames.CARD_HEARTS_04, RegionNames.CARD_HEARTS_05, RegionNames.CARD_HEARTS_06, RegionNames.CARD_HEARTS_07, RegionNames.CARD_HEARTS_08, RegionNames.CARD_HEARTS_09, RegionNames.CARD_HEARTS_10, RegionNames.CARD_HEARTS_JACK, RegionNames.CARD_HEARTS_QUEEN, RegionNames.CARD_HEARTS_KING, RegionNames.CARD_HEARTS_ACE,
        RegionNames.CARD_SPADES_02, RegionNames.CARD_SPADES_03, RegionNames.CARD_SPADES_04, RegionNames.CARD_SPADES_05, RegionNames.CARD_SPADES_06, RegionNames.CARD_SPADES_07, RegionNames.CARD_SPADES_08, RegionNames.CARD_SPADES_09, RegionNames.CARD_SPADES_10, RegionNames.CARD_SPADES_JACK, RegionNames.CARD_SPADES_QUEEN, RegionNames.CARD_SPADES_KING, RegionNames.CARD_SPADES_ACE
    };
    private static final int[] cardValues = {
        2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11,
        2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11,
        2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11,
        2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11
    };

    public static void initDeck() {
        for (int i = 0; i < 52; i++) {
            cardDeck.add(new Pair<>(new Card(cardValues[i], cardTextures[i]), GameManager.INSTANCE.getInitDeckNum().getValue()));
        }
    };

    public static Card getRandomCard() {
        Random random = new Random();
        int index = random.nextInt(cardDeck.size());
        Pair<Card, Integer> cardPair = cardDeck.get(index);
        Card card = cardPair.getFirst();
        int newValue = cardPair.getSecond() - 1;
        if (newValue == 0) {
            cardDeck.remove(index);
        } else {
            cardDeck.set(index, new Pair<>(card, newValue));
        }
        return card;
    }

    public static int getCardsLeftInDeck() {
        int cardsLeft = 0;
        for (Pair<Card, Integer> cardPair : cardDeck) {
            cardsLeft += cardPair.getSecond();
        }
        return cardsLeft;
    }
}
