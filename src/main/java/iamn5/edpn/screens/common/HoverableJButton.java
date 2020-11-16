package iamn5.edpn.screens.common;

import javax.swing.*;
import java.awt.*;

public class HoverableJButton extends JButton {
    private Color backgroundColor;
    private Color pressedBackgroundColor;
    private Color hoverBackgroundColor;

    public HoverableJButton() {
        this(new Color(148, 88, 214), new Color(127, 46, 214), new Color(101, 36, 171));
    }

    public HoverableJButton(Color backgroundColor, Color hoverBackgroundColor, Color pressedBackgroundColor) {
        this.backgroundColor = backgroundColor;
        this.hoverBackgroundColor = hoverBackgroundColor;
        this.pressedBackgroundColor = pressedBackgroundColor;

        setOpaque(false);
        setContentAreaFilled(false);
        setRolloverEnabled(true);
        setForeground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        if (getModel().isPressed()) {
            g.setColor(pressedBackgroundColor);
        } else if (getModel().isRollover()) {
            g.setColor(hoverBackgroundColor);
        } else {
            g.setColor(backgroundColor);
        }

        g.fillRect(0, 0, getSize().width - 1, getSize().height - 1);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getBackground());
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(Color hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }
}
