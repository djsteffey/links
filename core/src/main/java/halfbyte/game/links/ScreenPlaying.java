package halfbyte.game.links;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import halfbyte.game.links.board.Board;

public class ScreenPlaying extends ScreenAbstract {
    // variables
    private Board m_board;
    private Label m_label_moves;

    // methods
    public ScreenPlaying(IGameServices game_services){
        super(game_services);

        // create the board
        this.m_board = new Board(this.m_game_services.getAssetManager(), 6, 5, new Board.IListener() {
            @Override
            public void onMoveComplete(int moves) {
                ScreenPlaying.this.onBoardMoveComplete(moves);
            }

            @Override
            public void onGameComplete() {
                ScreenPlaying.this.onBoardGameComplete();
            }
        });
        this.m_board.setPosition(Constants.RESOLUTION_WIDTH / 2, Constants.RESOLUTION_HEIGHT / 2, Align.center);
        this.m_stage.addActor(this.m_board);

        // label for moves
        this.m_label_moves = new Label("Moves: 0", this.m_game_services.getAssetManager().get("ui/skin.json", Skin.class));
        this.m_label_moves.setPosition(0.0f, 0.0f);
        this.m_stage.addActor(this.m_label_moves);
    }

    private void onBoardMoveComplete(int moves){
        this.m_label_moves.setText("Moves: " + moves);
    }

    private void onBoardGameComplete(){

    }
}
