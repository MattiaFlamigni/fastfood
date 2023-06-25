package db.fastfood.api;

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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ManagerRichiesteImpl implements ManagerRichieste{

    private Connection conn;

    public ManagerRichiesteImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisciRichiesta() {
        //un addetto puo effettuare una richiesta di riposo, di ferie o di malattia
        String tipoRichiesta = JOptionPane.showInputDialog(null, "Inserisci il tipo di richiesta (riposo, ferie, malattia):");
        String dataInizio = JOptionPane.showInputDialog(null, "Inserisci la data di inizio (YYYY-MM-DD):");
        String dataFine = JOptionPane.showInputDialog(null, "Inserisci la data di fine (YYYY-MM-DD):");
        String idAddetto = JOptionPane.showInputDialog(null, "Inserisci CF dell'addetto:");

        try {
            //Statement statement = conn.createStatement();
            String query = "INSERT INTO richieste (tipo, datainizio, datafine, CF_addetto, dataRichiesta) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, tipoRichiesta);
            preparedStatement.setString(2, dataInizio);
            preparedStatement.setString(3, dataFine);
            preparedStatement.setString(4, idAddetto);
            preparedStatement.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();


            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Richiesta aggiunta con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Impossibile aggiungere la richiesta.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'aggiunta della richiesta.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    @Override
    public void visualizzaRifiutaRichieste() {
        //visualizza tutte le richieste di riposo, ferie o malattia e permette di rifiutarle
        //se una richiesta viene rifiutata, viene eliminata dalla tabella richieste

        try{
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM richieste";
            ResultSet resultSet = statement.executeQuery(query);
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Tipo");
            tableModel.addColumn("datainizio");
            tableModel.addColumn("datafine");
            tableModel.addColumn("CF_addetto");
            tableModel.addColumn("CF_manager");
            tableModel.addColumn("DataRichiesta");
            while (resultSet.next()) {
                String tipo = resultSet.getString("tipo");
                String datainizio = resultSet.getString("datainizio");
                String datafine = resultSet.getString("datafine");
                String CF_addetto = resultSet.getString("CF_addetto");
                String dataRichiesta = resultSet.getString("dataRichiesta");
                tableModel.addRow(new Object[]{tipo, datainizio, datafine, CF_addetto, dataRichiesta});
            }
            JTable table = new JTable(tableModel);
            JFrame tableFrame = new JFrame("Tabella Richieste");
            tableFrame.setSize(400, 300);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableFrame.setLocationRelativeTo(null);
            tableFrame.getContentPane().add(new JScrollPane(table));
            tableFrame.setVisible(true);
            resultSet.close();
            statement.close();

            //l'utente seleziona la richiesta da rifiutare selezionando la riga della tabella
            //e cliccando sul pulsante "Rifiuta"

            JButton rifiutaButton = new JButton("Rifiuta");
            tableFrame.getContentPane().add(rifiutaButton, BorderLayout.SOUTH);
            rifiutaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    //solo un direttore puo rifiutare una richiesta
                    //l'utente deve inserire il proprio CF per rifiutare la richiesta

                    String CF_manager = JOptionPane.showInputDialog(tableFrame, "Inserisci il tuo CF:");
                    //se è presente nella tabella direttori allora è un direttore

                    try {
                        //Statement statement = conn.createStatement();
                        String query = "SELECT * FROM direttori WHERE CF = ?";
                        PreparedStatement preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setString(1, CF_manager);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (!resultSet.next()) {
                            JOptionPane.showMessageDialog(tableFrame, "Non sei un direttore.", "Errore", JOptionPane.ERROR_MESSAGE);
                        } else {


                            int selectedRow = table.getSelectedRow();
                            if (selectedRow == -1) {
                                JOptionPane.showMessageDialog(tableFrame, "Seleziona una riga.", "Errore", JOptionPane.ERROR_MESSAGE);
                            } else {
                                String tipo = (String) table.getValueAt(selectedRow, 0);
                                String datainizio = (String) table.getValueAt(selectedRow, 1);
                                String datafine = (String) table.getValueAt(selectedRow, 2);
                                String CF_addetto = (String) table.getValueAt(selectedRow, 3);
                                String dataRichiesta = (String) table.getValueAt(selectedRow, 4);
                                try {
                                    Statement statementdelete = conn.createStatement();
                                    String querydelete = "DELETE FROM richieste WHERE tipo = ? AND datainizio = ? AND datafine = ? AND CF_addetto = ? AND dataRichiesta = ?";
                                    PreparedStatement preparedStatementdelete = conn.prepareStatement(querydelete);
                                    preparedStatementdelete.setString(1, tipo);
                                    preparedStatementdelete.setString(2, datainizio);
                                    preparedStatementdelete.setString(3, datafine);
                                    preparedStatementdelete.setString(4, CF_addetto);
                                    preparedStatementdelete.setString(5, dataRichiesta);
                                    int rowsAffected = preparedStatementdelete.executeUpdate();
                                    preparedStatementdelete.close();
                                    if (rowsAffected > 0) {
                                        JOptionPane.showMessageDialog(tableFrame, "Richiesta rifiutata con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                                        //aggirona la tabella richieste
                                        tableModel.removeRow(selectedRow);
                                    } else {
                                        JOptionPane.showMessageDialog(tableFrame, "Impossibile rifiutare la richiesta.", "Errore", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(tableFrame, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
                            }
                        }


                    }
                        resultSet.close();
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(tableFrame, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });



        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
