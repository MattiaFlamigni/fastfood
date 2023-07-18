package db.fastfood.api;

public interface Vendita {

    /**
     * to start a new order
     */
    public void nuovo_cliente();

    /**
     * 
     * an order with fidelity card
     */
    public void inserisci_offerta();

    /**
     * 
     * @param nomeprodotto
     *                     sold product
     *
     */
    public void vendita(String nomeprodotto);

    /**
     * an order delivered by app
     */
    public void delivery();

    /**
     * allow an employee eat for free
     * 
     */
    public void buonoPasto();

}
