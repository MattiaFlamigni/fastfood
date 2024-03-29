package db.fastfood.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;

import db.fastfood.Impl.Manager.ManagerPrenotazioniImpl;
import db.fastfood.api.Manager.ManagerPrenotazioni;

public class ButtonClickListenerPrenotazioni implements ActionListener {
    Connection conn;
    ManagerPrenotazioni prenotazioni;

    public ButtonClickListenerPrenotazioni(Connection conn) {
        this.conn = conn;
        prenotazioni = new ManagerPrenotazioniImpl(conn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonName = button.getText();

        switch (buttonName) {
            case "Inserisci tavoli":
                prenotazioni.AddTable();
                break;

            case "Visualizza tavoli":

                prenotazioni.showTable();
                break;

            case "Inserisci prenotazione":
                prenotazioni.AddReservation();
                break;

            case "Visualizza prenotazioni":
                prenotazioni.showreservation();
                break;
        }
    }

}
