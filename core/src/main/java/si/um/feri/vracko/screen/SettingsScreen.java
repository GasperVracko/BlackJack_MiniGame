package si.um.feri.vracko.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.um.feri.vracko.SettingsState.NumOfDecksState;
import si.um.feri.vracko.assets.AssetDescriptors;
import si.um.feri.vracko.assets.RegionNames;
import si.um.feri.vracko.BlackJackMiniGame;
import si.um.feri.vracko.common.GameManager;
import si.um.feri.vracko.config.GameConfig;

public class SettingsScreen extends ScreenAdapter {

    private final BlackJackMiniGame game;
    private final AssetManager assetManager;
    private TextureAtlas gameplayAtlas;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private ButtonGroup<CheckBox> checkBoxGroup;
    private CheckBox checkBox4;
    private CheckBox checkBox6;
    private CheckBox checkBox8;
    private CheckBox musicCheckBox;
    private CheckBox soundEffectsCheckBox;
    private TextField nameField;
    private Slider chipsSlider;
    public SettingsScreen(BlackJackMiniGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
        skin = assetManager.get(AssetDescriptors.UI_SKIN);

        stage.addActor(createUi());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);

        if (GameManager.MUSIC_ENABLED){
            game.menuMusic.play();
        } else {
            game.menuMusic.stop();
        }

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

    private Actor createUi() {
        Table rootTable = new Table();
        Table table = new Table();
        table.defaults().pad(5);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        rootTable.setBackground(new TextureRegionDrawable(backgroundRegion));

        //Title
        Label titleLabel = new Label("Settings", skin);
        titleLabel.setFontScale(2f);
        rootTable.add(titleLabel).row();

        //Name
        Label nameLable = new Label("Enter your name:", skin);
        nameLable.setFontScale(1.5f);
        table.add(nameLable).row();

        nameField = new TextField(GameManager.playerName, skin);
        nameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = nameField.getText();
                if (name.isEmpty()) {
                    name = "Player";
                }
                GameManager.INSTANCE.setName(name);
            }
        });
        table.add(nameField).center().width(300).pad(5).row();


        //Slider for chips
        Label chipsLabel = new Label("Select amount of chips:", skin);
        chipsLabel.setFontScale(1.5f);
        table.add(chipsLabel).row();

        Label chipsValueLabel = new Label(String.valueOf(GameManager.numOfChips), skin);
        table.add(chipsValueLabel).row();

        chipsSlider = new Slider(10, 500, 10, false, skin);
        chipsSlider.setValue(GameManager.numOfChips);
        chipsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int chips = (int) chipsSlider.getValue();
                GameManager.INSTANCE.setNumOfChips(chips);
                chipsValueLabel.setText(String.valueOf(chips));
            }
        });
        table.add(chipsSlider).center().width(500).pad(5).row();
        //Choose number of decks
        Label chooseLabel = new Label("Choose number of decks", skin);
        chooseLabel.setFontScale(1.5f);
        table.add(chooseLabel).row();

        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                CheckBox checked = checkBoxGroup.getChecked();
                if (checked == checkBox4) {
                    GameManager.INSTANCE.setInitDeckNum(NumOfDecksState.FOUR);
                } else if (checked == checkBox6) {
                    GameManager.INSTANCE.setInitDeckNum(NumOfDecksState.SIX);
                } else if (checked == checkBox8) {
                    GameManager.INSTANCE.setInitDeckNum(NumOfDecksState.EIGHT);
                }
            }
        };
        checkBox4 = new CheckBox(String.valueOf(NumOfDecksState.FOUR.getValue()), skin);
        checkBox6 = new CheckBox(String.valueOf(NumOfDecksState.SIX.getValue()), skin);
        checkBox8 = new CheckBox(String.valueOf(NumOfDecksState.EIGHT.getValue()), skin);

        checkBox4.addListener(listener);
        checkBox6.addListener(listener);
        checkBox8.addListener(listener);

        // Set the scale of the checkboxes
        checkBox4.setTransform(true);
        checkBox4.setScale(0.8f);
        checkBox4.setOrigin(Align.center);
        checkBox6.setTransform(true);
        checkBox6.setScale(0.8f);
        checkBox6.setOrigin(Align.center);
        checkBox8.setTransform(true);
        checkBox8.setScale(0.8f);
        checkBox8.setOrigin(Align.center);

        checkBoxGroup = new ButtonGroup<>(checkBox4, checkBox6, checkBox8);
        checkBoxGroup.setChecked(String.valueOf(GameManager.INSTANCE.getInitDeckNum().getValue()));

        Table checkBoxTable = new Table();
        checkBoxTable.add(checkBox4).pad(5);
        checkBoxTable.add(checkBox6).pad(5);
        checkBoxTable.add(checkBox8).pad(5);

        table.add(checkBoxTable).center().row();

        //Delaer hits on soft 17
        Label dealerLabel = new Label("Dealer hits on soft 17", skin);
        dealerLabel.setFontScale(1.5f);
        table.add(dealerLabel).row();

        CheckBox dealerCheckBox = new CheckBox("", skin);
        dealerCheckBox.setChecked(GameManager.DEALER_HIT_SOFT_17_ENABLED);
        dealerCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.INSTANCE.setDealerHitSoft17(dealerCheckBox.isChecked());
            }
        });
        dealerCheckBox.setTransform(true);
        dealerCheckBox.setScale(0.8f);
        dealerCheckBox.setOrigin(Align.center);

        table.add(dealerCheckBox).center().row();

        //Sounds
        Label soundsLabel = new Label("Sounds", skin);
        soundsLabel.setFontScale(1.5f);
        table.add(soundsLabel).row();

        musicCheckBox = new CheckBox("Music", skin);
        musicCheckBox.setChecked(GameManager.MUSIC_ENABLED);
        musicCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.INSTANCE.setMusicState(musicCheckBox.isChecked());
            }
        });

        soundEffectsCheckBox = new CheckBox("Effects", skin);
        soundEffectsCheckBox.setChecked(GameManager.SOUND_EFFECTS_ENABLED);
        soundEffectsCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.INSTANCE.setSoundEffectsState(soundEffectsCheckBox.isChecked());
            }
        });

        musicCheckBox.setTransform(true);
        musicCheckBox.setScale(0.8f);
        musicCheckBox.setOrigin(Align.center);
        soundEffectsCheckBox.setTransform(true);
        soundEffectsCheckBox.setScale(0.8f);
        soundEffectsCheckBox.setOrigin(Align.center);

        Table soundTable = new Table();
        soundTable.add(musicCheckBox).pad(5).uniformX();
        soundTable.add(soundEffectsCheckBox).pad(5).uniformX();

        table.add(soundTable).center().row();

        //Back Button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(table, skin);// Disables touch scrolling of the scroll bars
        scrollPane.setFadeScrollBars(true); // Disables fading of the scroll bars
        scrollPane.setScrollbarsVisible(false); // Completely hides the scroll bars
        scrollPane.setScrollingDisabled(true, false); // Allow only vertical scrolling
        scrollPane.setScrollbarsOnTop(true); // Puts the scroll bars on top of the scroll pane

        rootTable.add(scrollPane).fill().expand().padTop(10).row();
        rootTable.add(backButton).center().pad(5).row();
        rootTable.setFillParent(true);

        return rootTable;
    }
}
