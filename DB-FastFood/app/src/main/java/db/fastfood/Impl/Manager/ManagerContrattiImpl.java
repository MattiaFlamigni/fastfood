package db.fastfood.Impl.Manager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import db.fastfood.api.Manager.ManagerContratti;
import db.fastfood.util.CustomTable;

public class ManagerContrattiImpl implements ManagerContratti {

    private final Connection conn;
    private final CustomTable customizeTable = new CustomTable();

    public ManagerContrattiImpl(Connection conn) {
        this.conn = conn;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void visualizzaContratti(String cfDipendenteDaCercare) {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM CONTRATTO C, ADDETTO D WHERE C.CF_addetto = D.CF";

            if (cfDipendenteDaCercare != null && !cfDipendenteDaCercare.isEmpty()) {
                query += " AND D.Cognome = '" + cfDipendenteDaCercare + "'";
            }

            ResultSet resultSet = statement.executeQuery(query);
            String[] columnNames = { "ID", "Stipendio", "Data fine", "Ore settimanali", "Data inizio", "CF addetto",
                    "Nome", "Cognome" };
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                int ID = resultSet.getInt("ID");
                String stipendio = resultSet.getString("stipendio");
                String dataFine = resultSet.getString("data_fine");
                String ore = resultSet.getString("ore_previste_settimanali");
                String dataInizio = resultSet.getString("data_inizio");
                String CF = resultSet.getString("CF_addetto");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                Object[] row = { ID, stipendio, dataFine, ore, dataInizio, CF, nome, cognome };
                tableModel.addRow(row);
            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            customizeTable.doGraphic(table);
            JFrame frame = new JFrame("Contratti");
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setSize(800, 800);

            JButton eliminaButton = new JButton("Elimina");
            eliminaButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int idContratto = (int) table.getValueAt(selectedRow, 0);

                        try {
                            Statement deleteStatement = conn.createStatement();
                            String deleteQuery = "DELETE FROM CONTRATTO WHERE ID = " + idContratto;
                            int rowsAffected = deleteStatement.executeUpdate(deleteQuery);

                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(frame, "Record eliminato con successo.", "Eliminazione",
                                        JOptionPane.INFORMATION_MESSAGE);
                                visualizzaContratti(cfDipendenteDaCercare); // Aggiorna la visualizzazione dei contratti
                            } else {
                                JOptionPane.showMessageDialog(frame, "Nessun record eliminato.", "Eliminazione",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Errore durante l'eliminazione del record.", "Errore",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Seleziona un record da eliminare.", "Eliminazione",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            JButton modificaButton = new JButton("conferma modifica");

            modificaButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int idContratto = (int) table.getValueAt(selectedRow, 0);
                        String stipendio = (String) table.getValueAt(selectedRow, 1);
                        String dataFine = (String) table.getValueAt(selectedRow, 2);
                        String ore = (String) table.getValueAt(selectedRow, 3);
                        String dataInizio = (String) table.getValueAt(selectedRow, 4);
                        String CF = (String) table.getValueAt(selectedRow, 5);

                        try {
                            String updateQuery = "UPDATE CONTRATTO SET stipendio = ?, data_fine = ?, ore_previste_settimanali = ?, data_inizio = ?, CF_addetto = ? WHERE ID = ?";
                            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
                            preparedStatement.setString(1, stipendio);
                            preparedStatement.setString(2, dataFine);
                            preparedStatement.setString(3, ore);
                            preparedStatement.setString(4, dataInizio);
                            preparedStatement.setString(5, CF);
                            preparedStatement.setInt(6, idContratto);
                            int rowsAffected = preparedStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(frame, "Record modificato con successo.", "Modifica",
                                        JOptionPane.INFORMATION_MESSAGE);
                                        
                                frame.dispose();
                                visualizzaContratti(cfDipendenteDaCercare); // Aggiorna la visualizzazione dei contratti
                            } else {
                                JOptionPane.showMessageDialog(frame, "Nessun record modificato.", "Modifica",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Errore durante la modifica del record.", "Errore",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Seleziona un record da modificare.", "Modifica",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
                
            });


            JPanel buttonPanel = new JPanel();
            buttonPanel.add(eliminaButton);
            buttonPanel.add(modificaButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


     /**
     * {@inheritDoc}
     */
    @Override
    public void ricercaContratti() {
        String CFricercato = JOptionPane.showInputDialog(null, "Cognome del dipendente:");
        visualizzaContratti(CFricercato);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void inserisciContratto() {
        // si connette alla tabella e conta il numero di righe
        int count = 0;
        try {
            String query = "SELECT MAX(ID) FROM contratto";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                count = resultSet.getInt(1);
                System.out.println("Numero di righe: " + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // inserisce il contratto
        String CF = JOptionPane.showInputDialog(null, "CF del dipendente:");
        String stipendio = JOptionPane.showInputDialog(null, "Stipendio:");
        String dataInizio = JOptionPane.showInputDialog(null, "Data di inizio (YYYY-MM-DD):");
        String dataFine = JOptionPane.showInputDialog(null, "Data di fine (YYYY-MM-DD):");
        String ore = JOptionPane.showInputDialog(null, "Ore settimanali:");

        try {
            Statement statement = conn.createStatement();
            String query = "INSERT INTO CONTRATTO VALUES(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, count + 1);
            preparedStatement.setString(2, stipendio);
            preparedStatement.setString(3, dataFine);
            preparedStatement.setString(4, ore);
            preparedStatement.setString(5, dataInizio);
            preparedStatement.setString(6, CF);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Contratto inserito con successo.", "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Impossibile inserire il contratto.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Uno o più campi non sono compatibili", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
}
