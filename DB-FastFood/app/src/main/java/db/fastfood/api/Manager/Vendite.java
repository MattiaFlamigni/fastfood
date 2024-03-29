package db.fastfood.api.Manager;

public interface Vendite {
    /**
     * Method that allow to show the monthly money earned
     */
    public void monthlyEarned();

    /**
     * Method that allow to show the daily sales
     */
    public void showDailySales();

    /**
     * Method that allow to show the average receipt
     */
    public void averageReceipt();

    /**
     * Method that allow to show the receipt by date
     */
    public void showReceiptByDate();

    /**
     * Method that allow to show the report
     */
    public void report();

    /**
     * Method that allow to show the report delivery
     */

    public void reportDelivery();
}
