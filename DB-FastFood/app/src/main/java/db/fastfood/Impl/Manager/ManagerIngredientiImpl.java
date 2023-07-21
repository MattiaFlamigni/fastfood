package db.fastfood.Impl.Manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import db.fastfood.api.Manager.ManagerIngredienti;
import db.fastfood.util.CustomTable;

import java.awt.BorderLayout;
import java.awt.GridLayout;

public class ManagerIngredientiImpl implements ManagerIngredienti {

    private final Connection conn;
    CustomTable customTable = new CustomTable();

    public ManagerIngredientiImpl(Connection conn) {
        this.conn = conn;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addIngredient() {
        String nome = JOptionPane.showInputDialog(null, "Inserisci il nome dell'ingrediente:");
        if(nome==null) return;
        String quantita = JOptionPane.showInputDialog(null, "Inserisci la quantità disponibile:");
        if(quantita==null) return;
        String idIngrediente = JOptionPane.showInputDialog(null, "Inserisci l'ID dell'ingrediente:");
        if(idIngrediente==null) return;
        String prezzounitario = JOptionPane.showInputDialog(null, "Inserisci il costo di acquisto:");
        if(prezzounitario==null) return;

        try {
            Statement statement = conn.createStatement();
            String query = "INSERT INTO Ingredienti (ID, prezzoUnitario, nome_commerciale, quantita) VALUES ('"
                    + idIngrediente + "', '" + prezzounitario + "', '" + nome + "', " + quantita + ")";
            int rowsAffected = statement.executeUpdate(query);
            statement.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Ingrediente aggiunto con successo.", "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Impossibile aggiungere l'ingrediente.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'aggiunta dell'ingrediente.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void showIngredientIntoProduct() {
        String nome_prodotto = JOptionPane.showInputDialog(null, "Inserisci il codice(nome) del prodotto:");

        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Ingredienti I, ingredienti_prodotti IP, Prodotti P WHERE  I.ID = IP.ID_ingrediente AND IP.codice_prodotto = P.codice AND P.descrizione = '"
                    + nome_prodotto + "'";

            ResultSet resultSet = statement.executeQuery(query);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Ingredienti");

            while (resultSet.next()) {
                String ingrediente = resultSet.getString("nome_commerciale");
                tableModel.addRow(new Object[] { ingrediente });
            }

            JTable table = new JTable(tableModel);
            customTable.doGraphic(table);
            customTable.notEditable(table);

            JFrame tableFrame = new JFrame("Tabella Ingredienti per Prodotto");
            tableFrame.setSize(400, 300);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableFrame.setLocationRelativeTo(null);
            tableFrame.getContentPane().add(new JScrollPane(table));
            tableFrame.setVisible(true);

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addIngredientToProduct() {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Ingredienti";
            ResultSet resultSet = statement.executeQuery(query);
            // Creazione della finestra per la selezione degli ingredienti
            JFrame ingredientiFrame = new JFrame("Seleziona Ingredienti");
            ingredientiFrame.setSize(400, 300);
            ingredientiFrame.setLayout(new GridLayout(0, 1));

            // Aggiunta dei bottoni per gli ingredienti
            while (resultSet.next()) {
                String nome = resultSet.getString("nome_commerciale");
                JButton btn = new JButton(nome);
                btn.addActionListener(e -> aggiungiIngredienteAProdotto(nome));
                ingredientiFrame.add(btn);
            }

            // Mostra la finestra degli ingredienti
            ingredientiFrame.setVisible(true);

            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void showWarehouse() {
        String query = "SELECT * FROM ingredienti";

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            String[] columnNames = { "ID", "Nome", "Quantità", "Prezzo unitario" };
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                double prezzoUnitario = resultSet.getDouble("prezzoUnitario");
                String nome = resultSet.getString("nome_commerciale");
                double quantita = resultSet.getDouble("quantita");

                Object[] row = { ID, nome, quantita, prezzoUnitario };
                tableModel.addRow(row);

            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            JFrame frame = new JFrame("Magazzino");
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setSize(800, 600);

            frame.setVisible(true);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * method to add an ingredient to a product
     */
    private void aggiungiIngredienteAProdotto(String nomeIngrediente) {
        String nomeProdotto = JOptionPane.showInputDialog(null, "Inserisci il nome del prodotto:");
        String quantita = JOptionPane.showInputDialog(null,
                "Inserisci la quantità di " + nomeIngrediente + " da aggiungere al prodotto:");
        String idIngrediente = "";

        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Ingredienti WHERE nome_commerciale = '" + nomeIngrediente + "'";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            idIngrediente = resultSet.getString("ID");
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }

        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Prodotti WHERE descrizione = '" + nomeProdotto + "'";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            String codice = resultSet.getString("codice");
            String query2 = "INSERT INTO ingredienti_prodotti (codice_prodotto, ID_ingrediente, quantita_utilizzata) VALUES ('"
                    + codice + "', '" + idIngrediente + "', " + quantita + ")";
            int rowsAffected = statement.executeUpdate(query2);
            statement.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Ingrediente aggiunto con successo.", "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Impossibile aggiungere l'ingrediente.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'aggiunta dell'ingrediente.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
