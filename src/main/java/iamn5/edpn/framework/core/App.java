package iamn5.edpn.framework.core;

import iamn5.edpn.framework.monitors.JournalMonitor;
import iamn5.edpn.framework.Logger;
import iamn5.edpn.framework.core.ui.JFrameManager;
import com.bulenkov.darcula.DarculaLaf;
import iamn5.edpn.framework.events.EventHandler;
import iamn5.edpn.screens.MainScreen;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.util.concurrent.ConcurrentLinkedQueue;

public class App {
    private final JFrameManager frameManager;
    private final EventHandler eventHandler;
    private final ListenableQueue<JSONObject> eventQueue = new ListenableQueue<>(new ConcurrentLinkedQueue<>());

    public App() {
        Logger.initialization();
        frameManager = new JFrameManager(eventQueue, new JournalMonitor(eventQueue));
        eventHandler = new EventHandler(frameManager);
    }

    public void run() {
        BasicLookAndFeel darcula = new DarculaLaf();
        try {
            UIManager.setLookAndFeel(darcula);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        eventQueue.addListener(eventHandler::handle);

        frameManager.load(new MainScreen(frameManager));
    }

    public JFrameManager getFrameManager() {
        return frameManager;
    }
}
