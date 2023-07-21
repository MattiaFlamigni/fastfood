package db.fastfood.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;

import db.fastfood.Impl.Manager.ManagerRichiesteImpl;
import db.fastfood.api.Manager.ManagerRichieste;

public class ButtonClickListenerRichieste implements ActionListener {

    ManagerRichieste richieste;

    public ButtonClickListenerRichieste(Connection conn) {
        richieste = new ManagerRichiesteImpl(conn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonName = button.getText();

        switch (buttonName) {
            case "Nuova richiesta":
                richieste.addRequest();
                break;
            case "Visualizza/rifiuta richieste":
                richieste.ShowDeclineRequest();
                break;
            default:
                break;
        }
    }

}
