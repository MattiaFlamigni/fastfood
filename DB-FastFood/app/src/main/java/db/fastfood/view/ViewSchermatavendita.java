package db.fastfood.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import db.fastfood.Impl.VenditaFideltyImpl;
import db.fastfood.Impl.VenditaImpl;
import db.fastfood.api.Vendita;
import db.fastfood.api.VenditaFidelty;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
//import java.awt.FlowLayout;
import java.awt.Font;
//import java.awt.GridLayout;
import java.sql.*;

public class ViewSchermatavendita extends JFrame {
    private Connection conn;
    private JPanel panel;

    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;

    boolean flag = false;

    public ViewSchermatavendita(Connection conn) {
        this.conn = conn;

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 300));
        // panel.setAlignmentX(0);
        add(panel);
        createButtons();

        model = new DefaultTableModel();
        model.addColumn("Quantita");
        model.addColumn("Prodotto");
        model.addColumn("Prezzo");

        table = new JTable(model);
        table.setBackground(new Color(240, 240, 240));
        // i bordi delle righe li voglio invisibili
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        // aggiungi spazio tra le righe
        table.setIntercellSpacing(new Dimension(0, 1));

        scrollPane = new JScrollPane(table);
        // JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.EAST);

        setTitle("Schermata Vendite");
        // setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setSize(800, 600);

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
                // button.setBorder(BorderFactory.createBevelBorder(5));
                button.addActionListener(e -> {
                    // attribuisce al cliente corrente l'id del prodotto selezionato
                    Vendita vendita = new VenditaImpl(getView(), conn);
                    vendita.sold(nomeprodotto);
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
            Vendita vendita = new VenditaImpl(getView(), conn);
            vendita.newCustomer();
        });

        JButton fidelty = new JButton("Assegna a fidelty");
        panel.add(fidelty);
        fidelty.addActionListener(e -> {
            VenditaFidelty venditaFidelty = new VenditaFideltyImpl(conn);
            venditaFidelty.assign_fidelty();
        });

        JButton button2 = new JButton("Inserisci Offerta");
        panel.add(button2);
        button2.addActionListener(e -> {
            Vendita vendita = new VenditaImpl(getView(), conn);
            vendita.addOffer();
        });

        JButton utilizzaFidelty = new JButton("Inserisci Fidelty");
        panel.add(utilizzaFidelty);
        utilizzaFidelty.addActionListener(e -> {
            VenditaFidelty venditaFidelty = new VenditaFideltyImpl(conn);
            venditaFidelty.use_fidelty();
        });

        JButton btnDelivery = new JButton("Delivery");
        panel.add(btnDelivery);
        btnDelivery.addActionListener(e -> {
            Vendita vendita = new VenditaImpl(getView(), conn);
            vendita.delivery();
        });

        JButton btnBuonoPasto = new JButton("Buono Pasto");
        panel.add(btnBuonoPasto);
        btnBuonoPasto.addActionListener(e -> {
            Vendita vendita = new VenditaImpl(getView(), conn);
            vendita.mealVoucher();
        });

    }

    /** 
     * method to update a table
     * 
     */
    public void updateTable(String nomeprodotto, int quantita, double prezzo) {
        model.addRow(new Object[] { quantita, nomeprodotto, prezzo });
    }

    // ritorna questa view
    private ViewSchermatavendita getView() {
        return this;
    }

    /**
     * method to clear all table
     */
    public void clearTable() {
        model.setRowCount(0);
    }

    /**
     * method to delete a row of a table
     * @param nomeprodotto
     */
    public void deleteRow(String nomeprodotto) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).equals(nomeprodotto)) {
                model.removeRow(i);
                break;
            }
        }
    }

    /**
     * method to get quantity of a product into a table
     * @param nomeprodotto
     * @return
     */
    public int getQuantita(String nomeprodotto) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).equals(nomeprodotto)) {
                return (int) model.getValueAt(i, 0);
            }
        }
        return 0;
    }

    /**
     * method to get a price of a product into a table
     * @param nomeprodotto
     * @return
     */
    public double getPrezzo(String nomeprodotto) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).equals(nomeprodotto)) {
                return (double) model.getValueAt(i, 2);
            }
        }
        return 0;
    }

    /**
     * method to update a row of a table
     * @param nomeprodotto
     */
    public void updateRow(String nomeprodotto) {
        double prezzoVecchio = getPrezzo(nomeprodotto);
        int quantitaVecchia = getQuantita(nomeprodotto);

        deleteRow(nomeprodotto);

        updateTable(nomeprodotto, quantitaVecchia + 1, prezzoVecchio + prezzoVecchio);
    }

}
