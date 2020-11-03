package iamn5.edpn.framework.events;

import iamn5.edpn.framework.core.ui.JFrameManager;
import org.json.JSONObject;

public class EventHandler {
    private final JFrameManager frameManager;

    public EventHandler(JFrameManager frameManager) {
        this.frameManager = frameManager;
    }

    public void handle(JSONObject event) {
        switch (event.getString("event")) {
            case "Fileheader":
                handleFileHeaderEvent(event);
                break;
            case "Commander":
                handleCommanderEvent(event);
                break;
            default:
                break;
        }
    }

    private void handleCommanderEvent(JSONObject commanderEvent) {
        frameManager.setCommanderObject(commanderEvent);
    }

    private void handleFileHeaderEvent(JSONObject fileHeader) {
        frameManager.setGameVersion(fileHeader.getString("gameversion"));
        frameManager.setGameBuild(fileHeader.getString("build"));
    }
}
