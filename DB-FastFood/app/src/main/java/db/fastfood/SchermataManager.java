package db.fastfood;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class SchermataManager extends JFrame {
    private final Connection conn;

    public SchermataManager(Connection conn) {
        this.conn = conn;
        setTitle("Schermata Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creazione dei pulsanti
        JButton btnVisualizzaProdotti = new JButton("Visualizza Prodotti Disponibili");
        JButton btnAggiungiProdotto = new JButton("Aggiungi Prodotto");
        JButton btnAggiungiIngredienti = new JButton("Aggiungi Ingredienti a Prodotto");
        JButton btnAggiungiIngrediente = new JButton("Aggiungi Ingrediente");
        JButton btnVisualizzaIngredienti = new JButton("Visualizza Ingredienti di un Prodotto");
        JButton btnFatturatoMensile = new JButton("Visualizza Fatturato Mensile");
        JButton btnInserisciRichiesta = new JButton("Nuova richiesta");
        JButton btnVisualizzaRifiuta = new JButton("Visualizza/rifiuta richieste");
        JButton btnInserisciAddetto = new JButton("Inserisci Dipendente");
        JButton btnVisualizzaAddetti = new JButton("Visualizza Dipendenti");
        JButton btncontratti = new JButton("Crea contratto");
        JButton btnVisualizzaContratti = new JButton("Visualizza contratti");
        JButton btnRicercaContratto = new JButton("Ricerca contratto");
        JButton btnCreaFidelty = new JButton("Crea Fidelity");


        // Creazione del layout
        Container container = getContentPane();
        //i pulsanti li metto in una griglia 2x2
        container.setLayout(new FlowLayout());

        container.add(btnVisualizzaProdotti);
        container.add(btnAggiungiProdotto);
        container.add(btnAggiungiIngredienti);
        container.add(btnAggiungiIngrediente);
        container.add(btnVisualizzaIngredienti);
        container.add(btnFatturatoMensile);
        container.add(btnInserisciRichiesta);
        container.add(btnVisualizzaRifiuta);
        container.add(btnInserisciAddetto);
        container.add(btnVisualizzaAddetti);
        container.add(btncontratti);
        container.add(btnVisualizzaContratti);
        container.add(btnRicercaContratto);
        container.add(btnCreaFidelty);

        // Aggiunta delle azioni ai pulsanti
        btnVisualizzaProdotti.addActionListener(e -> visualizzaProdottiDisponibili());

        btnAggiungiProdotto.addActionListener(e -> aggiungiProdotto());

        btnAggiungiIngredienti.addActionListener(e -> aggiungiIngredientiAProdotto());

        btnAggiungiIngrediente.addActionListener(e -> aggiungiIngrediente());

        btnVisualizzaIngredienti.addActionListener(e -> visualizzaIngredientiProdotto());

        btnFatturatoMensile.addActionListener(e -> visualizzaFatturatoMensile());

        btnInserisciRichiesta.addActionListener(e -> inserisciRichiesta());

        btnVisualizzaRifiuta.addActionListener(e -> visualizzaRifiutaRichieste());

        btnInserisciAddetto.addActionListener(e -> inserisciDipendente());

        btnVisualizzaAddetti.addActionListener(e -> visualizzaAddetti());

        btncontratti.addActionListener(e-> inserisciContratto());

        btnVisualizzaContratti.addActionListener(e-> visualizzaContratti(""));

        btnRicercaContratto.addActionListener(e-> ricercaContratti());

        btnCreaFidelty.addActionListener(e-> creaFidelity());

    }

    private void visualizzaProdottiDisponibili() {
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

                tableModel.addRow(new Object[]{codice, descrizione, prezzoVendita});
            }

            JTable table = new JTable(tableModel);

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
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiungiProdotto() {
        String codice = JOptionPane.showInputDialog(this, "Inserisci il codice del prodotto:");
        String descrizione = JOptionPane.showInputDialog(this, "Inserisci la descrizione del prodotto:");
        double prezzoVendita = Double.parseDouble(JOptionPane.showInputDialog(this, "Inserisci il prezzo di vendita del prodotto:"));

        try {
            Statement statement = conn.createStatement();
            String query = "INSERT INTO Prodotti (codice, descrizione, prezzovendita) VALUES ('" + codice + "', '" + descrizione + "', " + prezzoVendita + ")";
            int rowsAffected = statement.executeUpdate(query);
            statement.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Prodotto aggiunto con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile aggiungere il prodotto.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'aggiunta del prodotto.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiungiIngredientiAProdotto() {
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
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*private void aggiungiIngredienteAProdotto(String nomeIngrediente) {
        String codice = JOptionPane.showInputDialog(this, "Inserisci il codice del prodotto:");
        String quantita = JOptionPane.showInputDialog(this, "Inserisci la quantità di " + nomeIngrediente + " da aggiungere al prodotto:");
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
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }

        try {
            Statement statement = conn.createStatement();
            String query = "INSERT INTO ingredienti_prodotti (codice_prodotto, ID_ingrediente, quantita_utilizzata) VALUES ('" + codice + "', '" + idIngrediente + "', " + quantita + ")";
            int rowsAffected = statement.executeUpdate(query);
            statement.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Ingrediente aggiunto con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile aggiungere l'ingrediente.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'aggiunta dell'ingrediente.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }*/



    private void aggiungiIngredienteAProdotto(String nomeIngrediente) {
        String nomeProdotto = JOptionPane.showInputDialog(this, "Inserisci il nome del prodotto:");
        String quantita = JOptionPane.showInputDialog(this, "Inserisci la quantità di " + nomeIngrediente + " da aggiungere al prodotto:");
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
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }

        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Prodotti WHERE descrizione = '" + nomeProdotto + "'";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            String codice = resultSet.getString("codice");
            String query2 = "INSERT INTO ingredienti_prodotti (codice_prodotto, ID_ingrediente, quantita_utilizzata) VALUES ('" + codice + "', '" + idIngrediente + "', " + quantita + ")";
            int rowsAffected = statement.executeUpdate(query2);
            statement.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Ingrediente aggiunto con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile aggiungere l'ingrediente.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'aggiunta dell'ingrediente.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*dato il nome del prodotto, visualizza gli ingredienti che lo compongono*/
    private void visualizzaIngredientiProdotto() {
        String nome_prodotto = JOptionPane.showInputDialog(this, "Inserisci il codice(nome) del prodotto:");

        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM Ingredienti I, ingredienti_prodotti IP, Prodotti P WHERE  I.ID = IP.ID_ingrediente AND IP.codice_prodotto = P.codice AND P.descrizione = '" + nome_prodotto + "'";


            ResultSet resultSet = statement.executeQuery(query);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Ingredienti");

            while (resultSet.next()) {
                String ingrediente = resultSet.getString("nome_commerciale");
                tableModel.addRow(new Object[]{ingrediente});
            }

            JTable table = new JTable(tableModel);

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
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void aggiungiIngrediente() {
        String nome = JOptionPane.showInputDialog(this, "Inserisci il nome dell'ingrediente:");
        String quantita = JOptionPane.showInputDialog(this, "Inserisci la quantità disponibile:");
        String idIngrediente = JOptionPane.showInputDialog(this, "Inserisci l'ID dell'ingrediente:");
        String prezzounitario = JOptionPane.showInputDialog(this, "Inserisci il costo di acquisto:");



        try {
            Statement statement = conn.createStatement();
            String query = "INSERT INTO Ingredienti (ID, prezzoUnitario, nome_commerciale, quantita) VALUES ('" + idIngrediente + "', '" + prezzounitario + "', '" + nome + "', " + quantita + ")";
            int rowsAffected = statement.executeUpdate(query);
            statement.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Ingrediente aggiunto con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile aggiungere l'ingrediente.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'aggiunta dell'ingrediente.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void visualizzaFatturatoMensile(){
        try{
            Statement statement = conn.createStatement();
                String query = """
                        SELECT SUM(P.prezzovendita * D.quantita) as TotIncassoLordo, SUM(P.prezzounitario*D.quantita) as MateriePrime, SUM(P.prezzovendita * D.quantita)-SUM(P.prezzounitario*D.quantita) as TotNetto
                        FROM prodotti P, ordine O, dettaglio_ordini D
                        WHERE D.ID_ordine = O.ID and D.codice_prodotto = P.codice;""";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            int totaleIncassoLordo = resultSet.getInt("TotIncassoLordo");
            int totaleMateriePrime = resultSet.getInt("MateriePrime");
            int totaleNetto = resultSet.getInt("TotNetto");
            resultSet.close();
            statement.close();
            JOptionPane.showMessageDialog(this, "Fatturato Lordo: " + totaleIncassoLordo +"\nMaterie prime: " + totaleMateriePrime + "\nFatturato netto: "+totaleNetto, "Fatturato", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inserisciRichiesta(){
        //un addetto puo effettuare una richiesta di riposo, di ferie o di malattia
        String tipoRichiesta = JOptionPane.showInputDialog(this, "Inserisci il tipo di richiesta (riposo, ferie, malattia):");
        String dataInizio = JOptionPane.showInputDialog(this, "Inserisci la data di inizio (YYYY-MM-DD):");
        String dataFine = JOptionPane.showInputDialog(this, "Inserisci la data di fine (YYYY-MM-DD):");
        String idAddetto = JOptionPane.showInputDialog(this, "Inserisci CF dell'addetto:");

        try {
            Statement statement = conn.createStatement();
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
                JOptionPane.showMessageDialog(this, "Richiesta aggiunta con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile aggiungere la richiesta.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'aggiunta della richiesta.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void visualizzaRifiutaRichieste(){
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
                        Statement statement = conn.createStatement();
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
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inserisciAddetto() {
        try{
            String CF = JOptionPane.showInputDialog(this, "Inserisci il CF:");
            String nome = JOptionPane.showInputDialog(this, "Inserisci il nome:");
            String cognome = JOptionPane.showInputDialog(this, "Inserisci il cognome:");
            Statement statement = conn.createStatement();
            String query = "INSERT INTO ADDETTO VALUES(?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, CF);
            preparedStatement.setString(2, nome);
            preparedStatement.setString(3, cognome);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Dipendente inserito con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile inserire il dipendente.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void visualizzaAddetti() {

        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM addetto";
            ResultSet resultSet = statement.executeQuery(query);
            String[] columnNames = {"CF", "Nome", "Cognome"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (resultSet.next()) {
                String CF = resultSet.getString("CF");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                Object[] row = {CF, nome, cognome};
                tableModel.addRow(row);
            }
            resultSet.close();
            statement.close();
            JTable table = new JTable(tableModel);
            JFrame tableFrame = new JFrame("Addetti");
            tableFrame.setSize(400, 300);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tableFrame.setLocationRelativeTo(null);
            tableFrame.getContentPane().add(new JScrollPane(table));
            tableFrame.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void inserisciDipendente(){
        String tipo = JOptionPane.showInputDialog(this, "manager o addetto?:");
        tipo.toLowerCase();
        if(tipo.equals("manager")){
            try{
                String CF = JOptionPane.showInputDialog(this, "Inserisci il CF:");
                String nome = JOptionPane.showInputDialog(this, "Inserisci il nome:");
                String cognome = JOptionPane.showInputDialog(this, "Inserisci il cognome:");
                String direttore = JOptionPane.showInputDialog(this, "Inserisci il CF del direttore:");
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
                    JOptionPane.showMessageDialog(this, "Dipendente inserito con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Impossibile inserire il dipendente.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(tipo.equals("addetto")){
            try{
                String CF = JOptionPane.showInputDialog(this, "Inserisci il CF:");
                String nome = JOptionPane.showInputDialog(this, "Inserisci il nome:");
                String cognome = JOptionPane.showInputDialog(this, "Inserisci il cognome:");
                Statement statement = conn.createStatement();
                String query = "INSERT INTO ADDETTO VALUES(?,?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, CF);
                preparedStatement.setString(2, nome);
                preparedStatement.setString(3, cognome);
                int rowsAffected = preparedStatement.executeUpdate();
                preparedStatement.close();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Dipendente inserito con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Impossibile inserire il dipendente.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(this, "Errore durante l'inserimento del dipendente.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void inserisciContratto(){
        //si connette alla tabella e conta il numero di righe
        int count=0;
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
        

        //inserisce il contratto
        String CF = JOptionPane.showInputDialog(this, "CF del dipendente:");
        String stipendio = JOptionPane.showInputDialog(this, "Stipendio:");
        String dataInizio = JOptionPane.showInputDialog(this, "Data di inizio (YYYY-MM-DD):");
        String dataFine = JOptionPane.showInputDialog(this, "Data di fine (YYYY-MM-DD):");
        String ore = JOptionPane.showInputDialog(this, "Ore settimanali:");

        try{
            Statement statement = conn.createStatement();
            String query = "INSERT INTO CONTRATTO VALUES(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, count+1);
            preparedStatement.setString(2, stipendio);
            preparedStatement.setString(3, dataFine);
            preparedStatement.setString(4, ore);
            preparedStatement.setString(5, dataInizio);
            preparedStatement.setString(6, CF);
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Contratto inserito con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile inserire il contratto.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

        /*private void visualizzaContratti(String CFricercato){
            try{
                Statement statement = conn.createStatement();
                //oltre al contratto voglio visualizzare i dati del dipendente
                String query = "SELECT * FROM CONTRATTO C, ADDETTO D WHERE C.CF_addetto = D.CF";
                if (CFricercato != null && !CFricercato.isEmpty()) {
                    query += " AND D.CF = '" + CFricercato + "'";
                }
                ResultSet resultSet = statement.executeQuery(query);
                String[] columnNames = {"ID", "Stipendio", "Data fine", "Ore settimanali", "Data inizio", "CF addetto", "Nome", "Cognome"};
                if(resultSet.next()){
                    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
                    do{
                        int ID = resultSet.getInt("ID");
                        String stipendio = resultSet.getString("stipendio");
                        String dataFine = resultSet.getString("data_fine");
                        String ore = resultSet.getString("ore_previste_settimanali");
                        String dataInizio = resultSet.getString("data_inizio");
                        String CF = resultSet.getString("CF_addetto");
                        String nome = resultSet.getString("nome");
                        String cognome = resultSet.getString("cognome");
                        Object[] row = {ID, stipendio, dataFine, ore, dataInizio, CF, nome, cognome};
                        tableModel.addRow(row);
                    } while(resultSet.next());
                    JTable table = new JTable(tableModel);
                    JScrollPane scrollPane = new JScrollPane(table);
                    JFrame frame = new JFrame("Contratti");
                    frame.add(scrollPane, BorderLayout.CENTER);
                    frame.setSize(800, 600);
                    frame.setVisible(true);

                }
                else{
                    JOptionPane.showMessageDialog(this, "Non ci sono contratti da visualizzare.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                

            } catch(SQLException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }*/
        
        private void visualizzaContratti(String cfDipendenteDaCercare) {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM CONTRATTO C, ADDETTO D WHERE C.CF_addetto = D.CF";

            if (cfDipendenteDaCercare != null && !cfDipendenteDaCercare.isEmpty()) {
                query += " AND D.Cognome = '" + cfDipendenteDaCercare + "'";
            }

            ResultSet resultSet = statement.executeQuery(query);
            String[] columnNames = {"ID", "Stipendio", "Data fine", "Ore settimanali", "Data inizio", "CF addetto", "Nome", "Cognome"};
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
                Object[] row = {ID, stipendio, dataFine, ore, dataInizio, CF, nome, cognome};
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
                                JOptionPane.showMessageDialog(frame, "Record eliminato con successo.", "Eliminazione", JOptionPane.INFORMATION_MESSAGE);
                                visualizzaContratti(cfDipendenteDaCercare); // Aggiorna la visualizzazione dei contratti
                            } else {
                                JOptionPane.showMessageDialog(frame, "Nessun record eliminato.", "Eliminazione", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Errore durante l'eliminazione del record.", "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Seleziona un record da eliminare.", "Eliminazione", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(eliminaButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante l'esecuzione della query.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

        


    private void ricercaContratti(){
        String CFricercato = JOptionPane.showInputDialog(this, "Cognome del dipendente:");
        visualizzaContratti(CFricercato);
    }

    private void creaFidelity(){

        String id = JOptionPane.showInputDialog(this, "numero tessera:");
        int timbri = 0;
        String scadenza = JOptionPane.showInputDialog(this, "scadenza card [YYYY-MM-DD]:"); 
        int menu=0;
        String cliente = JOptionPane.showInputDialog(this, "CF cliente:");
        int storico=0;
        String query = "INSERT INTO FIDELTY VALUES (" + id + ", " + timbri + ", '" + scadenza + "', " + menu + ", '" + Integer.parseInt(cliente) + "', " + storico + ")";
        try {
            Statement statement = conn.createStatement();
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Fidelity creata con successo.", "Creazione", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nessuna fidelity creata.", "Creazione", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante la creazione della fidelity.", "Errore", JOptionPane.ERROR_MESSAGE);
        }





    }



  
}

