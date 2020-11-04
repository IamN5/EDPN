/*
 * Created by JFormDesigner on Sun Nov 01 01:11:12 BRT 2020
 */

package iamn5.edpn.screens;

import java.awt.*;
import java.awt.event.*;

import iamn5.edpn.framework.Logger;
import iamn5.edpn.framework.core.ListenableQueue;
import iamn5.edpn.framework.core.ui.JFrameManager;
import iamn5.edpn.framework.core.ui.MovableScreen;
import iamn5.edpn.framework.core.ui.Screen;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout;
import java.io.IOException;
import java.util.Date;

/**
 * @author IamN5
 */
public class MainScreen extends MovableScreen {
    private boolean isLoading = false;

    public MainScreen(JFrameManager frameManager) {
        super("EDPN - Project Nova", frameManager);

        initComponents();
        eventsText.setEditable(false);

        try {
            loadingIcon.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/assets/logos/loading.png"))));
            edpnIcon.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/assets/logos/edpn-50x45.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(this::startLoading).start();
    }

    private void stopLoading() {
        Logger.info("Finished loading.");

        edpnIcon.setVisible(true);
        eventsScroll.setVisible(true);
        eventsText.setVisible(true);
        greetingText.setVisible(true);
        commanderName.setVisible(true);

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
            eventsText.append(element.toString() + "\n");
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
            try {
                frameManager.getJournalMonitor().start();
            } catch (InterruptedException e) {
                Logger.error("Error while starting JournalMonitor: " + e.getMessage());
            }
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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - IamN5
        edpnIcon = new JLabel();
        loadingIcon = new JLabel();
        loadingProgress = new JProgressBar();
        eventsScroll = new JScrollPane();
        eventsText = new JTextArea();
        greetingText = new JLabel();
        commanderName = new JLabel();
        lastLoaded = new JLabel();
        closeButton = new JButton();
        minimizeButton = new JButton();
        buildText = new JLabel();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border
        .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn" , javax. swing .border . TitledBorder. CENTER ,javax
        . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .awt . Font. BOLD ,
        12 ) ,java . awt. Color .red ) , getBorder () ) );  addPropertyChangeListener( new java. beans
        .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e) { if( "\u0062ord\u0065r" .equals ( e.
        getPropertyName () ) )throw new RuntimeException( ) ;} } );
        setLayout(null);

        //---- edpnIcon ----
        edpnIcon.setText(" ");
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

        //======== eventsScroll ========
        {
            eventsScroll.setVisible(false);

            //---- eventsText ----
            eventsText.setVisible(false);
            eventsText.setFont(new Font("Fira Code", eventsText.getFont().getStyle(), 14));
            eventsScroll.setViewportView(eventsText);
        }
        add(eventsScroll);
        eventsScroll.setBounds(90, 440, 770, 150);

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
        closeButton.addActionListener(e -> closeButtonActionPerformed(e));
        add(closeButton);
        closeButton.setBounds(900, 0, 50, 25);

        //---- minimizeButton ----
        minimizeButton.setText("-");
        minimizeButton.setBorder(null);
        minimizeButton.addActionListener(e -> minimizeButtonActionPerformed(e));
        add(minimizeButton);
        minimizeButton.setBounds(850, 0, 50, 25);

        //---- buildText ----
        buildText.setText("Game build");
        buildText.setFont(new Font("Eurostile-Roman", buildText.getFont().getStyle(), 14));
        buildText.setVisible(false);
        add(buildText);
        buildText.setBounds(655, 5, 190, buildText.getPreferredSize().height);

        setPreferredSize(new Dimension(950, 630));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - IamN5
    private JLabel edpnIcon;
    private JLabel loadingIcon;
    private JProgressBar loadingProgress;
    private JScrollPane eventsScroll;
    private JTextArea eventsText;
    private JLabel greetingText;
    private JLabel commanderName;
    private JLabel lastLoaded;
    private JButton closeButton;
    private JButton minimizeButton;
    private JLabel buildText;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
