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
        JButton btnPrenotazioni = new JButton("Prenotazioni");

        // Creazione del layout
        Container container = getContentPane();
        container.setLayout(new GridLayout(5, 1)); // 5 righe per suddividere in sezioni

        // Aggiunta dei pulsanti alle sezioni principali
        JPanel prodottiPanel = createSectionPanel(btnProdotti);
        JPanel richiestePanel = createSectionPanel(btnRichieste);
        JPanel dipendentiPanel = createSectionPanel(btnDipendenti);
        JPanel altroPanel = createSectionPanel(btnAltro);
        JPanel ordiniFornitoriPanel = createSectionPanel(btnOrdiniFornitori);
        JPanel prenotazioni = createSectionPanel(btnPrenotazioni);

        container.add(prodottiPanel);
        container.add(richiestePanel);
        container.add(dipendentiPanel);
        container.add(altroPanel);
        container.add(ordiniFornitoriPanel);
        container.add(prenotazioni);

        // Aggiunta delle azioni ai pulsanti
        btnProdotti.addActionListener(e -> openProdottiSection());
        btnRichieste.addActionListener(e -> openRichiesteSection());
        btnDipendenti.addActionListener(e -> openDipendentiSection());
        btnAltro.addActionListener(e -> openAltroSection());
        btnOrdiniFornitori.addActionListener(e -> openOrdiniFornitoriSection());
        btnPrenotazioni.addActionListener(e -> openPrenotazioniSection());
    }

    private JPanel createSectionPanel(JButton button) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel(button.getText()), BorderLayout.NORTH);
        panel.add(button, BorderLayout.CENTER);
        return panel;
    }

    private void openProdottiSection() {
        // apre la schermata prodotti
        SchermataProdotti schermataProdotti = new SchermataProdotti(conn);
        schermataProdotti.setVisible(true);

    }

    private void openRichiesteSection() {
        SchermataRichieste schermataRichieste = new SchermataRichieste(conn);
        schermataRichieste.setVisible(true);
    }

    private void openDipendentiSection() {
        SchermataDipendenti schermataDipendenti = new SchermataDipendenti(conn);
        schermataDipendenti.setVisible(true);
    }

    private void openAltroSection() {
        SchermataAltro schermataAltro = new SchermataAltro(conn);
        schermataAltro.setVisible(true);
    }

    private void openOrdiniFornitoriSection() {
        SchermataOrdiniFornitori schermataOrdiniFornitori = new SchermataOrdiniFornitori(conn);
        schermataOrdiniFornitori.setVisible(true);
    }

    private void openPrenotazioniSection() {
        SchermataPrenotazioni schermataPrenotazioni = new SchermataPrenotazioni(conn);
        schermataPrenotazioni.setVisible(true);
    }
}
