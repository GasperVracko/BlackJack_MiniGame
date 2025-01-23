package si.um.feri.vracko.model;

public class Card {
    private final int value;
    private final String texture;

    public Card(int value, String texture) {
        this.value = value;
        this.texture = texture;
    }
    public int getValue() {
        return value;
    }
    public String getTexture() {
        return texture;
    }
}

