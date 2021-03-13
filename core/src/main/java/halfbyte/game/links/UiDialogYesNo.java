package halfbyte.game.links;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class UiDialogYesNo extends Group {
    public interface IListener{
        void onButtonYes(UiDialogYesNo ui);
        void onButtonNo(UiDialogYesNo ui);
    }

    // variables
    private IListener m_listener;
    private Button m_button_no;
    private Button m_button_yes;

    // methods
    public UiDialogYesNo(AssetManager am, String text, IListener listener){
        // save
        this.m_listener = listener;

        // size
        this.setSize(640.0f, 640.0f);

        // skin
        Skin skin = am.get("ui/skin.json", Skin.class);

        // create a background image and set it
        Image background = new Image(new NinePatch(am.get("panel_blue.png", Texture.class), 32, 32, 32, 32));
        background.setSize(this.getWidth(), this.getHeight());
        background.setColor(1.0f, 1.0f, 1.0f, 0.95f);
        this.addActor(background);
        background.toBack();

        // label
        Label label = new Label(text, am.get("ui/skin.json", Skin.class), "large");
        label.setAlignment(Align.center);
        label.setX(this.getWidth() / 2, Align.center);
        label.setY(this.getHeight() - label.getHeight() - 8);
        this.addActor(label);

        // restart and next buttons
        float button_size = 150.0f;
        this.m_button_no = new TextButton("No", skin.get("text_button_style_long", TextButton.TextButtonStyle.class));
        this.m_button_no.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UiDialogYesNo.this.onButtonNo();
            }
        });
        this.m_button_no.setSize(button_size, button_size);

        this.m_button_yes = new TextButton("Yes", skin.get("text_button_style_long", TextButton.TextButtonStyle.class));
        this.m_button_yes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UiDialogYesNo.this.onButtonYes();
            }
        });
        this.m_button_yes.setSize(button_size, button_size);


        Table table = new Table();
        table.defaults().pad(16.0f);
        this.addPreButtons(am, table);
        table.add(this.m_button_no).width(button_size).height(button_size).expand();
        table.add(this.m_button_yes).width(button_size).height(button_size).expand();
        this.addPostButtons(am, table);
        table.pack();
        table.setX(this.getWidth() / 2, Align.center);
        table.setY(8.0f);
        this.addActor(table);
    }

    private void onButtonNo(){
        this.m_listener.onButtonNo(this);
    }

    private void onButtonYes(){
        this.m_listener.onButtonYes(this);
    }

    protected void addPreButtons(AssetManager am, Table table){

    }

    protected void addPostButtons(AssetManager am, Table table){

    }

    public void enableYesButton(boolean enabled){
        if (enabled) {
            this.m_button_yes.setTouchable(Touchable.enabled);
            this.m_button_yes.setDisabled(false);
        }
        else{
            this.m_button_yes.setTouchable(Touchable.disabled);
            this.m_button_yes.setDisabled(true);
        }
    }

    public void enableNoButton(boolean enabled){
        if (enabled) {
            this.m_button_no.setTouchable(Touchable.enabled);
            this.m_button_no.setDisabled(false);
        }
        else{
            this.m_button_no.setTouchable(Touchable.disabled);
            this.m_button_no.setDisabled(true);
        }
    }
}
