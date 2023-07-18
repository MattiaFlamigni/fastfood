package db.fastfood.api.Manager;

public interface ManagerContratti {
    /**
     * Method that allow to add a new contract
     */
    public void inserisciContratto();

    /**
     * Method that allow to show all contracts
     * 
     * @param cfDipendenteDaCercare
     */
    public void visualizzaContratti(String cfDipendenteDaCercare);

    /**
     * Method that allow to search a contract
     */
    public void ricercaContratti();
}
