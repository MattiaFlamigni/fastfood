package db.fastfood.api.Manager;

public interface ManagerIngredienti {
    /**
     * Method that allow to add ingredients
     */
    public void addIngredient();

    /**
     * Method that allow to show all ingredients into a product
     */
    public void showIngredientIntoProduct();

    /**
     * Method that allow to add an ingredient to a product
     */
    public void addIngredientToProduct();

    /*
     * Method that allow to show all ingredients into the warehouse
     */
    public void showWarehouse();

}
