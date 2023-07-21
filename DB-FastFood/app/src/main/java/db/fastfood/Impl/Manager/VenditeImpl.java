package db.fastfood.Impl.Manager;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import db.fastfood.api.Manager.Vendite;
import db.fastfood.util.CustomTable;

public class VenditeImpl implements Vendite {
    private final Connection conn;
    private final CustomTable customizeTable;
    DecimalFormat df = new DecimalFormat("#.##");

    public VenditeImpl(Connection conn) {
        this.conn = conn;
        customizeTable = new CustomTable();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void monthlyEarned() {
        try {
            Statement statement = conn.createStatement();
            String query = """
                    SELECT SUM(P.prezzovendita * D.quantita) as TotIncassoLordo, SUM(P.prezzounitario*D.quantita) as MateriePrime, SUM(P.prezzovendita * D.quantita)-SUM(P.prezzounitario*D.quantita) as TotNetto
                    FROM prodotti P, ordine O, dettaglio_ordini D
                    WHERE D.ID_ordine = O.ID and D.codice_prodotto = P.codice
                    AND MONTH(O.data) = MONTH(CURRENT_DATE());""";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            double totaleIncassoLordo = resultSet.getInt("TotIncassoLordo");
            double totaleMateriePrime = resultSet.getInt("MateriePrime");
            double totaleNetto = resultSet.getInt("TotNetto");
            resultSet.close();
            statement.close();
            JOptionPane
                    .showMessageDialog(null,
                            "Fatturato Lordo: " + totaleIncassoLordo + "\nMaterie prime: " + totaleMateriePrime
                                    + "\nFatturato netto: " + totaleNetto,
                            "Fatturato", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDailySales() {
        try {
            Statement statement = conn.createStatement();
            String query = """
                    select p.descrizione, p.prezzovendita, SUM(d.quantita) as venduto
                    from prodotti p, ordine o, dettaglio_ordini d
                    where 	d.ID_ordine = o.ID and d.codice_prodotto = p.codice and
                      o.data = current_date() and o.data is not null
                    group by p.descrizione, p.prezzovendita;""";

            ResultSet resultSet = statement.executeQuery(query);
            String[] columnNames = { "descrizione", "prezzovendita", "venduto" };
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                String nome = resultSet.getString("descrizione");
                // prodotti.add(nome);
                double prezzo = resultSet.getDouble("prezzovendita");
                int venduto = resultSet.getInt("venduto");
                Object[] row = { nome, prezzo, venduto, /* data */ };

                tableModel.addRow(row);
            }

            // aggiunge alla tabella il fatturato totale
            query = """
                    select SUM(p.prezzovendita * d.quantita) as fatturato
                    from prodotti p, ordine o, dettaglio_ordini d
                    where 	d.ID_ordine = o.ID and d.codice_prodotto = p.codice and
                            o.data = current_date() and o.data is not null;""";
            resultSet = statement.executeQuery(query);
            resultSet.next();
            double fatturato = resultSet.getDouble("fatturato");
            Object[] row2 = { "Fatturato totale", fatturato };
            tableModel.addRow(row2);

            resultSet.close();
            statement.close();

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            JFrame frame = new JFrame("Vendite giornaliere");

            customizeTable.notEditable(table);
            customizeTable.doGraphic(table);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setSize(800, 600);
            // evidenzia l'ultimo elemento della tabella
            table.changeSelection(table.getRowCount() - 1, 0, false, false);

            frame.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void averageReceipt() {
        try {
            Statement statement = conn.createStatement();
            String query = """
                    select extract(month from data) as mese, avg(totale) as scontrino_medio
                    from dettaglio_ordini, ordine
                    where dettaglio_ordini.ID_ordine = ordine.ID and ordine.data is not null
                    group by mese
                    """;
            ResultSet resultSet = statement.executeQuery(query);
            String[] columnNames = { "mese", "scontrino_medio" };

            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                int mese = resultSet.getInt("mese");
                String scontrino_medio = df.format(resultSet.getFloat("scontrino_medio"));
                Object[] row = { mese, scontrino_medio };
                tableModel.addRow(row);
            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            JFrame frame = new JFrame("Scontrino medio");

            customizeTable.notEditable(table);
            customizeTable.doGraphic(table);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setSize(400, 300);
            frame.setVisible(true);

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showReceiptByDate() {
        JFrame frame = new JFrame("Visualizza scontrini per data");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(3, 2));

        JLabel dataInizioLabel = new JLabel("Data inizio (aaaa-mm-gg): ");
        dataInizioLabel.setSize(100, 100);
        JTextField dataInizio = new JTextField();
        JLabel dataFineLabel = new JLabel("Data fine (aaaa-mm-gg): ");
        JTextField dataFine = new JTextField();

        JButton visualizza = new JButton("Visualizza");
        // posizione sotto la tabella
        visualizza.setBounds(100, 100, 140, 40);

        visualizza.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (dataInizio.getText().equals("") || dataFine.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Inserire entrambe le date.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Statement statement = conn.createStatement();
                    String query = """
                            select ID, data, totale
                            from ordine o, dettaglio_ordini d
                            where o.ID = d.ID_ordine and data between ? and ?
                            """;
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setDate(1, java.sql.Date.valueOf(dataInizio.getText()));
                    preparedStatement.setDate(2, java.sql.Date.valueOf(dataFine.getText()));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    String[] columnNames = { "ID", "data", "totale" };

                    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

                    while (resultSet.next()) {
                        int ID = resultSet.getInt("ID");
                        String data = resultSet.getString("data");
                        String totale = df.format(resultSet.getFloat("totale"));
                        Object[] row = { ID, data, totale };
                        tableModel.addRow(row);
                    }

                    JTable table = new JTable(tableModel);
                    JScrollPane scrollPane = new JScrollPane(table);
                    JFrame frame = new JFrame("Scontrini");

                    customizeTable.notEditable(table);
                    customizeTable.doGraphic(table);

                    frame.add(scrollPane, BorderLayout.CENTER);
                    frame.setSize(400, 300);
                    frame.setVisible(true);

                    resultSet.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(dataInizioLabel);
        frame.add(dataInizio);
        frame.add(dataFineLabel);

        frame.add(dataFine);
        frame.add(visualizza);

        frame.setVisible(true);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void report() {

        JFrame frame = new JFrame("Report");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(3, 2));

        JLabel meseLabel = new JLabel("Mese (1-12): ");
        meseLabel.setSize(100, 100);
        JTextField mese = new JTextField();
        JLabel annoLabel = new JLabel("Anno (aaaa): ");
        JTextField anno = new JTextField();
        JButton visualizza = new JButton("Visualizza");
        // posizione sotto la tabella
        visualizza.setBounds(100, 100, 140, 40);
        frame.add(meseLabel);
        frame.add(mese);
        frame.add(annoLabel);
        frame.add(anno);
        frame.add(visualizza);
        frame.setVisible(true);

        visualizza.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // visualizza tabella con tot vendite, tot scarti, tot materie prime
                if (mese.getText().equals("") || anno.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Inserire entrambi i campi.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // ottengo gli scarti totali
                    Statement statement = conn.createStatement();
                    String query = """
                            select SUM(s.quantita * p.prezzounitario)
                            from scarti_giornalieri s, prodotti p
                            where s.codice_prodotto = p.codice and MONTH(s.data)=? and YEAR(s.data)=?;
                                """;

                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(mese.getText()));
                    preparedStatement.setInt(2, Integer.parseInt(anno.getText()));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    String totScarti = df.format(resultSet.getFloat(1));
                    resultSet.close();

                    // ottengo il totale delle vendite
                    query = """
                                select SUM(d.quantita * p.prezzovendita)
                                from dettaglio_ordini d, prodotti p, ordine o
                                where d.codice_prodotto = p.codice and d.ID_ordine = o.ID and MONTH(o.data)=? and YEAR(o.data)=?;
                            """;

                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(mese.getText()));
                    preparedStatement.setInt(2, Integer.parseInt(anno.getText()));
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    String totVendite = df.format(resultSet.getFloat(1));
                    resultSet.close();

                    // ottengo il totale delle materie prime
                    query = """
                            SELECT SUM(P.prezzounitario*D.quantita) as MateriePrime
                            FROM prodotti P, dettaglio_ordini D, ordine O
                            WHERE D.codice_prodotto = P.codice and D.ID_ordine = O.ID
                            AND MONTH(O.data) = ? AND YEAR(O.data) = ?;""";

                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(mese.getText()));
                    preparedStatement.setInt(2, Integer.parseInt(anno.getText()));
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    String totMateriePrime = df.format(resultSet.getFloat(1));
                    resultSet.close();

                    // ottengo il totale netto
                    float totaleNetto = Float.parseFloat(totVendite) - Float.parseFloat(totScarti)
                            - Float.parseFloat(totMateriePrime);

                    // costruisco la tabella
                    String[] columnNames = { "Totale vendite", "Totale scarti", "Totale materie prime",
                            "Totale netto" };
                    Object[][] data = { { totVendite, totScarti, totMateriePrime, totaleNetto } };
                    JTable table = new JTable(data, columnNames);
                    customizeTable.doGraphic(table);
                    JScrollPane scrollPane = new JScrollPane(table);
                    JFrame frame = new JFrame("Report");
                    frame.add(scrollPane, BorderLayout.CENTER);
                    frame.setSize(800, 400);
                    frame.setVisible(true);

                    resultSet.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                            JOptionPane.ERROR_MESSAGE);

                }
            }

        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportDelivery() {

        JFrame frame = new JFrame("Report consegne");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(3, 2));

        JLabel dataInizioLabel = new JLabel("Data inizio (aaaa-mm-gg): ");
        dataInizioLabel.setSize(100, 100);
        JTextField dataInizio = new JTextField();
        JLabel dataFineLabel = new JLabel("Data fine (aaaa-mm-gg): ");
        JTextField dataFine = new JTextField();

        JButton visualizza = new JButton("Visualizza");
        // posizione sotto la tabella
        visualizza.setBounds(100, 100, 140, 40);

        visualizza.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                if (dataInizio.getText().equals("") || dataFine.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Inserire entrambi i campi.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // ottengo il totale delle consegne di glovo
                    Statement statement = conn.createStatement();
                    String query = """
                            select SUM(c.quantita * p.prezzovendita)
                            from dettaglio_consegna c, prodotti p
                            where c.codice_prodotto = p.codice and c.data between ? and ? and c.nomeApp = 'Glovo';
                                """;

                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, dataInizio.getText());
                    preparedStatement.setString(2, dataFine.getText());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    double totGlovo = resultSet.getDouble(1);
                    resultSet.close();

                    // ottengo il totale delle consegne di deliveroo
                    query = """
                                select SUM(c.quantita * p.prezzovendita)
                                from dettaglio_consegna c, prodotti p
                                where c.codice_prodotto = p.codice and c.data between ? and ? and c.nomeApp = 'Deliveroo';
                            """;

                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, dataInizio.getText());
                    preparedStatement.setString(2, dataFine.getText());
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    double totDeliveroo = resultSet.getDouble(1);
                    resultSet.close();

                    // ottengo il totale delle consegne di justeat

                    query = """
                                select SUM(c.quantita * p.prezzovendita)
                                from dettaglio_consegna c, prodotti p
                                where c.codice_prodotto = p.codice and c.data between ? and ? and c.nomeApp = 'JustEat';
                            """;

                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, dataInizio.getText());
                    preparedStatement.setString(2, dataFine.getText());

                    resultSet = preparedStatement.executeQuery();

                    resultSet.next();
                    Double totJustEat = resultSet.getDouble(1);

                    resultSet.close();

                    // costruisco la tabella
                    String[] columnNames = { "Totale Glovo", "Totale Netto Glovo", "Totale Deliveroo",
                            "Totale Netto Deliveroo", "Totale JustEat", "Totale Netto JustEat" };
                    Object[][] data = { { totGlovo, totGlovo - (totGlovo * 20 / 100), totDeliveroo,
                            totDeliveroo - (totDeliveroo * 20 / 100), totJustEat,
                            totJustEat - (totJustEat * 20 / 100) } };
                    JTable table = new JTable(data, columnNames);
                    customizeTable.doGraphic(table);
                    customizeTable.notEditable(table);

                    JScrollPane scrollPane = new JScrollPane(table);
                    JFrame frame = new JFrame("Report");
                    frame.add(scrollPane, BorderLayout.CENTER);
                    frame.setSize(800, 400);
                    frame.setVisible(true);

                    resultSet.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore durante l'esecuzione della query.", "Errore",
                            JOptionPane.ERROR_MESSAGE);

                }

            }

        });

        frame.add(dataInizioLabel);
        frame.add(dataInizio);
        frame.add(dataFineLabel);
        frame.add(dataFine);
        frame.add(visualizza);
        frame.setVisible(true);

    }
}
