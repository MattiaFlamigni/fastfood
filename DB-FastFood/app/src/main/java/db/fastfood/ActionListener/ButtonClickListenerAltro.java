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
        JButton button = (JButton)e.getSource();
        String buttonName = button.getText();

        switch (buttonName){
            case "Visualizza Fatturato Mensile":
                vendita.visualizzaFatturatoMensile();
                break;
            case "Crea Fidelty":
                manager.creaFidelty();
                break;
            case "Visualizza Vendite Giornaliere":
                vendita.visualizzaVenditeGiornaliere();
                break;
            case "Registra Scarti":
                manager.registraScarti();
                break;

            case "Scontrino Medio":
                vendita.scontrinoMedio();
                break; 
            case "Visualizza scontrini per data":
                vendita.visualizzaScontriniPerData();
                break;
            case "Visualizza Scarti":
                manager.visualizzaScarti();
                break;
            case "Report":
                vendita.report();
                break;
            case "Spese Extra":
                manager.speseExtra();
                break;
        }

    }
    
}
