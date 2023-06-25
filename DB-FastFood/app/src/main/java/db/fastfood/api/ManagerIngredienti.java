package db.fastfood.api;

public interface ManagerIngredienti {
    /**
     * Method that allow to add ingredients
     */
    public void aggiungiIngrediente();

    /**
     * Method that allow to show all ingredients into a product
     */
    public void visualizzaIngredientiProdotto();

    /**
     * Method that allow to add an ingredient to a product
     */
    public void aggiungiIngredienteAProdotto();

    /*
     * Method that allow to show all ingredients into the warehouse
     */
    public void visualizzaMagazzino();

}
