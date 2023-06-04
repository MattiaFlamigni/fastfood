package db.fastfood;

import javax.swing.*;
import java.sql.*;
import java.util.Map;

public class Schermatavendite2 extends JFrame {
    private Connection conn;
    private JPanel panel;

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

    private void vendita(String nomeprodotto){
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            //ottengo ingredienti del prodotto
            String query = "SELECT P.codice, I.ID, I.quantita, P.descrizione, I.nome_commerciale, PI.quantita_utilizzata  FROM PRODOTTI P JOIN INGREDIENTI_PRODOTTI PI ON P.codice = PI.codice_prodotto JOIN INGREDIENTI I ON PI.ID_ingrediente = I.ID where P.descrizione = ?";
            statement = conn.prepareStatement(query);
            statement.setString(1, nomeprodotto);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idIngrediente = resultSet.getInt("ID");
                double quantitaIngrediente = resultSet.getDouble("quantita");
                double quantitaUtilizzata = resultSet.getDouble("quantita_utilizzata");
                System.out.println(quantitaUtilizzata);
                int idProdotto = resultSet.getInt("codice");

                // Calcolo della nuova quantità dell'ingrediente dopo la vendita
                double nuovaQuantita = quantitaIngrediente - quantitaUtilizzata;
                System.out.println(nuovaQuantita);

                // Aggiornamento della quantità dell'ingrediente nel database
                String updateQuery = "UPDATE Ingredienti SET Quantita = ? WHERE ID = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                updateStatement.setDouble(1, nuovaQuantita);
                updateStatement.setInt(2, idIngrediente);
                updateStatement.executeUpdate();


                //inserisco la vendita nel database del cliente corrente

                int idcliente = getCurrentcliente();
                int idordine = getCurrentordine();
                String query2 = "INSERT INTO dettaglio_ordini (ID_ordine, codice_prodotto, quantita) VALUES (?,?,?)";
                statement = conn.prepareStatement(query2);
                statement.setInt(1, idordine);
                statement.setInt(2, idProdotto);
                statement.setInt(3, 1);
                statement.executeUpdate();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
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

            String query2 = "INSERT INTO ordine(ID, ID_cliente) VALUES (?, ?) ";
            PreparedStatement statement2 = conn.prepareStatement(query2);
            statement2.setInt(1, idordine);
            statement2.setInt(2, idcliente);
            statement2.executeUpdate();



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //aggiorno i correnti

        getCurrentcliente();
        getCurrentordine();

    }

}
