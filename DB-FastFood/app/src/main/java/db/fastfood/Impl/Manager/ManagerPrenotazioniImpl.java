package db.fastfood.Impl.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;

import db.fastfood.api.Manager.ManagerPrenotazioni;

public class ManagerPrenotazioniImpl implements ManagerPrenotazioni {

    private Connection connection;

    public ManagerPrenotazioniImpl(Connection connection) {
        this.connection = connection;
    }
    

    @Override
    public void inserisciTavolo() {
        try{
            String numero = JOptionPane.showInputDialog("Inserisci numero tavolo");
            String nposti = JOptionPane.showInputDialog("Inserisci numero posti");

            String query = "INSERT INTO tavolo (numero, nposti) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(numero));
            statement.setInt(2, Integer.parseInt(nposti));
            statement.executeUpdate();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visualizzaTavoli() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visualizzaTavoli'");
    }

    @Override
    public void visualizzaPrenotazioni() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visualizzaPrenotazioni'");
    }

    @Override
    public void inserisciPrenotazione() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'inserisciPrenotazione'");
    }
    
}
