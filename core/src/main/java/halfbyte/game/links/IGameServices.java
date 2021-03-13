package halfbyte.game.links;

import com.badlogic.gdx.assets.AssetManager;

public interface IGameServices {
    AssetManager getAssetManager();
    void setNextScreen(ScreenAbstract next);
    GameState getGameState();
    void saveGameState();
    IPlatformServices getPlatformServices();
}
