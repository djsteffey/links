package halfbyte.game.links;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UiButtonWithIcon extends Button {
    // variables
    private Image m_icon;

    // methods
    public UiButtonWithIcon(AssetManager am, String icon_filename){
        super(am.get("ui/skin.json", Skin.class).get("image_button_style", ButtonStyle.class));

        // add the icon
        this.m_icon = new Image(am.get(icon_filename, Texture.class));
        this.addActor(this.m_icon);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        if (this.m_icon != null){
            this.m_icon.setSize(width, height);
        }
    }
}
