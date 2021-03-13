package halfbyte.game.links.board;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import halfbyte.game.links.Constants;
import halfbyte.game.links.Util;

public class Block extends Group {
    public enum EType{
        BLACK, BLUE, GREEN, GREY, PINK, RED, YELLOW, NONE;
        public static EType getRandom(Random rand){
            return EType.values()[Util.getRandomIntInRange(rand, 0, EType.values().length - 2)];
        }
        public String getGraphicFilename(){
            String s = "";
            switch (this){
                case BLACK: s = "blocks/tileBlack_01.png"; break;
                case BLUE: s = "blocks/tileBlue_01.png"; break;
                case GREEN: s = "blocks/tileGreen_01.png"; break;
                case GREY: s = "blocks/tileGrey_01.png"; break;
                case PINK: s = "blocks/tilePink_01.png"; break;
                case RED: s = "blocks/tileRed_01.png"; break;
                case YELLOW: s = "blocks/tileYellow_01.png"; break;
            }
            return s;
        }
    }

    // variables
    private EType m_type;
    private Image m_image;
    private List<BlockLink> m_links;
    private int m_current_tile_x;
    private int m_current_tile_y;
    private int m_original_tile_x;
    private int m_original_tile_y;

    // methods
    public Block(EType type, AssetManager am, int tile_x, int tile_y){
        // save
        this.m_type = type;
        this.m_current_tile_x = tile_x;
        this.m_current_tile_y = tile_y;

        // size
        this.setSize(Constants.TILE_SIZE, Constants.TILE_SIZE);

        // position
        this.setPosition(this.m_current_tile_x * Constants.TILE_SIZE, this.m_current_tile_y * Constants.TILE_SIZE);

        // image
        this.m_image = new Image(am.get(this.m_type.getGraphicFilename(), Texture.class));
        this.m_image.setSize(this.getWidth(), this.getHeight());
        this.addActor(this.m_image);

        // links
        this.m_links = new ArrayList<>();
    }

    public void setCurrentAsOriginal(){
        this.m_original_tile_x = this.m_current_tile_x;
        this.m_original_tile_y = this.m_current_tile_y;
    }

    public void restoreToOriginal(){
        this.m_current_tile_x = this.m_original_tile_x;
        this.m_current_tile_y = this.m_original_tile_y;
        this.setPosition(this.m_current_tile_x * Constants.TILE_SIZE, this.m_current_tile_y * Constants.TILE_SIZE);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (this.m_image.hit(x, y, touchable) != null){
            return this;
        }
        return null;
    }

    public EType getType(){
        return this.m_type;
    }

    public List<BlockLink> getLinks(){
        return this.m_links;
    }

    public int getCurrentTileX(){
        return this.m_current_tile_x;
    }

    public int getCurrentTileY(){
        return this.m_current_tile_y;
    }

    public void setCurrentTile(int tile_x, int tile_y){
        this.m_current_tile_x = tile_x;
        this.m_current_tile_y = tile_y;
    }
}
