package iamn5.edpn.screens.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class HoverableJList extends JList<String> implements MouseMotionListener {
    private Color hoveredColor;
    HoverableListCellRenderer renderer;

    public HoverableJList() {
        this(new HoverableListCellRenderer());
    }

    public HoverableJList(HoverableListCellRenderer renderer) {
        this(renderer, new Color(48, 48, 48));
    }

    public HoverableJList(HoverableListCellRenderer renderer, Color hoveredColor) {
        this.hoveredColor = hoveredColor;
        this.renderer = renderer;

        setCellRenderer(renderer);
        setSelectionBackground(new Color(36,36,36));
        setFixedCellHeight(45);

        addMouseMotionListener(this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                renderer.setHoveredIndex(-1);
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
        int index = locationToIndex(e.getPoint());

        if (index != getSelectedIndex() && index != renderer.getHoveredIndex()) {
            renderer.setHoveredIndex(index);
            repaint();
        }
    }

    public void setHoveredColor(Color hoveredColor) {
        this.hoveredColor = hoveredColor;
    }

    public Color getHoveredColor() {
        return hoveredColor;
    }

    public HoverableListCellRenderer getStocksCellRenderer() {
        return renderer;
    }
}

