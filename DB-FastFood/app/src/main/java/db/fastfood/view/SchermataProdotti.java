package db.fastfood.view;

import java.awt.Button;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SchermataProdotti extends JFrame {
    private final Connection conn;
    ButtonClickListener listener;

    public SchermataProdotti(Connection conn){
        this.conn = conn;
        listener = new ButtonClickListener(conn);
        setTitle("Schermata Prodotti");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creazione dei pulsanti per la sezione "Prodotti"
        JButton btnVisualizzaProdotti = new JButton("Visualizza Prodotti Disponibili");
        JButton btnAggiungiProdotto = new JButton("Aggiungi Prodotto");
        JButton btnAggiungiIngredienti = new JButton("Aggiungi Ingredienti a Prodotto");
        JButton btnAggiungiIngrediente = new JButton("Aggiungi Ingrediente");
        JButton btnVisualizzaIngredienti = new JButton("Visualizza Ingredienti di un Prodotto");
        JButton btnMagazzino = new JButton("Situazione magazzino");

        // Creazione del layout
        Container container = getContentPane();
        container.setLayout(new GridLayout(3, 2)); // 3 righe e 2 colonne per coppie di pulsanti

        // Aggiunta dei pulsanti alla sezione "Prodotti"
        JPanel prodottiPanel = new JPanel();
        prodottiPanel.setLayout(new FlowLayout());
        prodottiPanel.add(btnVisualizzaProdotti);
        prodottiPanel.add(btnAggiungiProdotto);
        container.add(prodottiPanel);

        prodottiPanel = new JPanel();
        prodottiPanel.setLayout(new FlowLayout());
        prodottiPanel.add(btnAggiungiIngredienti);
        prodottiPanel.add(btnAggiungiIngrediente);
        container.add(prodottiPanel);

        prodottiPanel = new JPanel();
        prodottiPanel.setLayout(new FlowLayout());
        prodottiPanel.add(btnVisualizzaIngredienti);
        prodottiPanel.add(btnMagazzino);
        container.add(prodottiPanel);

        // Mostra la finestra
        setVisible(true);


        //per ogni pulsante aggiungo il listener
        btnVisualizzaProdotti.addActionListener(listener);
        btnAggiungiProdotto.addActionListener(listener);
        btnAggiungiIngredienti.addActionListener(listener);
        btnAggiungiIngrediente.addActionListener(listener);
        btnVisualizzaIngredienti.addActionListener(listener);
        btnMagazzino.addActionListener(listener);

        
    }
}
