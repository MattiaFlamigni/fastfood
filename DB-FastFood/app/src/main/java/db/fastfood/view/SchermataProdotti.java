package db.fastfood.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import db.fastfood.ActionListener.ButtonClickListenerProducts;

public class SchermataProdotti extends JFrame {
    @SuppressWarnings("unused")
    private final Connection conn;
    ButtonClickListenerProducts listener;

    public SchermataProdotti(Connection conn) {
        this.conn = conn;
        listener = new ButtonClickListenerProducts(conn);
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
        JButton btnTop10 = new JButton("Top 10 Prodotti");

        JButton btnIndietro = new JButton("Indietro");

        // Creazione del layout
        Container container = getContentPane();
        container.setLayout(new GridLayout(4, 2)); // 3 righe e 2 colonne per coppie di pulsanti

        // Aggiunta dei pulsanti alla sezione "Prodotti"
        JPanel prodottiPanel = new JPanel();
        prodottiPanel.setLayout(new FlowLayout());
        prodottiPanel.add(btnVisualizzaProdotti);
        prodottiPanel.add(btnTop10);
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

        ImageIcon iconaIndietro = new javax.swing.ImageIcon(getClass().getResource("images/arrow-left-circle.png"));
        ImageIcon iconaIndietroRidimensionata = new ImageIcon(
                iconaIndietro.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));

        btnIndietro.setIcon(iconaIndietroRidimensionata);
        JPanel indietroPanel = new JPanel();
        indietroPanel.add(btnIndietro);

        btnIndietro.setBackground(java.awt.Color.WHITE);
        btnIndietro.setForeground(java.awt.Color.RED);

        container.add(indietroPanel);

        // Mostra la finestra
        setVisible(true);

        // per ogni pulsante aggiungo il listener
        btnVisualizzaProdotti.addActionListener(listener);
        btnAggiungiProdotto.addActionListener(listener);
        btnAggiungiIngredienti.addActionListener(listener);
        btnAggiungiIngrediente.addActionListener(listener);
        btnVisualizzaIngredienti.addActionListener(listener);
        btnMagazzino.addActionListener(listener);
        btnTop10.addActionListener(listener);

        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
                new SchermataInizialeFinale(conn);
            }
        });

    }
}
