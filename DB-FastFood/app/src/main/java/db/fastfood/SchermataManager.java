package db.fastfood;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;

public class SchermataManager extends JFrame {
    private final Connection conn;

    public SchermataManager(Connection conn) {
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

        // Creazione del pulsante per la sezione "Altro"
        JButton btnFatturatoMensile = new JButton("Visualizza Fatturato Mensile");
        JButton btnCreaFidelity = new JButton("Crea Fidelity");

        


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
        //richiestePanel.add(richiesteTitle);
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
        container.add(altroPanel);

        // Aggiunta delle azioni ai pulsanti
        btnVisualizzaProdotti.addActionListener(e->{
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.visualizzaProdottiDisponibili();
        });
        btnAggiungiProdotto.addActionListener(e -> {
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.aggiungiProdotto();
        });
        btnAggiungiIngredienti.addActionListener(e ->{
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.aggiungiIngredienteAProdotto();
        });
        btnAggiungiIngrediente.addActionListener(e ->{
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.aggiungiIngrediente();
        });
        btnVisualizzaIngredienti.addActionListener(e ->{ 
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.visualizzaIngredientiProdotto();
        });
        btnInserisciRichiesta.addActionListener(e ->{ 
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.inserisciRichiesta();
        });
        btnVisualizzaRifiuta.addActionListener(e -> {
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.visualizzaRifiutaRichieste();
        });
        btnInserisciAddetto.addActionListener(e -> {
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.inserisciDipendente();
        });
        btnVisualizzaAddetti.addActionListener(e -> {
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.visualizzaAddetti();
        });
        btnContratti.addActionListener(e -> {
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.inserisciContratto();
        });
        btnVisualizzaContratti.addActionListener(e -> {
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.visualizzaContratti("");
        });
        btnRicercaContratto.addActionListener(e -> {
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.ricercaContratti();
        });
        btnFatturatoMensile.addActionListener(e -> {
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.visualizzaFatturatoMensile();
        }); 
        btnCreaFidelity.addActionListener(e -> {
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.creaFidelty();
        });
        btnMagazzino.addActionListener(e -> {
            schermataManagerLogic logic = new schermataManagerLogicImpl(conn);
            logic.visualizzaMagazzino();
        });
    } 
}

