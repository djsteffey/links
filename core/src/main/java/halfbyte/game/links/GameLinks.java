package halfbyte.game.links;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameLinks extends Game implements IGameServices{
	// const
	private static final String GAME_STATE_FILENAME = "game_state";

	// variables
	private IPlatformServices m_platform_services;
	private GameState m_game_state;
	private AssetManager m_asset_manager;
	private ScreenAbstract m_next_screen;

	// methods
	public GameLinks(IPlatformServices platform_service){
		this.m_platform_services = platform_service;
	}

	@Override
	public void create() {
		// default clear color
		Gdx.gl.glClearColor(0.67f, 0.84f, 0.90f, 1.0f);

		// state
		this.loadGameState();

		// assets
		this.m_asset_manager = new AssetManager();
		this.loadAssets();

		// first screen
		this.m_next_screen = new ScreenMain(this);
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
		this.m_asset_manager.load("cross.png", Texture.class);
		this.m_asset_manager.load("return.png", Texture.class);
		this.m_asset_manager.load("forward.png", Texture.class);
		this.m_asset_manager.load("particle.png", Texture.class);
		this.m_asset_manager.load("buttonSquare_brown.png", Texture.class);
		this.m_asset_manager.load("buttonSquare_brown_pressed.png", Texture.class);
		this.m_asset_manager.load("buttonSquare_grey.png", Texture.class);
		this.m_asset_manager.load("panel_brown.png", Texture.class);
		this.m_asset_manager.load("panel_blue.png", Texture.class);
		this.m_asset_manager.load("blocks/tileBlack_01.png", Texture.class);
		this.m_asset_manager.load("blocks/tileBlue_01.png", Texture.class);
		this.m_asset_manager.load("blocks/tileGreen_01.png", Texture.class);
		this.m_asset_manager.load("blocks/tileGrey_01.png", Texture.class);
		this.m_asset_manager.load("blocks/tilePink_01.png", Texture.class);
		this.m_asset_manager.load("blocks/tileRed_01.png", Texture.class);
		this.m_asset_manager.load("blocks/tileYellow_01.png", Texture.class);
		this.m_asset_manager.load("buttonLong_brown.png", Texture.class);
		this.m_asset_manager.load("buttonLong_brown_pressed.png", Texture.class);
		this.m_asset_manager.load("buttonLong_grey.png", Texture.class);
		this.m_asset_manager.load("cart.png", Texture.class);

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

		// image button style
		Button.ButtonStyle ibs = new Button.ButtonStyle();
		ibs.up = new NinePatchDrawable(new NinePatch(
				this.m_asset_manager.get("buttonSquare_brown.png", Texture.class),
				20, 20, 20, 20
		));
		ibs.down = new NinePatchDrawable(new NinePatch(
				this.m_asset_manager.get("buttonSquare_brown_pressed.png", Texture.class),
				20, 20, 20, 20
		));
		ibs.disabled = new NinePatchDrawable(new NinePatch(
				this.m_asset_manager.get("buttonSquare_grey.png", Texture.class),
				20, 20, 20, 20
		));
		skin.add("image_button_style", ibs);

		// image button style long
		TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
		tbs.font = skin.getFont("font-48");
		tbs.up = new NinePatchDrawable(new NinePatch(
				this.m_asset_manager.get("buttonLong_brown.png", Texture.class),
				20, 20, 20, 20
		));
		tbs.down = new NinePatchDrawable(new NinePatch(
				this.m_asset_manager.get("buttonLong_brown_pressed.png", Texture.class),
				20, 20, 20, 20
		));
		tbs.disabled = new NinePatchDrawable(new NinePatch(
				this.m_asset_manager.get("buttonLong_grey.png", Texture.class),
				20, 20, 20, 20
		));
		skin.add("text_button_style_long", tbs);
	}

	private void loadGameState(){
		try {
			FileHandle fh = Gdx.files.local(GAME_STATE_FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fh.read());
			this.m_game_state = (GameState)ois.readObject();
			ois.close();
		}
		catch (Exception e){
			e.printStackTrace();
			this.m_game_state = new GameState();
		}
	}

	// igameservices
	@Override
	public AssetManager getAssetManager() {
		return this.m_asset_manager;
	}

	@Override
	public void setNextScreen(ScreenAbstract next) {
		this.m_next_screen = next;
	}

	@Override
	public GameState getGameState() {
		return this.m_game_state;
	}

	@Override
	public void saveGameState() {
		try {
			FileHandle fh = Gdx.files.local(GAME_STATE_FILENAME);
			ObjectOutputStream oos = new ObjectOutputStream(fh.write(false));
			oos.writeObject(this.m_game_state);
			oos.flush();
			oos.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public IPlatformServices getPlatformServices() {
		return this.m_platform_services;
	}
}