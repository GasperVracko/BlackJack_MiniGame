package si.um.feri.vracko.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.um.feri.vracko.BlackJackMiniGame;
import si.um.feri.vracko.assets.AssetDescriptors;
import si.um.feri.vracko.assets.RegionNames;
import si.um.feri.vracko.common.GameManager;
import si.um.feri.vracko.config.GameConfig;

public class IntroScreen extends ScreenAdapter {
    public static final float INTRO_DURATION_IN_SEC = 2.4f;
    private final BlackJackMiniGame game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private TextureAtlas gameplayAtlas;
    private float duration = 0f;
    private Stage stage;

    public IntroScreen(BlackJackMiniGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        if (GameManager.MUSIC_ENABLED){
            game.menuMusic.play();
        }

        stage.addActor(createBackground());
        stage.addActor(createCardAnimation());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(65 / 255f, 159 / 255f, 221 / 255f, 0f);

        duration += delta;

        // go to the MenuScreen after INTRO_DURATION_IN_SEC seconds
        if (duration > INTRO_DURATION_IN_SEC) {
            game.setScreen(new MenuScreen(game));
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

    private Actor createBackground() {
        Image background = new Image(gameplayAtlas.findRegion(RegionNames.BACKGROUND));
        background.setFillParent(true);
        return background;
    }
    private Actor createCardAnimation() {
        Image card1 = new Image(gameplayAtlas.findRegion(RegionNames.CARD_SPADES_ACE));
        Image card2 = new Image(gameplayAtlas.findRegion(RegionNames.CARD_SPADES_KING));

        // Set initial positions off-screen
        card1.setPosition(-card1.getWidth(), viewport.getWorldHeight() / 2f - card1.getHeight() / 2f);
        card2.setPosition(viewport.getWorldWidth(), viewport.getWorldHeight() / 2f - card2.getHeight() / 2f);

        card1.setOrigin(Align.center);
        card2.setOrigin(Align.center);

        card1.setScale(2.5f);
        card2.setScale(2.5f);

        float centerX = viewport.getWorldWidth() / 2f;
        float centerY = viewport.getWorldHeight() / 2f;

        card1.addAction(
            Actions.sequence(
                Actions.moveTo(centerX - card1.getWidth(), centerY - card1.getHeight() / 2f, .8f),
                Actions.parallel(
                    Actions.moveBy(-200, 150, .8f, Interpolation.pow3Out),
                    Actions.rotateBy(360, .8f)
                ),
                Actions.moveBy(0, -viewport.getWorldHeight(), .6f),
                Actions.removeActor()
            )
        );

        card2.addAction(
            Actions.sequence(
                Actions.moveTo(centerX, centerY - card2.getHeight() / 2f, .8f),
                Actions.parallel(
                    Actions.moveBy(200, 150, .8f, Interpolation.pow3Out),
                    Actions.rotateBy(-360, .8f)
                ),
                Actions.moveBy(0, -viewport.getWorldHeight(), .6f),
                Actions.removeActor()
            )
        );

        stage.addActor(card1);
        stage.addActor(card2);

        return card1; // return one of the cards to satisfy the method signature
    }
}
