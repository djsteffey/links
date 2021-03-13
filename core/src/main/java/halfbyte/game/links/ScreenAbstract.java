package halfbyte.game.links;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class ScreenAbstract implements Screen {
    // variables
    protected IGameServices m_game_services;
    protected Stage m_stage;
    protected ScreenAbstract m_sub_screen;

    // methods
    public ScreenAbstract(IGameServices game_services){
        // save
        this.m_game_services = game_services;

        // make the stage
        this.m_stage = new Stage(new FitViewport(Constants.RESOLUTION_WIDTH, Constants.RESOLUTION_HEIGHT));
    }

    protected void setSubScreen(ScreenAbstract screen){
        this.m_sub_screen = screen;
        this.m_sub_screen.show();
    }

    protected void clearSubScreen(){
        this.m_sub_screen.hide();
        this.m_sub_screen = null;
    }

    @Override
    public void dispose() {
        this.m_stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.m_stage.act(delta);
        this.m_stage.draw();
        if (this.m_sub_screen != null){
            this.m_sub_screen.render(delta);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.m_stage);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resize(int width, int height) {

    }
}
