package halfbyte.game.links;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import java.util.ArrayList;
import java.util.List;

public class ScreenShop extends ScreenAbstract {
    private class ShopItem{
        public String name;
        public String description;
        public float price;
        public Object context;
        public ShopItem(String name, String description, float price, Object context){
            this.name = name;
            this.description = description;
            this.price = price;
            this.context = context;
        }
    }

    // variables
    private List<ShopItem> m_items;

    // methods
    public ScreenShop(IGameServices game_service){
        // super
        super(game_service);

        // skin
        Skin skin = this.m_game_services.getAssetManager().get("ui/skin.json", Skin.class);

        // label
        Label label = new Label("text", this.m_game_services.getAssetManager().get("ui/skin.json", Skin.class), "large");
        label.setAlignment(Align.center);
        label.setX(Constants.RESOLUTION_WIDTH / 2.0f, Align.center);
        label.setY(Constants.RESOLUTION_HEIGHT / 2.0f, Align.center);
        this.m_stage.addActor(label);

        // get items
        this.m_items = new ArrayList<>();
        this.m_game_services.getPlatformServices().getShopItems(new IPlatformServices.IGetShopItemsListener() {
            @Override
            public void onItem(String name, String description, float price, Object context) {
                ScreenShop.this.m_items.add(new ShopItem(name, description, price, context));
            }

            @Override
            public void onComplete() {
                Table table = new Table();
                table.defaults().pad(16.0f);
                for (ShopItem item : ScreenShop.this.m_items){
                    Label name = new Label(item.name, skin);
                    table.row();
                    table.add(label);
                }
                table.pack();
                table.setPosition(Constants.RESOLUTION_WIDTH / 2, Constants.RESOLUTION_HEIGHT / 2, Align.center);
                ScreenShop.this.m_stage.addActor(table);
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void onButtonPurchaseCoins(){

    }

    private void onButtonPurchaseAdRemoval(){

    }
}
