package db.fastfood.view;

import javax.swing.*;

import db.fastfood.Impl.VenditaImpl;
import db.fastfood.api.Vendita;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.*;

public class ViewSchermatavendita extends JFrame {
    private Connection conn;
    private JPanel panel;

    boolean flag = false;

    public ViewSchermatavendita(Connection conn) {
        this.conn = conn;
        panel = new JPanel();
        add(panel);
        createButtons();

        setTitle("Schermata Vendite");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        //setLayout(new FlowLayout());
        setSize(400, 150);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createButtons() {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT descrizione FROM prodotti";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String nomeprodotto = result.getString("descrizione");
                JButton button = new JButton(nomeprodotto);
                button.setFont(new Font("Arial", Font.PLAIN, 12));
                button.setBackground(new Color(240, 240, 240));
                button.setFocusPainted(false);
                //button.setBorder(BorderFactory.createBevelBorder(5));
                button.addActionListener(e -> {
                    // attribuisce al cliente corrente l'id del prodotto selezionato
                    Vendita vendita = new VenditaImpl(conn);
                    vendita.vendita(nomeprodotto);
                });
                panel.add(button);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // in basso a destra un bottone "nuovo cliente"
        JButton button = new JButton("Nuovo Cliente");
        panel.add(button);
        button.addActionListener(e -> {
            Vendita vendita = new VenditaImpl(conn);
            vendita.nuovo_cliente();
        });

        JButton fidelty = new JButton("Assegna a fidelty");
        panel.add(fidelty);
        fidelty.addActionListener(e -> {
            Vendita vendita = new VenditaImpl(conn);
            vendita.assegna_fidelty();
        });

        // in basso a destra sotto il precedente bottone un bottone "inserisci offerta"
        JButton button2 = new JButton("Inserisci Offerta");
        panel.add(button2);
        button2.addActionListener(e -> {
            Vendita vendita = new VenditaImpl(conn);
            vendita.inserisci_offerta();
        });
    }

}
