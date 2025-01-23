package si.um.feri.vracko.SettingsState;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class NumOfDecksActor extends Image {

    private NumOfDecksState state;

    public NumOfDecksActor(TextureRegion region) {
        super(region);
        state = NumOfDecksState.SIX;
    }

    public void setState(NumOfDecksState state) {
        this.state = state;
    }

    public void setDrawable(TextureRegion region) {
        super.setDrawable(new TextureRegionDrawable(region));
        addAnimation(); // play animation when region changed
    }

    public boolean isDefaultValue() {
        return state == NumOfDecksState.SIX;
    }

    private void addAnimation() {
        setOrigin(Align.center);
        addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.rotateBy(720, 0.25f),
                                Actions.scaleTo(0, 0, 0.25f)
                        ),
                        Actions.scaleTo(1, 1, 0.25f)
                )
        );
    }
}
