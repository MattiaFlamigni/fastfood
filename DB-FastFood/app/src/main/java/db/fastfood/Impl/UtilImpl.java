package db.fastfood.Impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import db.fastfood.api.Util;

public class UtilImpl implements Util {
    private final Connection conn;

    public UtilImpl(Connection conn) {
        this.conn = conn;
    }

    public int getCurrentordine() {
        int idordine = 0;
        try {
            Statement statement = conn.createStatement();
            String query2 = "SELECT ID FROM ordine";
            ResultSet result2 = statement.executeQuery(query2);
            while (result2.next()) {
                idordine = result2.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idordine;
    }

    public int getCurrentcliente() {
        int idcliente = 0;
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT ID FROM cliente";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                idcliente = result.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idcliente;
    }
}
