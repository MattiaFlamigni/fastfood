package db.fastfood.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.ImageIcon;
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
        JButton btnIndietro = new JButton("Indietro");

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

        JPanel indietroPanel = new JPanel();
        ImageIcon iconaIndietro = new javax.swing.ImageIcon(getClass().getResource("images/arrow-left-circle.png"));
        ImageIcon iconaIndietroRidimensionata = new ImageIcon(iconaIndietro.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));

        btnIndietro.setIcon(iconaIndietroRidimensionata);

        indietroPanel.add(btnIndietro);

        btnIndietro.setBackground(java.awt.Color.WHITE);
        btnIndietro.setForeground(java.awt.Color.RED);

        container.add(indietroPanel);

        setVisible(true);
        

        /*aggiungo i listener */
        btnInserisciRichiesta.addActionListener(new ButtonClickListenerRichieste(conn));
        btnVisualizzaRifiuta.addActionListener(new ButtonClickListenerRichieste(conn));

        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
                new SchermataInizialeFinale(conn);
            }
        });


        


    }


    
}
