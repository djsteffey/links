package halfbyte.game.links.tileset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class TilesetForBlock extends Tileset {
    // variables


    // methods
    public TilesetForBlock(AssetManager am){
        super(am.get("blocks_eelhovercraft.png", Texture.class), 20);
    }
}
