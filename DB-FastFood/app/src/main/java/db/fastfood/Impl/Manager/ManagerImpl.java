package db.fastfood.Impl.Manager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;



import db.fastfood.api.Manager.Manager;
import db.fastfood.util.CustomTable;
import db.fastfood.util.Util;
import db.fastfood.util.UtilImpl;

public class ManagerImpl implements Manager {

    private final Connection conn;
    CustomTable customizeTable = new CustomTable();
    Util util;

    public ManagerImpl(Connection conn) {
        this.conn = conn;
        util = new UtilImpl(conn);
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

    public void visualizzaVenditeGiornaliere() {
        // visualizza una tabella con le vendite giornaliere
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
                // prodotti.add(nome);
                double prezzo = resultSet.getDouble("prezzovendita");
                int venduto = resultSet.getInt("venduto");
                // int quantita = resultSet.getInt("quantita");
                // String data = resultSet.getString("data");
                Object[] row = { nome, prezzo, venduto, /* data */ };

                tableModel.addRow(row);
            }

            // aggiunge alla tabella il fatturato totale
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

            customizeTable.notEditable(table);
            customizeTable.doGraphic(table);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setSize(800, 600);
            // evidenzia l'ultimo elemento della tabella
            table.changeSelection(table.getRowCount() - 1, 0, false, false);

            frame.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void registraScarti() {
        // apertura di una nuova finestra con una conmbobox per la scelta del prodotto e
        // un campo per la quantità

        // codice per la creazione della finestra
        JFrame frame = new JFrame("Registra scarti");
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // codice per la creazione della combobox
        JComboBox<String> prodotti = new JComboBox<String>();

        try {
            Statement statement = conn.createStatement();
            String query = """
                    select p.descrizione
                    from prodotti p
                    """;
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String nome = resultSet.getString("descrizione");
                prodotti.addItem(nome);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }

        // codice per la creazione del campo di testo
        JTextField quantita = new JTextField();
        quantita.setPreferredSize(new Dimension(100, 30));

        // codice per la creazione del bottone

        JButton registra = new JButton("Registra");
        //var codiceee = 0;
        registra.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Map <String, Double> prodottiIngredienti = new HashMap<String, Double>();
                
                int codice = 0;
                try {
                    String query = "SELECT codice FROM prodotti WHERE descrizione = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, prodotti.getSelectedItem().toString());
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        codice = resultSet.getInt("codice");
                        // Ottieni la data corrente come java.sql.Date
                        java.sql.Date dataCorrente = java.sql.Date.valueOf(java.time.LocalDate.now());

                        query = "INSERT INTO scarti_giornalieri (data, prodotto, quantita, codice_prodotto) VALUES (?, ?, ?, ?)";
                        statement = conn.prepareStatement(query);
                        statement.setDate(1, dataCorrente);
                        statement.setString(2, prodotti.getSelectedItem().toString());
                        statement.setInt(3, Integer.parseInt(quantita.getText()));
                        statement.setInt(4, codice);
                        statement.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Scarto registrato con successo.", "Registrazione",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Prodotto non trovato.", "Errore",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    //popolo la tabella di collegamento tra prodotti e scarti
                    query = """
                            select p.descrizione, s.quantita
                            from prodotti p, scarti_giornalieri s
                            where p.codice = s.codice_prodotto and s.data = current_date()
                            """;
                    resultSet = statement.executeQuery(query);
                    String[] columnNames = { "descrizione", "quantita" };

                    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
                    
                    while (resultSet.next()) {
                        String nome = resultSet.getString("descrizione");
                        int quantita = resultSet.getInt("quantita");
                        Object[] row = { nome, quantita };
                        tableModel.addRow(row);
                    }

                    JTable table = new JTable(tableModel);
                    JScrollPane scrollPane = new JScrollPane(table);
                    JFrame frame = new JFrame("Scarti giornalieri");
                    
                    customizeTable.notEditable(table);
                    customizeTable.doGraphic(table);

                    frame.add(scrollPane, BorderLayout.CENTER);
                    frame.setSize(400, 300);
                    frame.setVisible(true);


                    //ottengo gli ingredienti del prodotto e la quantita che serve per ogni ingrediente

                    query = """
                            select i.nome_commerciale, r.quantita_utilizzata
                            from ingredienti i, ingredienti_prodotti r
                            where r.codice_prodotto = ? and r.ID_ingrediente = i.ID
                            """;

                    statement = conn.prepareStatement(query);
                    statement.setInt(1, codice);
                    resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        String nome = resultSet.getString("nome_commerciale");
                        double quantita = resultSet.getDouble("quantita_utilizzata");
                        System.out.println(nome + " " + quantita);
                        prodottiIngredienti.put(nome, quantita);
                    }   

                    //aggiorno la quantita degli ingredienti
                    for (Map.Entry<String, Double> entry : prodottiIngredienti.entrySet()) {
                        String key = entry.getKey();
                        Double value = entry.getValue();
                        //System.out.println(key + " " + value);
                        query = """
                                update ingredienti
                                set quantita = quantita - ?
                                where nome_commerciale = ?
                                """;
                        statement = conn.prepareStatement(query);
                        statement.setDouble(1, value);
                        statement.setString(2, key);
                        statement.executeUpdate();
                    }

                    








                    


                    resultSet.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }

            
                
            }
        });

        frame.add(prodotti);
        frame.add(quantita);
        frame.add(registra);

        frame.setVisible(true);

    }
}
