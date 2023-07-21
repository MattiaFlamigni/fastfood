package db.fastfood.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;

import db.fastfood.Impl.Manager.ManagerImpl;
import db.fastfood.Impl.Manager.VenditeImpl;
import db.fastfood.api.Manager.Manager;
import db.fastfood.api.Manager.Vendite;

public class ButtonClickListenerAltro implements ActionListener {
    Manager manager;
    Vendite vendita;

    public ButtonClickListenerAltro(Connection conn) {
        manager = new ManagerImpl(conn);
        vendita = new VenditeImpl(conn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonName = button.getText();

        switch (buttonName) {
            case "Visualizza Fatturato Mensile":
                vendita.monthlyEarned();
                break;
            case "Crea Fidelty":
                manager.createFidelity();
                break;
            case "Visualizza Vendite Giornaliere":
                vendita.showDailySales();
                break;
            case "Registra Scarti":
                manager.newWaste();
                break;

            case "Scontrino Medio":
                vendita.averageReceipt();
                break;
            case "Visualizza scontrini per data":
                vendita.showReceiptByDate();
                break;
            case "Visualizza Scarti":
                manager.showWaste();
                break;
            case "Report":
                vendita.report();
                break;
            case "Spese Extra":
                manager.extra();
                break;
            case "Visualizza Spese Extra":
                manager.showExtra();
                break;

            case "Report Delivery":
                vendita.reportDelivery();
                break;

        }

    }

}
