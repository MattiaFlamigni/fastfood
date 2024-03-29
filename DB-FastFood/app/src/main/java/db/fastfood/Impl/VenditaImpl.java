package db.fastfood.Impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import db.fastfood.api.Vendita;
import db.fastfood.util.Util;
import db.fastfood.util.UtilImpl;
import db.fastfood.view.ViewSchermatavendita;

public class VenditaImpl implements Vendita {
    private final Connection conn;

    ViewSchermatavendita view;

    public VenditaImpl(ViewSchermatavendita view, Connection conn) {
        this.conn = conn;
        this.view = view;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void newCustomer() {

        Util util = new UtilImpl(conn);

        try {
            int idcliente = util.getCurrentcliente();
            String query = "INSERT INTO cliente(ID) VALUES (?) ";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idcliente + 1);
            statement.executeUpdate();

            int idordine = util.getCurrentordine() + 1;
            java.util.Date currentTime = new java.util.Date();
            Time ora = new java.sql.Time(currentTime.getTime());

            String query2 = "INSERT INTO ordine(ora, ID, data, ID_cliente) VALUES (?, ?, ?, ?) ";
            PreparedStatement statement2 = conn.prepareStatement(query2);
            statement2.setTime(1, ora);
            statement2.setInt(2, idordine);
            statement2.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            statement2.setInt(4, idcliente);
            statement2.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        util.getCurrentcliente();
        util.getCurrentordine();
        view.clearTable();

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addOffer() {
        Util util = new UtilImpl(conn);
        try {
            // int idcliente=getCurrentcliente();
            int idordine = util.getCurrentordine();
            // textbox che chiede il numero di offerta
            JDialog dialog = new JDialog();
            dialog.setAlwaysOnTop(true);
            String numero_offerta = JOptionPane.showInputDialog(dialog, "Inserisci il numero dell'offerta");
            int numero_offerta_int = Integer.parseInt(numero_offerta);

            // recupera il valore dello sconto
            String query2 = "SELECT percentuale FROM offerta WHERE codice = ?";
            PreparedStatement statement2 = conn.prepareStatement(query2);
            statement2.setInt(numero_offerta_int, numero_offerta_int);
            ResultSet resultSet = statement2.executeQuery();
            int sconto = 0;
            while (resultSet.next()) {
                sconto = resultSet.getInt("percentuale");
            }

            // aggiorna il totale dell'ordine
            String query3 = "UPDATE dettaglio_ordini SET totale = totale - (totale * ? / 100) WHERE ID_ordine = ?";
            PreparedStatement statement3 = conn.prepareStatement(query3);
            statement3.setInt(1, sconto);
            statement3.setInt(2, idordine);
            statement3.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        util.getCurrentcliente();
        util.getCurrentordine();
    }

    public void sold(String nomeprodotto) {
        Util util = new UtilImpl(conn);

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean flag = false;

        try {
            @SuppressWarnings("unused")
            int idcliente = util.getCurrentcliente();
            int idordine = util.getCurrentordine();
            int idProdotto = 0;

            // Ottieni il codice del prodotto selezionato
            String query = "SELECT codice FROM prodotti WHERE descrizione = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, nomeprodotto);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                idProdotto = resultSet.getInt("codice");
            }
            statement.close();
            resultSet.close();
            // ottieni il prezzo del prodotto selezionato
            String queryprezzo = "SELECT prezzovendita FROM prodotti WHERE descrizione = ?";
            statement = conn.prepareStatement(queryprezzo);
            statement.setString(1, nomeprodotto);
            resultSet = statement.executeQuery();
            double prezzo = 0;
            while (resultSet.next()) {
                prezzo = resultSet.getDouble("prezzovendita");
            }

            // Verifica se il cliente corrente ha già quel codice prodotto nel suo
            // ordinativo
            String check = "SELECT codice_prodotto FROM dettaglio_ordini WHERE ID_ordine = ? AND codice_prodotto = ?";
            PreparedStatement statement22 = conn.prepareStatement(check);
            statement22.setInt(1, idordine);
            statement22.setInt(2, idProdotto);
            ResultSet result2 = statement22.executeQuery();

            // Verifica l'esistenza del record nel dettaglio ordine
            if (result2.next()) {
                // Il record esiste
                int codice = result2.getInt("codice_prodotto");
                if (codice == idProdotto) {
                    // Se il cliente ha già quel prodotto nel suo ordine, aggiorna la quantità

                    String update = "UPDATE dettaglio_ordini SET quantita = quantita + 1 WHERE ID_ordine = ? AND codice_prodotto = ?";
                    PreparedStatement updateStatement = conn.prepareStatement(update);
                    updateStatement.setInt(1, idordine);
                    updateStatement.setInt(2, idProdotto);
                    updateStatement.executeUpdate();

                    String updatePrezzo = "UPDATE dettaglio_ordini SET totale = totale + ? WHERE ID_ordine = ?";
                    PreparedStatement updatePrezzoStatement = conn.prepareStatement(updatePrezzo);
                    updatePrezzoStatement.setDouble(1, prezzo);
                    updatePrezzoStatement.setInt(2, idordine);
                    updatePrezzoStatement.executeUpdate();

                    // !!!!!
                    updateQuantity(nomeprodotto, idProdotto);

                    // aggiorno la tabella della view

                    view.updateRow(nomeprodotto);

                    flag = true;
                }
            }

            if (!flag) {
                // Il prodotto non esiste ancora nel dettaglio ordinei

                updateQuantity(nomeprodotto, idProdotto);

                // Inserisci la vendita nel database del cliente corrente
                idcliente = util.getCurrentcliente();
                idordine = util.getCurrentordine();
                String query2 = "INSERT INTO dettaglio_ordini (ID_ordine, codice_prodotto, quantita, totale) VALUES (?,?,?, ?)";
                statement = conn.prepareStatement(query2);
                statement.setInt(1, idordine);
                statement.setInt(2, idProdotto);
                statement.setInt(3, 1);
                statement.setDouble(4, prezzo);
                statement.executeUpdate();

                view.updateTable(nomeprodotto, 1, prezzo);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Chiudi le risorse correttamente
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        flag = false;
    }

    private void updateQuantity(String nomeprodotto, int idProdotto) throws SQLException {
        // decrementa la quantita di ingredienti
        String query1 = "SELECT P.codice, I.ID, I.quantita, P.descrizione, I.nome_commerciale, PI.quantita_utilizzata  FROM PRODOTTI P JOIN INGREDIENTI_PRODOTTI PI ON P.codice = PI.codice_prodotto JOIN INGREDIENTI I ON PI.ID_ingrediente = I.ID where P.descrizione = '"
                + nomeprodotto + "'";
        Statement statement1 = conn.createStatement();
        ResultSet resultSet = statement1.executeQuery(query1);

        while (resultSet.next()) {
            int idIngrediente = resultSet.getInt("ID");
            double quantitaIngrediente = resultSet.getDouble("quantita");
            double quantitaUtilizzata = resultSet.getDouble("quantita_utilizzata");

            idProdotto = resultSet.getInt("codice");

            double nuovaQuantita = quantitaIngrediente - quantitaUtilizzata;

            String updateQuery = "UPDATE Ingredienti SET Quantita = ? WHERE ID = ?";
            PreparedStatement updateStatement2 = conn.prepareStatement(updateQuery);
            updateStatement2.setDouble(1, nuovaQuantita);
            updateStatement2.setInt(2, idIngrediente);
            updateStatement2.executeUpdate();
        }

    }

    public void delivery() {
        Util util = new UtilImpl(conn);

        int idordine = util.getCurrentordine();

        // viene visualizzato un menu a tendina contenente i valori "glovo, justeat,
        // deliveroo"

        String[] options = { "Glovo", "Justeat", "Deliveroo" };
        String delivery = (String) JOptionPane.showInputDialog(null, "Scegli il servizio di delivery",
                "Servizio di delivery", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        System.out.println(delivery);

        while (true) {
            // Date dataCorrente = java.sql.Date.valueOf(java.time.LocalDate.now());
            try {
                String query = "INSERT INTO dettaglio_consegna (ID_ordine, codice_prodotto, quantita, totale, nomeApp) SELECT ID_ordine, codice_prodotto, quantita, totale, ? FROM dettaglio_ordini WHERE ID_ordine = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, delivery);
                statement.setInt(2, idordine);
                // statement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                statement.executeUpdate();

                // aggiorna il record inserndo la data nella colonna data
                String update = "UPDATE dettaglio_consegna SET data = ? WHERE ID_ordine = ?";
                PreparedStatement updateStatement = conn.prepareStatement(update);
                updateStatement.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                updateStatement.setInt(2, idordine);
                updateStatement.executeUpdate();

                // cancella i record con l'id dell'ordine dalla tabella dettaglio_ordini
                String delete = "DELETE FROM dettaglio_ordini WHERE ID_ordine = ?";
                PreparedStatement deleteStatement = conn.prepareStatement(delete);
                deleteStatement.setInt(1, idordine);
                deleteStatement.executeUpdate();

                break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @inheritDoc
     */
    @Override
    public void mealVoucher() {

        Util util = new UtilImpl(conn);
        int ordine = util.getCurrentordine();
        boolean flag = false;

        String CFAddetto = JOptionPane.showInputDialog("Inserisci il codice fiscale del dipendente");

        // se non esiste quel codice fiscale

        // controllo se è gia stato inserito un buono pasto per quell'addetto nella data
        // odierna

        try {
            String query = "SELECT * FROM buonopasto WHERE CFAddetto = ? AND data = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, CFAddetto);
            statement.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Buono pasto gia inserito per questo addetto");
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true && flag == false) {
            try {
                String nome = "";
                // prende il codice del prodotto dalla tabella dettaglio_ordini
                String query = "SELECT codice_prodotto FROM dettaglio_ordini WHERE ID_ordine = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, ordine);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int codice = resultSet.getInt("codice_prodotto");

                    // prende il nome del prodotto dalla tabella prodotti
                    String query2 = "SELECT descrizione FROM prodotti WHERE codice = ?";
                    PreparedStatement statement2 = conn.prepareStatement(query2);
                    statement2.setInt(1, codice);
                    ResultSet resultSet2 = statement2.executeQuery();

                    while (resultSet2.next()) {
                        nome = resultSet2.getString("descrizione");
                    }
                    String query3 = "INSERT INTO buonopasto (ID, descrizione, data, CFAddetto) VALUES (?,?,?,?)";
                    PreparedStatement statement3 = conn.prepareStatement(query3);
                    statement3.setInt(1, ordine);
                    statement3.setString(2, nome);
                    statement3.setDate(3, Date.valueOf(LocalDate.now()));
                    statement3.setString(4, CFAddetto);

                    statement3.executeUpdate();

                }

                break;
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore");
                return;
            }
        }

    }

}
