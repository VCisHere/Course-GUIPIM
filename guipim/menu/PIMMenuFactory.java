package guipim.menu;

public class PIMMenuFactory {
    public static PIMMenu createMenu(String productName) {
        PIMMenu product = null;
        switch (productName) {
            case "新建": product = new PIMCreateMenu(); break;
            case "查看": product = new PIMShowMenu(); break;
        }
        return product;
    }
}
