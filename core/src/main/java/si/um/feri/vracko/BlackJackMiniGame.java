package si.um.feri.vracko;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;

import si.um.feri.vracko.assets.AssetDescriptors;
import si.um.feri.vracko.config.GameConfig;
import si.um.feri.vracko.screen.IntroScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BlackJackMiniGame extends Game {
    private AssetManager assetManager;
    private SpriteBatch batch;
    public Music menuMusic;
    public Music gameMusic;
    public Sound drawCardSound;
    public Sound chipSound;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Logger.DEBUG);

        // load assets
        assetManager.load(AssetDescriptors.UI_FONT);
        assetManager.load(AssetDescriptors.UI_SKIN);
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.load(AssetDescriptors.DRAW_CARD_SOUND);
        assetManager.load(AssetDescriptors.CHIP_SOUND);
        assetManager.load(AssetDescriptors.MENU_MUSIC);
        assetManager.load(AssetDescriptors.GAME_MUSIC);
        assetManager.finishLoading();   // blocks until all assets are loaded

        GameConfig.initAssets(assetManager);
        drawCardSound = assetManager.get(AssetDescriptors.DRAW_CARD_SOUND);
        chipSound = assetManager.get(AssetDescriptors.CHIP_SOUND);
        menuMusic = assetManager.get(AssetDescriptors.MENU_MUSIC);
        menuMusic.setLooping(true);
        gameMusic = assetManager.get(AssetDescriptors.GAME_MUSIC);
        gameMusic.setLooping(true);

        batch = new SpriteBatch();
        setScreen(new IntroScreen(this));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        batch.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
