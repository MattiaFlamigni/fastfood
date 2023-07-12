package db.fastfood.api.Manager;

public interface Manager {

    /**
     * Method that allow to add a new employee
     */
    public void inserisciDipendente();

    /**
     * Method that allow to show all employees
     */
    public void visualizzaAddetti();


    

    /**
     * Method that allow to show the monthly money earned
     */
    public void visualizzaFatturatoMensile();

    /**
     * Method that allow to create a fidelity card
     */
    public void creaFidelty();

    /**
     * Method that allow to show the daily sales
     */
    public void visualizzaVenditeGiornaliere();

    /**
     * Method that allow to show the average receipt
     */
    public void scontrinoMedio();

    /**
     * Method that allow to register the waste
     */
    public void registraScarti();

}
