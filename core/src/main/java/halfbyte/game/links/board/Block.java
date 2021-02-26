package halfbyte.game.links.board;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.util.ArrayList;
import java.util.List;
import halfbyte.game.links.Constants;
import halfbyte.game.links.Util;
import halfbyte.game.links.tileset.TilesetForBlock;

public class Block extends Group {
    public enum EType{
        RED, GREEN, BLUE, YELLOW, ORANGE, PURPLE;
        public static EType getRandom(){
            return EType.values()[Util.getRandomIntInRange(0, EType.values().length - 1)];
        }
        public int toTilesetIndex(){
            int index = -1;
            switch (this){
                case RED: index = 0; break;
                case GREEN: index = 1; break;
                case BLUE: index = 2; break;
                case YELLOW: index = 3; break;
                case ORANGE: index = 4; break;
                case PURPLE: index = 5; break;
            }
            return index;
        }
        public int toTilesetIndex2(){
            int index = this.toTilesetIndex();
            return 4 * 8 + index;
        }
    }

    // variables
    private EType m_type;
    private TextureRegionDrawable m_normal_face;
    private TextureRegionDrawable m_alternate_face;
    private Image m_image;
    private List<BlockLink> m_links;
    private int m_tile_x;
    private int m_tile_y;

    // methods
    public Block(EType type, TilesetForBlock tileset, int tile_x, int tile_y){
        // save
        this.m_type = type;
        this.m_tile_x = tile_x;
        this.m_tile_y = tile_y;

        // size
        this.setSize(Constants.TILE_SIZE, Constants.TILE_SIZE);

        // position
        this.setPosition(this.m_tile_x * Constants.TILE_SIZE, this.m_tile_y * Constants.TILE_SIZE);

        // image
        this.m_normal_face = new TextureRegionDrawable(tileset.getTilesetTileByIndex(this.m_type.toTilesetIndex()).getTextureRegion());
        this.m_alternate_face = new TextureRegionDrawable(tileset.getTilesetTileByIndex(this.m_type.toTilesetIndex2()).getTextureRegion());
        this.m_image = new Image(this.m_normal_face);
        this.m_image.setSize(this.getWidth(), this.getHeight());
        this.addActor(this.m_image);

        // links
        this.m_links = new ArrayList<>();
    }

    public void setAlternateFace(boolean alternate){
        if (alternate){
            this.m_image.setDrawable(this.m_alternate_face);
        }
        else{
            this.m_image.setDrawable(this.m_normal_face);
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (this.m_image.hit(x, y, touchable) != null){
            return this;
        }
        return null;
    }

    public List<BlockLink> getLinks(){
        return this.m_links;
    }

    public int getTileX(){
        return this.m_tile_x;
    }

    public int getTileY(){
        return this.m_tile_y;
    }

    public void setTile(int tile_x, int tile_y){
        this.m_tile_x = tile_x;
        this.m_tile_y = tile_y;
    }
}
