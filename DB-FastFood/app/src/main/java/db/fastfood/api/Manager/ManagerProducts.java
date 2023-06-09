package db.fastfood.api.Manager;

public interface ManagerProducts {
    /**
     * Method that allow to show all available products
     */
    public void visualizzaProdottiDisponibili();

    /**
     * Method that allow to add a new product
     */
    public void aggiungiProdotto();

    /**
     * show the top 10 products sold
     */
    public void visualizzaTop10();
}
