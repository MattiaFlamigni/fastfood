package db.fastfood.view;

import javax.swing.*;


import java.awt.*;
import java.sql.*;

public class ViewSchermataManager extends JFrame {
    @SuppressWarnings("unused")
    private final Connection conn;

    public ViewSchermataManager(Connection conn) {
        this.conn = conn;
        setTitle("Schermata Manager");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creazione dei pulsanti per la sezione "Prodotti"
        JButton btnVisualizzaProdotti = new JButton("Visualizza Prodotti Disponibili");
        JButton btnAggiungiProdotto = new JButton("Aggiungi Prodotto");
        JButton btnAggiungiIngredienti = new JButton("Aggiungi Ingredienti a Prodotto");
        JButton btnAggiungiIngrediente = new JButton("Aggiungi Ingrediente");
        JButton btnVisualizzaIngredienti = new JButton("Visualizza Ingredienti di un Prodotto");
        JButton btnMagazzino = new JButton("Situazione magazzino");

        // Creazione dei pulsanti per la sezione "Richieste"
        JButton btnInserisciRichiesta = new JButton("Nuova richiesta");
        JButton btnVisualizzaRifiuta = new JButton("Visualizza/rifiuta richieste");

        // Creazione dei pulsanti per la sezione "Dipendenti"
        JButton btnInserisciAddetto = new JButton("Inserisci Dipendente");
        JButton btnVisualizzaAddetti = new JButton("Visualizza Dipendenti");
        JButton btnContratti = new JButton("Crea contratto");
        JButton btnVisualizzaContratti = new JButton("Visualizza contratti");
        JButton btnRicercaContratto = new JButton("Ricerca contratto");

        //creazione dei pulsanti per la sezione "ordini ai fornitori"
        JButton btnInserisciFornitore = new JButton("Inserisci Fornitore");
        JButton btnVisualizzaFornitori = new JButton("Visualizza Fornitori");
        JButton btnInserisciOrdine = new JButton("Inserisci Ordine");

        // Creazione del pulsante per la sezione "Altro"
        JButton btnFatturatoMensile = new JButton("Visualizza Fatturato Mensile");
        JButton btnCreaFidelity = new JButton("Crea Fidelity");
        JButton btnVenditeGiornaliere = new JButton("Visualizza Vendite Giornaliere");

        // Creazione del layout
        Container container = getContentPane();
        container.setLayout(new GridLayout(4, 0)); // 4 righe per suddividere in sezioni

        // Aggiunta dei pulsanti alla sezione "Prodotti"
        JPanel prodottiPanel = new JPanel();
        prodottiPanel.setLayout(new FlowLayout());
        prodottiPanel.add(btnVisualizzaProdotti);
        prodottiPanel.add(btnAggiungiProdotto);
        prodottiPanel.add(btnAggiungiIngredienti);
        prodottiPanel.add(btnAggiungiIngrediente);
        prodottiPanel.add(btnVisualizzaIngredienti);
        prodottiPanel.add(btnMagazzino);
        container.add(prodottiPanel);

        // Aggiunta dei pulsanti alla sezione "Richieste"
        JPanel richiestePanel = new JPanel();

        richiestePanel.setLayout(new FlowLayout());
        richiestePanel.add(btnInserisciRichiesta);
        richiestePanel.add(btnVisualizzaRifiuta);
        // richiestePanel.add(richiesteTitle);
        container.add(richiestePanel);

        // Aggiunta dei pulsanti alla sezione "Dipendenti"
        JPanel dipendentiPanel = new JPanel();

        dipendentiPanel.setLayout(new FlowLayout());
        dipendentiPanel.add(btnInserisciAddetto);
        dipendentiPanel.add(btnVisualizzaAddetti);
        dipendentiPanel.add(btnContratti);
        dipendentiPanel.add(btnVisualizzaContratti);
        dipendentiPanel.add(btnRicercaContratto);
        container.add(dipendentiPanel);


        // Aggiunta dei pulsanti alla sezione "Altro"
        JPanel altroPanel = new JPanel();

        altroPanel.setLayout(new FlowLayout());
        altroPanel.add(btnFatturatoMensile);
        altroPanel.add(btnCreaFidelity);
        altroPanel.add(btnVenditeGiornaliere);
        container.add(altroPanel);


        //aggiunta dei pulsanti per la sezione "ordini ai fornitori"
        JPanel ordiniFornitoriPanel = new JPanel();
        ordiniFornitoriPanel.setLayout(new FlowLayout());
        ordiniFornitoriPanel.add(btnInserisciFornitore);
        ordiniFornitoriPanel.add(btnVisualizzaFornitori);
        ordiniFornitoriPanel.add(btnInserisciOrdine);
        container.add(ordiniFornitoriPanel);



        // Aggiunta delle azioni ai pulsanti
        /*
        btnVisualizzaProdotti.addActionListener(e -> {
            ManagerProducts managerProducts = new ManagerProductsImpl(conn);
            managerProducts.visualizzaProdottiDisponibili();
        });
        btnAggiungiProdotto.addActionListener(e -> {
            ManagerProducts managerProducts = new ManagerProductsImpl(conn);
            managerProducts.aggiungiProdotto();
        });
        btnAggiungiIngredienti.addActionListener(e -> {
            ManagerIngredientiImpl managerIngredienti = new ManagerIngredientiImpl(conn);
            managerIngredienti.aggiungiIngredienteAProdotto();
        });
        btnAggiungiIngrediente.addActionListener(e -> {
            ManagerIngredientiImpl managerIngredienti = new ManagerIngredientiImpl(conn);
            managerIngredienti.aggiungiIngrediente();
        });
        btnVisualizzaIngredienti.addActionListener(e -> {
            ManagerIngredientiImpl managerIngredienti = new ManagerIngredientiImpl(conn);
            managerIngredienti.visualizzaIngredientiProdotto();
        });
        btnInserisciRichiesta.addActionListener(e -> {
            ManagerRichieste managerRichieste = new ManagerRichiesteImpl(conn);
            managerRichieste.inserisciRichiesta();
        });
        btnVisualizzaRifiuta.addActionListener(e -> {
            ManagerRichieste managerRichieste = new ManagerRichiesteImpl(conn);
            managerRichieste.visualizzaRifiutaRichieste();
        });
        btnInserisciAddetto.addActionListener(e -> {
            ManagerAddetti logic = new ManagerAddettiImpl(conn);
            logic.inserisciDipendente();
        });
        btnVisualizzaAddetti.addActionListener(e -> {
            ManagerAddetti logic = new ManagerAddettiImpl(conn);
            logic.visualizzaAddetti();
        });
        btnContratti.addActionListener(e -> {
            ManagerContratti logic = new ManagerContrattiImpl(conn);
            logic.inserisciContratto();
        });
        btnVisualizzaContratti.addActionListener(e -> {
            ManagerContratti logic = new ManagerContrattiImpl(conn);
            logic.visualizzaContratti("");
        });
        btnRicercaContratto.addActionListener(e -> {
            ManagerContratti logic = new ManagerContrattiImpl(conn);
            logic.ricercaContratti();
        });
        btnFatturatoMensile.addActionListener(e -> {
            Vendite logic = new VenditeImpl(conn);
            logic.visualizzaFatturatoMensile();
        });
        btnCreaFidelity.addActionListener(e -> {
            Manager logic = new ManagerImpl(conn);
            logic.creaFidelty();
        });
        btnMagazzino.addActionListener(e -> {
            ManagerIngredientiImpl managerIngredienti = new ManagerIngredientiImpl(conn);
            managerIngredienti.visualizzaMagazzino();
        });
        btnVenditeGiornaliere.addActionListener(e -> {
            Vendite logic = new VenditeImpl(conn);
            logic.visualizzaVenditeGiornaliere();
        });
        btnInserisciFornitore.addActionListener(e -> {
            ManagerFornitori logic = new ManagerFornitoriImpl(conn);
            logic.inserisciFornitore();
        });
        btnVisualizzaFornitori.addActionListener(e -> {
            ManagerFornitori logic = new ManagerFornitoriImpl(conn);
            logic.visualizzaFornitori();
        });
        btnInserisciOrdine.addActionListener(e -> {
            ManagerFornitori logic = new ManagerFornitoriImpl(conn);
            logic.makeOrder();
        });*/
    }
}
