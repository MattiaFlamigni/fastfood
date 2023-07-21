package db.fastfood.api.Manager;

public interface ManagerProducts {
    /**
     * Method that allow to show all available products
     */
    public void showavaiableProduct();

    /**
     * Method that allow to add a new product
     */
    public void addProduct();

    /**
     * show the top 10 products sold
     */
    public void Top10();
}
