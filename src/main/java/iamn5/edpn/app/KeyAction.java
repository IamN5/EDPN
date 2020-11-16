package iamn5.edpn.app;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyAction implements Runnable {
    private int keyCode;
    private int modifiers;
    private int type;
    private int delay;
    private Robot robot = new Robot();

    public KeyAction(KeyEvent keyEvent, int type, int delay) throws AWTException {
        this(keyEvent.getKeyCode(), keyEvent.getModifiersEx(), type, delay);
    }

    public KeyAction(int vk, int modifiers, int type, int delay) throws AWTException {
        keyCode = vk;
        this.modifiers = modifiers;
        this.type = type;
        this.delay = delay;
    }

    @Override
    public void run() {
        switch (type) {
            case 1:
                robot.keyPress(keyCode);
                break;
            case 2:
                robot.keyRelease(keyCode);
                break;
            default:
                robot.keyPress(keyCode);
                robot.delay(delay);
                robot.keyRelease(keyCode);
                break;
        }
    }

    @Override
    public String toString() {
        String string = typeString[type] + KeyEvent.getKeyText(keyCode);

        if (type == 0) {
            string = string + " with delay of " + delay + " ms";
        }

        return string;
    }

    private static final String[] typeString = { "Press and release ", "Press ", "Release " };
}
