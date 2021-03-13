package halfbyte.game.links;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import java.text.DecimalFormat;
import halfbyte.game.links.board.Board;

public class ScreenSizeSelect extends ScreenAbstract {
    // variables


    // methods
    public ScreenSizeSelect(IGameServices game_services) {
        super(game_services);

        // skin
        Skin skin = this.m_game_services.getAssetManager().get("ui/skin.json", Skin.class);

        // buttons
        Table table = new Table();
        table.defaults().pad(8.0f);

        float button_width = 384.0f;
        float button_height = 128.0f;

        DecimalFormat df = new DecimalFormat("#.##");
        String percent = df.format(this.m_game_services.getGameState().getPercentComplete(Board.EType.SIZE_4x4) * 100) + "%";
        TextButton tb = new TextButton("4 x 4 - " + percent, skin.get("text_button_style_long", TextButton.TextButtonStyle.class));
        tb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenSizeSelect.this.m_game_services.setNextScreen(new ScreenPlaying(
                        ScreenSizeSelect.this.m_game_services,
                        Board.EType.SIZE_4x4,
                        ScreenSizeSelect.this.m_game_services.getGameState().getFirstIncomplete(Board.EType.SIZE_4x4)
                ));
            }
        });
        tb.setSize(button_width, button_height);
        table.add(tb).width(button_width).height(button_height).expand();

        percent = df.format(this.m_game_services.getGameState().getPercentComplete(Board.EType.SIZE_5x5) * 100) + "%";
        tb = new TextButton("5 x 5 - " + percent, skin.get("text_button_style_long", TextButton.TextButtonStyle.class));
        tb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenSizeSelect.this.m_game_services.setNextScreen(new ScreenPlaying(
                        ScreenSizeSelect.this.m_game_services,
                        Board.EType.SIZE_5x5,
                        ScreenSizeSelect.this.m_game_services.getGameState().getFirstIncomplete(Board.EType.SIZE_5x5)
                ));
            }
        });
        tb.setSize(button_width, button_height);
        table.row();
        table.add(tb).width(button_width).height(button_height).expand();

        percent = df.format(this.m_game_services.getGameState().getPercentComplete(Board.EType.SIZE_6x6) * 100) + "%";
        tb = new TextButton("6 x 6 - " + percent, skin.get("text_button_style_long", TextButton.TextButtonStyle.class));
        tb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenSizeSelect.this.m_game_services.setNextScreen(new ScreenPlaying(
                        ScreenSizeSelect.this.m_game_services,
                        Board.EType.SIZE_6x6,
                        ScreenSizeSelect.this.m_game_services.getGameState().getFirstIncomplete(Board.EType.SIZE_6x6)
                ));
            }
        });
        tb.setSize(button_width, button_height);
        table.row();
        table.add(tb).width(button_width).height(button_height).expand();

        tb = new TextButton("Back", skin.get("text_button_style_long", TextButton.TextButtonStyle.class));
        tb.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenSizeSelect.this.m_game_services.setNextScreen(new ScreenMain(ScreenSizeSelect.this.m_game_services));
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
