package db.fastfood.api.Manager;

public interface Vendite {
    /**
     * Method that allow to show the monthly money earned
     */
    public void visualizzaFatturatoMensile();

     /**
     * Method that allow to show the daily sales
     */
    public void visualizzaVenditeGiornaliere();

    /**
     * Method that allow to show the average receipt
     */
    public void scontrinoMedio();

    /**
     * Method that allow to show the receipt by date
     */
    public void visualizzaScontriniPerData();
}
