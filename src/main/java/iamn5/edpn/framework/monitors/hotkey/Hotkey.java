package iamn5.edpn.framework.monitors.hotkey;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Hotkey {
    private final int id;
    private final String name;
    private ConcurrentLinkedQueue<Runnable> actionQueue;

    public Hotkey(int id, String name, ConcurrentLinkedQueue<Runnable> actionQueue) {
        this.id = id;
        this.name = name;
        this.actionQueue = actionQueue;
    }

    public Runnable getAction() {
        return () -> {
            while (actionQueue.peek() != null) {
                actionQueue.poll().run();
            }
        };
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ConcurrentLinkedQueue<Runnable> getActionQueue() {
        return actionQueue;
    }
}
