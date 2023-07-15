package db.fastfood.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

//import com.mysql.cj.xdevapi.PreparableStatement;

import db.fastfood.api.Vendita;
import db.fastfood.util.Util;
import db.fastfood.util.UtilImpl;
import db.fastfood.view.ViewSchermatavendita;

public class VenditaImpl implements Vendita {
    private final Connection conn;
    
    //non devo creare una nuova view ma devo aggiornare quella esistente
    ViewSchermatavendita view;




    public VenditaImpl(ViewSchermatavendita view, Connection conn) {
        this.conn = conn;
        this.view = view;
    }

    public void nuovo_cliente() {

        Util util = new UtilImpl(conn);

        try {
            int idcliente =util.getCurrentcliente();
            String query = "INSERT INTO cliente(ID) VALUES (?) ";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idcliente + 1);
            statement.executeUpdate();

            // associo id cliente alla tabella ordine
            int idordine = util.getCurrentordine() + 1;
            //salva l'orario in cui è stato effettuato l'ordine in formato sql
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

        // aggiorno i correnti

        util.getCurrentcliente();
        util.getCurrentordine();
        view.clearTable();

    }

    public void inserisci_offerta() {
        Util util = new UtilImpl(conn);
        try {
            // int idcliente=getCurrentcliente();
            int idordine = util.getCurrentordine();
            // textbox che chiede il numero di offerta
            JDialog dialog = new JDialog();
            dialog.setAlwaysOnTop(true);
            String numero_offerta = JOptionPane.showInputDialog(dialog, "Inserisci il numero dell'offerta");
            int numero_offerta_int = Integer.parseInt(numero_offerta);
            /*
             * String query =
             * "INSERT INTO possedimento_offerta(ID_cliente, codice_offerta) VALUES (?, ?) "
             * ;
             * PreparedStatement statement = conn.prepareStatement(query);
             * statement.setInt(1, idcliente);
             * statement.setInt(2, idordine);
             * statement.executeUpdate();
             */

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

        // aggiorno i correnti

        util.getCurrentcliente();
        util.getCurrentordine();
    }

    public void vendita(String nomeprodotto) {
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

                    // Aggiorna il prezzo totale dell'ordine
                    //System.out.println("Il cliente ha già quel prodotto nel suo ordine");
                    //System.out.println(prezzo);
                    

                    String updatePrezzo = "UPDATE dettaglio_ordini SET totale = totale + ? WHERE ID_ordine = ?";
                    PreparedStatement updatePrezzoStatement = conn.prepareStatement(updatePrezzo);
                    updatePrezzoStatement.setDouble(1, prezzo);
                    updatePrezzoStatement.setInt(2, idordine);
                    updatePrezzoStatement.executeUpdate();

                    // !!!!!
                    updateQuantity(nomeprodotto, idProdotto);

                    //aggiorno la tabella della view
                    
                    view.updateRow(nomeprodotto);

                    flag = true;
                }
            }

            if (!flag) {
                // Il prodotto non esiste ancora nel dettaglio ordine, procedi con il resto
                // delle operazioni

                // decremento la quantita degli ingredienti in magazzino
                updateQuantity(nomeprodotto, idProdotto);

                // Inserisci la vendita nel database del cliente corrente
                idcliente = util.getCurrentcliente();
                idordine = util.getCurrentordine();
                String query2 = "INSERT INTO dettaglio_ordini (ID_ordine, codice_prodotto, quantita) VALUES (?,?,?)";
                statement = conn.prepareStatement(query2);
                statement.setInt(1, idordine);
                statement.setInt(2, idProdotto);
                statement.setInt(3, 1);
                statement.executeUpdate();

                // Aggiorna il prezzo totale dell'ordine
                String updatePrezzo = "UPDATE dettaglio_ordini SET totale = totale + ? WHERE ID_ordine = ?";
                PreparedStatement updatePrezzoStatement = conn.prepareStatement(updatePrezzo);
                updatePrezzoStatement.setDouble(1, prezzo);
                updatePrezzoStatement.setInt(2, idordine);
                updatePrezzoStatement.executeUpdate();

                //controller.updateTable(nomeprodotto, 1, prezzo);

                //ViewSchermatavendita view = new ViewSchermatavendita(conn);
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
            // String nomeIngrediente = resultSet.getString("nome_commerciale");
            int idIngrediente = resultSet.getInt("ID");
            double quantitaIngrediente = resultSet.getDouble("quantita");
            double quantitaUtilizzata = resultSet.getDouble("quantita_utilizzata");
            // System.out.println(quantitaUtilizzata);
            /* int */
            idProdotto = resultSet.getInt("codice");

            // Calcolo della nuova quantità dell'ingrediente dopo la vendita
            double nuovaQuantita = quantitaIngrediente - quantitaUtilizzata;
            // System.out.println(nuovaQuantita);

            // Aggiornamento della quantità dell'ingrediente nel database
            String updateQuery = "UPDATE Ingredienti SET Quantita = ? WHERE ID = ?";
            PreparedStatement updateStatement2 = conn.prepareStatement(updateQuery);
            updateStatement2.setDouble(1, nuovaQuantita);
            updateStatement2.setInt(2, idIngrediente);
            updateStatement2.executeUpdate();
        }

    }

    public void delivery(){
        //sposta i record con l'id dell'ordine nella tabella dttaglio_consegne
        //cancella i record con l'id dell'ordine dalla tabella dettaglio_ordini
        Util util = new UtilImpl(conn);

        int idordine = util.getCurrentordine();
        
        //prende tutti i record con l'id dell'ordine dalla tabella dettaglio_ordini e li sposta nella tabella dettaglio_consegne
        while (true) {
            try {
                String query = "INSERT INTO dettaglio_consegna (ID_ordine, codice_prodotto, quantita, totale) SELECT ID_ordine, codice_prodotto, quantita, totale FROM dettaglio_ordini WHERE ID_ordine = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, idordine);
                statement.executeUpdate();

                //cancella i record con l'id dell'ordine dalla tabella dettaglio_ordini
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

    
}
