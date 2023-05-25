package db.fastfood;

public class Prodotto {
    private String codice;
    private String descrizione;
    private double prezzoVendita;
    private double prezzoUnitario;

    //lista degli ingredienti
    private final String[] ingredienti;


    public Prodotto(String codice, String descrizione, double prezzoVendita, String[] ingredienti) {
        this.codice = codice;
        this.descrizione = descrizione;
        this.prezzoVendita = prezzoVendita;
        this.ingredienti = ingredienti;
    }

    public String getCodice() {
        return codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public double getPrezzoVendita() {
        return prezzoVendita;
    }

    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public String[] getIngredienti() {
        return ingredienti;
    }

}