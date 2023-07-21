package db.fastfood.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;

import db.fastfood.Impl.Manager.ManagerFornitoriImpl;
import db.fastfood.api.Manager.ManagerFornitori;

public class ButtonClickListenerFornitori implements ActionListener {
    ManagerFornitori fornitori;

    public ButtonClickListenerFornitori(Connection conn) {
        fornitori = new ManagerFornitoriImpl(conn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonName = button.getText();

        switch (buttonName) {
            case "Inserisci Fornitore":
                fornitori.addSupplier();
                break;
            case "Visualizza Fornitori":
                fornitori.showSupplier();
                break;
            case "Inserisci Ordine":
                fornitori.makeOrder();
                break;
        }
    }

}
