package db.fastfood;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;

public class SchermataManager extends JFrame {
    private Connection conn;

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

        // Creazione del layout
        Container container = getContentPane();
        container.setLayout(new FlowLayout());
        container.add(btnVisualizzaProdotti);
        container.add(btnAggiungiProdotto);
        container.add(btnAggiungiIngredienti);
        container.add(btnAggiungiIngrediente);
        container.add(btnVisualizzaIngredienti);
        container.add(btnFatturatoMensile);
        container.add(btnInserisciRichiesta);

        // Aggiunta delle azioni ai pulsanti
        btnVisualizzaProdotti.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                visualizzaProdottiDisponibili();
            }
        });

        btnAggiungiProdotto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aggiungiProdotto();
            }
        });

        btnAggiungiIngredienti.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aggiungiIngredientiAProdotto();
            }
        });

        btnAggiungiIngrediente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aggiungiIngrediente();
            }
        });

        btnVisualizzaIngredienti.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                visualizzaIngredientiProdotto();
            }
        });

        btnFatturatoMensile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                visualizzaFatturatoMensile();
            }
        });

        btnInserisciRichiesta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inserisciRichiesta();
            }
        });

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
                btn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        aggiungiIngredienteAProdotto(nome);
                    }
                });
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
                String query = "SELECT SUM(P.prezzovendita * D.quantita) as TotIncassoLordo, SUM(P.prezzounitario*D.quantita) as MateriePrime, SUM(P.prezzovendita * D.quantita)-SUM(P.prezzounitario*D.quantita) as TotNetto\n" +
                        "FROM prodotti P, ordine O, dettaglio_ordini D\n" +
                        "WHERE D.ID_ordine = O.ID and D.codice_prodotto = P.codice;";
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
}
