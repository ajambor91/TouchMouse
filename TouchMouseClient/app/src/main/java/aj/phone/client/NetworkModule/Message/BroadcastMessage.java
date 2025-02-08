package aj.phone.client.NetworkModule.Message;

import aj.phone.client.NetworkModule.Enums.BroadcastMessageTypeEnum;

public class BroadcastMessage implements NetworkMessage {
    private String mouseId;
    private String sessionId;
    private BroadcastMessageTypeEnum type;
    private String hostAddress;
    private String appName;

    public BroadcastMessage() {
    }

    public String getMouseId() {
        return this.mouseId;
    }

    public void setMouseId(String mouseId) {
        this.mouseId = mouseId;
    }

    public String getHostAddress() {
        return this.hostAddress;
    }

    public void setHostAddress(String address) {
        this.hostAddress = address;
    }

    public String getMouseName() {
        return null;
    }

    public void setMouseName(String mouseName) {

    }

    public String getHostname() {
        return null;
    }

    public void setHostname(String hostname) {

    }

    public String getMouseAddress() {
        return null;
    }

    public void setMouseAddress(String mouseAddress) {

    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

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
}
