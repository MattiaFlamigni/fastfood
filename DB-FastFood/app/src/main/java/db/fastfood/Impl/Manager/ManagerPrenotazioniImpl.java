package db.fastfood.Impl.Manager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import db.fastfood.api.Manager.ManagerPrenotazioni;
import db.fastfood.util.CustomTable;

public class ManagerPrenotazioniImpl implements ManagerPrenotazioni {

    private Connection connection;
    private CustomTable customTable = new CustomTable();

    public ManagerPrenotazioniImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void AddTable() {
        try {
            String numero = JOptionPane.showInputDialog("Inserisci numero tavolo");
            String nposti = JOptionPane.showInputDialog("Inserisci numero posti");

            String query = "INSERT INTO tavolo (numero, nposti) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(numero));
            statement.setInt(2, Integer.parseInt(nposti));
            statement.executeUpdate();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void showTable() {
        JFrame frame = new JFrame("Tavoli");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JButton btnElimina = new JButton("Elimina");
        // JButton btnModifica = new JButton("Modifica");

        JTable table = new JTable();
        customTable.doGraphic(table);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        try {
            String query = "SELECT * FROM tavolo";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ResultSetMetaData metaData = (ResultSetMetaData) resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel();

            for (int i = 1; i <= numberOfColumns; i++) {
                tableModel.addColumn(metaData.getColumnLabel(i));
            }

            while (resultSet.next()) {
                Object[] row = new Object[numberOfColumns];
                for (int i = 1; i <= numberOfColumns; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(row);
            }

            table.setModel(tableModel);
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        frame.add(btnElimina, BorderLayout.SOUTH);

        btnElimina.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                String cella = table.getModel().getValueAt(row, 0).toString();
                String query = "DELETE FROM tavolo WHERE numero = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt(cella));
                statement.executeUpdate();
                statement.close();
                JOptionPane.showMessageDialog(null, "Tavolo eliminato");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void showreservation() {
        // Apre una tabella con tutte le prenotazioni
        JFrame frame = new JFrame("Prenotazioni");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JButton btnElimina = new JButton("Elimina");
        // JButton btnModifica = new JButton("Modifica");

        JTable table = new JTable();
        customTable.doGraphic(table);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        try {
            String query = "SELECT * FROM prenotazione_tavolo";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ResultSetMetaData metaData = (ResultSetMetaData) resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel();

            for (int i = 1; i <= numberOfColumns; i++) {
                tableModel.addColumn(metaData.getColumnLabel(i));
            }

            while (resultSet.next()) {
                Object[] row = new Object[numberOfColumns];
                for (int i = 1; i <= numberOfColumns; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(row);
            }

            table.setModel(tableModel);
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        frame.setLayout(new FlowLayout());

        btnElimina.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                String cella = table.getModel().getValueAt(row, 0).toString();
                String query = "DELETE FROM prenotazione_tavolo WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt(cella));
                statement.executeUpdate();
                statement.close();
                JOptionPane.showMessageDialog(null, "Prenotazione eliminata");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void AddReservation() {
        try {

            String nominativo = JOptionPane.showInputDialog("Inserisci nominativo");
            String data = JOptionPane.showInputDialog("Inserisci data");
            String ora = JOptionPane.showInputDialog("Inserisci ora");
            String numeroPersone = JOptionPane.showInputDialog("Inserisci numero persone");
            String numeroTavolo = JOptionPane.showInputDialog("Inserisci numero tavolo");

            String query = "INSERT INTO prenotazione_tavolo (nominativo, data, n_persone, ora, numeroTavolo) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nominativo);
            statement.setString(2, data);
            statement.setString(3, numeroPersone);
            statement.setString(4, ora);
            statement.setString(5, numeroTavolo);
            statement.executeUpdate();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
