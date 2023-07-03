package db.fastfood.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import db.fastfood.api.Util;
import db.fastfood.api.VenditaFidelty;

public class VenditaFideltyImpl implements VenditaFidelty {
    

    private final Connection conn;

    public VenditaFideltyImpl(Connection conn) {
        this.conn = conn;
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


    public void utilizza_fidelty(){
        Util util = new UtilImpl(conn);

        //VenditaFidelty vendita = new VenditaFideltyImpl(conn);

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
                statement3.setInt(2, util.getCurrentordine());
                statement3.executeUpdate();
            }
            

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
