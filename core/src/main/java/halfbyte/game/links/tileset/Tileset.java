package halfbyte.game.links.tileset;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.List;

public class Tileset {
    // variables
    private List<TilesetTile> m_tiles;

    // methods
    public Tileset(Texture texture, int tile_size){
        // create tiles
        this.m_tiles = new ArrayList<>();
        for (int y = 0; y < texture.getHeight(); y += tile_size){
            for (int x = 0; x < texture.getWidth(); x += tile_size){
                this.m_tiles.add(new TilesetTile(new TextureRegion(texture, x, y, tile_size, tile_size)));
            }
        }
    }

    public TilesetTile getTilesetTileByIndex(int index){
        return this.m_tiles.get(index);
    }
}
