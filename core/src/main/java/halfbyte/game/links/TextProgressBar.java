package halfbyte.game.links;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class TextProgressBar extends Group {
    // variables
    private float m_border_size;
    private Image m_background;
    private Image m_foreground;
    private int m_max_value;
    private int m_current_value;
    private Label m_label;
    private boolean m_text_enabled;

    // methods
    public TextProgressBar(AssetManager am, float width, float height) {
        // size
        this.m_border_size = 2.0f;
        this.setSize(width, height);

        // create background
        this.m_background = new Image(am.get("pixel.png", Texture.class));
        this.m_background.setSize(this.getWidth(), this.getHeight());
        this.m_background.setColor(Color.BLUE);
        this.addActor(this.m_background);

        // foreground
        this.m_foreground = new Image(am.get("pixel.png", Texture.class));
        this.m_foreground.setSize(this.getWidth() - 2 * this.m_border_size, this.getHeight() - 2 * this.m_border_size);
        this.m_foreground.setColor(Color.RED);
        this.m_foreground.setPosition(this.m_border_size, this.m_border_size);
        this.m_foreground.setScaleX(0.50f);
        this.addActor(this.m_foreground);

        // starting values
        this.m_max_value = 100;
        this.m_current_value = 50;
        float percent = this.m_current_value / (float)this.m_max_value;
        this.m_foreground.setScaleX(percent);

        // label
        this.m_text_enabled = true;
        this.m_label = new Label(this.m_current_value + " / " + this.m_max_value, am.get("ui/skin.json", Skin.class), "small");
        this.m_label.setSize(this.getWidth(), this.getHeight());
        this.m_label.setAlignment(Align.center);
        this.addActor(this.m_label);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void setMaxValue(int value){
        this.m_max_value = value;
        float percent = this.m_current_value / (float)this.m_max_value;
        this.m_foreground.setScaleX(percent);
        this.m_label.setText(this.m_current_value + " / " + this.m_max_value);
    }

    public void setCurrentValue(int value){
        this.m_current_value = value;
        if (this.m_current_value > this.m_max_value){
            this.m_current_value = this.m_max_value;
        }
        float percent = this.m_current_value / (float)this.m_max_value;
        this.m_foreground.setScaleX(percent);
        this.m_label.setText(this.m_current_value + " / " + this.m_max_value);
    }

    public void setTextEnabled(boolean enabled){
        if (enabled){
            this.addActor(this.m_label);
        }
        else{
            this.m_label.remove();
        }
    }

    public void setBackgroundColor(Color color){
        this.m_background.setColor(color);
    }

    public void setForegroundColor(Color color){
        this.m_foreground.setColor(color);
    }

    public void setTextColor(Color color){
        this.m_label.setColor(color);
    }
}
