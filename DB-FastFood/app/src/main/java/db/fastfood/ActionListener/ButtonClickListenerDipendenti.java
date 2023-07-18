package db.fastfood.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;

import db.fastfood.Impl.Manager.ManagerAddettiImpl;
import db.fastfood.Impl.Manager.ManagerContrattiImpl;
import db.fastfood.Impl.Manager.ManagerImpl;
import db.fastfood.api.Manager.Manager;
import db.fastfood.api.Manager.ManagerAddetti;
import db.fastfood.api.Manager.ManagerContratti;
import db.fastfood.view.SchermataInizialeFinale;

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
                addetti.visualizzaAddetti();
                break;
            case "Inserisci Dipendente":
                addetti.inserisciDipendente();
                break;

            case "Visualizza contratti":
                System.out.println("Visualizza Contratti");
                contratti.visualizzaContratti("");
                break;
            case "Crea contratto":
                contratti.inserisciContratto();
                break;
            case "Ricerca contratto":
                // String cf = JOptionPane.showInputDialog("Inserisci il codice fiscale del
                // dipendente");
                contratti.ricercaContratti();
                break;

            default:
                break;
        }
    }

}
