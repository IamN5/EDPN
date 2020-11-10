package iamn5.edpn.framework.monitors.hotkey;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import iamn5.edpn.framework.Logger;
import iamn5.edpn.framework.monitors.IMonitor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HotkeyMonitor implements IMonitor {
    private Runnable listenerTask;
    private final ScheduledThreadPoolExecutor scheduledExecutor;

    private ScheduledFuture<?> scheduledTask;
    private final ConcurrentHashMap<Integer, Hotkey> hotkeys = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> hotkeysIds = new ConcurrentHashMap<>();
    private int uniqueId = 0;

    public HotkeyMonitor() {
        scheduledExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledExecutor.setRemoveOnCancelPolicy(true);
    }

    @Override
    public void start() {
        Logger.info("HotkeyMonitor started!");

        listenerTask = () -> {
            WinUser.MSG msg = new WinUser.MSG();

            if (User32.INSTANCE.PeekMessage(msg, null, 0, 0, 1)) {
                if (msg.message == WinUser.WM_HOTKEY) {
                    int id = msg.wParam.intValue();

                    if (hotkeysIds.contains(id)) {
                        Logger.info("Getting hotkey " + id);

                        Runnable action = hotkeys.get(id).getAction();

                        new Thread(action).start();
                    }
                }
            }
        };

        scheduledTask = scheduledExecutor.scheduleAtFixedRate(listenerTask, 0, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
    }

    @Override
    public boolean isRunning() {
        return scheduledTask != null && !scheduledTask.isDone();
    }

    public boolean addHotkey(int key, int modifiers, Hotkey hotkey) {
        String uniqueString = String.valueOf(key) + modifiers;

        if (!hotkeysIds.containsKey(uniqueString) && User32.INSTANCE.RegisterHotKey(null, ++uniqueId, modifiers, key)) {
            hotkeys.put(uniqueId, hotkey);
            hotkeysIds.put(uniqueString, uniqueId);

            return true;
        }

        return false;
    }

    public boolean removeHotkey(int key, int modifiers) {
        String uniqueString = String.valueOf(key) + modifiers;

        if (hotkeysIds.containsKey(uniqueString)) {
            int uniqueId =  hotkeysIds.get(uniqueString);

            if (User32.INSTANCE.UnregisterHotKey(null, uniqueId)) {
                hotkeysIds.remove(uniqueString);
                hotkeys.remove(uniqueId);

                return true;
            }
        }

        return false;
    }
}
