package db.fastfood.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import db.fastfood.ActionListener.ButtonClickListenerAltro;

public class SchermataAltro extends JFrame {
    @SuppressWarnings("unused")
    private Connection connection;

    public SchermataAltro(Connection connection){
        this.connection = connection;
        setTitle("Schermata Altro");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnFatturatoMensile = new JButton("Visualizza Fatturato Mensile");
        JButton btnscontrinoMedio = new JButton("Scontrino Medio");
        JButton btnScontrini = new JButton("Visualizza scontrini per data");
        JButton btnCreaFidelity = new JButton("Crea Fidelity");
        JButton btnVenditeGiornaliere = new JButton("Visualizza Vendite Giornaliere");
        JButton btnScarti = new JButton("Registra Scarti");
        JButton btnVisualizzaScarti = new JButton("Visualizza Scarti");
        JButton btnReport = new JButton("Report");
        JButton btnSpeseExtra = new JButton("Spese Extra");
        JButton btnVisualizzaSpeseExtra = new JButton("Visualizza Spese Extra");
        JButton btnReportrDelivery = new JButton("Report Delivery");

        Container container = getContentPane();
        container.setLayout(new GridLayout(4,1));
        
        JPanel altroPanel = new JPanel();
        altroPanel.setLayout(new FlowLayout());
        altroPanel.add(btnFatturatoMensile);
        altroPanel.add(btnVenditeGiornaliere);
        altroPanel.add(btnscontrinoMedio);
        altroPanel.add(btnScontrini);
        altroPanel.add(btnReport);
        container.add(altroPanel);

        altroPanel=new JPanel();
        altroPanel.add(btnCreaFidelity);
        container.add(altroPanel);

        altroPanel=new JPanel();
        altroPanel.add(btnVisualizzaScarti);
        altroPanel.add(btnScarti);
        altroPanel.add(btnSpeseExtra);
        altroPanel.add(btnVisualizzaSpeseExtra);
        altroPanel.add(btnReportrDelivery);
        container.add(altroPanel);


        //voglio un bottone che mi consenta di tornare alla schermata iniziale. il bottone deve stare nella parte bassa della schermata
        JPanel indietroPanel = new JPanel();
        JButton btnIndietro = new JButton("Indietro");
    
        container.add(indietroPanel, BorderLayout.SOUTH);

        //voglio che il bottone indietro abbia l'icona di una freccia che punta verso sinistra

        ImageIcon iconaIndietro = new javax.swing.ImageIcon(getClass().getResource("images/arrow-left-circle.png"));
        ImageIcon iconaIndietroRidimensionata = new ImageIcon(iconaIndietro.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));

        btnIndietro.setIcon(iconaIndietroRidimensionata);

        indietroPanel.add(btnIndietro);

        btnIndietro.setBackground(java.awt.Color.WHITE);
        btnIndietro.setForeground(java.awt.Color.RED);







        setVisible(true);


        btnFatturatoMensile.addActionListener(new ButtonClickListenerAltro(connection));
        btnCreaFidelity.addActionListener(new ButtonClickListenerAltro(connection));
        btnVenditeGiornaliere.addActionListener(new ButtonClickListenerAltro(connection));
        btnScarti.addActionListener(new ButtonClickListenerAltro(connection));
        btnscontrinoMedio.addActionListener(new ButtonClickListenerAltro(connection));
        btnScontrini.addActionListener(new ButtonClickListenerAltro(connection));
        btnVisualizzaScarti.addActionListener(new ButtonClickListenerAltro(connection));
        btnReport.addActionListener(new ButtonClickListenerAltro(connection));
        btnSpeseExtra.addActionListener(new ButtonClickListenerAltro(connection));
        btnReportrDelivery.addActionListener(new ButtonClickListenerAltro(connection));
        btnVisualizzaSpeseExtra.addActionListener(new ButtonClickListenerAltro(connection));
        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
                new SchermataInizialeFinale(connection);
            }
        });


    }
}
