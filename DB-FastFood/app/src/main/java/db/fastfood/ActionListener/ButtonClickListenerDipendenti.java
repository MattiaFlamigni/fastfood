package db.fastfood.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;

import db.fastfood.Impl.Manager.ManagerAddettiImpl;
import db.fastfood.Impl.Manager.ManagerContrattiImpl;
import db.fastfood.api.Manager.ManagerAddetti;
import db.fastfood.api.Manager.ManagerContratti;

public class ButtonClickListenerDipendenti implements ActionListener {
    ManagerContratti contratti;
    ManagerAddetti addetti;

    public ButtonClickListenerDipendenti(Connection conn) {
        contratti = new ManagerContrattiImpl(conn);
        addetti = new ManagerAddettiImpl(conn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonName = button.getText();

        switch (buttonName) {
            case "Visualizza Dipendenti":
                addetti.showEmployee();
                break;
            case "Inserisci Dipendente":
                addetti.addEmployee();
                break;

            case "Visualizza contratti":
                System.out.println("Visualizza Contratti");
                contratti.showContract("");
                break;
            case "Crea contratto":
                contratti.newContract();
                break;
            case "Ricerca contratto":
                // String cf = JOptionPane.showInputDialog("Inserisci il codice fiscale del
                // dipendente");
                contratti.searchContract();
                break;

            default:
                break;
        }
    }

}
