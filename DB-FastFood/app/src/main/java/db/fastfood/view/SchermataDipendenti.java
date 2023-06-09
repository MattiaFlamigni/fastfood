package db.fastfood.view;


import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import db.fastfood.ActionListener.ButtonClickListenerDipendenti;

public class SchermataDipendenti extends JFrame {
    @SuppressWarnings("unused")
    private final Connection conn;
    //ButtonClickListenerDipendenti listener;

    public SchermataDipendenti(Connection conn) {
        this.conn = conn;
        //listener = new ButtonClickListenerDipendenti(conn);
        setTitle("Schermata Prodotti");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnInserisciAddetto = new JButton("Inserisci Dipendente");
        JButton btnVisualizzaAddetti = new JButton("Visualizza Dipendenti");
        JButton btnContratti = new JButton("Crea contratto");
        JButton btnVisualizzaContratti = new JButton("Visualizza contratti");
        JButton btnRicercaContratto = new JButton("Ricerca contratto");


        Container container = getContentPane();
        container.setLayout(new GridLayout(3, 2));  

        // Aggiunta dei pulsanti alla sezione "Dipendenti"
        JPanel dipendentiPanel = new JPanel();
        dipendentiPanel.setLayout(new FlowLayout());
        dipendentiPanel.add(btnInserisciAddetto);
        dipendentiPanel.add(btnVisualizzaAddetti);
        container.add(dipendentiPanel);

        dipendentiPanel = new JPanel();
        dipendentiPanel.add(btnContratti);
        dipendentiPanel.add(btnVisualizzaContratti);
        dipendentiPanel.add(btnRicercaContratto);
        container.add(dipendentiPanel);

        setVisible(true);


        /* aggiungo i listener */
        btnInserisciAddetto.addActionListener(new ButtonClickListenerDipendenti(conn));
        btnVisualizzaAddetti.addActionListener(new ButtonClickListenerDipendenti(conn));
        btnContratti.addActionListener(new ButtonClickListenerDipendenti(conn));
        btnVisualizzaContratti.addActionListener(new ButtonClickListenerDipendenti(conn));
        btnRicercaContratto.addActionListener(new ButtonClickListenerDipendenti(conn));


    }
}
