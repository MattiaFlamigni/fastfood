package db.fastfood.Impl.Manager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
        String piva = JOptionPane.showInputDialog(null, "Inserisci la partita iva del fornitore", "Inserisci fornitore",
                JOptionPane.INFORMATION_MESSAGE);
        if (piva == null)
            return;
        String azienda = JOptionPane.showInputDialog(null, "Inserisci il nome dell'azienda", "Inserisci fornitore",
                JOptionPane.INFORMATION_MESSAGE);
        if (azienda == null)
            return;
        String cellulare = JOptionPane.showInputDialog(null, "Inserisci il numero di cellulare", "Inserisci fornitore",
                JOptionPane.INFORMATION_MESSAGE);
        if (cellulare == null)
            return;
        String indirizzo = JOptionPane.showInputDialog(null, "Inserisci l'indirizzo", "Inserisci fornitore",
                JOptionPane.INFORMATION_MESSAGE);
        if (indirizzo == null)
            return;
        String descrizione = JOptionPane.showInputDialog(null, "Inserisci una descrizione", "Inserisci fornitore",
                JOptionPane.INFORMATION_MESSAGE);
        try {

            Statement stmt = conn.createStatement();
            String query = "INSERT INTO Fornitori VALUES ('" + piva + "', '" + azienda + "', '" + cellulare + "', '"
                    + indirizzo + "', '" + descrizione + "')";
            stmt.executeUpdate(query);

            JOptionPane.showMessageDialog(null, "Fornitore inserito correttamente", "Inserisci fornitore",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Errore nell'inserimento del fornitore", "Inserisci fornitore",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public void visualizzaFornitori() {
        // visualizzo in una nuova schermata la tabella dei fornitori
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM Fornitori";
            ResultSet rs = stmt.executeQuery(query);
            JTable table = new JTable(customtable);
            customtable.doGraphic(table);
            // customtable.notEditable(table);
            customtable.addColumn("Partita IVA");
            customtable.addColumn("Azienda");
            customtable.addColumn("Cellulare");
            customtable.addColumn("Indirizzo");
            customtable.addColumn("Descrizione");
            while (rs.next()) {
                customtable.addRow(new Object[] { rs.getString("piva"), rs.getString("azienda"),
                        rs.getString("cellulare"), rs.getString("indirizzo"), rs.getString("descrizione") });
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
                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Seleziona un fornitore", "Elimina fornitore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String piva = (String) table.getValueAt(row, 0);
                    try {
                        Statement stmt = conn.createStatement();
                        String query = "DELETE FROM Fornitori WHERE piva = '" + piva + "'";
                        stmt.executeUpdate(query);
                        customtable.removeRow(row);
                        JOptionPane.showMessageDialog(null, "Fornitore eliminato correttamente", "Elimina fornitore",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Errore nell'eliminazione del fornitore",
                                "Elimina fornitore", JOptionPane.ERROR_MESSAGE);
                    }
                }

            });

            JButton modificaButton = new JButton("Modifica");
            modificaButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    int row = table.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(null, "Seleziona un fornitore", "Modifica fornitore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String piva = (String) table.getValueAt(row, 0);
                    String azienda = (String) table.getValueAt(row, 1);
                    String cellulare = (String) table.getValueAt(row, 2);
                    String indirizzo = (String) table.getValueAt(row, 3);
                    String descrizione = (String) table.getValueAt(row, 4);

                    try {
                        Statement stmt = conn.createStatement();
                        String query = "UPDATE Fornitori SET azienda = '" + azienda + "', cellulare = '" + cellulare
                                + "', indirizzo = '" + indirizzo + "', descrizione = '" + descrizione
                                + "' WHERE piva = '" + piva + "'";
                        stmt.executeUpdate(query);
                        JOptionPane.showMessageDialog(null, "Fornitore modificato correttamente", "Modifica fornitore",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Errore nella modifica del fornitore", "Modifica fornitore",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(eliminaButton);
            buttonPanel.add(modificaButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Errore nel caricamento dei fornitori", "Visualizza fornitori",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void makeOrder() {
        // double totale=0;
        String piva = "";
        Map<String, Integer> ingredientiquantita = new HashMap<String, Integer>();

        /* autenticazione manager */
        String username = JOptionPane.showInputDialog(null, "Inserisci il tuo username", "Inserisci fornitura",
                JOptionPane.INFORMATION_MESSAGE);
        if (username == null)
            return;
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM Manager WHERE CF = '" + username + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "Username non valido", "Inserisci fornitura",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Errore nell'autenticazione", "Inserisci fornitura",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // chiede all'utente di selezionare un fornitore tramite una combobox
        String[] fornitori = new String[0];
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT COUNT(*) FROM Fornitori";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            fornitori = new String[rs.getInt(1)];
            query = "SELECT azienda, Piva FROM Fornitori";
            rs = stmt.executeQuery(query);
            int i = 0;
            while (rs.next()) {
                piva = rs.getString("Piva");
                fornitori[i] = rs.getString("azienda");

                i++;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Errore nel caricamento dei fornitori", "Inserisci fornitura",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fornitore = (String) JOptionPane.showInputDialog(null, "Seleziona il fornitore", "Inserisci fornitura",
                JOptionPane.INFORMATION_MESSAGE, null, fornitori, fornitori[0]);
        if (fornitore == null)
            return;

        // si apre una nuova schermata con la tabella degli ngredienti. L'utente
        // seleziona gli ingredienti e inserisce la quantità

        // creo la tabella.
        JTable table = new JTable(customtable);
        customtable.doGraphic(table);
        customtable.addColumn("Ingrediente");
        customtable.addColumn("Quantità");

        // aggiungo i dati alla tabella
        try {
            String query = "SELECT * FROM Ingredienti";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                customtable.addRow(new Object[] { rs.getString("nome_commerciale"), 0 });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Errore nel caricamento degli ingredienti", "Inserisci fornitura",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // creo la schermata
        JFrame frame = new JFrame("Inserisci fornitura");
        frame.add(new JScrollPane(table));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(800, 600);

        JButton inserisciButton = new JButton("Inserisci");
        inserisciButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                double totale = 0;
                // per ogni riga della tabella seleziono il prodotto e la quantita
                int rows = table.getRowCount();
                for (int i = 0; i < rows; i++) {
                    // prendo il prodotto e la quantità
                    String ingrediente = (String) table.getValueAt(i, 0);
                    int quantita = Integer.parseInt(table.getValueAt(i, 1).toString());

                    ingredientiquantita.put(ingrediente, quantita);

                    // calcolo il prezzo del prodotto
                    try {
                        String query = "SELECT prezzoUnitario FROM Ingredienti WHERE nome_commerciale = '" + ingrediente
                                + "'";
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (!rs.next()) {
                            JOptionPane.showMessageDialog(null, "Errore nel calcolo del prezzo", "Inserisci fornitura",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        double prezzo = rs.getFloat("prezzoUnitario");
                        totale = totale + prezzo * quantita;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Errore nel calcolo del prezzo", "Inserisci fornitura",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // ottengo il massimo id e lo incremento di 1
                int id;
                try {
                    String query = "SELECT MAX(ID) FROM ordineapprovigionamento";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(null, "Errore generico", "Inserisci fornitura",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    id = rs.getInt("MAX(ID)");
                    id++;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Errore nel calcolo del prezzo", "Inserisci fornitura",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                System.out.println(ingredientiquantita);

                // inserisco la fornitura
                try {
                    String query = "INSERT INTO ordineapprovigionamento (ID, prezzo, data, CF_manager) VALUES ('" + id
                            + "', '" + totale + "', '"
                            + java.time.LocalDate.now()
                                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                            + "', '" + username + "')";

                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(query);
                    JOptionPane.showMessageDialog(null, "Fornitura inserita correttamente", "Inserisci fornitura",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Errore nell'inserimento della fornitura",
                            "Inserisci fornitura", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                System.out.println("DEVE" + ingredientiquantita);
                // incremento la quantita degli ingredienti
                try {
                    for (String ingrediente : ingredientiquantita.keySet()) {
                        String query = "UPDATE Ingredienti SET quantita = quantita + "
                                + ingredientiquantita.get(ingrediente) + " WHERE nome_commerciale = '" + ingrediente
                                + "'";
                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate(query);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Errore nell'inserimento della fornitura",
                            "Inserisci fornitura", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(inserisciButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

    }

}
