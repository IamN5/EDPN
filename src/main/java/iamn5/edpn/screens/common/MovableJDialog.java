package iamn5.edpn.screens.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MovableJDialog extends JDialog {
    private Point initialClick;

    public MovableJDialog(Window owner) {
        super(owner);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double yBorder = getHeight() * 0.04;
                double xBorder = getWidth() * 0.04;

                if (initialClick.y <= yBorder || initialClick.y >= getHeight() - yBorder
                        || initialClick.x <= xBorder || initialClick.x >= getWidth() - xBorder) {

                    // get location of Window
                    int thisX = getLocation().x;
                    int thisY = getLocation().y;

                    // Determine how much the mouse moved since the initial click
                    int xMoved = e.getX() - initialClick.x;
                    int yMoved = e.getY() - initialClick.y;

                    // Move window to this position
                    int X = thisX + xMoved;
                    int Y = thisY + yMoved;
                    setLocation(X, Y);
                }



            }
        });
    }
}
