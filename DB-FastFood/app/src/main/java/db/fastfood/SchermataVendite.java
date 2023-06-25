package db.fastfood;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SchermataVendite extends JFrame {
    private JLabel lblNomeProdotto;
    private JTextField txtNomeProdotto;
    private JLabel lblQuantita;
    private JTextField txtQuantita;
    private JButton btnVendita;
    private Connection conn;
    public SchermataVendite(Connection conn) {
        this.conn=conn;

        // Imposta il titolo della finestra
        setTitle("Schermata Vendite");

        // Crea i componenti della finestra
        lblNomeProdotto = new JLabel("Nome Prodotto:");
        txtNomeProdotto = new JTextField(20);
        lblQuantita = new JLabel("Quantità:");
        txtQuantita = new JTextField(10);
        btnVendita = new JButton("Vendi");

        // Imposta il layout della finestra
        setLayout(new GridLayout(3, 2));

        // Aggiungi i componenti alla finestra
        add(lblNomeProdotto);
        add(txtNomeProdotto);
        add(lblQuantita);
        add(txtQuantita);
        add(new JLabel()); // Spazio vuoto per allineare il pulsante a destra
        add(btnVendita);

        // Aggiungi un listener per il pulsante di vendita
        btnVendita.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nomeProdotto = txtNomeProdotto.getText();
                int quantita = Integer.parseInt(txtQuantita.getText());

                // Esegui la vendita del prodotto con il nome e la quantità specificati
                vendiProdotto(nomeProdotto, quantita);
            }
        });

        // Imposta le dimensioni e la visibilità della finestra
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Metodo per eseguire la vendita del prodotto
    private void vendiProdotto(String nomeProdotto, int quantita) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
    try{
        //ottengo ingredienti del prodotto
        String query = "SELECT P.codice, I.ID, I.quantita, P.descrizione, I.nome_commerciale, PI.quantita_utilizzata  FROM PRODOTTI P JOIN INGREDIENTI_PRODOTTI PI ON P.codice = PI.codice_prodotto JOIN INGREDIENTI I ON PI.ID_ingrediente = I.ID where P.descrizione = ?";
        statement = conn.prepareStatement(query);
        statement.setString(1, nomeProdotto);
        resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int idIngrediente = resultSet.getInt("ID");
            int quantitaIngrediente = resultSet.getInt("quantita");
            int quantitaUtilizzata = resultSet.getInt("quantita_utilizzata");
            int idProdotto = resultSet.getInt("codice");

            // Calcolo della nuova quantità dell'ingrediente dopo la vendita
            int nuovaQuantita = quantitaIngrediente - quantita;

            // Aggiornamento della quantità dell'ingrediente nel database
            String updateQuery = "UPDATE Ingredienti SET Quantita = ? WHERE ID = ?";
            PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
            updateStatement.setInt(1, nuovaQuantita);
            updateStatement.setInt(2, idIngrediente);
            updateStatement.executeUpdate();

            //inserisco la vendita nel database
            String query2 = "INSERT INTO dettaglio_ordini (ID_ordine, codice_prodotto, quantita) VALUES (?,?,?)";
            statement = conn.prepareStatement(query2);
            int progressivo = generaprogressivo(conn);
            statement.setInt(1, progressivo);
            statement.setInt(2, idProdotto);
            statement.setInt(3, quantita);
            statement.executeUpdate();





        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
        System.out.println("Vendita: Nome Prodotto = " + nomeProdotto + ", Quantità = " + quantita);
    }

    private int generaprogressivo (Connection conn) throws SQLException{
        String sql = "SELECT MAX(ID) FROM ordine";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();

        int maxId = 0;
        if (rs.next()) {
            maxId = rs.getInt(1);
        }
        return maxId;
    }


}
