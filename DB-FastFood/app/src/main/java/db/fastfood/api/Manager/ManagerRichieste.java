package db.fastfood.api.Manager;

public interface ManagerRichieste {
    /**
     * Method that allow insert a new request
     */
    public void inserisciRichiesta();

    /**
     * Method that allow to show and decline requests.
     * 
     */
    public void visualizzaRifiutaRichieste();
}
