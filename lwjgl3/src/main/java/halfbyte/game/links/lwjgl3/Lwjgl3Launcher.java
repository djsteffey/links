package halfbyte.game.links.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import halfbyte.game.links.GameLinks;
import halfbyte.game.links.IPlatformServices;

public class Lwjgl3Launcher implements IPlatformServices {
	public static void main(String[] args) {
		// config
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("Links");
		configuration.setWindowedMode(720 / 2, 1280 / 2);
		configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
		configuration.setWindowPosition(600, 100);
		configuration.setResizable(false);

		// game
		GameLinks game = new GameLinks(new Lwjgl3Launcher());

		// run it
		new Lwjgl3Application(game, configuration);
	}

	@Override
	public void getShopItems(IGetShopItemsListener listener) {
		for (int i = 0; i < 5; ++i){
			listener.onItem("Name " + i, "Description " + i, );
		}
	}

	@Override
	public void purchaseShopItem(Object context, IPurchaseShopItemListener listener) {

	}
}