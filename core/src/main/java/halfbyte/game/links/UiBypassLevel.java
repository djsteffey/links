package halfbyte.game.links;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class UiBypassLevel extends UiDialogYesNo {
    // methods
    public UiBypassLevel(IGameServices gs, IListener listener){
        // super
        super(gs.getAssetManager(), "Bypass level for\n" + Constants.COIN_COST_TO_BYPASS + " coins?", listener);

        // skin
        Skin skin = gs.getAssetManager().get("ui/skin.json", Skin.class);

        // add label for num coins
        Label label = new Label("Current Coins: " + gs.getGameState().getCoins(), skin);
        label.setAlignment(Align.center);
        label.setX(this.getWidth() / 2, Align.center);
        label.setY(this.getHeight() * 0.50f);
        this.addActor(label);

        // disable yes if not enough coins
        if (gs.getGameState().getCoins() < Constants.COIN_COST_TO_BYPASS){
            this.enableYesButton(false);
        }
    }

    @Override
    protected void addPreButtons(AssetManager am, Table table) {
        // purchase coins button
        Button button = new UiButtonWithIcon(am, "cart.png");
        button.setSize(150.0f, 150.0f);
        table.add(button).width(button.getWidth()).height(button.getHeight()).expand();
    }
}
