package db.fastfood.Impl.Manager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import db.fastfood.api.Manager.ManagerAddetti;
import db.fastfood.util.CustomTable;

public class ManagerAddettiImpl implements ManagerAddetti {

    private Connection conn;
    CustomTable customizeTable = new CustomTable();

    public ManagerAddettiImpl(Connection conn) {
        this.conn = conn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inserisciDipendente() {
        String[] options = { "Addetto", "Manager" };
        int choice = JOptionPane.showOptionDialog(null, "Seleziona il tipo di dipendente:", "Inserisci dipendente",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 1) {
            try {
                String CF = JOptionPane.showInputDialog(null, "Inserisci il CF:");
                if(CF == null) return;
                String nome = JOptionPane.showInputDialog(null, "Inserisci il nome:");
                if(nome == null) return;
                String cognome = JOptionPane.showInputDialog(null, "Inserisci il cognome:");
                if(cognome == null) return;
                String direttore = JOptionPane.showInputDialog(null, "Inserisci il CF del direttore:");
                if(direttore == null) return;
                Statement statement = conn.createStatement();
                String query = "INSERT INTO MANAGER VALUES(?,?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, CF);
                preparedStatement.setString(2, nome);
                preparedStatement.setString(3, cognome);
                preparedStatement.setString(4, direttore);
                int rowsAffected = preparedStatement.executeUpdate();
                preparedStatement.close();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Dipendente inserito con successo.", "Successo",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Impossibile inserire il dipendente.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (choice == 0) {
            try {
                String CF = JOptionPane.showInputDialog(null, "Inserisci il CF:");
                if(CF == null) return;
                String nome = JOptionPane.showInputDialog(null, "Inserisci il nome:");
                if(nome == null) return;
                String cognome = JOptionPane.showInputDialog(null, "Inserisci il cognome:");
                if(cognome == null) return;
                Statement statement = conn.createStatement();
                String query = "INSERT INTO ADDETTO VALUES(?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, CF);
                preparedStatement.setString(2, nome);
                preparedStatement.setString(3, cognome);
                int rowsAffected = preparedStatement.executeUpdate();
                preparedStatement.close();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Dipendente inserito con successo.", "Successo",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Impossibile inserire il dipendente.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Errore durante l'inserimento del dipendente.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visualizzaAddetti() {
        String[] options = { "Addetto", "Direttore", "Manager" };
        int choice = JOptionPane.showOptionDialog(null, "Seleziona il tipo di dipendente:", "Inserisci dipendente",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        String tabella = null;
        if (choice == 0) {
            tabella = "addetto";
        } else if (choice == 1) {
            tabella = "direttori";
        } else if (choice == 2) {
            tabella = "manager";
        }

        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM " + tabella;
            ResultSet resultSet = statement.executeQuery(query);
            String[] columnNames = { "CF", "Nome", "Cognome" };
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (resultSet.next()) {
                String CF = resultSet.getString("CF");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                Object[] row = { CF, nome, cognome };
                tableModel.addRow(row);
            }
            resultSet.close();
            statement.close();
            JTable table = new JTable(tableModel);
            JFrame tableFrame = new JFrame(tabella);
            customizeTable.doGraphic(table);
            tableFrame.setSize(400, 300);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableFrame.setLocationRelativeTo(null);
            tableFrame.getContentPane().add(new JScrollPane(table));
            tableFrame.setVisible(true);

            // possibilita di eliminazione

            // SE HO SCELTO DIRETTORI, NON POSSO ELIMINARE

            JButton eliminaButton = new JButton("Elimina");
            tableFrame.getContentPane().add(eliminaButton, BorderLayout.SOUTH);
            eliminaButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    String tabella = null;
                    if (choice == 0) {
                        tabella = "addetto";
                    } else if (choice == 1) {
                        // non posso eliminare direttori
                        JOptionPane.showMessageDialog(tableFrame, "Un direttore non puo essere eliminato", "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (choice == 2) {
                        tabella = "manager";
                    }
                    String codF = null;
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(tableFrame, "Seleziona una riga.", "Errore",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        codF = (String) table.getValueAt(selectedRow, 0);
                    }
                    try {

                        // se il contratto è scaduto, elimina il contratto
                        Statement statement = conn.createStatement();
                        String query = "SELECT data_fine FROM contratto WHERE CF_addetto = '" + codF + "'";
                        ResultSet resultSet = statement.executeQuery(query);
                        if (resultSet.next()) {
                            Date dataFine = resultSet.getDate("data_fine");
                            if (dataFine.before(new Date())) {
                                query = "DELETE FROM contratto WHERE CF_Addetto= '" + codF + "'";
                                statement.executeUpdate(query);
                                // avvisa che il contratto è scaduto
                                JOptionPane.showMessageDialog(tableFrame, "Il contratto è scaduto", "Contratto scaduto",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        resultSet.close();
                        statement.close();

                        // elimina il dipendente

                        statement = conn.createStatement();
                        query = "DELETE FROM " + tabella + " WHERE CF = '" + codF + "'";
                        int rowsAffected = statement.executeUpdate(query);
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(tableFrame, "Dipendente eliminato con successo.", "Successo",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(tableFrame, "Impossibile eliminare il dipendente.", "Errore",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        statement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(tableFrame,
                                "Il dipendente ha un contratto valido in corso oppure dipendenze rilevate", "Errore",
                                JOptionPane.ERROR_MESSAGE);

                    }
                }
            });
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);

        }
    }
}
