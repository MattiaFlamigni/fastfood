package db.fastfood.view;

import java.sql.Connection;

import javax.swing.JFrame;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import db.fastfood.ActionListener.ButtonClickListenerPrenotazioni;



public class SchermataPrenotazioni extends JFrame {
    
    private final Connection conn;
    private ButtonClickListenerPrenotazioni listener;

    public SchermataPrenotazioni(Connection conn) {
        this.conn = conn;
        setTitle("Schermata Ordini Fornitori");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnTavolo = new JButton("Inserisci tavoli");
        JButton btnVisualizzaTavoli = new JButton("Visualizza tavoli");
        JButton btnPrenotazione = new JButton("Inserisci prenotazione");
        JButton btnVisualizzaPrenotazioni = new JButton("Visualizza prenotazioni");

        Container container = getContentPane();
        container.setLayout(new GridLayout(4, 2));

        JPanel ordiniFornitoriPanel = new JPanel();
        ordiniFornitoriPanel.setLayout(new FlowLayout());
        ordiniFornitoriPanel.add(btnTavolo);
        ordiniFornitoriPanel.add(btnVisualizzaTavoli);
        container.add(ordiniFornitoriPanel);

        ordiniFornitoriPanel = new JPanel();
        ordiniFornitoriPanel.add(btnPrenotazione);
        ordiniFornitoriPanel.add(btnVisualizzaPrenotazioni);
        container.add(ordiniFornitoriPanel);

        setVisible(true);

        btnTavolo.addActionListener(new ButtonClickListenerPrenotazioni(conn));
        btnVisualizzaTavoli.addActionListener(new ButtonClickListenerPrenotazioni(conn));
        btnPrenotazione.addActionListener(new ButtonClickListenerPrenotazioni(conn));
        btnVisualizzaPrenotazioni.addActionListener(new ButtonClickListenerPrenotazioni(conn));
        
        
        
    }
}
