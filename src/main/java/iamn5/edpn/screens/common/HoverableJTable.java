package iamn5.edpn.screens.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class HoverableJTable extends JTable implements MouseMotionListener {
    private Color hoveredColor;
    HoverableTableCellRenderer renderer;

    public HoverableJTable() {
        this(new HoverableTableCellRenderer());
    }

    public HoverableJTable(HoverableTableCellRenderer renderer) {
        this(renderer, new Color(92, 92, 92));
    }

    public HoverableJTable(HoverableTableCellRenderer renderer, Color hoveredColor) {
        this.hoveredColor = hoveredColor;
        this.renderer = renderer;

        setDefaultRenderer(String.class, renderer);
        setSelectionBackground(new Color(54, 54, 54));
        setRowHeight(25);

        addMouseMotionListener(this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                renderer.setHoveredRow(-1);
                repaint();
            }
        });
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        isItemBeingHovered(e);
    }

    private void isItemBeingHovered(MouseEvent e) {
        int row = rowAtPoint(e.getPoint());

        if (row != getSelectedRow() && row != renderer.getHoveredRow()) {
            renderer.setHoveredRow(row);
            repaint();
        }
    }

    public void setHoveredColor(Color hoveredColor) {
        this.hoveredColor = hoveredColor;
    }

    public Color getHoveredColor() {
        return hoveredColor;
    }
}
