package halfbyte.game.links;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameLinks extends Game implements IGameServices{
	// variables
	private IPlatformServices m_platform_services;
	private AssetManager m_asset_manager;
	private ScreenAbstract m_next_screen;

	// methods
	public GameLinks(IPlatformServices platform_service){
		this.m_platform_services = platform_service;
	}

	@Override
	public void create() {
		// assets
		this.m_asset_manager = new AssetManager();
		this.loadAssets();

		this.m_next_screen = new ScreenPlaying(this);
	}

	@Override
	public void render() {
		// see if need to change screens
		if (this.m_next_screen != null){
			this.setScreen(this.m_next_screen);
			this.m_next_screen = null;
		}

		// super
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	private void loadAssets(){
		// create asset manager
		this.m_asset_manager = new AssetManager();

		// load some images
		this.m_asset_manager.load("pixel.png", Texture.class);
		this.m_asset_manager.load("blocks_eelhovercraft.png", Texture.class);

		// load the ui skin
		this.m_asset_manager.load("ui/skin.atlas", TextureAtlas.class);
		this.m_asset_manager.load("ui/skin.json", Skin.class, new SkinLoader.SkinParameter("ui/skin.atlas"));

		// finish loading everything
		this.m_asset_manager.finishLoading();

		// get the skin and modify some of its fonts
		Skin skin = this.m_asset_manager.get("ui/skin.json", Skin.class);

		// generate some ttf fonts
		int sizes[] = { 24, 32, 48, 64, 96};
		for (int size : sizes) {
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/droid_serif_bold.ttf"));
			FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameters.size = size;
			BitmapFont font = generator.generateFont(parameters);
			skin.add("font-" + size, font);
		}

		// set default fonts to size 24 of this ttf
		skin.get(TextButton.TextButtonStyle.class).font = skin.getFont("font-32");
		skin.get(Label.LabelStyle.class).font = skin.getFont("font-48");
		skin.get("smaller", Label.LabelStyle.class).font = skin.getFont("font-24");
		skin.get("small", Label.LabelStyle.class).font = skin.getFont("font-32");
		skin.get("medium", Label.LabelStyle.class).font = skin.getFont("font-48");
		skin.get("large", Label.LabelStyle.class).font = skin.getFont("font-64");
		skin.get("default", CheckBox.CheckBoxStyle.class).font = skin.getFont("font-48");
	}

	// igameservices
	@Override
	public AssetManager getAssetManager() {
		return this.m_asset_manager;
	}
}