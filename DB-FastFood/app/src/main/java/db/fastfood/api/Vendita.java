package db.fastfood.api;

public interface Vendita {

    /**
     * to start a new order
     */
    public void newCustomer();

    /**
     * 
     * an order with a discount
     */
    public void addOffer();

    /**
     * 
     * @param nomeprodotto
     *                     sold product
     *
     */
    public void sold(String nomeprodotto);

    /**
     * an order delivered by app
     */
    public void delivery();

    /**
     * allow an employee eat for free
     * 
     */
    public void mealVoucher();

}
