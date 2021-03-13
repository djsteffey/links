package halfbyte.game.links;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class ScreenMain extends ScreenAbstract {
    // variables


    // methods
    public ScreenMain(IGameServices game_services) {
        super(game_services);

        // skin
        Skin skin = this.m_game_services.getAssetManager().get("ui/skin.json", Skin.class);

        // buttons
        Table table = new Table();
        table.defaults().pad(8.0f);

        float button_width = 256.0f;
        float button_height = 128.0f;

        TextButton tb = new TextButton("Play", skin.get("text_button_style_long", TextButton.TextButtonStyle.class));
        tb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenMain.this.m_game_services.setNextScreen(new ScreenSizeSelect(ScreenMain.this.m_game_services));
            }
        });
        tb.setSize(button_width, button_height);
        table.add(tb).width(button_width).height(button_height).expand();

        tb = new TextButton("Shop", skin.get("text_button_style_long", TextButton.TextButtonStyle.class));
        tb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenMain.this.m_game_services.setNextScreen(new ScreenShop(ScreenMain.this.m_game_services));
            }
        });
        tb.setSize(button_width, button_height);
        table.add(tb).width(button_width).height(button_height).expand();

        tb = new TextButton("Exit", skin.get("text_button_style_long", TextButton.TextButtonStyle.class));
        tb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        tb.setSize(button_width, button_height);
        table.row();
        table.add(tb).width(button_width).height(button_height).expand();

        table.pack();
        table.setPosition(Constants.RESOLUTION_WIDTH / 2, Constants.RESOLUTION_HEIGHT / 2, Align.center);
        this.m_stage.addActor(table);
    }
}
