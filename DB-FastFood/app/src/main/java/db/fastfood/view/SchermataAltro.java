package db.fastfood.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;

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

        Container container = getContentPane();
        container.setLayout(new GridLayout(3,1));
        
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
        container.add(altroPanel);

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
        btnVisualizzaSpeseExtra.addActionListener(new ButtonClickListenerAltro(connection));


    }
}
