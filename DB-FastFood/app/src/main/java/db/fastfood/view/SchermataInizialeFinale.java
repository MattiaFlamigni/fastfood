package db.fastfood.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class SchermataInizialeFinale extends JFrame {
    private final Connection conn;

    public SchermataInizialeFinale(Connection conn) {
        this.conn = conn;
        setTitle("Schermata Manager");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creazione dei pulsanti per le sezioni principali
        JButton btnProdotti = new JButton("Prodotti");
        JButton btnRichieste = new JButton("Richieste");
        JButton btnDipendenti = new JButton("Dipendenti");
        JButton btnAltro = new JButton("Altro");
        JButton btnOrdiniFornitori = new JButton("Ordini ai Fornitori");

        // Creazione del layout
        Container container = getContentPane();
        container.setLayout(new GridLayout(5, 1)); // 5 righe per suddividere in sezioni

        // Aggiunta dei pulsanti alle sezioni principali
        JPanel prodottiPanel = createSectionPanel(btnProdotti);
        JPanel richiestePanel = createSectionPanel(btnRichieste);
        JPanel dipendentiPanel = createSectionPanel(btnDipendenti);
        JPanel altroPanel = createSectionPanel(btnAltro);
        JPanel ordiniFornitoriPanel = createSectionPanel(btnOrdiniFornitori);

        container.add(prodottiPanel);
        container.add(richiestePanel);
        container.add(dipendentiPanel);
        container.add(altroPanel);
        container.add(ordiniFornitoriPanel);

        // Aggiunta delle azioni ai pulsanti
        btnProdotti.addActionListener(e -> openProdottiSection());
        btnRichieste.addActionListener(e -> openRichiesteSection());
        btnDipendenti.addActionListener(e -> openDipendentiSection());
        btnAltro.addActionListener(e -> openAltroSection());
        btnOrdiniFornitori.addActionListener(e -> openOrdiniFornitoriSection());
    }

    // Metodo di utilità per creare un pannello per una sezione con il titolo e il pulsante
    private JPanel createSectionPanel(JButton button) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel(button.getText()), BorderLayout.NORTH);
        panel.add(button, BorderLayout.CENTER);
        return panel;
    }

    // Metodi per aprire le sezioni corrispondenti
    private void openProdottiSection() {
        //apre la schermata prodotti
        SchermataProdotti schermataProdotti = new SchermataProdotti(conn);
        schermataProdotti.setVisible(true);

    }

    private void openRichiesteSection() {
        SchermataRichieste schermataRichieste = new SchermataRichieste(conn);
        schermataRichieste.setVisible(true);
    }

    private void openDipendentiSection() {
        // Aggiungi qui la logica per aprire la sezione "Dipendenti"
    }

    private void openAltroSection() {
        // Aggiungi qui la logica per aprire la sezione "Altro"
    }

    private void openOrdiniFornitoriSection() {
        // Aggiungi qui la logica per aprire la sezione "Ordini ai Fornitori"
    }
}
