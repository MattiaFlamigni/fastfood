package db.fastfood.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.mysql.cj.xdevapi.PreparableStatement;

import db.fastfood.api.Vendita;

public class VenditaImpl implements Vendita {
    private final Connection conn;

    public VenditaImpl(Connection conn) {
        this.conn = conn;
    }

    public void nuovo_cliente() {
        try {
            int idcliente = getCurrentcliente();
            String query = "INSERT INTO cliente(ID) VALUES (?) ";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idcliente + 1);
            statement.executeUpdate();

            // associo id cliente alla tabella ordine
            int idordine = getCurrentordine() + 1;

            String query2 = "INSERT INTO ordine(ID, data, ID_cliente) VALUES (?, ?, ?) ";
            PreparedStatement statement2 = conn.prepareStatement(query2);
            statement2.setInt(1, idordine);
            statement2.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            statement2.setInt(3, idcliente);
            statement2.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // aggiorno i correnti

        getCurrentcliente();
        getCurrentordine();

    }

    public void inserisci_offerta() {
        try {
            // int idcliente=getCurrentcliente();
            int idordine = getCurrentordine();
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

        getCurrentcliente();
        getCurrentordine();
    }

    public void vendita(String nomeprodotto) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean flag = false;

        try {
            @SuppressWarnings("unused")
            int idcliente = getCurrentcliente();
            int idordine = getCurrentordine();
            int idProdotto = 0;

            // Ottieni il codice del prodotto selezionato
            String query = "SELECT codice FROM prodotti WHERE descrizione = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, nomeprodotto);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                idProdotto = resultSet.getInt("codice");
            }
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
                    String updatePrezzo = "UPDATE dettaglio_ordini SET totale = totale + ? WHERE ID = ?";
                    PreparedStatement updatePrezzoStatement = conn.prepareStatement(updatePrezzo);
                    updatePrezzoStatement.setDouble(1, prezzo);
                    updatePrezzoStatement.setInt(2, idordine);

                    // !!!!!
                    updateQuantity(nomeprodotto, idProdotto);

                    flag = true;
                }
            }

            if (!flag) {
                // Il prodotto non esiste ancora nel dettaglio ordine, procedi con il resto
                // delle operazioni

                // decremento la quantita degli ingredienti in magazzino
                updateQuantity(nomeprodotto, idProdotto);

                // Inserisci la vendita nel database del cliente corrente
                idcliente = getCurrentcliente();
                idordine = getCurrentordine();
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

    public void assegna_fidelty() {
        String numero_fidelty_tmp = JOptionPane.showInputDialog("Inserisci il numero di fidelty");
        int numero_fidelty = Integer.parseInt(numero_fidelty_tmp);
        String numero_timbri_corrente_tmp = JOptionPane.showInputDialog("N. menu acquistati");
        int numero_timbri_corrente = Integer.parseInt(numero_timbri_corrente_tmp);

        try {
            String query = "select n_timbri from fidelty where numero = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, numero_fidelty);
            ResultSet resultSet = statement.executeQuery();
            int numero_timbri = 0;
            while (resultSet.next()) {
                numero_timbri = resultSet.getInt("n_timbri");
            }
            numero_timbri = numero_timbri + numero_timbri_corrente;
            if (numero_timbri >= 10) {
                String resetCounterQuery = "UPDATE fidelty SET n_timbri = ? WHERE numero = ?";
                PreparedStatement statement2 = conn.prepareStatement(resetCounterQuery);
                statement2.setInt(1, 0);
                statement2.setInt(2, numero_fidelty);
                statement2.executeUpdate();

                /* il numero di menu omaggio si incrementa */
                String query2 = "SELECT menuomaggio FROM fidelty WHERE numero = ?";
                PreparedStatement statement3 = conn.prepareStatement(query2);
                statement3.setInt(1, numero_fidelty);
                ResultSet resultSet2 = statement3.executeQuery();
                int menuomaggio = 0;
                while (resultSet2.next()) {
                    menuomaggio = resultSet2.getInt("menuomaggio");
                }

                menuomaggio = menuomaggio + 1;
                String query3 = "UPDATE fidelty SET menuomaggio = ? WHERE numero = ?";
                PreparedStatement statement4 = conn.prepareStatement(query3);
                statement4.setInt(1, menuomaggio);
                statement4.setInt(2, numero_fidelty);
                statement4.executeUpdate();

            } else {
                String query4 = "UPDATE fidelty SET n_timbri = ? WHERE numero = ?";
                PreparedStatement statement5 = conn.prepareStatement(query4);
                statement5.setInt(1, numero_timbri);
                statement5.setInt(2, numero_fidelty);
                statement5.executeUpdate();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private int getCurrentcliente() {
        int idcliente = 0;
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT ID FROM cliente";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                idcliente = result.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idcliente;
    }

    private int getCurrentordine() {
        int idordine = 0;
        try {
            Statement statement = conn.createStatement();
            String query2 = "SELECT ID FROM ordine";
            ResultSet result2 = statement.executeQuery(query2);
            while (result2.next()) {
                idordine = result2.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idordine;
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

    public void utilizza_fidelty(){
        int nTimbri=0;
        String numeroFideltyTmp= JOptionPane.showInputDialog("Inserisci il numero di fidelty");
        int numeroFidelty = Integer.parseInt(numeroFideltyTmp);

        try{
            String nTimbriQuery = "SELECT n_timbri FROM fidelty WHERE numero = '"+numeroFidelty+"'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(nTimbriQuery);
            while(resultSet.next()){
                nTimbri = resultSet.getInt("n_timbri");          
            }

            

            if(nTimbri<10){
                JOptionPane.showMessageDialog(null, "La tessera ha meno di 10 punti");
            }else{

                String query = "UPDATE fidelty SET n_timbri = ? WHERE numero = ?";
                PreparedStatement statement2 = conn.prepareStatement(query);
                statement2.setInt(1, nTimbri-10);
                statement2.setInt(2, numeroFidelty);
                statement2.executeUpdate();
                JOptionPane.showMessageDialog(null, "Saldo punti aggiornato");

                //il totale dell'ordine viene settato a 0
                String query2 = "UPDATE dettaglio_ordini SET totale = ? WHERE ID_ordine = ?";
                PreparedStatement statement3 = conn.prepareStatement(query2);
                statement3.setDouble(1, 0);
                statement3.setInt(2, getCurrentordine());
                statement3.executeUpdate();
            }
            

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
