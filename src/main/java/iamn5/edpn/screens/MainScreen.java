/*
 * Created by JFormDesigner on Sun Nov 01 01:11:12 BRT 2020
 */

package iamn5.edpn.screens;

import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;

import iamn5.edpn.app.KeyAction;
import iamn5.edpn.framework.Logger;
import iamn5.edpn.framework.core.ListenableQueue;
import iamn5.edpn.framework.core.ui.JFrameManager;
import iamn5.edpn.screens.common.MovableScreen;
import iamn5.edpn.framework.monitors.hotkey.Hotkey;
import iamn5.edpn.screens.common.*;
import iamn5.edpn.screens.common.RoundJButton;
import iamn5.edpn.screens.modals.KeyActionForm;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static java.awt.event.InputEvent.*;

/**
 * @author IamN5
 */
public class MainScreen extends MovableScreen {
    private boolean isLoading = false;

    public MainScreen(JFrameManager frameManager) {
        super("EDPN - Project Nova", frameManager);

        initComponents();
        eventsText.setEditable(false);

        actionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                removeActionButton.setVisible(actionList.getSelectedIndex() != -1);
            }
        });

        try {
            loadingIcon.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/assets/logos/loading.png"))));
            edpnIcon.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/assets/logos/edpn-50x45.png"))));
            backIcon.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/assets/images/back_arrow.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(this::startLoading).start();
    }

    private void stopLoading() {
        Logger.info("Finished loading.");
        
        closeButton.setVisible(true);
        minimizeButton.setVisible(true);
        edpnIcon.setVisible(true);
        greetingText.setVisible(true);
        commanderName.setVisible(true);
        ((CardLayout) content.getLayout()).show(content, "card1");
        content.setVisible(true);


        loadingIcon.setVisible(false);
        loadingProgress.setVisible(false);

        isLoading = false;
    }

    private void startLoading() {
        Logger.info("Started loading.");
        isLoading = true;

        edpnIcon.setVisible(false);

        loadingProgress.setVisible(true);
        loadingIcon.setVisible(true);

        ListenableQueue<JSONObject> eventQueue = frameManager.getEventQueue();
        eventQueue.addListener(element -> {
            eventsText.append("Triggered event " + element.getString("event") + "\n");
            eventsText.setCaretPosition(eventsText.getDocument().getLength());
        });

        for (int i = 1; i < 3; i++) {
            try {
                Thread.sleep(500);
                loadingProgress.setValue(i * 33);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        new Thread(() -> {
            frameManager.getJournalMonitor().start();
        }).start();

        loadingProgress.setValue(100);

        stopLoading();
    }

    public void showCommanderName(String name, Date lastLoadDate) {
        commanderName.setText("Commander " + name);
        lastLoaded.setText("Last loaded at " + lastLoadDate);
        
        lastLoaded.setVisible(true);
    }

    public void showGameBuild(String build) {
        buildText.setText("Game build: " + build);

        buildText.setVisible(true);
    }

    private void closeButtonActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void minimizeButtonActionPerformed(ActionEvent e) {
        frameManager.getFrame().setState(JFrame.ICONIFIED);
    }

    private void hotkeyButtonActionPerformed(ActionEvent e) {
        ((CardLayout) content.getLayout()).show(content, "card2");
        actionList.setModel(new ActionListModel(new ArrayList<>()));
    }

    private void backIconMouseClicked(MouseEvent e) {
        ((CardLayout) content.getLayout()).show(content, "card1");
    }

    private void hotkeyTextFieldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_SHIFT && e.getKeyCode() != KeyEvent.VK_CONTROL && e.getKeyCode() != KeyEvent.VK_ALT) {
            int modifiers = e.getModifiersEx();
            int offMask = ALT_DOWN_MASK | CTRL_DOWN_MASK | SHIFT_DOWN_MASK;

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

    private void keyActionButtonActionPerformed(ActionEvent e) {
        frameManager.loadModal(KeyActionForm.class, "Add Key action", true);
    }

    private void createHotkeyButtonActionPerformed(ActionEvent e) {
        
    }

    public boolean addAction(KeyAction action) {
        if (actionList.getModel() instanceof ActionListModel) {
            ((ActionListModel) actionList.getModel()).addKeyAction(action);

            SwingUtilities.invokeLater(() -> actionList.repaint());

            Logger.info("Added action " + action.toString());

            return true;
        }

        return false;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - IamN5
        edpnIcon = new JLabel();
        loadingIcon = new JLabel();
        loadingProgress = new JProgressBar();
        greetingText = new JLabel();
        commanderName = new JLabel();
        lastLoaded = new JLabel();
        closeButton = new HoverableJButton();
        minimizeButton = new HoverableJButton();
        buildText = new JLabel();
        content = new JPanel();
        mainScreen = new JPanel();
        hotkeyButton = new RoundJButton();
        eventsScroll = new JScrollPane();
        eventsText = new JTextArea();
        hotkeyScreen = new JPanel();
        backIcon = new JLabel();
        hotkeyTextField = new RoundJTextField();
        nameLabel = new JLabel();
        nameTextField = new RoundJTextField();
        keyActionButton = new RoundJButton();
        hotkeylabel2 = new JLabel();
        hotkeylabel = new JLabel();
        listScroll = new JScrollPane();
        actionList = new HoverableJList();
        removeActionButton = new RoundJButton();
        createHotkeyButton = new RoundJButton();

        //======== this ========
        setLayout(null);

        //---- edpnIcon ----
        edpnIcon.setText(" ");
        edpnIcon.setVisible(false);
        add(edpnIcon);
        edpnIcon.setBounds(25, 25, 50, 45);

        //---- loadingIcon ----
        loadingIcon.setText(" ");
        loadingIcon.setVisible(false);
        add(loadingIcon);
        loadingIcon.setBounds(245, 160, 461, 316);

        //---- loadingProgress ----
        loadingProgress.setForeground(new Color(148, 88, 214));
        loadingProgress.setVisible(false);
        loadingProgress.setBorder(null);
        add(loadingProgress);
        loadingProgress.setBounds(-5, 620, 965, 19);

        //---- greetingText ----
        greetingText.setText("Greetings,");
        greetingText.setFont(new Font("Eurostile-Roman", greetingText.getFont().getStyle(), 16));
        greetingText.setVerticalAlignment(SwingConstants.BOTTOM);
        greetingText.setVisible(false);
        add(greetingText);
        greetingText.setBounds(90, 25, 75, greetingText.getPreferredSize().height);

        //---- commanderName ----
        commanderName.setText("Commander not found!");
        commanderName.setFont(new Font("Eurostile-Roman", commanderName.getFont().getStyle() & ~Font.ITALIC, 22));
        commanderName.setVerticalAlignment(SwingConstants.BOTTOM);
        commanderName.setVisible(false);
        add(commanderName);
        commanderName.setBounds(90, 40, 400, 25);

        //---- lastLoaded ----
        lastLoaded.setText("Last loaded at BRT 2020");
        lastLoaded.setFont(new Font("Eurostile-Roman", lastLoaded.getFont().getStyle(), 14));
        lastLoaded.setVerticalAlignment(SwingConstants.BOTTOM);
        lastLoaded.setVisible(false);
        add(lastLoaded);
        lastLoaded.setBounds(90, 65, 430, 20);

        //---- closeButton ----
        closeButton.setText("x");
        closeButton.setBorder(null);
        closeButton.setBackgroundColor(new Color(60, 63, 65));
        closeButton.setHoverBackgroundColor(new Color(47, 49, 50));
        closeButton.setPressedBackgroundColor(new Color(40, 42, 43));
        closeButton.setVisible(false);
        closeButton.addActionListener(e -> closeButtonActionPerformed(e));
        add(closeButton);
        closeButton.setBounds(900, 0, 50, 25);

        //---- minimizeButton ----
        minimizeButton.setText("-");
        minimizeButton.setBorder(null);
        minimizeButton.setBackgroundColor(new Color(60, 63, 65));
        minimizeButton.setHoverBackgroundColor(new Color(47, 49, 50));
        minimizeButton.setPressedBackgroundColor(new Color(40, 42, 43));
        minimizeButton.setVisible(false);
        minimizeButton.addActionListener(e -> minimizeButtonActionPerformed(e));
        add(minimizeButton);
        minimizeButton.setBounds(850, 0, 50, 25);

        //---- buildText ----
        buildText.setText("Game build");
        buildText.setFont(new Font("Eurostile-Roman", buildText.getFont().getStyle(), 14));
        buildText.setVisible(false);
        add(buildText);
        buildText.setBounds(655, 5, 190, buildText.getPreferredSize().height);

        //======== content ========
        {
            content.setLayout(new CardLayout());

            //======== mainScreen ========
            {
                mainScreen.setLayout(null);

                //---- hotkeyButton ----
                hotkeyButton.setText("Hotkeys");
                hotkeyButton.addActionListener(e -> hotkeyButtonActionPerformed(e));
                mainScreen.add(hotkeyButton);
                hotkeyButton.setBounds(0, 0, 335, hotkeyButton.getPreferredSize().height);

                //======== eventsScroll ========
                {

                    //---- eventsText ----
                    eventsText.setFont(new Font("Fira Code", eventsText.getFont().getStyle(), 14));
                    eventsScroll.setViewportView(eventsText);
                }
                mainScreen.add(eventsScroll);
                eventsScroll.setBounds(0, 290, 770, 150);
            }
            content.add(mainScreen, "card1");

            //======== hotkeyScreen ========
            {
                hotkeyScreen.setLayout(null);

                //---- backIcon ----
                backIcon.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        backIconMouseClicked(e);
                    }
                });
                hotkeyScreen.add(backIcon);
                backIcon.setBounds(0, 0, 15, 25);

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
                hotkeyScreen.add(hotkeyTextField);
                hotkeyTextField.setBounds(250, 70, 185, 30);

                //---- nameLabel ----
                nameLabel.setText("Name:");
                nameLabel.setFont(new Font("Eurostile-Roman", nameLabel.getFont().getStyle(), 16));
                hotkeyScreen.add(nameLabel);
                nameLabel.setBounds(45, 50, 125, 17);
                hotkeyScreen.add(nameTextField);
                nameTextField.setBounds(45, 70, 185, nameTextField.getPreferredSize().height);

                //---- keyActionButton ----
                keyActionButton.setText("Key");
                keyActionButton.setFont(new Font("Eurostile-Roman", keyActionButton.getFont().getStyle(), 14));
                keyActionButton.addActionListener(e -> keyActionButtonActionPerformed(e));
                hotkeyScreen.add(keyActionButton);
                keyActionButton.setBounds(45, 145, 80, 45);

                //---- hotkeylabel2 ----
                hotkeylabel2.setText("Actions:");
                hotkeylabel2.setFont(new Font("Eurostile-Roman", hotkeylabel2.getFont().getStyle(), 16));
                hotkeyScreen.add(hotkeylabel2);
                hotkeylabel2.setBounds(45, 115, 110, 17);

                //---- hotkeylabel ----
                hotkeylabel.setText("Hotkey:");
                hotkeylabel.setFont(new Font("Eurostile-Roman", hotkeylabel.getFont().getStyle(), 16));
                hotkeyScreen.add(hotkeylabel);
                hotkeylabel.setBounds(250, 50, 110, hotkeylabel.getPreferredSize().height);

                //======== listScroll ========
                {

                    //---- actionList ----
                    actionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    listScroll.setViewportView(actionList);
                }
                hotkeyScreen.add(listScroll);
                listScroll.setBounds(145, 145, 505, 205);

                //---- removeActionButton ----
                removeActionButton.setText("Remove");
                removeActionButton.setVisible(false);
                hotkeyScreen.add(removeActionButton);
                removeActionButton.setBounds(690, 140, 78, 45);

                //---- createHotkeyButton ----
                createHotkeyButton.setText("Create new hotkey");
                createHotkeyButton.setFont(new Font("Eurostile-Roman", createHotkeyButton.getFont().getStyle(), 16));
                createHotkeyButton.addActionListener(e -> createHotkeyButtonActionPerformed(e));
                hotkeyScreen.add(createHotkeyButton);
                createHotkeyButton.setBounds(300, 395, 170, 45);
            }
            content.add(hotkeyScreen, "card2");
        }
        add(content);
        content.setBounds(90, 150, 770, 440);

        setPreferredSize(new Dimension(950, 630));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - IamN5
    private JLabel edpnIcon;
    private JLabel loadingIcon;
    private JProgressBar loadingProgress;
    private JLabel greetingText;
    private JLabel commanderName;
    private JLabel lastLoaded;
    private HoverableJButton closeButton;
    private HoverableJButton minimizeButton;
    private JLabel buildText;
    private JPanel content;
    private JPanel mainScreen;
    private RoundJButton hotkeyButton;
    private JScrollPane eventsScroll;
    private JTextArea eventsText;
    private JPanel hotkeyScreen;
    private JLabel backIcon;
    private RoundJTextField hotkeyTextField;
    private JLabel nameLabel;
    private RoundJTextField nameTextField;
    private RoundJButton keyActionButton;
    private JLabel hotkeylabel2;
    private JLabel hotkeylabel;
    private JScrollPane listScroll;
    private HoverableJList actionList;
    private RoundJButton removeActionButton;
    private RoundJButton createHotkeyButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    static class HotkeyTableModel extends DefaultTableModel {
        private ArrayList<Hotkey> values;

        public HotkeyTableModel(ArrayList<Hotkey> hotkeys) {
            values = hotkeys;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public String getColumnName(int column) {
            return switch (column) {
                case 0 -> "ID";
                case 1 -> "Name";
                default -> "";
            };
        }

        @Override
        public int getRowCount() {
            return values != null ? values.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex) {
                case 0 -> values.get(rowIndex).getId();
                case 1 -> values.get(rowIndex).getName();
                default -> null;
            };
        }

        public void updateHotkeys(ArrayList<Hotkey> hotkeys) {
            values = hotkeys;
        }
    }

    static class HotkeyListModel extends AbstractListModel<String> {
        private ArrayList<Hotkey> values;

        public HotkeyListModel(ArrayList<Hotkey> hotkeys) {
            values = hotkeys;
        }

        @Override
        public int getSize() {
            return values.size();
        }

        @Override
        public String getElementAt(int index) {
            return values.get(index).getName();
        }

        public void updateHotkeys(ArrayList<Hotkey> hotkeys) {
            values = hotkeys;
        }
    }
    
    static class ActionListModel extends AbstractListModel<String> {
        private ArrayList<KeyAction> values;

        public ActionListModel(ArrayList<KeyAction> actions) {
            values = actions;
        }

        @Override
        public int getSize() {
            return values.size();
        }

        @Override
        public String getElementAt(int index) {
            return values.get(index).toString();
        }
        
        public void addKeyAction(KeyAction action) {
            values.add(action);
        }
        
        public void updateActions(ArrayList<KeyAction> actions) {
            values = actions;
        }
    }
}
