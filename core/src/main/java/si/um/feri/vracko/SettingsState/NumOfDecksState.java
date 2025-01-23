package si.um.feri.vracko.SettingsState;

public enum NumOfDecksState {
    FOUR(4),
    SIX(6),
    EIGHT(8);

    private final int value;

    NumOfDecksState(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static NumOfDecksState fromValue(int value) {
        for (NumOfDecksState state : values()) {
            if (state.value == value) {
                return state;
            }
        }
        return null;
    }
}
