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

    public void visualizzaVenditeGiornaliere();

    public void registraScarti();

}
