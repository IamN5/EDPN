/*
 * Created by JFormDesigner on Tue Nov 10 17:21:30 BRT 2020
 */

package iamn5.edpn.screens.modals;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import iamn5.edpn.app.KeyAction;
import iamn5.edpn.framework.Logger;
import iamn5.edpn.framework.core.ui.JFrameManager;
import iamn5.edpn.screens.MainScreen;
import iamn5.edpn.screens.common.*;

import static java.awt.event.InputEvent.*;

/**
 * @author IamN5
 */
public class KeyActionForm extends MovableJDialog {
    private JFrameManager frameManager;
    private KeyEvent lastKeyEvent;

    public KeyActionForm(Window owner, JFrameManager frameManager) {
        this(owner);
        this.frameManager = frameManager;
    }

    public KeyActionForm(Window owner) {
        super(owner);
        initComponents();

        try {
            edpnLogo.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/assets/logos/edpn-50x45.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeButtonActionPerformed(ActionEvent e) {
        dispose();
    }

    private void hotkeyTextFieldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_SHIFT && e.getKeyCode() != KeyEvent.VK_CONTROL && e.getKeyCode() != KeyEvent.VK_ALT) {
            int modifiers = e.getModifiersEx();
            int offMask = ALT_DOWN_MASK | CTRL_DOWN_MASK | SHIFT_DOWN_MASK;

            lastKeyEvent = e;

            if ((modifiers & offMask) == 0) {
                hotkeyTextField.setText(KeyEvent.getKeyText(e.getKeyCode()));
            } else {
                hotkeyTextField.setText(KeyEvent.getModifiersExText(modifiers) + "+" + KeyEvent.getKeyText(e.getKeyCode()));
            }

        }
    }

    private void hotkeyTextFieldKeyTyped(KeyEvent e) {
        e.consume();
    }

    private void keyActionTypeItemStateChanged(ItemEvent e) {
        if (keyActionType.getSelectedIndex() == 0) {
            delayLabel.setVisible(true);
            delaySpinner.setVisible(true);
        } else {
            delayLabel.setVisible(false);
            delaySpinner.setVisible(false);
        }
    }

    private void addActionPerformed(ActionEvent e) {
        if (lastKeyEvent != null && frameManager != null && frameManager.getFrame().getContentPane() instanceof MainScreen) {
            MainScreen mainScreen = (MainScreen) frameManager.getFrame().getContentPane();

            try {
                mainScreen.addAction(new KeyAction(lastKeyEvent, keyActionType.getSelectedIndex(), (Integer) delaySpinner.getValue()));

            } catch (AWTException awtException) {
                Logger.error("Error while trying to add KeyAction:");

                awtException.printStackTrace();
            }

            dispose();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - IamN5
        closeButton = new HoverableJButton();
        edpnLogo = new JLabel();
        keyText = new JLabel();
        keyActionType = new JComboBox<>();
        hotkeyTextField = new RoundJTextField();
        typeLabel = new JLabel();
        delayLabel = new JLabel();
        delaySpinner = new JSpinner();
        add = new RoundJButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- closeButton ----
        closeButton.setText("x");
        closeButton.setBackgroundColor(new Color(60, 63, 65));
        closeButton.setHoverBackgroundColor(new Color(47, 49, 50));
        closeButton.setPressedBackgroundColor(new Color(40, 42, 43));
        closeButton.addActionListener(e -> closeButtonActionPerformed(e));
        contentPane.add(closeButton);
        closeButton.setBounds(460, 0, 50, 25);
        contentPane.add(edpnLogo);
        edpnLogo.setBounds(230, 85, 50, 45);

        //---- keyText ----
        keyText.setText("Key/Hotkey:");
        keyText.setFont(new Font("Eurostile-Roman", keyText.getFont().getStyle(), 17));
        keyText.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(keyText);
        keyText.setBounds(0, 160, 510, keyText.getPreferredSize().height);

        //---- keyActionType ----
        keyActionType.setFont(new Font("Eurostile-Roman", keyActionType.getFont().getStyle(), keyActionType.getFont().getSize()));
        keyActionType.setModel(new DefaultComboBoxModel<>(new String[] {
            "Key press and release",
            "Key press",
            "Key release"
        }));
        keyActionType.addItemListener(e -> keyActionTypeItemStateChanged(e));
        contentPane.add(keyActionType);
        keyActionType.setBounds(160, 265, 185, 30);

        //---- hotkeyTextField ----
        hotkeyTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                hotkeyTextFieldKeyPressed(e);
            }
            @Override
            public void keyTyped(KeyEvent e) {
                hotkeyTextFieldKeyTyped(e);
            }
        });
        contentPane.add(hotkeyTextField);
        hotkeyTextField.setBounds(160, 185, 185, 30);

        //---- typeLabel ----
        typeLabel.setText("Type of action:");
        typeLabel.setFont(new Font("Eurostile-Roman", typeLabel.getFont().getStyle(), 17));
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(typeLabel);
        typeLabel.setBounds(0, 240, 510, 19);

        //---- delayLabel ----
        delayLabel.setText("Delay between press and release:");
        delayLabel.setFont(new Font("Eurostile-Roman", delayLabel.getFont().getStyle(), 17));
        delayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(delayLabel);
        delayLabel.setBounds(0, 315, 510, 19);

        //---- delaySpinner ----
        delaySpinner.setModel(new SpinnerNumberModel(100, 0, 5000, 50));
        delaySpinner.setFont(new Font("Eurostile-Roman", delaySpinner.getFont().getStyle(), delaySpinner.getFont().getSize()));
        contentPane.add(delaySpinner);
        delaySpinner.setBounds(205, 340, delaySpinner.getPreferredSize().width, 30);

        //---- add ----
        add.setText("Add key action");
        add.setFont(new Font("Eurostile-Roman", add.getFont().getStyle(), 16));
        add.addActionListener(e -> addActionPerformed(e));
        contentPane.add(add);
        add.setBounds(75, 460, 351, 35);

        contentPane.setPreferredSize(new Dimension(510, 560));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - IamN5
    private HoverableJButton closeButton;
    private JLabel edpnLogo;
    private JLabel keyText;
    private JComboBox<String> keyActionType;
    private RoundJTextField hotkeyTextField;
    private JLabel typeLabel;
    private JLabel delayLabel;
    private JSpinner delaySpinner;
    private RoundJButton add;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
