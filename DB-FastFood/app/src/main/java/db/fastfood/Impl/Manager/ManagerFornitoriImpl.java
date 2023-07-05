package db.fastfood.Impl.Manager;

import java.sql.Connection;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.plaf.nimbus.State;

import db.fastfood.api.Manager.ManagerFornitori;

public class ManagerFornitoriImpl implements ManagerFornitori {

    private final Connection conn;

    public ManagerFornitoriImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisciFornitore() {
        String piva = JOptionPane.showInputDialog(null, "Inserisci la partita iva del fornitore", "Inserisci fornitore", JOptionPane.INFORMATION_MESSAGE);
        if(piva == null) return; // Se l'utente preme "Annulla" non viene inserito il fornitore
        String azienda = JOptionPane.showInputDialog(null, "Inserisci il nome dell'azienda", "Inserisci fornitore", JOptionPane.INFORMATION_MESSAGE);
        String cellulare = JOptionPane.showInputDialog(null, "Inserisci il numero di cellulare", "Inserisci fornitore", JOptionPane.INFORMATION_MESSAGE);
        String indirizzo = JOptionPane.showInputDialog(null, "Inserisci l'indirizzo", "Inserisci fornitore", JOptionPane.INFORMATION_MESSAGE);
        String descrizione = JOptionPane.showInputDialog(null, "Inserisci una descrizione", "Inserisci fornitore", JOptionPane.INFORMATION_MESSAGE);
        try{

            Statement stmt = conn.createStatement();
            String query = "INSERT INTO Fornitori VALUES ('" + piva + "', '" + azienda + "', '" + cellulare + "', '" + indirizzo + "', '" + descrizione + "')";
            stmt.executeUpdate(query);

            JOptionPane.showMessageDialog(null, "Fornitore inserito correttamente", "Inserisci fornitore", JOptionPane.INFORMATION_MESSAGE);
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Errore nell'inserimento del fornitore", "Inserisci fornitore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
