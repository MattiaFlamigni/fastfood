package db.fastfood.ActionListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;

import db.fastfood.Impl.Manager.ManagerImpl;
import db.fastfood.api.Manager.Manager;

public class ButtonClickListenerAltro implements ActionListener {
    Manager manager;

    public ButtonClickListenerAltro(Connection conn) {
        manager = new ManagerImpl(conn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        String buttonName = button.getText();

        switch (buttonName){
            case "Visualizza Fatturato Mensile":
                manager.visualizzaFatturatoMensile();
                break;
            case "Crea Fidelty":
                manager.creaFidelty();
                break;
            case "Visualizza Vendite Giornaliere":
                manager.visualizzaVenditeGiornaliere();
                break;
            case "Registra Scarti":
                manager.registraScarti();
                break;

            case "Scontrino Medio":
                manager.scontrinoMedio();
                break; 
            case "Visualizza scontrini per data":
                manager.visualizzaScontriniPerData();
                break;
        }

    }
    
}
