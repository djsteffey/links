package halfbyte.game.links.board;

import com.badlogic.gdx.scenes.scene2d.Group;

import halfbyte.game.links.Util;

public class Board extends Group {
    // variables
    private int m_width_in_blocks;
    private int m_height_in_blocks;
    private Block[][] m_blocks;

    // methods
    public Board(int width_in_blocks, int height_in_blocks){
        this.m_width_in_blocks = width_in_blocks;
        this.m_height_in_blocks = height_in_blocks;
        this.m_blocks = new Block[this.m_width_in_blocks][this.m_height_in_blocks];
        for (int y = 0; y < this.m_height_in_blocks; ++y){
            for (int x = 0; x < this.m_width_in_blocks; ++x){
                if (Util.getRandomIntInRange(0, 1000) < 500){
                    this.m_blocks[x][y] = new Block();
                }
            }
        }

    }

}
