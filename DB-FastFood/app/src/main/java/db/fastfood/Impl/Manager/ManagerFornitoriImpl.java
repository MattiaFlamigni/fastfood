package db.fastfood.Impl.Manager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.plaf.nimbus.State;
import javax.swing.table.DefaultTableModel;

import db.fastfood.api.Manager.ManagerFornitori;
import db.fastfood.util.CustomTable;

public class ManagerFornitoriImpl implements ManagerFornitori {

    private final Connection conn;
    private final CustomTable customtable = new CustomTable();

    public ManagerFornitoriImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserisciFornitore() {
        String piva = JOptionPane.showInputDialog(null, "Inserisci la partita iva del fornitore", "Inserisci fornitore", JOptionPane.INFORMATION_MESSAGE);
        if(piva == null) return; 
        String azienda = JOptionPane.showInputDialog(null, "Inserisci il nome dell'azienda", "Inserisci fornitore", JOptionPane.INFORMATION_MESSAGE);
        if(azienda == null) return;
        String cellulare = JOptionPane.showInputDialog(null, "Inserisci il numero di cellulare", "Inserisci fornitore", JOptionPane.INFORMATION_MESSAGE);
        if (cellulare == null) return;
        String indirizzo = JOptionPane.showInputDialog(null, "Inserisci l'indirizzo", "Inserisci fornitore", JOptionPane.INFORMATION_MESSAGE);
        if(indirizzo == null) return;
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

    @Override
    public void visualizzaFornitori() {
        //visualizzo in una nuova schermata la tabella dei fornitori
        try{
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM Fornitori";
            ResultSet rs = stmt.executeQuery(query);
            JTable table = new JTable(customtable);
            customtable.doGraphic(table);
            //customtable.notEditable(table);
            customtable.addColumn("Partita IVA");
            customtable.addColumn("Azienda");
            customtable.addColumn("Cellulare");
            customtable.addColumn("Indirizzo");
            customtable.addColumn("Descrizione");
            while(rs.next()){
                customtable.addRow(new Object[]{rs.getString("piva"), rs.getString("azienda"), rs.getString("cellulare"), rs.getString("indirizzo"), rs.getString("descrizione")});
            }
            JFrame frame = new JFrame("Fornitori");
            frame.add(new JScrollPane(table));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setSize(800, 600);

            JButton eliminaButton = new JButton("Elimina");

            eliminaButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    int row = table.getSelectedRow();
                    if(row == -1){
                        JOptionPane.showMessageDialog(null, "Seleziona un fornitore", "Elimina fornitore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String piva = (String) table.getValueAt(row, 0);
                    try{
                        Statement stmt = conn.createStatement();
                        String query = "DELETE FROM Fornitori WHERE piva = '" + piva + "'";
                        stmt.executeUpdate(query);
                        customtable.removeRow(row);
                        JOptionPane.showMessageDialog(null, "Fornitore eliminato correttamente", "Elimina fornitore", JOptionPane.INFORMATION_MESSAGE);
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null, "Errore nell'eliminazione del fornitore", "Elimina fornitore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
            });


            JButton modificaButton = new JButton("Modifica");
            modificaButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    int row = table.getSelectedRow();
                    if(row == -1){
                        JOptionPane.showMessageDialog(null, "Seleziona un fornitore", "Modifica fornitore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String piva = (String) table.getValueAt(row, 0);
                    String azienda = (String) table.getValueAt(row, 1);
                    String cellulare = (String) table.getValueAt(row, 2);
                    String indirizzo = (String) table.getValueAt(row, 3);
                    String descrizione = (String) table.getValueAt(row, 4);

                    try{
                        Statement stmt = conn.createStatement();
                        String query = "UPDATE Fornitori SET azienda = '" + azienda + "', cellulare = '" + cellulare + "', indirizzo = '" + indirizzo + "', descrizione = '" + descrizione + "' WHERE piva = '" + piva + "'";
                        stmt.executeUpdate(query);
                        JOptionPane.showMessageDialog(null, "Fornitore modificato correttamente", "Modifica fornitore", JOptionPane.INFORMATION_MESSAGE);
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null, "Errore nella modifica del fornitore", "Modifica fornitore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(eliminaButton);
            buttonPanel.add(modificaButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);
            

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Errore nel caricamento dei fornitori", "Visualizza fornitori", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
