package db.fastfood;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SchermataIniziale extends JFrame {

    public SchermataIniziale(Connection conn) {
        setTitle("Schermata Iniziale");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creazione dei pulsanti
        JButton btnVendita = new JButton("Avvia Schermata Vendita");
        JButton btnManager = new JButton("Avvia Schermata Manager");

        // Aggiunta dei pulsanti al contenitore
        Container container = getContentPane();
        container.setLayout(new GridLayout(2, 1));
        container.add(btnVendita);
        container.add(btnManager);

        // Aggiunta dei listener ai pulsanti
        btnVendita.addActionListener(e -> {
            // Avvia la schermata di vendita
            Schermatavendite2 vendita = new Schermatavendite2(conn);
            vendita.setVisible(true);

            //aggiunge un cliente alla tabella clienti

            try{
                int idcliente=generaprogressivo(conn);
                String query = "INSERT INTO cliente(ID) VALUES (?) ";

                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, idcliente);
                statement.executeUpdate();
                System.out.println("ID cliente: "+idcliente);


                //associo id cliente alla tabella ordine
                int idordine=generaprogressivoordine(conn);

                String query2 = "INSERT INTO ordine(ID, ID_cliente) VALUES (?, ?) ";
                PreparedStatement statement2 = conn.prepareStatement(query2);
                statement2.setInt(1, idordine);
                statement2.setInt(2, idcliente);
                statement2.executeUpdate();
                System.out.println("ID ordine: "+idordine);



            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }




        });

        btnManager.addActionListener(e -> {
            // Avvia la schermata del manager
            JFrame manager = new SchermataManager(conn);
            manager.setVisible(true);
        });

    }




    private int generaprogressivo (Connection conn) throws SQLException{
        String sql = "SELECT MAX(ID) FROM CLIENTE";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();

        int maxId = 0;
        if (rs.next()) {
            maxId = rs.getInt(1);
        }
        return maxId + 1;
    }

    private int generaprogressivoordine (Connection conn) throws SQLException{
        String sql = "SELECT MAX(ID) FROM ORDINE";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();

        int maxId = 0;
        if (rs.next()) {
            maxId = rs.getInt(1);
        }

        return maxId + 1;
    }
}



