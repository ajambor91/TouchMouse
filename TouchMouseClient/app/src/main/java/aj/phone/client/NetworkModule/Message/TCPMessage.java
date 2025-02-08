package aj.phone.client.NetworkModule.Message;

import aj.phone.client.NetworkModule.Enums.TCPMessageTypeEnum;

public class TCPMessage implements NetworkMessage {

    private String hostname;
    private String mouseAddress;
    private String mouseId;
    private String sessionId;

    private String appName;

    private String mouseName;

    private TCPMessageTypeEnum type;

    private String address;

    public TCPMessage() {
    }

    public String getMouseId() {
        return this.mouseId;
    }

    public void setMouseId(String mouseId) {
        this.mouseId = mouseId;
    }

    public String getHostAddress() {
        return this.address;
    }

    public void setHostAddress(String address) {
        this.address = address;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public TCPMessageTypeEnum getType() {
        return this.type;
    }

    public void setType(TCPMessageTypeEnum type) {
        this.type = type;
    }

    public String getMouseName() {
        return this.mouseName;
    }

    public void setMouseName(String mouseName) {
        this.mouseName = mouseName;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getMouseAddress() {
        return this.mouseAddress;
    }

    public void setMouseAddress(String mouseAddress) {
        this.mouseAddress = mouseAddress;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
