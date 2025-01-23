package si.um.feri.vracko.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import si.um.feri.vracko.BlackJackMiniGame;
import si.um.feri.vracko.assets.AssetDescriptors;
import si.um.feri.vracko.assets.RegionNames;
import si.um.feri.vracko.config.GameConfig;
import si.um.feri.vracko.common.GameManager;
import si.um.feri.vracko.model.Player;

import java.util.Comparator;
import java.util.List;

public class LeaderboardScreen extends ScreenAdapter {
    private final BlackJackMiniGame game;
    private final AssetManager assetManager;
    private TextureAtlas gameplayAtlas;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private final List<Player> players;

    public LeaderboardScreen(BlackJackMiniGame game) {
        this.game = game;
        assetManager = game.getAssetManager();

        this.players = GameManager.getResults();

    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        players.sort(Comparator.comparingInt(Player::getScore).reversed());

        stage.addActor(createUi());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        Table table = new Table();
        table.defaults().pad(20);

        TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        Label titleLabel = new Label("Leaderboard", skin);
        titleLabel.setFontScale(1.5f);
        table.add(titleLabel).colspan(2);
        table.row();

        Label nameSubtitle = new Label("Name", skin);
        Label handsWonSubtitle = new Label("Hands Won", skin);
        table.add(nameSubtitle).uniformX();
        table.add(handsWonSubtitle).uniformX();
        table.row();

        // Player rows
        Table playerTable = new Table();
        playerTable.defaults().expandX().uniformX().pad(10); // Apply to all rows
        for (Player player : players) {
            Label playerName = new Label(player.getName(), skin);
            Label playerScore = new Label(String.valueOf(player.getScore()), skin);
            playerTable.add(playerName).align(Align.center);
            playerTable.add(playerScore).align(Align.center);
            playerTable.row();
        }

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(playerTable, skin);// Disables touch scrolling of the scroll bars
        scrollPane.setFadeScrollBars(true); // Disables fading of the scroll bars
        scrollPane.setScrollbarsVisible(false); // Completely hides the scroll bars
        scrollPane.setScrollingDisabled(true, false); // Allow only vertical scrolling
        scrollPane.setScrollbarsOnTop(true); // Puts the scroll bars on top of the scroll pane

        table.add(scrollPane).colspan(2).fill().expand().padTop(10);
        table.row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        table.add(backButton).colspan(2);

        table.center();
        table.setFillParent(true);
        return table;
    }
}
