package si.um.feri.vracko.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;
import java.util.List;

import si.um.feri.vracko.SettingsState.NumOfDecksState;
import si.um.feri.vracko.BlackJackMiniGame;
import si.um.feri.vracko.model.Player;

public class GameManager {

    public static final GameManager INSTANCE = new GameManager();
    private static final String INIT_DECK_NUM = "initDeckNum";
    private static final String MUSIC_STATE = "musicState";
    private static final String SOUND_EFFECTS_STATE = "soundEffectsState";
    private static final String DEALER_HIT_SOFT_17 = "dealerHitSoft17";
    private static final String RESULTS_FILE = "results.json";
    private static final String NAME = "nameState";
    private static final String NUM_OF_CHIPS = "numOfChips";
    public static boolean MUSIC_ENABLED;
    public static boolean SOUND_EFFECTS_ENABLED;
    public static boolean DEALER_HIT_SOFT_17_ENABLED;
    public static String playerName;
    public static int numOfChips;
    private static NumOfDecksState initDeckNum;
    private final Preferences PREFS;
    private static List<Player> results;

    private GameManager() {
        PREFS = Gdx.app.getPreferences(BlackJackMiniGame.class.getSimpleName());
        int deckNum = PREFS.getInteger(INIT_DECK_NUM, NumOfDecksState.SIX.getValue());
        initDeckNum = NumOfDecksState.fromValue(deckNum);
        MUSIC_ENABLED = PREFS.getBoolean(MUSIC_STATE, true);
        SOUND_EFFECTS_ENABLED = PREFS.getBoolean(SOUND_EFFECTS_STATE, true);
        DEALER_HIT_SOFT_17_ENABLED = PREFS.getBoolean(DEALER_HIT_SOFT_17, false);
        playerName = PREFS.getString(NAME, "Player");
        numOfChips = PREFS.getInteger(NUM_OF_CHIPS, 250);
        loadResults();
    }

    public NumOfDecksState getInitDeckNum() {
        return initDeckNum;
    }

    public void setInitDeckNum(NumOfDecksState move) {
        initDeckNum = move;
        PREFS.putInteger(INIT_DECK_NUM, move.getValue());
        PREFS.flush();
    }

    public void setMusicState(boolean state) {
        MUSIC_ENABLED = state;
        PREFS.putBoolean(MUSIC_STATE, state);
        PREFS.flush();
    }

    public void setSoundEffectsState(boolean state) {
        SOUND_EFFECTS_ENABLED = state;
        PREFS.putBoolean(SOUND_EFFECTS_STATE, state);
        PREFS.flush();
    }

    public void setDealerHitSoft17(boolean state) {
        DEALER_HIT_SOFT_17_ENABLED = state;
        PREFS.putBoolean(DEALER_HIT_SOFT_17, state);
        PREFS.flush();
    }

    public void setName(String name) {
        playerName = name;
        PREFS.putString(NAME, name);
        PREFS.flush();
    }

    public void setNumOfChips(int chips) {
        numOfChips = chips;
        PREFS.putInteger(NUM_OF_CHIPS, chips);
        PREFS.flush();
    }

    public static void addOrUpdateResult(Player player) {
        boolean playerExists = false;
        for (Player p : results) {
            if (p.getName().equals(player.getName())) {
                p.setScore(p.getScore() + player.getScore());
                playerExists = true;
                break;
            }
        }
        if (!playerExists) {
            results.add(player);
        }
        saveResults();
    }

    public static List<Player> getResults() {
        return results;
    }

    public void loadResults() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        results = json.fromJson(ArrayList.class, Player.class, Gdx.files.local(RESULTS_FILE));
        if (results == null) {
            results = new ArrayList<>();
        }
    }

    private static void saveResults() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        Gdx.files.local(RESULTS_FILE).writeString(json.toJson(results), false);
    }
}
