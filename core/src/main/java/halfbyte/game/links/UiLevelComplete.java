package halfbyte.game.links;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class UiLevelComplete extends Group {
    public interface IListener{
        void onButtonNext(UiLevelComplete ui);
        void onButtonExit(UiLevelComplete ui);
    }

    // variables
    private IListener m_listener;

    // methods
    public UiLevelComplete(AssetManager am, IListener listener){
        // save
        this.m_listener = listener;

        // size
        this.setSize(640.0f, 640.0f);

        // create a background image and set it
        Image background = new Image(new NinePatch(am.get("panel_blue.png", Texture.class), 32, 32, 32, 32));
        background.setSize(this.getWidth(), this.getHeight());
        background.setColor(1.0f, 1.0f, 1.0f, 0.95f);
        this.addActor(background);
        background.toBack();

        // moves label
        Label label = new Label("Level\nComplete", am.get("ui/skin.json", Skin.class), "large");
        label.setAlignment(Align.center);
        label.setX(this.getWidth() / 2, Align.center);
        label.setY(this.getHeight() - label.getHeight() - 8);
        this.addActor(label);

        // restart and next buttons
        float button_size = 150.0f;
        Button back = new UiButtonWithIcon(am, "cross.png");
        back.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UiLevelComplete.this.onButtonExit();
            }
        });
        back.setSize(button_size, button_size);

        Button next = new UiButtonWithIcon(am, "forward.png");
        next.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UiLevelComplete.this.onButtonNext();
            }
        });
        next.setSize(button_size, button_size);

        Table table = new Table();
        table.defaults().pad(16.0f);
        table.add(back).width(button_size).height(button_size).expand();
        table.add(next).width(button_size).height(button_size).expand();
        table.pack();
        table.setX(this.getWidth() / 2, Align.center);
        table.setY(8.0f);
        this.addActor(table);
    }

    private void onButtonNext(){
        this.m_listener.onButtonNext(this);
    }

    private void onButtonExit(){
        this.m_listener.onButtonExit(this);
    }
}
