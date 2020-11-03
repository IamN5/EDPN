package iamn5.edpn.framework.core.ui;

import iamn5.edpn.framework.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MovableScreen extends Screen {
    private Point initialClick;

    public MovableScreen(String title, JFrameManager frameManager) {
        super(title, frameManager);

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
                    JFrame frame = frameManager.getFrame();

                    // get location of Window
                    int thisX = frame.getLocation().x;
                    int thisY = frame.getLocation().y;

                    // Determine how much the mouse moved since the initial click
                    int xMoved = e.getX() - initialClick.x;
                    int yMoved = e.getY() - initialClick.y;

                    // Move window to this position
                    int X = thisX + xMoved;
                    int Y = thisY + yMoved;
                    frame.setLocation(X, Y);
                }



            }
        });
    }
}
