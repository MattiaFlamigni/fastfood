package db.fastfood.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;

import db.fastfood.Impl.Manager.ManagerIngredientiImpl;
import db.fastfood.Impl.Manager.ManagerProductsImpl;
import db.fastfood.api.Manager.ManagerIngredienti;
import db.fastfood.api.Manager.ManagerProducts;

public class ButtonClickListenerProducts implements ActionListener {
    ManagerProducts products;
    ManagerIngredienti ingredienti;

    public ButtonClickListenerProducts(Connection conn) {
        products = new ManagerProductsImpl(conn);
        ingredienti = new ManagerIngredientiImpl(conn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonName = button.getText();

        /* chiamo funzioni diverse a seconda del bottone */
        switch (buttonName) {
            case "Visualizza Prodotti Disponibili":
                products.visualizzaProdottiDisponibili();
                break;
            case "Aggiungi Prodotto":
                products.aggiungiProdotto();
                break;
            case "Aggiungi Ingredienti a Prodotto":
                ingredienti.aggiungiIngredienteAProdotto();
                break;
            case "Aggiungi Ingrediente":
                ingredienti.aggiungiIngrediente();
                break;
            case "Visualizza Ingredienti di un Prodotto":
                ingredienti.visualizzaIngredientiProdotto();
                break;
            case "Situazione magazzino":
                ingredienti.visualizzaMagazzino();
                break;
            case "Top 10 Prodotti":
                products.visualizzaTop10();
                break;
            default:
                break;
        }
    }

}
