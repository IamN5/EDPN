package iamn5.edpn.screens.common;

import iamn5.edpn.framework.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class HoverableTableCellRenderer extends DefaultTableCellRenderer {
    private int hoveredRow = -1;

    public HoverableTableCellRenderer() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        HoverableJTable customTable = (HoverableJTable) table;

        if (isSelected) {
            setBackground(customTable.getSelectionBackground());
            setForeground(customTable.getSelectionForeground());
        } else if (row == hoveredRow) {
            setBackground(customTable.getHoveredColor());
        } else {
            setBackground(customTable.getBackground());
            setForeground(customTable.getForeground());
        }

        setBorder(noFocusBorder);
        setText(value.toString());

        return this;
    }

    public void setHoveredRow(int row) {
        Logger.info("Hovered row is " + row);
        hoveredRow = row;
    }

    public int getHoveredRow() {
        return hoveredRow;
    }

}

