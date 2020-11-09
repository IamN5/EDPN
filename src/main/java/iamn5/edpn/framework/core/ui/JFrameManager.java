package iamn5.edpn.framework.core.ui;

import iamn5.edpn.app.monitors.JournalMonitor;
import iamn5.edpn.framework.Logger;
import iamn5.edpn.framework.core.ListenableQueue;
import iamn5.edpn.screens.MainScreen;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class JFrameManager {
    private JFrame frame;
    private final ListenableQueue<JSONObject> eventQueue;
    private final JournalMonitor journalMonitor;
    //TODO Use one class/object that contains all the game relevant information.
    private JSONObject commanderObject;
    private String gameVersion;
    private String gameBuild;

    public JFrameManager(ListenableQueue<JSONObject> eventQueue, JournalMonitor journalMonitor) {
        this.eventQueue = eventQueue;
        this.journalMonitor = journalMonitor;
    }

    public void load(Screen pane) {
        load(pane, true);
    }

    public void load(Screen pane, boolean centerFrame) {
        load(pane, centerFrame, 0, 0);
    }

    public void load(Screen pane, boolean centerFrame, int width, int height) {
        if (frame != null) frame.dispose();

        frame = new JFrame(pane.getTitle());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(pane);
        frame.setUndecorated(true);
        frame.pack();
        frame.setResizable(false);
        frame.setIconImages(getIconList());
        frame.setVisible(true);

        if (width != 0 && height != 0) frame.setSize(width, height);
        if (centerFrame) centerFrame();

        Logger.info(pane.getClass().getSimpleName() + "screen loaded.");
    }

    private ArrayList<Image> getIconList() {
        ArrayList<Image> imageList = new ArrayList<>();

        URL cloud20Url = getClass().getResource("/assets/logos/edpn-20x18.png");
        URL cloud40Url = getClass().getResource("/assets/logos/edpn-40x36.png");
        URL cloud50Url = getClass().getResource("/assets/logos/edpn-50x45.png");

        BufferedImage img20 = null;
        BufferedImage img40 = null;
        BufferedImage img50 = null;
        try {
            img20 = ImageIO.read(cloud20Url);
            img40 = ImageIO.read(cloud40Url);
            img50 = ImageIO.read(cloud50Url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageList.add(img20);
        imageList.add(img40);
        imageList.add(img50);

        return imageList;
    }

    private void center(Window window) {
        Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((dimensions.width - window.getWidth()) / 2, (dimensions.height - window.getHeight()) / 2);
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public void centerFrame() {
        center(frame);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JournalMonitor getJournalMonitor() {
        return journalMonitor;
    }

    public ListenableQueue<JSONObject> getEventQueue() {
        return eventQueue;
    }

    public void setCommanderObject(JSONObject commanderObject) {
        this.commanderObject = commanderObject;

        if (frame.getContentPane() instanceof MainScreen) {
            MainScreen mainScreen = (MainScreen) frame.getContentPane();
            String timestamp = commanderObject.getString("timestamp");
            try {
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = format.parse(timestamp);

                mainScreen.showCommanderName(commanderObject.getString("Name"), date);

            } catch (ParseException e) {
                Logger.error("Error while getting date for commander load: " + e.getMessage());
            }
        }
    }

    public JSONObject getCommanderObject() {
        return commanderObject;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public void setGameBuild(String gameBuild) {
        this.gameBuild = gameBuild;

        if (frame.getContentPane() instanceof MainScreen) {
            ((MainScreen) frame.getContentPane()).showGameBuild(gameBuild);
        }
    }
}

