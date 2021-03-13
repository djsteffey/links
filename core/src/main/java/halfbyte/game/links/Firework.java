package halfbyte.game.links;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import java.util.Random;

public class Firework extends Group {
    // variables
    private AssetManager m_asset_manager;
    private float m_velocity_x;
    private float m_velocity_y;
    private float m_duration;
    private boolean m_explode;

    // methods
    public Firework(AssetManager am, float x, float y, float vx, float vy, float duration, Color color, boolean explode){
        // size
        this.setSize(8.0f, 8.0f);

        // save
        this.m_asset_manager = am;
        this.setPosition(x, y, Align.center);
        this.m_velocity_x = vx;
        this.m_velocity_y = vy;
        this.m_duration = duration;
        this.m_explode = explode;

        // image
        Image image = new Image(am.get("particle.png", Texture.class));
        image.setSize(this.getWidth(), this.getHeight());
        image.setColor(color);
        this.addActor(image);
    }

    @Override
    public void act(float delta) {
        this.moveBy(this.m_velocity_x * delta, this.m_velocity_y * delta);
        this.m_duration -= delta;
        if (this.m_duration <= 0.0f){
            if (this.m_explode){
                this.explode();
            }
            this.remove();
        }
        super.act(delta);
    }

    private void explode(){
        Random rand = new Random();
        for (int i = 0; i < 100; ++i){
            float rot = Util.getRandomFloatInRange(rand, 0.0f, (float)(2 * Math.PI));
            float speed = Util.getRandomFloatInRange(rand, 1.0f, 100.0f);
            float duration = Util.getRandomFloatInRange(rand, 0.5f, 2.0f);
            Color color = Util.getRandomColor();
            Firework f = new Firework(
                    this.m_asset_manager,
                    this.getX(),
                    this.getY(),
                    (float)(Math.cos(rot) * speed),
                    (float)(Math.sin(rot) * speed),
                    duration,
                    color,
                    false
            );
            f.addAction(Actions.fadeOut(duration));
            this.getStage().addActor(f);
        }
    }
}
