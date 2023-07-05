package db.fastfood.Impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CustomTable extends DefaultTableModel {

    public void doGraphic(JTable table) {
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        table.setGridColor(Color.BLACK);
        table.setShowHorizontalLines(true);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 30));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setPreferredScrollableViewportSize(table.getPreferredSize()); 
        table.setFillsViewportHeight(true);
            
        table.setDragEnabled(false);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);

    }
    
}
