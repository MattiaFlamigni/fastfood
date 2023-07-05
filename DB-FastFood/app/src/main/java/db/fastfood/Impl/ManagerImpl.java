package db.fastfood.Impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import db.fastfood.api.Manager;

public class ManagerImpl implements Manager {

    private final Connection conn;
    CustomTable prova = new CustomTable();

    public ManagerImpl(Connection conn) {
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
                String nome = JOptionPane.showInputDialog(null, "Inserisci il nome:");
                String cognome = JOptionPane.showInputDialog(null, "Inserisci il cognome:");
                String direttore = JOptionPane.showInputDialog(null, "Inserisci il CF del direttore:");
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
                String nome = JOptionPane.showInputDialog(null, "Inserisci il nome:");
                String cognome = JOptionPane.showInputDialog(null, "Inserisci il cognome:");
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
            JFrame frame = new JFrame("Contratti");
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setSize(800, 600);

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

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(eliminaButton);
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
    public void visualizzaFatturatoMensile() {
        try {
            Statement statement = conn.createStatement();
            String query = """
                    SELECT SUM(P.prezzovendita * D.quantita) as TotIncassoLordo, SUM(P.prezzounitario*D.quantita) as MateriePrime, SUM(P.prezzovendita * D.quantita)-SUM(P.prezzounitario*D.quantita) as TotNetto
                    FROM prodotti P, ordine O, dettaglio_ordini D
                    WHERE D.ID_ordine = O.ID and D.codice_prodotto = P.codice
                    AND MONTH(O.data) = MONTH(CURRENT_DATE());""";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            double totaleIncassoLordo = resultSet.getInt("TotIncassoLordo");
            double totaleMateriePrime = resultSet.getInt("MateriePrime");
            double totaleNetto = resultSet.getInt("TotNetto");
            resultSet.close();
            statement.close();
            JOptionPane
                    .showMessageDialog(null,
                            "Fatturato Lordo: " + totaleIncassoLordo + "\nMaterie prime: " + totaleMateriePrime
                                    + "\nFatturato netto: " + totaleNetto,
                            "Fatturato", JOptionPane.INFORMATION_MESSAGE);
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
    public void creaFidelty() {
        String id = JOptionPane.showInputDialog(null, "numero tessera:");
        int timbri = 0;
        String scadenza = JOptionPane.showInputDialog(null, "scadenza card [YYYY-MM-DD]:");
        int menu = 0;
        String cliente = JOptionPane.showInputDialog(null, "CF cliente:");
        int storico = 0;
        String query = "INSERT INTO FIDELTY VALUES (" + id + ", " + timbri + ", '" + scadenza + "', " + menu + ", '"
                + Integer.parseInt(cliente) + "', " + storico + ")";
        try {
            Statement statement = conn.createStatement();
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Fidelity creata con successo.", "Creazione",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Nessuna fidelity creata.", "Creazione",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante la creazione della fidelity.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void visualizzaVenditeGiornaliere(){
        List<String> prodotti = new ArrayList<String>();
        //visualizza una tabella con le vendite giornaliere
        try {
            Statement statement = conn.createStatement();
            String query = """
                    select p.descrizione, p.prezzovendita, SUM(d.quantita) as venduto
                    from prodotti p, ordine o, dettaglio_ordini d
                    where 	d.ID_ordine = o.ID and d.codice_prodotto = p.codice and
		                    o.data = current_date() and o.data is not null 
                    group by p.descrizione, p.prezzovendita;""";
                    
            ResultSet resultSet = statement.executeQuery(query);
            String[] columnNames = { "descrizione", "prezzovendita", "venduto" };
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                String nome = resultSet.getString("descrizione");
                //prodotti.add(nome);
                double prezzo = resultSet.getDouble("prezzovendita");
                int venduto = resultSet.getInt("venduto");
                //int quantita = resultSet.getInt("quantita");
                //String data = resultSet.getString("data");
                Object[] row = { nome, prezzo, venduto, /*data*/ };

                tableModel.addRow(row);
            }

            //aggiunge alla tabella il fatturato totale
            query = """
                    select SUM(p.prezzovendita * d.quantita) as fatturato
                    from prodotti p, ordine o, dettaglio_ordini d
                    where 	d.ID_ordine = o.ID and d.codice_prodotto = p.codice and
                            o.data = current_date() and o.data is not null;""";
            resultSet = statement.executeQuery(query);
            resultSet.next();
            double fatturato = resultSet.getDouble("fatturato");
            Object[] row2 = { "Fatturato totale", fatturato };
            tableModel.addRow(row2);
            

            


            resultSet.close();
            statement.close();


            



            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            JFrame frame = new JFrame("Vendite giornaliere");
            //rendi la tabella non modificabile
            prova.notEditable(table);
            
            prova.doGraphic(table);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setSize(800, 600);
            //evidenzia l'ultimo elemento della tabella
            table.changeSelection(table.getRowCount() - 1, 0, false, false);

            frame.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
