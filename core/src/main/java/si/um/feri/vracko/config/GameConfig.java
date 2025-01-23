package si.um.feri.vracko.config;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


import si.um.feri.vracko.assets.AssetDescriptors;
import si.um.feri.vracko.assets.RegionNames;

/**
 * Be careful which 'GameConfig' class you use in a specific examples. This class is 'public' because
 * of demonstration purposes. In this way, you can use it in all packages inside gameplay and also in
 * Desktop Launcher for this example. But because it is set to 'public' you can also access it from
 * other examples in different packages. Be careful which class/package you include inside an example.
 */
public class GameConfig {

    private static AssetManager assetManager;
    private static TextureAtlas gameplayAtlas;
    public static final float WIDTH = 900f; // pixels
    public static final float HEIGHT = 600f;    // pixels
    public static final float WORLD_WIDTH = 90f;    // world units
    public static final float WORLD_HEIGHT = 60f;   // world units
    public static final float xOffset = 15f; // Adjust this value as needed
    public static final float yOffset = 15f; // Adjust this value as needed

    public static float deckCardPositionX;
    public static float deckCardPositionY;

    public static float singleHandPosX;
    public static float firstHandPosX;
    public static float secondHandPosX;
    public static float cardPosY = 80f;

    public static float singleHandLabelPosX;
    public static float firstHandLabelPosX;
    public static float secondHandLabelPosX;

    public static float singleHandResultLabelPosX = GameConfig.WIDTH / 2;
    public static float singleHandResultLabelPosY = GameConfig.HEIGHT / 2;

    public static float firstHandResultLabelPosX;
    public static float secondHandResultLabelPosX;

    private GameConfig() {
    }

    public static void initAssets(AssetManager assetManager){
        GameConfig.assetManager = assetManager;
        GameConfig.gameplayAtlas = GameConfig.assetManager.get(AssetDescriptors.GAMEPLAY);
        Image deckCard = new Image(new TextureRegionDrawable(gameplayAtlas.findRegion(RegionNames.CARD_BACK)));
        GameConfig.deckCardPositionX = GameConfig.WIDTH - deckCard.getWidth() * 1.5f - 10;
        GameConfig.deckCardPositionY =  GameConfig.HEIGHT - deckCard.getHeight() * 1.5f - 20;
        GameConfig.singleHandPosX = GameConfig.WIDTH / 2 - deckCard.getWidth() * 1.5f / 2;
        GameConfig.firstHandPosX = singleHandPosX + deckCard.getWidth() * 2f ;
        GameConfig.secondHandPosX = singleHandPosX - deckCard.getWidth() * 2f;
        GameConfig.singleHandLabelPosX = singleHandPosX + deckCard.getWidth() * 1.5f / 2;
        GameConfig.firstHandLabelPosX = firstHandPosX + deckCard.getWidth() * 1.5f / 2;
        GameConfig.secondHandLabelPosX = secondHandPosX + deckCard.getWidth() * 1.5f / 2;
        GameConfig.firstHandResultLabelPosX = firstHandPosX + deckCard.getWidth() * 1.5f / 2;
        GameConfig.secondHandResultLabelPosX = secondHandPosX + deckCard.getWidth() * 1.5f / 2;

    }
}
