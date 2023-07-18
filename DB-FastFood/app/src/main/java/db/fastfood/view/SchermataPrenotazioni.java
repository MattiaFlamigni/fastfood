package db.fastfood.view;

import java.sql.Connection;

import javax.swing.JFrame;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import db.fastfood.ActionListener.ButtonClickListenerPrenotazioni;

public class SchermataPrenotazioni extends JFrame {

    final Connection conn;

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
        JButton btnIndietro = new JButton("Indietro");

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

        JPanel indietroPanel = new JPanel();
        ImageIcon iconaIndietro = new javax.swing.ImageIcon(getClass().getResource("images/arrow-left-circle.png"));
        ImageIcon iconaIndietroRidimensionata = new ImageIcon(
                iconaIndietro.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));

        btnIndietro.setIcon(iconaIndietroRidimensionata);

        indietroPanel.add(btnIndietro);

        btnIndietro.setBackground(java.awt.Color.WHITE);
        btnIndietro.setForeground(java.awt.Color.RED);

        container.add(indietroPanel);

        setVisible(true);

        btnTavolo.addActionListener(new ButtonClickListenerPrenotazioni(conn));
        btnVisualizzaTavoli.addActionListener(new ButtonClickListenerPrenotazioni(conn));
        btnPrenotazione.addActionListener(new ButtonClickListenerPrenotazioni(conn));
        btnVisualizzaPrenotazioni.addActionListener(new ButtonClickListenerPrenotazioni(conn));

        btnIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
                new SchermataInizialeFinale(conn);
            }
        });

    }
}
