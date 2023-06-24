package db.fastfood;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Map;

public class Schermatavendite2 extends JFrame {
    private Connection conn;
    private JPanel panel;

    boolean flag = false;

    public Schermatavendite2(Connection conn) {
        this.conn = conn;
        panel = new JPanel();
        add(panel);
        createButtons();

        setTitle("Schermata Vendite");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(400, 150);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createButtons() {
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT descrizione FROM prodotti";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                String nomeprodotto = result.getString("descrizione");
                JButton button = new JButton(nomeprodotto);
                button.addActionListener(e -> {
                    //attribuisce al cliente corrente l'id del prodotto selezionato
                    vendita(nomeprodotto);
                });
                panel.add(button);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //in basso a destra un bottone "nuovo cliente"
        JButton button = new JButton("Nuovo Cliente");
        panel.add(button);
        button.addActionListener(e -> {
            nuovo_cliente();
        });

        JButton fidelty = new JButton("Assegna a fidelty");
        panel.add(fidelty);
        fidelty.addActionListener(e -> {
            assegna_fidelty();
        });

        //in basso a destra sotto il precedente bottone un bottone "inserisci offerta"
        JButton button2 = new JButton("Inserisci Offerta");
        panel.add(button2);
        button2.addActionListener(e -> {
            inserisci_offerta();
        });
    }

    //restituisce id cliente e id ordine
    /*private Map<Integer, Integer> current(){
        int idcliente=0;
        int idordine=0;
        Map<Integer, Integer> current = null;


        try{
            Statement statement = conn.createStatement();
            String query = "SELECT ID FROM cliente";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                idcliente = result.getInt("ID");
                System.out.println("ID cliente: "+idcliente);
            }
            String query2 = "SELECT ID FROM ordine";
            ResultSet result2 = statement.executeQuery(query2);
            while (result2.next()) {
                idordine = result2.getInt("ID");
                System.out.println("ID ordine: "+idordine);
            }
            current.put(idcliente, idordine);
        }catch (Exception e){
            e.printStackTrace();
        }
        return current;
    }*/

    private int getCurrentcliente(){
        int idcliente=0;
        try{
            Statement statement = conn.createStatement();
            String query = "SELECT ID FROM cliente";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                idcliente = result.getInt("ID");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return idcliente;
    }

    private int getCurrentordine(){
        int idordine=0;
        try{
            Statement statement = conn.createStatement();
            String query2 = "SELECT ID FROM ordine";
            ResultSet result2 = statement.executeQuery(query2);
            while (result2.next()) {
                idordine = result2.getInt("ID");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return idordine;
    }

    private void vendita0000(String nomeprodotto){
        PreparedStatement statement = null;
        ResultSet resultSet = null;


        try {

            int idcliente = getCurrentcliente();
            int idordine = getCurrentordine();
            //ottengo il codice del prodotto selezionato
            int idProdotto = 0;
            String query = "SELECT codice FROM prodotti WHERE descrizione = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, nomeprodotto);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                idProdotto = resultSet.getInt("codice");
            }


            //verifico se il cliente corrente ha già quel codice prodotto nel suo ordinativo
            String check = "SELECT codice_prodotto FROM dettaglio_ordini WHERE ID_ordine = ? AND codice_prodotto = ?";
            PreparedStatement statement22 = conn.prepareStatement(check);
            statement22.setInt(1, idordine);
            statement22.setInt(2, idProdotto);
            ResultSet result2 = statement22.executeQuery();



            while (result2.next()) {
                int codice = result2.getInt("codice_prodotto");
                if (codice == idProdotto) {
                    //se il cliente ha già quel prodotto nel suo ordine, aggiorno la quantità
                    String update = "UPDATE dettaglio_ordini SET quantita = quantita + 1 WHERE ID_ordine = ? AND codice_prodotto = ?";
                    PreparedStatement updateStatement = conn.prepareStatement(update);
                    updateStatement.setInt(1, idordine);
                    updateStatement.setInt(2, idProdotto);
                    updateStatement.executeUpdate();
                }
            }


            while (result2.next()) {
                int codice = result2.getInt("codice_prodotto");
                if (codice == idProdotto) {
                    //se il cliente ha già quel prodotto nel suo ordine, aggiorno la quantità
                    String update = "UPDATE dettaglio_ordini SET quantita = quantita + 1 WHERE ID_ordine = ? AND codice_prodotto = ?";
                    PreparedStatement updateStatement = conn.prepareStatement(update);
                    updateStatement.setInt(1, idordine);
                    updateStatement.setInt(2, idProdotto);
                    updateStatement.executeUpdate();

                    flag = true;
                }
            }


            if (flag==false) {



                //ottengo ingredienti del prodotto
                String query1 = "SELECT P.codice, I.ID, I.quantita, P.descrizione, I.nome_commerciale, PI.quantita_utilizzata  FROM PRODOTTI P JOIN INGREDIENTI_PRODOTTI PI ON P.codice = PI.codice_prodotto JOIN INGREDIENTI I ON PI.ID_ingrediente = I.ID where P.descrizione = ?";
                statement = conn.prepareStatement(query1);
                statement.setString(1, nomeprodotto);
                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int idIngrediente = resultSet.getInt("ID");
                    double quantitaIngrediente = resultSet.getDouble("quantita");
                    double quantitaUtilizzata = resultSet.getDouble("quantita_utilizzata");
                    //System.out.println(quantitaUtilizzata);
                    /*int*/
                    idProdotto = resultSet.getInt("codice");

                    // Calcolo della nuova quantità dell'ingrediente dopo la vendita
                    double nuovaQuantita = quantitaIngrediente - quantitaUtilizzata;
                    //System.out.println(nuovaQuantita);

                    // Aggiornamento della quantità dell'ingrediente nel database
                    String updateQuery = "UPDATE Ingredienti SET Quantita = ? WHERE ID = ?";
                    PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                    updateStatement.setDouble(1, nuovaQuantita);
                    updateStatement.setInt(2, idIngrediente);
                    updateStatement.executeUpdate();


                    //inserisco la vendita nel database del cliente corrente

                    /*int*/
                    idcliente = getCurrentcliente();
                    /*int*/
                    idordine = getCurrentordine();
                    String query2 = "INSERT INTO dettaglio_ordini (ID_ordine, codice_prodotto, quantita) VALUES (?,?,?)";
                    statement = conn.prepareStatement(query2);
                    statement.setInt(1, idordine);
                    statement.setInt(2, idProdotto);
                    statement.setInt(3, 1);
                    statement.executeUpdate();
                }
            }

            }catch(Exception e){
                e.printStackTrace();

        }
        flag=false;
    }






    private void vendita(String nomeprodotto) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean flag = false;

        try {
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
            //ottieni il prezzo del prodotto selezionato
            String queryprezzo = "SELECT prezzovendita FROM prodotti WHERE descrizione = ?";
            statement = conn.prepareStatement(queryprezzo);
            statement.setString(1, nomeprodotto);
            resultSet = statement.executeQuery();
            double prezzo = 0;
            while (resultSet.next()) {
                prezzo = resultSet.getDouble("prezzovendita");
            }


            // Verifica se il cliente corrente ha già quel codice prodotto nel suo ordinativo
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

                    //decrementa la quantita di ingredienti
                    String query1 = "SELECT P.codice, I.ID, I.quantita, P.descrizione, I.nome_commerciale, PI.quantita_utilizzata  FROM PRODOTTI P JOIN INGREDIENTI_PRODOTTI PI ON P.codice = PI.codice_prodotto JOIN INGREDIENTI I ON PI.ID_ingrediente = I.ID where P.descrizione = ?";
                    statement = conn.prepareStatement(query1);
                    statement.setString(1, nomeprodotto);
                    resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        String nomeIngrediente = resultSet.getString("nome_commerciale");
                        int idIngrediente = resultSet.getInt("ID");
                        double quantitaIngrediente = resultSet.getDouble("quantita");
                        double quantitaUtilizzata = resultSet.getDouble("quantita_utilizzata");
                        //System.out.println(quantitaUtilizzata);
                        /*int*/
                        idProdotto = resultSet.getInt("codice");

                        // Calcolo della nuova quantità dell'ingrediente dopo la vendita
                        double nuovaQuantita = quantitaIngrediente - quantitaUtilizzata;
                        //System.out.println(nuovaQuantita);

                        // Aggiornamento della quantità dell'ingrediente nel database
                        String updateQuery = "UPDATE Ingredienti SET Quantita = ? WHERE ID = ?";
                        PreparedStatement updateStatement2 = conn.prepareStatement(updateQuery);
                        updateStatement2.setDouble(1, nuovaQuantita);
                        updateStatement2.setInt(2, idIngrediente);
                        updateStatement2.executeUpdate();
                    }

                    flag = true;
                }
            }

            if (!flag) {
                // Il prodotto non esiste ancora nel dettaglio ordine, procedi con il resto delle operazioni

                // Ottieni ingredienti del prodotto 

                String query1 = "SELECT P.codice, I.ID, I.quantita, P.descrizione, I.nome_commerciale, PI.quantita_utilizzata  FROM PRODOTTI P JOIN INGREDIENTI_PRODOTTI PI ON P.codice = PI.codice_prodotto JOIN INGREDIENTI I ON PI.ID_ingrediente = I.ID where P.descrizione = ?";
                statement = conn.prepareStatement(query1);
                statement.setString(1, nomeprodotto);
                resultSet = statement.executeQuery();

                



                while (resultSet.next()) {
                    String nomeIngrediente = resultSet.getString("nome_commerciale");
                    int idIngrediente = resultSet.getInt("ID");
                    double quantitaIngrediente = resultSet.getDouble("quantita");
                    double quantitaUtilizzata = resultSet.getDouble("quantita_utilizzata"); //se il prodotto è stato inserito due volte, la quantità utilizzata è doppia
                    //System.out.println(quantitaUtilizzata);
                    idProdotto = resultSet.getInt("codice");

                    // Calcolo della nuova quantità dell'ingrediente dopo la vendita
                    double nuovaQuantita = quantitaIngrediente - quantitaUtilizzata;
                    System.out.println(nomeIngrediente + " " + quantitaIngrediente + " " + quantitaUtilizzata + " " + nuovaQuantita);

                    // Aggiornamento della quantità dell'ingrediente nel database
                    String updateQuery = "UPDATE Ingredienti SET Quantita = ? WHERE ID = ?";
                    PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                    updateStatement.setDouble(1, nuovaQuantita);
                    updateStatement.setInt(2, idIngrediente);
                    updateStatement.executeUpdate();


                }  

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










    public void nuovo_cliente() {
        try{
            int idcliente=getCurrentcliente();
            String query = "INSERT INTO cliente(ID) VALUES (?) ";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idcliente+1);
            statement.executeUpdate();



            //associo id cliente alla tabella ordine
            int idordine=getCurrentordine()+1;

            String query2 = "INSERT INTO ordine(ID, data, ID_cliente) VALUES (?, ?, ?) ";
            PreparedStatement statement2 = conn.prepareStatement(query2);
            statement2.setInt(1, idordine);
            statement2.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            statement2.setInt(3, idcliente);
            statement2.executeUpdate();



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //aggiorno i correnti

        getCurrentcliente();
        getCurrentordine();

    }

    public void inserisci_offerta(){
        try{
            int idcliente=getCurrentcliente();
            int idordine=getCurrentordine();
            //textbox che chiede il numero di offerta
            JDialog dialog = new JDialog();
            dialog.setAlwaysOnTop(true);
            String numero_offerta = JOptionPane.showInputDialog(dialog, "Inserisci il numero dell'offerta");
            int numero_offerta_int = Integer.parseInt(numero_offerta);
            /*String query = "INSERT INTO possedimento_offerta(ID_cliente, codice_offerta) VALUES (?, ?) ";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idcliente);
            statement.setInt(2, idordine);
            statement.executeUpdate();*/

            //recupera il valore dello sconto
            String query2 = "SELECT percentuale FROM offerta WHERE codice = ?";
            PreparedStatement statement2 = conn.prepareStatement(query2);
            statement2.setInt(numero_offerta_int, numero_offerta_int);
            ResultSet resultSet = statement2.executeQuery();
            int sconto = 0;
            while (resultSet.next()) {
                sconto = resultSet.getInt("percentuale");
            }

            //aggiorna il totale dell'ordine
            String query3 = "UPDATE dettaglio_ordini SET totale = totale - (totale * ? / 100) WHERE ID_ordine = ?";
            PreparedStatement statement3 = conn.prepareStatement(query3);
            statement3.setInt(1, sconto);
            statement3.setInt(2, idordine);
            statement3.executeUpdate();





        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //aggiorno i correnti

        getCurrentcliente();
        getCurrentordine();
    }



    private void assegna_fidelty(){
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
            if(numero_timbri >=10){
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

            }else{
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

}
