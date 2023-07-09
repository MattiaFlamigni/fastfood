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
        JButton btnCreaFidelity = new JButton("Crea Fidelity");
        JButton btnVenditeGiornaliere = new JButton("Visualizza Vendite Giornaliere");
        JButton btnScarti = new JButton("Registra Scarti");

        Container container = getContentPane();
        container.setLayout(new GridLayout(3,1));
        
        JPanel altroPanel = new JPanel();
        altroPanel.setLayout(new FlowLayout());
        altroPanel.add(btnFatturatoMensile);
        container.add(altroPanel);

        altroPanel=new JPanel();
        altroPanel.add(btnCreaFidelity);
        container.add(altroPanel);

        altroPanel=new JPanel();
        altroPanel.add(btnVenditeGiornaliere);
        altroPanel.add(btnScarti);
        container.add(altroPanel);

        setVisible(true);


        btnFatturatoMensile.addActionListener(new ButtonClickListenerAltro(connection));
        btnCreaFidelity.addActionListener(new ButtonClickListenerAltro(connection));
        btnVenditeGiornaliere.addActionListener(new ButtonClickListenerAltro(connection));
        btnScarti.addActionListener(new ButtonClickListenerAltro(connection));

    }
}
