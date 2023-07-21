package db.fastfood.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import db.fastfood.ActionListener.ButtonClickListenerFornitori;

public class SchermataOrdiniFornitori extends JFrame {
    @SuppressWarnings("unused")
    private Connection connection;
    ButtonClickListenerFornitori listener;

    public SchermataOrdiniFornitori(Connection connection) {
        this.connection = connection;
        setTitle("Schermata Ordini Fornitori");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnInserisciFornitore = new JButton("Inserisci Fornitore");
        JButton btnVisualizzaFornitori = new JButton("Visualizza Fornitori");
        JButton btnInserisciOrdine = new JButton("Inserisci Ordine");
        JButton btnIndietro = new JButton("Indietro");

        Container container = getContentPane();
        container.setLayout(new GridLayout(3, 2));

        JPanel ordiniFornitoriPanel = new JPanel();
        ordiniFornitoriPanel.setLayout(new FlowLayout());
        ordiniFornitoriPanel.add(btnInserisciFornitore);
        ordiniFornitoriPanel.add(btnVisualizzaFornitori);
        container.add(ordiniFornitoriPanel);

        ordiniFornitoriPanel = new JPanel();
        ordiniFornitoriPanel.add(btnInserisciOrdine);
        container.add(ordiniFornitoriPanel);

        ImageIcon iconaIndietro = new javax.swing.ImageIcon(getClass().getResource("images/arrow-left-circle.png"));
        ImageIcon iconaIndietroRidimensionata = new ImageIcon(
                iconaIndietro.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));

        btnIndietro.setIcon(iconaIndietroRidimensionata);

        JPanel indietroPanel = new JPanel();
        indietroPanel.add(btnIndietro);

        btnIndietro.setBackground(java.awt.Color.WHITE);
        btnIndietro.setForeground(java.awt.Color.RED);

        container.add(indietroPanel);

        setVisible(true);

        btnInserisciFornitore.addActionListener(new ButtonClickListenerFornitori(connection));
        btnVisualizzaFornitori.addActionListener(new ButtonClickListenerFornitori(connection));
        btnInserisciOrdine.addActionListener(new ButtonClickListenerFornitori(connection));

        btnIndietro.addActionListener(e -> {
            dispose();
            new SchermataInizialeFinale(connection);
        });
    }

}
