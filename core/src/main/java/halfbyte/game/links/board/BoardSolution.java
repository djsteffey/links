package halfbyte.game.links.board;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import halfbyte.game.links.Constants;

public class BoardSolution extends Group {
    // variables
    private Board m_board;

    // methods
    public BoardSolution(AssetManager am, Board board){
        // save
        this.m_board = board;

        // size
        this.setSize(
                this.m_board.getWidthInBlocks() * Constants.SOLUTION_TILE_SIZE,
                this.m_board.getHeightInBlocks() * Constants.SOLUTION_TILE_SIZE
        );

        // background
        Image background = new Image(new NinePatch(am.get("panel_brown.png", Texture.class), 32, 32, 32, 32));
        background.setSize(this.getWidth(), this.getHeight());
        this.addActor(background);
        background.toBack();

        // each solution block
        for (int y = 0; y < this.m_board.getHeightInBlocks(); ++y){
            for (int x = 0; x < this.m_board.getWidthInBlocks(); ++x){
                String s = this.m_board.getSolution()[x][y].getGraphicFilename();
                if (s != "") {
                    Image image = new Image(am.get(
                            this.m_board.getSolution()[x][y].getGraphicFilename(), Texture.class
                    ));
                    image.setSize(Constants.SOLUTION_TILE_SIZE, Constants.SOLUTION_TILE_SIZE);
                    image.setPosition(x * Constants.SOLUTION_TILE_SIZE, y * Constants.SOLUTION_TILE_SIZE);
                    this.addActor(image);
                }
            }
        }
    }
}
