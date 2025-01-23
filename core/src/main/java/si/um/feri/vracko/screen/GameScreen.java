package si.um.feri.vracko.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import si.um.feri.vracko.BlackJackMiniGame;
import si.um.feri.vracko.assets.AssetDescriptors;
import si.um.feri.vracko.assets.RegionNames;
import si.um.feri.vracko.common.GameManager;
import si.um.feri.vracko.config.GameConfig;
import si.um.feri.vracko.model.Card;
import si.um.feri.vracko.model.CardDeck;
import si.um.feri.vracko.model.Player;

public class GameScreen extends ScreenAdapter {
    private final BlackJackMiniGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private TextureAtlas gameplayAtlas;

    private Sound chipSound;
    private Sound drawCardSound;
    private TextButton dealButton;
    private Stack chipButton10;
    private Stack chipButton25;
    private Stack chipButton50;
    private Stack chipButton100;
    private final ArrayList<Card> playerCardsStartHand = new ArrayList<>();
    private final ArrayList<Card> dealerCards = new ArrayList<>();
    private final ArrayList<Card> playerCardsFirstHand = new ArrayList<>();
    private final ArrayList<Card> playerCardsSecondHand = new ArrayList<>();

    private Label playerHandValueLabelStartHand;
    private Label dealerHandValueLabel;
    private Label playerHandValueLabelFirstHand;
    private Label playerHandValueLabelSecondHand;
    private Label handResultLabel;
    private Label handResultLabelFirstHand;
    private Label handResultLabelSecondHand;
    private Label bet1Label;
    private Label bet2Label;
    private Label chipsLabel;
    private final ArrayList<Actor> playerCardsActors = new ArrayList<>();
    private final ArrayList<Actor> dealerCardsActors = new ArrayList<>();

    private int chips = GameManager.numOfChips;
    private int bet1 = 0;
    private int bet2 = 0;
    private int handCounter = 0;
    private boolean endHand = false;
    private boolean firstHand = true;
    private int handsWon = 0;
    private Player player;


    public GameScreen(BlackJackMiniGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
        drawCardSound = assetManager.get(AssetDescriptors.DRAW_CARD_SOUND);
        chipSound = assetManager.get(AssetDescriptors.CHIP_SOUND);

        if (GameManager.MUSIC_ENABLED){
            game.menuMusic.stop();
            game.gameMusic.play();
        }

        CardDeck.initDeck();
        stage.addActor(createUi());
        Gdx.input.setInputProcessor(stage);
        stage.addActor(cardBackImage());

        dealerHandValueLabel = new Label("", skin);
        playerHandValueLabelStartHand = new Label("", skin);
        playerHandValueLabelFirstHand = new Label("", skin);
        playerHandValueLabelSecondHand = new Label("", skin);

        stage.addActor(dealerHandValueLabel);
        stage.addActor(playerHandValueLabelStartHand);
        stage.addActor(playerHandValueLabelFirstHand);
        stage.addActor(playerHandValueLabelSecondHand);

        handResultLabel = new Label("", skin);
        handResultLabelFirstHand = new Label("", skin);
        handResultLabelSecondHand = new Label("", skin);

        stage.addActor(handResultLabelFirstHand);
        stage.addActor(handResultLabelSecondHand);
        stage.addActor(handResultLabel);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void savePlayerResult() {
        player = new Player(GameManager.playerName, handsWon);
        GameManager.addOrUpdateResult(player);
    }

    private Actor createUi() {
        Table table = new Table();
        table.defaults().pad(5);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        // Create buttons
        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(0.75f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                savePlayerResult();
                game.setScreen(new MenuScreen(game));
            }
        });

        chipsLabel = new Label("Chips: " + chips, skin);
        table.add(chipsLabel).left().top().row();

        bet1Label = new Label("Bet: " + bet1, skin);
        table.add(bet1Label).left().top().row();

        bet2Label = new Label("", skin);
        table.add(bet2Label).left().top().row();

        chipButton10 = createChipButtonWithLabel(RegionNames.CHIP10, "10");
        chipButton25 = createChipButtonWithLabel(RegionNames.CHIP25, "25");
        chipButton50 = createChipButtonWithLabel(RegionNames.CHIP50, "50");
        chipButton100 = createChipButtonWithLabel(RegionNames.CHIP100, "100");

        dealButton = new TextButton("Deal", skin);
        dealButton.getLabel().setFontScale(0.75f);
        dealButton.setTouchable(Touchable.disabled);
        dealButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (firstHand){
                    firstHand = false;
                    gameInit();
                } else {
                    resetGame(() -> gameInit());
                }
            }
        });


        chipButton10.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int chipValue = 10;
                if (chips >= chipValue) {
                    chips -= chipValue;
                    bet1 += chipValue;
                    chipsLabel.setText("Chips: " + chips);
                    bet1Label.setText("Bet: " + bet1);
                    if (GameManager.SOUND_EFFECTS_ENABLED) {
                        chipSound.play();
                    }
                    dealButton.setTouchable(Touchable.enabled);
                }
            }
        });

        chipButton25.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int chipValue = 25;
                if (chips >= chipValue) {
                    chips -= chipValue;
                    bet1 += chipValue;
                    chipsLabel.setText("Chips: " + chips);
                    bet1Label.setText("Bet: " + bet1);
                    if (GameManager.SOUND_EFFECTS_ENABLED) {
                        chipSound.play();
                    }
                    dealButton.setTouchable(Touchable.enabled);
                }
            }
        });

        chipButton50.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int chipValue = 50;
                if (chips >= chipValue) {
                    chips -= chipValue;
                    bet1 += chipValue;
                    chipsLabel.setText("Chips: " + chips);
                    bet1Label.setText("Bet: " + bet1);
                    if (GameManager.SOUND_EFFECTS_ENABLED) {
                        chipSound.play();
                    }
                    dealButton.setTouchable(Touchable.enabled);
                }
            }
        });

        chipButton100.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int chipValue = 100;
                if (chips >= chipValue) {
                    chips -= chipValue;
                    bet1 += chipValue;
                    chipsLabel.setText("Chips: " + chips);
                    bet1Label.setText("Bet: " + bet1);
                    if (GameManager.SOUND_EFFECTS_ENABLED) {
                        chipSound.play();
                    }
                    dealButton.setTouchable(Touchable.enabled);
                }
            }
        });

        Table chipButtonTable = new Table();
        chipButtonTable.defaults().pad(5);

        Table buttonTable = new Table();
        buttonTable.defaults().pad(5);

        chipButtonTable.add(chipButton10).expandX().right().top().row();
        chipButtonTable.add(chipButton25).expandX().right().top().row();
        chipButtonTable.add(chipButton50).expandX().right().top().row();
        chipButtonTable.add(chipButton100).expandX().right().top().row();

        // Add the chip button table to the main table on the right side
        table.add(chipButtonTable).right().top().row();

        buttonTable.add(backButton).expandX().left().top().width(130).height(100);
        buttonTable.add(dealButton).expandX().right().top().width(130).height(100).row();
        table.add(buttonTable).expand().fill().center();

        table.setFillParent(true);
        return table;
    }
    private void gameInit() {
        dealButton.setTouchable(Touchable.disabled);
        dealStartingCards(()-> {
            chipButton10.setTouchable(Touchable.disabled);
            chipButton25.setTouchable(Touchable.disabled);
            chipButton50.setTouchable(Touchable.disabled);
            chipButton100.setTouchable(Touchable.disabled);
            if(Integer.parseInt(getHandValue(playerCardsStartHand)) == 21) {
                showHandResultMessage(handResultLabel, "BLACKJACK! Player Wins!", GameConfig.singleHandResultLabelPosX, GameConfig.singleHandResultLabelPosY);
                chips += (int) Math.floor(bet1 * 2.5);
                chipsLabel.setText("Chips: " + chips);
                bet1 = 0;
                bet1Label.setText("Bet: " + bet1);
                handsWon++;
            } else{
                showDecisionButtons(playerCardsStartHand, GameConfig.singleHandPosX, GameConfig.cardPosY, playerHandValueLabelStartHand, GameConfig.singleHandLabelPosX);
            }
        });
    }

    private Stack createChipButtonWithLabel(String chipTextureName, String labelText) {
        TextureRegion chipRegion = gameplayAtlas.findRegion(chipTextureName);
        TextureRegionDrawable chipDrawable = new TextureRegionDrawable(chipRegion);
        ImageButton chipButton = new ImageButton(chipDrawable);

        Label label = new Label(labelText, skin);
        label.setFontScale(0.75f);
        label.setAlignment(Align.center);

        Stack stack = new Stack();
        stack.add(chipButton);
        stack.add(label);

        return stack;
    }

    private Actor cardBackImage() {
        TextureRegion cardBackRegion = gameplayAtlas.findRegion(RegionNames.CARD_BACK);
        Image cardBackImage = new Image(new TextureRegionDrawable(cardBackRegion));
        cardBackImage.setSize(cardBackImage.getWidth() * 1.5f, cardBackImage.getHeight() * 1.5f);
        cardBackImage.setPosition(GameConfig.deckCardPositionX, GameConfig.deckCardPositionY);
        return cardBackImage;
    }
    private void dealStartingCards(Runnable callback) {
        // Create a sequence of actions
        Action dealSequence = Actions.sequence(
            Actions.run(() -> dealCardToPlayer(GameConfig.singleHandPosX, GameConfig.cardPosY, playerCardsStartHand, playerHandValueLabelStartHand, GameConfig.singleHandLabelPosX, () -> {})),
            Actions.delay(0.5f), // Delay before dealing the next card
            Actions.run(() -> dealCardToPlayer(GameConfig.singleHandPosX, GameConfig.cardPosY, playerCardsStartHand, playerHandValueLabelStartHand, GameConfig.singleHandLabelPosX, () -> {})),
            Actions.delay(0.5f), // Delay before dealing the next card
            Actions.run(() -> dealCardToDealer(() -> {})),
            Actions.delay(0.5f), // Delay before enabling the button
            Actions.run(callback) // Call the callback after dealing is complete
        );
        // Add the sequence to the stage
        stage.addAction(dealSequence);
    }
    private void dealCardToPlayer(Float posX, Float posY, ArrayList<Card> playerCards, Label playerHandValueLabel, Float handLabelPosX, Runnable callback) {
        Card card = CardDeck.getRandomCard();
        Image cardImage = createCardImage(card);
        cardImage.setPosition(GameConfig.deckCardPositionX, GameConfig.deckCardPositionY);
        playerCardsActors.add(cardImage);
        stage.addActor(cardImage);

        if(GameManager.SOUND_EFFECTS_ENABLED){
            drawCardSound.play();
        }

        Action moveAction = Actions.moveTo(
            posX + (playerCards.size() * GameConfig.xOffset),
            posY + (playerCards.size() * GameConfig.yOffset),
            0.5f
        );

        cardImage.addAction(moveAction);
        cardImage.addAction(Actions.run(() -> {
            playerCards.add(card);
            updateHandValue(playerHandValueLabel, playerCards, handLabelPosX);
            callback.run();
        }));

    }
    private void dealCardToDealer(Runnable callback) {
        Card card = CardDeck.getRandomCard();
        Image cardImage = createCardImage(card);
        cardImage.setPosition(GameConfig.deckCardPositionX, GameConfig.deckCardPositionY);
        dealerCardsActors.add(cardImage);
        stage.addActor(cardImage);

        if(GameManager.SOUND_EFFECTS_ENABLED){
            drawCardSound.play();
        }

        Action moveAction = Actions.moveTo(
            (GameConfig.WIDTH - cardImage.getWidth()) / 2 + (dealerCards.size() * GameConfig.xOffset),
            (GameConfig.HEIGHT - cardImage.getHeight() - 50) - (dealerCards.size() * GameConfig.yOffset),
            0.5f
        );

        cardImage.addAction(moveAction);
        cardImage.addAction(Actions.run(() -> {
            dealerCards.add(card);
            updateDealerHandValue();
            callback.run();
        }));
    }
    private Image createCardImage(Card card) {
        TextureRegion cardRegion = gameplayAtlas.findRegion(card.getTexture());
        Image cardImage = new Image(new TextureRegionDrawable(cardRegion));
        cardImage.setSize(cardImage.getWidth() * 1.5f, cardImage.getHeight() * 1.5f);
        return cardImage;
    }
    private String getHandValue(ArrayList<Card> cards) {
        int handValue = 0;
        int aceCount = 0;
        for (Card card : cards) {
            handValue += card.getValue();
            if (card.getValue() == 11) {
                aceCount++;
            }
        }
        while (handValue > 21 && aceCount > 0) {
            handValue -= 10;
            aceCount--;
        }
        return String.valueOf(handValue);
    }
    private void updateDealerHandValue() {
        dealerHandValueLabel.setText(getHandValue(dealerCards));
        dealerHandValueLabel.pack();
        dealerHandValueLabel.setPosition((GameConfig.WIDTH / 2f - dealerHandValueLabel.getWidth()/2f), GameConfig.HEIGHT - 50);
    }
    private void updateHandValue(Label handValueLabel, ArrayList<Card> hand, float posX) {
        handValueLabel.setText(getHandValue(hand));
        handValueLabel.pack();
        handValueLabel.setPosition(posX  - handValueLabel.getWidth()/2f, 80 - handValueLabel.getHeight());
    }
    private void showDecisionButtons(ArrayList<Card> hand, float posX, float posY, Label handValueLabel, float handLabelPosX) {
        Table table = new Table();
        Table decisionTable = new Table();

        TextButton hitButton = new TextButton("Hit", skin);
        hitButton.getLabel().setFontScale(0.70f);
        hitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (handCounter == 0) {
                    decisionTable.remove();
                    dealCardToPlayer(posX, posY, hand, handValueLabel, handLabelPosX, () -> {
                        if (Integer.parseInt(getHandValue(hand)) == 21) {
                            drawDealerCards(() -> checkHandResultSingleHand());
                        } else if (Integer.parseInt(getHandValue(hand)) > 21) {
                            checkHandResultSingleHand();
                        } else if (Integer.parseInt(getHandValue(hand)) < 21) {
                            showDecisionButtons(hand, posX, posY, handValueLabel, handLabelPosX);
                        }
                    });
                } else if (!endHand) {
                    decisionTable.remove();
                    dealCardToPlayer(posX, posY, hand, handValueLabel, handLabelPosX, () -> {
                        if (Integer.parseInt(getHandValue(hand)) == 21) {
                            endHand = true;
                            dealCardToPlayer(GameConfig.secondHandPosX, GameConfig.cardPosY, playerCardsSecondHand, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX, () -> {
                                if (Integer.parseInt(getHandValue(playerCardsSecondHand)) == 21) {
                                    drawDealerCards(() -> checkHandResultSplitHand());
                                } else {
                                    showDecisionButtons(playerCardsSecondHand, GameConfig.secondHandPosX, GameConfig.cardPosY, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX);
                                    showWhichHandMessage(handResultLabelSecondHand, "HERE", GameConfig.secondHandResultLabelPosX, GameConfig.cardPosY);
                                    showWhichHandMessage(handResultLabelFirstHand, "", GameConfig.firstHandResultLabelPosX, GameConfig.cardPosY);
                                }
                            });
                        } else if (Integer.parseInt(getHandValue(hand)) > 21) {
                            endHand = true;
                            dealCardToPlayer(GameConfig.secondHandPosX, GameConfig.cardPosY, playerCardsSecondHand, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX, () -> {
                                if (Integer.parseInt(getHandValue(playerCardsSecondHand)) == 21) {
                                    drawDealerCards(() -> checkHandResultSplitHand());
                                } else {
                                    showDecisionButtons(playerCardsSecondHand, GameConfig.secondHandPosX, GameConfig.cardPosY, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX);
                                    showWhichHandMessage(handResultLabelSecondHand, "HERE", GameConfig.secondHandResultLabelPosX, GameConfig.cardPosY);
                                    showWhichHandMessage(handResultLabelFirstHand, "", GameConfig.firstHandResultLabelPosX, GameConfig.cardPosY);
                                }
                            });
                        } else if (Integer.parseInt(getHandValue(hand)) < 21) {
                            showDecisionButtons(hand, posX, posY, handValueLabel, handLabelPosX);
                        }
                    });
                } else {
                    decisionTable.remove();
                    dealCardToPlayer(posX, posY, hand, handValueLabel, handLabelPosX, () -> {
                        if (Integer.parseInt(getHandValue(hand)) == 21) {
                            drawDealerCards(() -> checkHandResultSplitHand());
                        } else if (Integer.parseInt(getHandValue(hand)) > 21) {
                            drawDealerCards(() -> checkHandResultSplitHand());
                        } else if (Integer.parseInt(getHandValue(hand)) < 21) {
                            showDecisionButtons(hand, posX, posY, handValueLabel, handLabelPosX);
                        }
                    });
                }
            }
        });

        TextButton standButton = new TextButton("Stand", skin);
        standButton.getLabel().setFontScale(0.70f);
        standButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (handCounter == 0) {
                    decisionTable.remove();
                    drawDealerCards(() -> checkHandResultSingleHand());
                } else if (!endHand) {
                    decisionTable.remove();
                    endHand = true;
                    dealCardToPlayer(GameConfig.secondHandPosX, GameConfig.cardPosY, playerCardsSecondHand, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX, () -> {
                        if (Integer.parseInt(getHandValue(playerCardsSecondHand)) == 21) {
                            drawDealerCards(() -> checkHandResultSplitHand());
                        } else {
                            showDecisionButtons(playerCardsSecondHand, GameConfig.secondHandPosX, GameConfig.cardPosY, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX);
                            showWhichHandMessage(handResultLabelSecondHand, "HERE", GameConfig.secondHandResultLabelPosX, GameConfig.cardPosY);
                            showWhichHandMessage(handResultLabelFirstHand, "", GameConfig.firstHandResultLabelPosX, GameConfig.cardPosY);
                        }
                    });
                } else {
                    decisionTable.remove();
                    drawDealerCards(() -> checkHandResultSplitHand());
                }
            }
        });

        TextButton doubleButton = new TextButton("Double", skin);
        doubleButton.getLabel().setFontScale(0.70f);
        doubleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (handCounter == 0) {
                    decisionTable.remove();
                    chips -= bet1;
                    bet1 *= 2;
                    chipsLabel.setText("Chips: " + chips);
                    bet1Label.setText("Bet: " + bet1);
                    dealCardToPlayer(posX, posY, hand, handValueLabel, handLabelPosX, () -> {
                        if (Integer.parseInt(getHandValue(hand)) > 21) {
                            checkHandResultSingleHand();
                        } else {
                            drawDealerCards(() -> checkHandResultSingleHand());
                        }
                    });
                } else if (!endHand) {
                    decisionTable.remove();
                    chips -= bet1;
                    bet1 *= 2;
                    chipsLabel.setText("Chips: " + chips);
                    bet1Label.setText("Bet: " + bet1);
                    dealCardToPlayer(posX, posY, hand, handValueLabel, handLabelPosX, () -> {
                        endHand = true;
                        dealCardToPlayer(GameConfig.secondHandPosX, GameConfig.cardPosY, playerCardsSecondHand, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX, () -> {
                            if (Integer.parseInt(getHandValue(playerCardsSecondHand)) == 21) {
                                drawDealerCards(() -> checkHandResultSplitHand());
                            } else {
                                showDecisionButtons(playerCardsSecondHand, GameConfig.secondHandPosX, GameConfig.cardPosY, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX);
                                showWhichHandMessage(handResultLabelSecondHand, "HERE", GameConfig.secondHandResultLabelPosX, GameConfig.cardPosY);
                                showWhichHandMessage(handResultLabelFirstHand, "", GameConfig.firstHandResultLabelPosX, GameConfig.cardPosY);
                            }
                        });
                    });
                } else {
                    decisionTable.remove();
                    chips -= bet2;
                    bet2 *= 2;
                    chipsLabel.setText("Chips: " + chips);
                    bet2Label.setText("Bet: " + bet2);
                    dealCardToPlayer(posX, posY, hand, handValueLabel, handLabelPosX, () -> {
                        drawDealerCards(() -> checkHandResultSplitHand());
                    });
                }
            }
        });

        TextButton splitButton = new TextButton("Split", skin);
        splitButton.getLabel().setFontScale(0.70f);
        splitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handCounter++;
                playerCardsFirstHand.add(hand.get(1));
                playerCardsSecondHand.add(hand.get(0));
                for (Actor card : playerCardsActors) {
                    card.remove();
                }
                playerHandValueLabelStartHand.setText("");
                splitHand(GameConfig.firstHandPosX, GameConfig.cardPosY, playerCardsFirstHand, playerHandValueLabelFirstHand, GameConfig.firstHandLabelPosX, GameConfig.singleHandPosX + GameConfig.xOffset, GameConfig.cardPosY + GameConfig.yOffset, () -> {
                    dealCardToPlayer(GameConfig.firstHandPosX, GameConfig.cardPosY, playerCardsFirstHand, playerHandValueLabelFirstHand, GameConfig.firstHandLabelPosX, () -> {
                        if (Integer.parseInt(getHandValue(playerCardsFirstHand)) == 21) {
                            endHand = true;
                            dealCardToPlayer(GameConfig.secondHandPosX, GameConfig.cardPosY, playerCardsSecondHand, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX, () -> {
                                if (Integer.parseInt(getHandValue(playerCardsSecondHand)) == 21) {
                                    showHandResultMessage(handResultLabel, "TWO BLACKJACKS! DOUBLE WINN!", GameConfig.singleHandResultLabelPosX, GameConfig.singleHandResultLabelPosY);
                                    chips += bet1 * 5;
                                    chipsLabel.setText("Chips: " + chips);
                                    bet1 = 0;
                                    bet1Label.setText("Bet: " + bet1);
                                    bet2 = 0;
                                    bet2Label.setText("Bet: " + bet2);
                                    handsWon += 2;
                                } else if (Integer.parseInt(getHandValue(playerCardsSecondHand)) < 21) {
                                    showWhichHandMessage(handResultLabelSecondHand, "HERE", GameConfig.secondHandResultLabelPosX, GameConfig.cardPosY);
                                    showDecisionButtons(playerCardsSecondHand, GameConfig.secondHandPosX, GameConfig.cardPosY, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX);
                                }
                            });
                        } else if (Integer.parseInt(getHandValue(playerCardsFirstHand)) < 21) {
                            showWhichHandMessage(handResultLabelFirstHand, "HERE", GameConfig.firstHandResultLabelPosX, GameConfig.cardPosY);
                            showDecisionButtons(playerCardsFirstHand, GameConfig.firstHandPosX, GameConfig.cardPosY, playerHandValueLabelFirstHand, GameConfig.firstHandLabelPosX);
                        }
                    });
                });
                splitHand(GameConfig.secondHandPosX, GameConfig.cardPosY, playerCardsSecondHand, playerHandValueLabelSecondHand, GameConfig.secondHandLabelPosX, GameConfig.singleHandPosX, GameConfig.cardPosY, () -> {});
                chips -= bet1;
                chipsLabel.setText("Chips: " + chips);
                bet2 = bet1;
                bet2Label.setText("Bet: " + bet2);
                decisionTable.remove();
            }
        });

        decisionTable.add(hitButton).width(GameConfig.WIDTH / 5.5f).height(GameConfig.HEIGHT / 6.2f).pad(5);
        decisionTable.add(standButton).width(GameConfig.WIDTH / 5.5f).height(GameConfig.HEIGHT / 6.2f).pad(5);
        if (chips >= bet1) {
            if (hand.size() == 2) {
                decisionTable.add(doubleButton).width(GameConfig.WIDTH / 5.5f).height(GameConfig.HEIGHT / 6f).pad(5);
                if (hand.get(0).getValue() == hand.get(1).getValue() && handCounter == 0) {
                    decisionTable.add(splitButton).width(GameConfig.WIDTH / 5.5f).height(GameConfig.HEIGHT / 6f).pad(5);
                }
            }
        }

        table.add(decisionTable);
        table.setFillParent(true);
        stage.addActor(table);
    }

    private void showWhichHandMessage(Label label, String message, float posX, float posY) {
        label.setText(message);
        label.pack();
        label.setPosition(posX - label.getWidth() / 2, posY - label.getHeight() * 2);
    }

    private void showHandResultMessage(Label label, String message, float posX, float posY) {
        label.setText(message);
        label.pack();
        label.setPosition(posX - label.getWidth()/2, posY);
        chipButton10.setTouchable(Touchable.enabled);
        chipButton25.setTouchable(Touchable.enabled);
        chipButton50.setTouchable(Touchable.enabled);
        chipButton100.setTouchable(Touchable.enabled);
    }

    private void drawDealerCards(Runnable callback) {
        dealCardToDealer(() -> {
            int dealerHandValue = Integer.parseInt(getHandValue(dealerCards));
            boolean hasSoft17 = isSoft17(dealerCards);
            if (dealerHandValue < 17 || ((dealerHandValue == 17 && hasSoft17) && GameManager.DEALER_HIT_SOFT_17_ENABLED)) {
                stage.addAction(Actions.sequence(
                    Actions.delay(0.5f), // Delay before dealing the next card
                    Actions.run(() -> drawDealerCards(callback)) // Recursive call
                ));
            } else {
                callback.run();
            }
        });
    }

    private boolean isSoft17(ArrayList<Card> cards) {
        int handValue = 0;
        int aceCount = 0;
        for (Card card : cards) {
            handValue += card.getValue();
            if (card.getValue() == 11) {
                aceCount++;
            }
        }
        while(handValue > 21 && aceCount > 0) {
            handValue -= 10;
            aceCount--;
        }
        return handValue == 17 && aceCount > 0;
    }

    private void checkHandResultSingleHand() {
        int playerHandValue = Integer.parseInt(getHandValue(playerCardsStartHand));
        int dealerHandValue = Integer.parseInt(getHandValue(dealerCards));
        if (playerHandValue > 21) {
            showHandResultMessage(handResultLabel, "Player Busts, Dealer Wins!", GameConfig.singleHandResultLabelPosX, GameConfig.singleHandResultLabelPosY);
            bet1 = 0;
            bet1Label.setText("Bet: " + bet1);
        } else if (dealerHandValue > 21) {
            showHandResultMessage(handResultLabel,"Dealer Busts, Player Wins!", GameConfig.singleHandResultLabelPosX, GameConfig.singleHandResultLabelPosY);
            chips += bet1 * 2;
            chipsLabel.setText("Chips: " + chips);
            bet1 = 0;
            bet1Label.setText("Bet: " + bet1);
            handsWon++;
        } else if (playerHandValue > dealerHandValue) {
            showHandResultMessage(handResultLabel,"Player Wins!", GameConfig.singleHandResultLabelPosX, GameConfig.singleHandResultLabelPosY);
            chips += bet1 * 2;
            chipsLabel.setText("Chips: " + chips);
            bet1 = 0;
            bet1Label.setText("Bet: " + bet1);
            handsWon++;
        } else if (playerHandValue < dealerHandValue) {
            showHandResultMessage(handResultLabel,"Dealer Wins!", GameConfig.singleHandResultLabelPosX, GameConfig.singleHandResultLabelPosY);
            bet1 = 0;
            bet1Label.setText("Bet: " + bet1);
        } else {
            showHandResultMessage(handResultLabel,"Push!", GameConfig.singleHandResultLabelPosX, GameConfig.singleHandResultLabelPosY);
            chips += bet1;
            chipsLabel.setText("Chips: " + chips);
            bet1 = 0;
            bet1Label.setText("Bet: " + bet1);
        }
    }

    private void checkHandResultSplitHand() {
        int playerHandValueFirstHand = Integer.parseInt(getHandValue(playerCardsFirstHand));
        int playerHandValueSecondHand = Integer.parseInt(getHandValue(playerCardsSecondHand));
        int dealerHandValue = Integer.parseInt(getHandValue(dealerCards));
        if (playerHandValueFirstHand > 21 || (playerHandValueFirstHand < dealerHandValue && dealerHandValue <= 21)) {
            showWhichHandMessage(handResultLabelFirstHand,"LOST!", GameConfig.firstHandResultLabelPosX, GameConfig.cardPosY);
            bet1 = 0;
            bet1Label.setText("Bet: " + bet1);
        } else if (dealerHandValue > 21 || playerHandValueFirstHand > dealerHandValue) {
            showWhichHandMessage(handResultLabelFirstHand,"WIN!", GameConfig.firstHandResultLabelPosX, GameConfig.cardPosY);
            chips += bet1 * 2;
            chipsLabel.setText("Chips: " + chips);
            bet1 = 0;
            bet1Label.setText("Bet: " + bet1);
            handsWon++;
        } else {
            showWhichHandMessage(handResultLabelFirstHand,"PUSH!", GameConfig.firstHandResultLabelPosX, GameConfig.cardPosY);
            chips += bet1;
            chipsLabel.setText("Chips: " + chips);
            bet1 = 0;
            bet1Label.setText("Bet: " + bet1);
        }
        if (playerHandValueSecondHand > 21 || (playerHandValueSecondHand < dealerHandValue && dealerHandValue <= 21)) {
            showWhichHandMessage(handResultLabelSecondHand,"LOST!", GameConfig.secondHandResultLabelPosX, GameConfig.cardPosY);
            bet2 = 0;
            bet2Label.setText("Bet: " + bet2);
        } else if (dealerHandValue > 21 || playerHandValueSecondHand > dealerHandValue) {
            showWhichHandMessage(handResultLabelSecondHand,"WIN!", GameConfig.secondHandResultLabelPosX, GameConfig.cardPosY);
            chips += bet2 * 2;
            chipsLabel.setText("Chips: " + chips);
            bet2 = 0;
            bet2Label.setText("Bet: " + bet2);
            handsWon++;
        } else {
            showWhichHandMessage(handResultLabelSecondHand, "PUSH!", GameConfig.secondHandResultLabelPosX, GameConfig.cardPosY);
            chips += bet2;
            chipsLabel.setText("Chips: " + chips);
            bet2 = 0;
            bet2Label.setText("Bet: " + bet2);
        }
        chipButton10.setTouchable(Touchable.enabled);
        chipButton25.setTouchable(Touchable.enabled);
        chipButton50.setTouchable(Touchable.enabled);
        chipButton100.setTouchable(Touchable.enabled);
    }
    private void resetGame(Runnable callback) {
        handCounter = 0;
        endHand = false;
        playerCardsStartHand.clear();
        playerCardsFirstHand.clear();
        playerCardsSecondHand.clear();
        dealerCards.clear();


        for (Actor card : playerCardsActors) {
            card.addAction(Actions.sequence(
                Actions.moveTo(-card.getWidth(), GameConfig.HEIGHT, 0.5f),
                Actions.run(card::remove)
            ));
        }
        playerCardsActors.clear();

        for (Actor card : dealerCardsActors) {
            card.addAction(Actions.sequence(
                Actions.moveTo(-card.getWidth(), GameConfig.HEIGHT, 0.5f),
                Actions.run(card::remove)
            ));
        }
        dealerCardsActors.clear();

        if (playerHandValueLabelStartHand != null) {
            playerHandValueLabelStartHand.setText("");
        }
        if (dealerHandValueLabel != null) {
            dealerHandValueLabel.setText("");
        }
        if (handResultLabel != null) {
            handResultLabel.setText("");
        }
        if (playerHandValueLabelFirstHand != null) {
            playerHandValueLabelFirstHand.setText("");
        }
        if (playerHandValueLabelSecondHand != null) {
            playerHandValueLabelSecondHand.setText("");
        }
        if (handResultLabelFirstHand != null) {
            handResultLabelFirstHand.setText("");
        }
        if (handResultLabelSecondHand != null) {
            handResultLabelSecondHand.setText("");
        }
        if (bet2Label != null) {
            bet2Label.setText("");
        }
        if (CardDeck.getCardsLeftInDeck() < 20) {
            CardDeck.initDeck();
        }

        callback.run();
    }
    private void splitHand(Float posX, Float posY, ArrayList<Card> playerCards, Label playerHandValueLabel, Float handLabelPosX, Float startPosX, Float startPosY, Runnable callback) {
        Card card = playerCards.get(0);
        Image cardImage = createCardImage(card);
        cardImage.setPosition(startPosX, startPosY);
        playerCardsActors.add(cardImage);
        stage.addActor(cardImage);

        Action moveAction = Actions.moveTo(posX, posY, 0.8f);
        cardImage.addAction(Actions.sequence(moveAction, Actions.run(() -> {
            updateHandValue(playerHandValueLabel, playerCards, handLabelPosX);
            callback.run();
        })));
    }
}
