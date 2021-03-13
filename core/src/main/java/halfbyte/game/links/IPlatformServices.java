package halfbyte.game.links;

public interface IPlatformServices {
    interface IGetShopItemsListener{
        void onItem(String name, String description, float price, Object context);
        void onComplete();
        void onError(String error);
    }
    void getShopItems(IGetShopItemsListener listener);

    interface IPurchaseShopItemListener{
        void onComplete();
        void onCancel();
        void onError();
    }
    void purchaseShopItem(Object context, IPurchaseShopItemListener listener);
}
