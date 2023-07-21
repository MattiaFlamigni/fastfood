package db.fastfood.Impl.Manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import db.fastfood.api.Manager.ManagerProducts;
import db.fastfood.util.CustomTable;

public class ManagerProductsImpl implements ManagerProducts {

    private final Connection conn;
    CustomTable customTable = new CustomTable();

    public ManagerProductsImpl(Connection conn) {
        this.conn = conn;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void showavaiableProduct() {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Prodotti";
            ResultSet resultSet = statement.executeQuery(query);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Codice");
            tableModel.addColumn("Descrizione");
            tableModel.addColumn("Prezzo di Vendita");

            while (resultSet.next()) {
                String codice = resultSet.getString("codice");
                String descrizione = resultSet.getString("descrizione");
                double prezzoVendita = resultSet.getDouble("prezzovendita");

                tableModel.addRow(new Object[] { codice, descrizione, prezzoVendita });
            }

            JTable table = new JTable(tableModel);
            customTable.doGraphic(table);
            customTable.notEditable(table);

            JFrame tableFrame = new JFrame("Tabella Prodotti Disponibili");
            tableFrame.setSize(600, 400);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableFrame.setLocationRelativeTo(null);
            tableFrame.getContentPane().add(new JScrollPane(table));
            tableFrame.setVisible(true);

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante il caricamento dei prodotti disponibili", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addProduct() {
        String codice = JOptionPane.showInputDialog(null, "Inserisci il codice del prodotto:");
        if(codice == null) return;
        String descrizione = JOptionPane.showInputDialog(null, "Inserisci la descrizione del prodotto:");
        if(descrizione == null) return;
        double prezzoVendita = Double
                .parseDouble(JOptionPane.showInputDialog(null, "Inserisci il prezzo di vendita del prodotto:"));
        if(prezzoVendita == 0) return;

        try {
            Statement statement = conn.createStatement();
            String query = "INSERT INTO Prodotti (codice, descrizione, prezzovendita) VALUES ('" + codice + "', '"
                    + descrizione + "', " + prezzoVendita + ")";
            int rowsAffected = statement.executeUpdate(query);
            statement.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Prodotto aggiunto con successo.", "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Impossibile aggiungere il prodotto.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'aggiunta del prodotto.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void Top10(){

        try {
            Statement statement = conn.createStatement();
            String query = "select p.descrizione, p.prezzovendita, count(codice_prodotto) as vendite\n" + //
                    "from prodotti p, dettaglio_ordini d\n" + //
                    "where p.codice=d.codice_prodotto \n" + //
                    "group by codice_prodotto\n" + //
                    "order by vendite DESC limit 10";
            ResultSet resultSet = statement.executeQuery(query);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Descrizione");
            tableModel.addColumn("Prezzo di Vendita");
            tableModel.addColumn("Vendite");

            while (resultSet.next()) {
                String descrizione = resultSet.getString("descrizione");
                double prezzoVendita = resultSet.getDouble("prezzovendita");
                int vendite = resultSet.getInt("vendite");

                tableModel.addRow(new Object[] {descrizione, prezzoVendita, vendite });
            }

            JTable table = new JTable(tableModel);
            customTable.doGraphic(table);
            customTable.notEditable(table);

            JFrame tableFrame = new JFrame("Tabella Prodotti Disponibili");
            tableFrame.setSize(600, 400);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableFrame.setLocationRelativeTo(null);
            tableFrame.getContentPane().add(new JScrollPane(table));
            tableFrame.setVisible(true);

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante il caricamento dei prodotti disponibili", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
