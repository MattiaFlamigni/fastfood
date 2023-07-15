package db.fastfood.Impl.Manager;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
    public void inserisciTavolo() {
        try{
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

    @Override
    public void visualizzaTavoli() {
        //Apre una tabella con tutti i tavoli
       JFrame frame = new JFrame("Tavoli");
         frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

            JButton btnElimina = new JButton("Elimina");
            JButton btnModifica = new JButton("Modifica");

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
            //frame.add(btnModifica, BorderLayout.SOUTH);

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

    @Override
    public void visualizzaPrenotazioni() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visualizzaPrenotazioni'");
    }

    @Override
    public void inserisciPrenotazione() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inserisciPrenotazione'");
    }
    
}
