package ajprogramming.TouchMouse.Network.Messages;

import ajprogramming.TouchMouse.Network.Enums.BroadcastMessageTypeEnum;

public class BroadcastMessage implements INetworkMessage {
    private String mouseId;
    private String sessionId;
    private BroadcastMessageTypeEnum type;
    private String hostAddress;
    private String appName;
    public String getMouseId() {
        return this.mouseId;
    }

    public void setMouseId(String mouseId) {
        this.mouseId = mouseId;
    }

    @Override
    public String getHostname() {
        return "";
    }

    @Override
    public void setHostname(String hostname) {

    }


    public String getHostAddress() {
        return this.hostAddress;
    }

    public void setHostAddress(String address) {
        this.hostAddress = address;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public BroadcastMessage(){}

    public BroadcastMessageTypeEnum getType() {
        return this.type;
    }

    public void setType(BroadcastMessageTypeEnum type) {
        this.type = type;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMouseName() {
        return null;
    }

    public void setMouseName(String mouseName) {

    }

    public void setMouseAddress(String mouseAddress) {

    }

    public String getMouseAddress() {
        return null;
    }
}
