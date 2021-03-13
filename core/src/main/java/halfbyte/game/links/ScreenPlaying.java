package halfbyte.game.links;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import java.util.Random;
import halfbyte.game.links.board.Board;
import halfbyte.game.links.board.BoardSolution;

public class ScreenPlaying extends ScreenAbstract {
    // variables
    private Board m_board;
    private BoardSolution m_solution;
    private Label m_label_level;
    private Button m_button_restart;
    private Button m_button_bypass;
    private Button m_button_exit;
    private Table m_button_table;
    private Board.EType m_board_type;
    private int m_current_level;

    // methods
    public ScreenPlaying(IGameServices game_services, Board.EType board_type, int current_level){
        super(game_services);

        // save
        this.m_board_type = board_type;
        this.m_current_level = current_level;

        // skin
        Skin skin = this.m_game_services.getAssetManager().get("ui/skin.json", Skin.class);

        // label for level
        this.m_label_level = new Label("Level: " + (this.m_current_level + 1), skin);
        this.m_label_level.setPosition(0.0f, 0.0f);
        this.m_stage.addActor(this.m_label_level);

        // create board and solution
        // since createNext increments the current level we subtract 1 first here in the beginning
        this. m_current_level -= 1;
        this.createNext();

        // buttons
        float button_size = 150.0f;
        this.m_button_restart = new UiButtonWithIcon(this.m_game_services.getAssetManager(), "return.png");
        this.m_button_restart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenPlaying.this.onButtonRestart();
            }
        });
        this.m_button_restart.setSize(button_size, button_size);

        this.m_button_exit = new UiButtonWithIcon(this.m_game_services.getAssetManager(), "cross.png");
        this.m_button_exit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenPlaying.this.onButtonExit();
            }
        });
        this.m_button_exit.setSize(button_size, button_size);

        this.m_button_bypass = new UiButtonWithIcon(this.m_game_services.getAssetManager(), "forward.png");
        this.m_button_bypass.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenPlaying.this.onButtonBypass();
            }
        });
        this.m_button_bypass.setSize(button_size, button_size);

        this.m_button_table = new Table();
        this.m_button_table.defaults().pad(4.0f);
        this.m_button_table.add(this.m_button_exit).width(button_size).height(button_size).expand();
        this.m_button_table.add(this.m_button_restart).width(button_size).height(button_size).expand();
        this.m_button_table.add(this.m_button_bypass).width(button_size).height(button_size).expand();
        this.m_button_table.pack();
        this.m_button_table.setPosition(Constants.RESOLUTION_WIDTH - 4.0f - this.m_button_table.getWidth(), 4.0f);
        this.m_stage.addActor(this.m_button_table);
    }

    private void onButtonRestart(){
        this.m_board.reset();
    }

    private void onButtonNext(){
        this.createNext();
    }

    private void onButtonBypass(){
        // hide buttons
        this.enableButtons(false);

        // setup the ui
        UiBypassLevel ui = new UiBypassLevel(this.m_game_services, new UiDialogYesNo.IListener() {
            @Override
            public void onButtonYes(UiDialogYesNo ui) {
                // setup ui to move off screen
                ui.addAction(Actions.sequence(
                        Actions.moveTo(
                                Constants.RESOLUTION_WIDTH,
                                (Constants.RESOLUTION_HEIGHT - ui.getHeight()) / 2,
                                0.50f,
                                Interpolation.slowFast
                        ),
                        new Action() {
                            @Override
                            public boolean act(float delta) {
                                ScreenPlaying.this.enableButtons(true);
                                return true;
                            }
                        },
                        Actions.removeActor()
                ));

                // mark this current level as complete
                ScreenPlaying.this.m_game_services.getGameState().setStatus(
                        ScreenPlaying.this.m_board_type,
                        ScreenPlaying.this.m_current_level,
                        GameState.EStatus.COMPLETE
                );

                // decrement coins
                ScreenPlaying.this.m_game_services.getGameState().removeCoins(Constants.COIN_COST_TO_BYPASS);

                // save state
                ScreenPlaying.this.m_game_services.saveGameState();

                // next board
                ScreenPlaying.this.onButtonNext();

                // ensure ui is in front
                ui.toFront();
            }

            @Override
            public void onButtonNo(UiDialogYesNo ui) {
                // animate ui off screen
                ui.addAction(Actions.sequence(
                        Actions.moveTo(
                                Constants.RESOLUTION_WIDTH,
                                (Constants.RESOLUTION_HEIGHT - ui.getHeight()) / 2,
                                0.50f,
                                Interpolation.slowFast
                        ),
                        new Action() {
                            @Override
                            public boolean act(float delta) {
                                ScreenPlaying.this.enableButtons(true);
                                return true;
                            }
                        },
                        Actions.removeActor()
                ));

                // ensure it is in front
                ui.toFront();
            }
        });

        // animate ui on screen
        ui.setPosition(-ui.getWidth(), (Constants.RESOLUTION_HEIGHT / 2), Align.center);
        ui.addAction(Actions.sequence(
                Actions.moveTo(
                        (Constants.RESOLUTION_WIDTH - ui.getWidth()) / 2,
                        (Constants.RESOLUTION_HEIGHT - ui.getHeight()) / 2,
                        0.50f,
                        Interpolation.fastSlow
                )
        ));
        this.m_stage.addActor(ui);
    }

    private void onButtonExit(){
        this.m_game_services.setNextScreen(new ScreenSizeSelect(this.m_game_services));
    }

    private void onBoardMoveComplete(){

    }

    private void onBoardGameComplete(){
        // disable buttons
        this.enableButtons(false);

        // mark complete
        this.m_game_services.getGameState().setStatus(this.m_board_type, this.m_current_level, GameState.EStatus.COMPLETE);
        this.m_game_services.saveGameState();

        // move in the level complete
        UiLevelComplete ui = new UiLevelComplete(this.m_game_services.getAssetManager(), new UiLevelComplete.IListener() {
            @Override
            public void onButtonNext(UiLevelComplete ui) {
                ui.addAction(Actions.sequence(
                        Actions.moveTo(
                                Constants.RESOLUTION_WIDTH,
                                (Constants.RESOLUTION_HEIGHT - ui.getHeight()) / 2,
                                0.50f,
                                Interpolation.slowFast
                        ),
                        new Action() {
                            @Override
                            public boolean act(float delta) {
                                ScreenPlaying.this.enableButtons(true);
                                return true;
                            }
                        },
                        Actions.removeActor()
                ));
                ScreenPlaying.this.onButtonNext();
                ui.toFront();
            }

            @Override
            public void onButtonExit(UiLevelComplete ui) {
                ScreenPlaying.this.m_game_services.setNextScreen(new ScreenSizeSelect(ScreenPlaying.this.m_game_services));
            }
        });
        ui.setPosition(-ui.getWidth(), (Constants.RESOLUTION_HEIGHT / 2), Align.center);
        ui.addAction(Actions.sequence(
                Actions.moveTo(
                        (Constants.RESOLUTION_WIDTH - ui.getWidth()) / 2,
                        (Constants.RESOLUTION_HEIGHT - ui.getHeight()) / 2,
                        0.50f,
                        Interpolation.fastSlow
                )
        ));
        this.m_stage.addActor(ui);

        // firework
        Random rand = new Random();
        for (int i = 0; i < 3; ++i) {
            Firework firework = new Firework(
                    this.m_game_services.getAssetManager(),
                    Constants.RESOLUTION_WIDTH * 0.25f * (i + 1),
                    0.0f,
                    Util.getRandomFloatInRange(rand, -10.0f, 10.0f),
                    Util.getRandomFloatInRange(rand, 320.0f, 512.0f),
                    Util.getRandomFloatInRange(rand, 1.75f, 2.0f),
                    Util.getRandomColor(),
                    true
            );
            this.m_stage.addActor(firework);
        }
    }

    private void createNext(){
        // increment current level
        this.m_current_level += 1;

        // clear current board
        if (this.m_board != null){
            this.m_board.remove();
        }

        // create new board
        this.m_board = new Board(this.m_game_services.getAssetManager(), this.m_board_type.getSize(), this.m_board_type.getSize(), this.m_current_level, new Board.IListener() {
            @Override
            public void onMoveComplete() {
                ScreenPlaying.this.onBoardMoveComplete();
            }

            @Override
            public void onGameComplete() {
                ScreenPlaying.this.onBoardGameComplete();
            }
        });
        this.m_board.setPosition(Constants.RESOLUTION_WIDTH / 2, Constants.RESOLUTION_HEIGHT / 2, Align.center);
        this.m_stage.addActor(this.m_board);

        // clear current solution
        if (this.m_solution != null){
            this.m_solution.remove();
        }

        // create new solution
        this.m_solution = new BoardSolution(this.m_game_services.getAssetManager(), this.m_board);
        this.m_solution.setPosition(4.0f, Constants.RESOLUTION_HEIGHT - 4.0f - this.m_solution.getHeight());
        this.m_stage.addActor(this.m_solution);

        // moves
        this.m_label_level.setText("Level: " + (this.m_current_level + 1));
    }

    private void enableButtons(boolean enable){
        if (enable){
            this.m_button_exit.setTouchable(Touchable.enabled);
            this.m_button_restart.setTouchable(Touchable.enabled);
            this.m_button_bypass.setTouchable(Touchable.enabled);
            this.m_button_table.addAction(Actions.moveTo(this.m_button_table.getX(), 4.0f, 0.50f));
        }
        else{
            this.m_button_exit.setTouchable(Touchable.disabled);
            this.m_button_restart.setTouchable(Touchable.disabled);
            this.m_button_bypass.setTouchable(Touchable.disabled);
            this.m_button_table.addAction(Actions.moveTo(this.m_button_table.getX(), -this.m_button_table.getHeight(), 0.50f));
        }
    }
}
