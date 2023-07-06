package db.fastfood.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import db.fastfood.ActionListener.ButtonClickListenerRichieste;

public class SchermataRichieste extends JFrame {
    @SuppressWarnings("unused")
    private final Connection conn;

    public SchermataRichieste(Connection conn){
        this.conn = conn;
        setTitle("Schermata Prodotti");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        // Creazione dei pulsanti per la sezione "Richieste"
        JButton btnInserisciRichiesta = new JButton("Nuova richiesta");
        JButton btnVisualizzaRifiuta = new JButton("Visualizza/rifiuta richieste");
        Container container = getContentPane();
        container.setLayout(new GridLayout(2,1));

        // Aggiunta dei pulsanti alla sezione "Richieste"
        JPanel richiestePanel = new JPanel();
        richiestePanel.setLayout(new FlowLayout());
        richiestePanel.add(btnInserisciRichiesta);
        container.add(richiestePanel);

        richiestePanel=new JPanel();
        richiestePanel.setLayout(new FlowLayout());
        richiestePanel.add(btnVisualizzaRifiuta);
        container.add(richiestePanel);

        setVisible(true);
        

        /*aggiungo i listener */
        btnInserisciRichiesta.addActionListener(new ButtonClickListenerRichieste(conn));
        btnVisualizzaRifiuta.addActionListener(new ButtonClickListenerRichieste(conn));


        


    }


    
}
