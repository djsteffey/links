package halfbyte.game.links.tileset;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TilesetTile {
    // variables
    private TextureRegion m_texture_region;

    // methods
    public TilesetTile(TextureRegion tr){
        // save
        this.m_texture_region = tr;
    }

    public TextureRegion getTextureRegion(){
        return this.m_texture_region;
    }
}
